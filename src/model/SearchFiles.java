package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.IndexableFieldType;
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
	int hitsPerPage = 20;

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
		boolean raw = false;
		int repeat = 0;

		final List<String> stopWords = Arrays.asList();
		final CharArraySet stopSet = new CharArraySet(stopWords, true);

		Analyzer analyzer = new StandardAnalyzer(stopSet);
		AnalyzingQueryParser parser = new AnalyzingQueryParser(contentOrTitle, analyzer);
		

		
		String[] items = q.split(" ");
		List<String> itemList = Arrays.asList(items);
		Query query=null;
		
		
		if (q.equals("")){
			System.out.println("no input");
			
		} else {
			query = parser.parse(q);

		}

		
		
		BufferedReader in = null;
		in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));

		if (repeat > 0) {                           // repeat & time as benchmark
			Date start = new Date();
			for (int i = 0; i < repeat; i++) {
				searcher.search(query, 100);
			}
			Date end = new Date();
			System.out.println("Time: "+(end.getTime()-start.getTime())+"ms");
		}

		return doPagingSearch(in, searcher, query, hitsPerPage, raw, true);
	}
	
	public static Map<String, String> doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {
		Map<String,String> result = new HashMap<String,String>();

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
				
		if (results.totalHits==0){	
			System.out.println("Zero Result Checker");

		}
		
		ArrayList<Double> docNumbers = new ArrayList<Double>();

		for (int i=0;i<results.scoreDocs.length;i++){
			Explanation results2 = searcher.explain(query, results.scoreDocs[i].doc);
			
			String stats = results2.toString();
		
			
			if (stats.contains("termFreq")){
				int positionTerm = stats.indexOf("termFreq");
				
				String termRange = stats.substring(positionTerm+9, positionTerm+17);
				double termFreq = Double.parseDouble(termRange);
							
				docNumbers.add(termFreq);
			} else if (stats.contains("phraseFreq")){
				int positionTerm = stats.indexOf("phraseFreq");
				
				String phraseRange = stats.substring(positionTerm+11, positionTerm+17);
				double phraseFreq = Double.parseDouble(phraseRange);
							
				docNumbers.add(phraseFreq);
			}
		}
		
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage); // end is the number of hits per page

		while (true) {
			if (end > hits.length) {
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);
			
			for (int i = start; i < end; i++) {

				Document doc = searcher.doc(hits[i].doc);	
				double termFrequency = docNumbers.get(i);
								
				String path = doc.get("path");

				if (path != null) {
					String title = doc.get("title");
					title = title+" || Term Frequency: " + termFrequency;
					if (title != null) {
						System.out.println("Title: " + doc.get("title"));

					}

					result.put(title, path);

				} else {

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
		if (results.totalHits==0){
			System.out.println("Zero Result Checker");
		}

		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage); // end is the number of hits per page

		while (true) {
			if (end > hits.length) {
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

			end = Math.min(hits.length, start + hitsPerPage);

			for (int i = start; i < end; i++) {

				Document doc = searcher.doc(hits[i].doc);

				
				String path = doc.get("path");

				if (path != null) {
					String title = doc.get("title");
					if (title != null) {
						
					}
					result.put(title, path);

				} else {
				}
			}


			if (!interactive || end == 0) {
				break;
			}

			break;
		}

		return result;
	}
	
	public void getHitsPerPage(int value){
		hitsPerPage = value;
	}
}
