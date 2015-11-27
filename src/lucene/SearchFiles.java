package lucene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.ClassicAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.CollectionStatistics;
import org.apache.lucene.search.Weight;
import org.apache.lucene.store.FSDirectory;

/** Simple command-line based search demo. */
public class SearchFiles {
 

	String index = "index";
	static String field = "contents";

	private SearchFiles() {}

	/** Simple command-line based search demo. */
	public static void main(String[] args) throws Exception {
		String usage =
				"Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}

		String index = "index";
		String field = "contents";
		String queries = null;
		int repeat = 0;
		boolean raw = false;
		String queryString = null;
		int hitsPerPage = 10;

		for(int i = 0;i < args.length;i++) {
			if ("-index".equals(args[i])) {
				index = args[i+1];
				i++;
			} else if ("-field".equals(args[i])) {
				field = args[i+1];
				i++;
			} else if ("-queries".equals(args[i])) {
				queries = args[i+1];
				i++;
			} else if ("-query".equals(args[i])) {
				queryString = args[i+1];
				i++;
			} else if ("-repeat".equals(args[i])) {
				repeat = Integer.parseInt(args[i+1]);
				i++;
			} else if ("-raw".equals(args[i])) {
				raw = true;
			} else if ("-paging".equals(args[i])) {
				hitsPerPage = Integer.parseInt(args[i+1]);
				if (hitsPerPage <= 0) {
					System.err.println("There must be at least 1 hit per page.");
					System.exit(1);
				}
				i++;
			}
		}

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		//		Analyzer analyzer = new ClassicAnalyzer();
		//		Analyzer analyzer = new AnalyzingQueryParser();


		BufferedReader in = null;
		if (queries != null) {
			in = Files.newBufferedReader(Paths.get(queries), StandardCharsets.UTF_8);
		} else {
			in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
		}
		//		QueryParser parser = new QueryParser(field, analyzer);
		AnalyzingQueryParser parser = new AnalyzingQueryParser(field, analyzer);
		while (true) {
			if (queries == null && queryString == null) {                        // prompt the user
				System.out.println("Enter query: ");
			}

			String line = queryString != null ? queryString : in.readLine();
			if (line == null || line.length() == -1) {
				break;
			}

			line = line.trim();
			if (line.length() == 0) {
				break;
			}

			Query query = parser.parse(line);
			//			System.out.println(query);
			//			System.out.println(query.getBoost());
			//			Weight queryWeight=query.createWeight(searcher, true);
			//			System.out.println();
			//			System.out.println(queryWeight.getValueForNormalization());
			//			query.setBoost(queryWeight.getValueForNormalization());
			//			System.out.println(query.getBoost());
			System.out.println("Searching for: " + query.toString(field));

			if (repeat > 0) {                           // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) {
					searcher.search(query, 100);
				}
				Date end = new Date();
				System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
			}

			doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

			if (queryString != null) {
				break;
			}
		}
		reader.close();
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search engine presents 
	 * pages of size n to the user. The user can then go to the next page if interested in
	 * the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results are collected
	 * to fill 5 result pages. If the user wants to page beyond this limit, then the query
	 * is executed another time and all hits are collected.
	 * 
	 */

	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query, 
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);

		Explanation results2 = searcher.explain(query, 268);

		System.out.println(results2);

		for (int i=0;i<results.scoreDocs.length;i++){
			System.out.println(i+1 + ": " + results.scoreDocs[i]);
		}

		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage); // end is the number of hits per page

		while (true) {
			if (end > hits.length) {
				System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {
				if (raw) {                              // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);

				String actualQueryString = query.toString().substring(9, query.toString().length());
				int sizeOfActualQueryString = actualQueryString.length();

				String termBeingSearched = doc.get("contents");


				for (int j = -1; (j = termBeingSearched.indexOf(actualQueryString, j + 1)) != -1; ) {

					// need to check the term is within the +25 and -25 otherwise it's out of bounds
					// can fix this later on

					//System.out.println(termBeingSearched);
					int startIndexOfTermBeingSearched = j;
					//System.out.println(startIndexOfTermBeingSearched);
					int endIndexOfTermBeingSearched = startIndexOfTermBeingSearched+sizeOfActualQueryString;
					//System.out.println(endIndexOfTermBeingSearched);

					if (startIndexOfTermBeingSearched<25){
						startIndexOfTermBeingSearched = 0;
					} else {
						startIndexOfTermBeingSearched=startIndexOfTermBeingSearched-25;
					}
					int temp = endIndexOfTermBeingSearched+25;
					int termLength = termBeingSearched.length();
					if (temp>=termLength){
						endIndexOfTermBeingSearched=termLength;
					} else {
						endIndexOfTermBeingSearched=endIndexOfTermBeingSearched+25;
					}

					String trial = termBeingSearched.substring(startIndexOfTermBeingSearched, endIndexOfTermBeingSearched);
					System.out.println();
					System.out.println("term in context of the page:");
					System.out.println("... \" " + trial + " \"...");

				}



				//				String text = "0123hello9012hello8901hello7890";
				//				String word = "hello";
				//
				//				System.out.println(text.indexOf(word)); // prints "4"
				//				System.out.println(text.lastIndexOf(word)); // prints "22"
				//				int length = 5;
				//				// find all occurrences forward
				//				for (int j = -1; (j = text.indexOf(word, j + 1)) != -1; ) {
				//				    System.out.println(text.substring(j-25,j+length+25));
				//				} // prints "4", "13", "22"


				//				System.out.println();
				//				System.out.println();
				//				System.out.println("start index: " + startIndexOfTermBeingSearched);
				//				System.out.println("end index: " + endIndexOfTermBeingSearched);
				//				System.out.println("try this out: " + trial);
				//				System.out.println();
				//				System.out.println();

				//System.out.println(doc.get("contents"));

				String path = doc.get("path");

				if (path != null) {
					System.out.println();
					System.out.println((i+1) + ". " + path);
					String title = doc.get("title");
					if (title != null) {
						System.out.println("Title: " + doc.get("title"));
					}

				} else {
					System.out.println((i+1) + ". " + "No path for this document");
				}

			}

			if (!interactive || end == 0) {
				break;
			}

			if (numTotalHits >= end) {
				boolean quit = false;


				while (true) {
					System.out.print("Press ");
					if (start - hitsPerPage >= 0) {
						System.out.print("(p)revious page, ");  
					}
					if (start + hitsPerPage < numTotalHits) {
						System.out.print("(n)ext page, ");
					}
					System.out.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0)=='q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start+=hitsPerPage;
						}
						break;
					} else {
						int page = Integer.parseInt(line);
						if ((page - 1) * hitsPerPage < numTotalHits) {
							start = (page - 1) * hitsPerPage;
							break;
						} else {
							System.out.println("No such page");
						}
					}
				}
				if (quit) break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}
}