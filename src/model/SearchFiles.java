package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.analyzing.AnalyzingQueryParser;
import org.apache.lucene.queryparser.complexPhrase.ComplexPhraseQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.BooleanQuery.Builder;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

/** Simple command-line based search demo. */
public class SearchFiles {


	static String index = "index";
	static String field = "contents";
	static String title = "title";

	public SearchFiles() {}

	/**
	 * This demonstrates a typical paging search scenario, where the search engine presents
	 * pages of size n to the user. The user can then go to the next page if interested in
	 * the next hits.
	 *
	 * When the query is executed for the first time, then only enough results are collected
	 * to fill 5 result pages. If the user wants to page beyond this limit, then the query
	 * is executed another time and all hits are collected.
	 * @param contentOrTitle 
	 * @throws IOException
	 * @throws ParseException
	 *
	 */

	public Map<String, String> directQuoteSearch (String q, String contentOrTitle) throws IOException, ParseException{

		
		if (contentOrTitle.equals("content")){
			contentOrTitle=field;
		} else if (contentOrTitle.equals("title")){
			contentOrTitle=title;
		}
		
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		int hitsPerPage = 20;
		boolean raw = false;
		int repeat = 0;

		final List<String> stopWords = Arrays.asList();
		final CharArraySet stopSet = new CharArraySet(stopWords, true);

		Analyzer analyzer = new StandardAnalyzer(stopSet);
		//AnalyzingQueryParser parser = new AnalyzingQueryParser(field, analyzer);
		AnalyzingQueryParser parser = new AnalyzingQueryParser(contentOrTitle, analyzer);
		
		String[] items = q.split(" ");
		List<String> itemList = Arrays.asList(items);
		Query query = null;
		
//		System.out.println("print this: " + itemList);
//		BooleanQuery.Builder bqb = new BooleanQuery.Builder();
//
//		for (int i=0;i<itemList.size();i++){
//			
//			Query tempQuery = parser.createBooleanQuery(field, itemList.get(i));
//			
//			bqb.add(tempQuery, Occur.MUST);
//		}

		
		PhraseQuery.Builder builder = new PhraseQuery.Builder();
		for (String word : itemList) {
			builder.add(new Term(contentOrTitle, word));
		}
		
		PhraseQuery pq = builder.build();


//		PhraseQuery pq1 = new PhraseQuery();
//		String[] words = sentence.split(" ");
//		for (String word : words) {
//		    pq.add(new Term("contents", word));
//		}
//		booleanQuery.add(pq1, BooleanClause.Occur.MUST);
		
		
//		String query1 = "";
//		String query2 = "";
//
//		for (int i=0;i<itemList.size();i++){
//			if (itemList.get(i).equalsIgnoreCase("and")){
//				query1=itemList.get(i-1);
//				query2=itemList.get(i+1);
//				Query query11 = parser.createBooleanQuery(field, query1, Occur.MUST);
//				query11 = parser.createBooleanQuery(field, query1+" " +query2, Occur.MUST);
//				query = query11;
//			}
//			
//		}
//		
		if (query==null){
			query = parser.parse(q);
		}
		
		
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

		//System.out.println("Searching for: " + query);
		System.out.println("Searching for: " + pq);
		if (repeat > 0) {                           // repeat & time as benchmark
			Date start = new Date();
			for (int i = 0; i < repeat; i++) {
				//searcher.search(query, 100);
				searcher.search(pq, 100);
			}
			Date end = new Date();
			System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
		}

		//return doPagingSearch(in, searcher, query, hitsPerPage, raw, true, bqb);
		return doPagingSearch(in, searcher, query, hitsPerPage, raw, true, pq);

	}

//	public Map<String, String> newSearch (String q) throws IOException, ParseException{
//
//		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
//		IndexSearcher searcher = new IndexSearcher(reader);
//		int hitsPerPage = 20;
//		boolean raw = false;
//		int repeat = 0;
//
//		// custom made list of stop words, removing key words like for, this, etc.
//		final List<String> stopWords = Arrays.asList(
//				"a", "an", "are", "as", "at", "be", "but", "by",
//				"in", "into", "is", "it",
//				"no", "on", "such",
//				"that", "the", "their", "then", "there", "these",
//				"they", "to", "was", "will", "with"
//				);
//
//		final CharArraySet stopSet = new CharArraySet(stopWords, true);
//
//
//		Analyzer analyzer = new StandardAnalyzer(stopSet);
//		AnalyzingQueryParser parser = new AnalyzingQueryParser(contentOrTitle, analyzer);
//		
//		Query query = parser.parse(q);
//		
//		
//		BufferedReader in = null;
//		in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
//
//		System.out.println("Searching for: " + query);
//		if (repeat > 0) {                           // repeat & time as benchmark
//			Date start = new Date();
//			for (int i = 0; i < repeat; i++) {
//				searcher.search(query, 100);
//			}
//			Date end = new Date();
//			System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
//		}
//
//		//return doPagingSearch(in, searcher, query, hitsPerPage, raw, true, bqb);
//		return doPagingSearch(in, searcher, query, hitsPerPage, raw, true);
//
//	}
	
