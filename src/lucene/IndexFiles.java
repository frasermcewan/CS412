package lucene;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.examples.HtmlToPlainText;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import org.apache.lucene.StopAnalyzer.ENGLISH_STOP_WORDS_SET*;

/** Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing.
 * Run it with no command-line arguments for usage information.
 */
public class IndexFiles {

	private IndexFiles() {}
	/** Index all text files under a directory. */
	public static void main(String[] args) {

		String usage = "java org.apache.lucene.demo.IndexFiles"
				+ " [-index INDEX_PATH] [-docs DOCS_PATH] [-update]\n\n"
				+ "This indexes the documents in DOCS_PATH, creating a Lucene index"
				+ "in INDEX_PATH that can be searched with SearchFiles";
		String indexPath = "index";
		String docPath = "C:/Users/User/git/CS412/data";
		boolean create = true;


		for(int i=0;i<args.length;i++) {
			if ("-index".equals(args[i])) {
				System.out.println("here1");
				indexPath = args[i+1];
				i++;
			} else if ("-docs".equals(args[i])) {
				System.out.println("here2");
				docPath = args[i+1];
				i++;
			} else if ("-update".equals(args[i])) {
				System.out.println("here3");

				create = false;
			}
		}
		if (docPath == null) {
			System.err.println("Usage: " + usage);
			System.exit(1);
		}
		final Path docDir = Paths.get(docPath);
		if (!Files.isReadable(docDir)) {
			System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
			System.exit(1);
		}

		Date start = new Date();
		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");
			Directory dir = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			if (create) {
				// Create a new index in the directory, removing any
				// previously indexed documents:
				iwc.setOpenMode(OpenMode.CREATE);
			} else {
				// Add new documents to an existing index:
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			}
			// Optional: for better indexing performance, if you
			// are indexing many documents, increase the RAM
			// buffer.  But if you do this, increase the max heap
			// size to the JVM (eg add -Xmx512m or -Xmx1g):
			//
			iwc.setRAMBufferSizeMB(256.0);
			IndexWriter writer = new IndexWriter(dir, iwc);
			indexDocs(writer, docDir);
			// NOTE: if you want to maximize search performance,
			// you can optionally call forceMerge here.  This can be
			// a terribly costly operation, so generally it's only
			// worth it when your index is relatively static (ie
			// you're done adding documents to it):
			//
			// writer.forceMerge(1);
			writer.close();
			Date end = new Date();
			System.out.println(end.getTime() - start.getTime() + " total milliseconds");
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass() +
					"\n with message: " + e.getMessage());
		}
	}
	/**
	 * Indexes the given file using the given writer, or if a directory is given,
	 * recurses over files and directories found under the given directory.
	 * 
	 * NOTE: This method indexes one document per input file.  This is slow.  For good
	 * throughput, put multiple documents into your input file(s).  An example of this is
	 * in the benchmark module, which can create "line doc" files, one document per line,
	 * using the
	 * <a href="../../../../../contrib-benchmark/org/apache/lucene/benchmark/byTask/tasks/WriteLineDocTask.html"
	 * >WriteLineDocTask</a>.
	 *  
	 * @param writer Writer to the index where the given file/dir info will be stored
	 * @param path The file to index, or the directory to recurse into to find files to index
	 * @throws IOException If there is a low-level I/O error
	 */
	static void indexDocs(final IndexWriter writer, Path path) throws IOException {
		if (Files.isDirectory(path)) {
			Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					try {
						String fileName=file.getFileName().toString();

						if(fileName.endsWith("gif") || fileName.endsWith("jpg")){
							throw new IOException();
						} 
						else {
							indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
						}
					} catch (IOException ignore) {
						// don't index files that can't be read.
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} else {
			indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
		}
	}
	/** Indexes a single document */
	static void indexDoc(IndexWriter writer, Path file, long lastModified) throws IOException {
		HashMap<String,String> mappingPathToTitle = new HashMap<String, String>();
		
		try (InputStream stream = Files.newInputStream(file)) {
			// make a new, empty document
			Document doc = new Document();

			// strip tags in here as they're unrequired
			//doc.add(new TextField("fullContents", new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))));

			// Add the path of the file as a field named "path".  Use a
			// field that is indexed (i.e. searchable), but don't tokenize 
			// the field into separate words and don't index term frequency
			// or positional information:
			Field pathField = new StringField("path", file.toString(), Field.Store.YES);
			//System.out.println(pathField);
			
			
			String path = file.toString();
			
			//System.out.println("what is path?? " + path);
			
			String pathContents=readFileToString(path, path,mappingPathToTitle);
			Field pathContents1 = new TextField("contents", pathContents, Field.Store.YES);

			if (!mappingPathToTitle.isEmpty()){
				
				Field title = new StringField("title", mappingPathToTitle.get(path), Field.Store.YES);
				
				doc.add(title);
			}
			
			//System.out.println("pls work :" + doc.get("title"));

			
			doc.add(pathField);
			doc.add(pathContents1);


			// Add the last modified date of the file a field named "modified".
			// Use a LongField that is indexed (i.e. efficiently filterable with
			// NumericRangeFilter).  This indexes to milli-second resolution, which
			// is often too fine.  You could instead create a number based on
			// year/month/day/hour/minutes/seconds, down the resolution you require.
			// For example the long value 2011021714 would mean
			// February 17, 2011, 2-3 PM.
			doc.add(new LongField("modified", lastModified, Field.Store.NO));

			// Add the contents of the file to a field named "contents".  Specify a Reader,
			// so that the text of the file is tokenized and indexed, but not stored.
			// Note that FileReader expects the file to be in UTF-8 encoding.
			// If that's not the case searching for special characters will fail.


			if (writer.getConfig().getOpenMode() == OpenMode.CREATE) {
				// New index, so we just add the document (no old document can be there):
				writer.addDocument(doc);
			} else {
				// Existing index (an old copy of this document may have been indexed) so 
				// we use updateDocument instead to replace the old one matching the exact 
				// path, if present:
				System.out.println("updating " + file);
				writer.updateDocument(new Term("path", file.toString()), doc);
			}
		}

	}

	public static String readFileToString(String filePath, String pathField, HashMap<String,String> mappingPathToTitle) throws java.io.IOException {

		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(
				new FileReader(filePath));
		char[] buf = new char[1024];

		int numRead=0;
		while((numRead=reader.read(buf)) != -1){
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();
		String fileContents=fileData.toString();
		fileContents =html2text(fileContents, pathField, mappingPathToTitle);
		return fileContents;
	}

	public static String html2text(String html, String pathField, HashMap<String,String> mappingPathToTitle) {
		final List<String> stopWords = Arrays.asList(
				"a", "an", "and", "are", "as", "at", "be", "but", "by",
				"in", "into", "is", "it",
				"no", "not", "of", "on", "or", "such",
				"that", "the", "their", "then", "there", "these",
				"they", "to", "was", "will", "with"
				);
		Pattern pattern = Pattern.compile("<TITLE>(.+?)</TITLE>");
		Matcher matcher = pattern.matcher(html.toUpperCase());

		if (matcher.find()){
			mappingPathToTitle.put(pathField, matcher.group(1));
		}
		
		return Jsoup.parse(html).text();
		//String htmlToText = Jsoup.parse(html).text();
		//
		//		//System.out.println("before**: " + htmlToText);
		//
		//		String[] arr = htmlToText.split(" ");  
		//		//System.out.println(arr);
		//		for (int i=0;i<arr.length;i++){
		//
		//			for (int j=0;j<stopWords.size();j++){
		//				if (arr[i].equals(stopWords.get(j))){
		//					arr[i]=null; // gets rid of stopwords
		//					break;
		//				}
		//			}
		//		}
		//
		//		String output ="";
		//		for(String str: arr)
		//			output=output+str+" ";
		//		//System.out.println("Output: " + output);
		//
		//		//System.out.println("after**: " + htmlToText);
		//
		//
		//		return output;


	}

}