	public static Map<String, String> doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {
		Map<String,String> result = new HashMap<String,String>();

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		//TopDocs results = searcher.search(bqb.build(), 5 * hitsPerPage);
		if (results.totalHits==0){
			System.out.println("got here");

		}

		//System.out.println(results2);

		for (int i=0;i<results.scoreDocs.length;i++){
			//	System.out.println(i+1 + ": " + results.scoreDocs[i]);
		}

		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		//System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage); // end is the number of hits per page

		while (true) {
			if (end > hits.length) {
				//System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				//System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {

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
					//System.out.println();
					//System.out.println("term in context of the page:");
					//System.out.println("... \" " + trial + " \"...");

				}

				String path = doc.get("path");

				if (path != null) {
					//System.out.println();
					//System.out.println((i+1) + ". " + path);
					String title = doc.get("title");
					if (title != null) {
						System.out.println("Title: " + doc.get("title"));

					}
					//          System.out.println(title + " - " + path);
					result.put(title, path);

				} else {
					//          System.out.println((i+1) + ". " + "No path for this document");
				}
			}


			if (!interactive || end == 0) {
				break;
			}

			break;
		}

		return result;
	}

	public static Map<String, String> doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
			int hitsPerPage, boolean raw, boolean interactive, PhraseQuery pq) throws IOException {

		Map<String,String> result = new HashMap<String,String>();

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(pq, 5 * hitsPerPage);
		//TopDocs results = searcher.search(bqb.build(), 5 * hitsPerPage);
		if (results.totalHits==0){
			System.out.println("got here");

		}

		//System.out.println(results2);

		for (int i=0;i<results.scoreDocs.length;i++){
			//	System.out.println(i+1 + ": " + results.scoreDocs[i]);
		}

		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		//System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage); // end is the number of hits per page

		while (true) {
			if (end > hits.length) {
				//System.out.println("Only results 1 - " + hits.length +" of " + numTotalHits + " total matching documents collected.");
				//System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {

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
					//System.out.println();
					//System.out.println("term in context of the page:");
					//System.out.println("... \" " + trial + " \"...");

				}

				String path = doc.get("path");

				if (path != null) {
					//System.out.println();
					//System.out.println((i+1) + ". " + path);
					String title = doc.get("title");
					if (title != null) {
						//System.out.println("Title: " + doc.get("title"));

					}
					//          System.out.println(title + " - " + path);
					result.put(title, path);

				} else {
					//          System.out.println((i+1) + ". " + "No path for this document");
				}
			}


			if (!interactive || end == 0) {
				break;
			}

			break;
		}

		return result;
	}
}
