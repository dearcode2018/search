/**
 * @filename SearchFiles.java
 * @description  
 * @version 1.0
 * @author qye.zheng
 */
package com.hua.test.lucene;

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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import com.hua.util.ProjectUtil;

/** Simple command-line based search demo. */
public class SearchFiles {

	private static String docsPath = ProjectUtil.getAbsolutePath(
			"/doc/source/", true);

	private static String index = ProjectUtil.getAbsolutePath("/doc/index/",
			true);

	private SearchFiles() {
	}

	/** Simple command-line based search demo. */
	public static void main(String[] args) throws Exception {
		String field = "contents";
		// 可以去多个文档中去查找
		String queries = docsPath + "\\title.txt";
		int repeat = 10;
		boolean raw = true;
		String queryString = "三溪";
		// 文档中每个字符都被索引起来
		queryString = "欢迎";
		queryString = "体育中心";
		
		//queryString = "11";
		
		
		// 每一页命中数
		int hitsPerPage = 100;

		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();

		BufferedReader bufferedReader = Files.newBufferedReader(
				Paths.get(queries), StandardCharsets.UTF_8);
		
		//System.out.println(bufferedReader.readLine());
		//System.out.println(bufferedReader.readLine());
		
		QueryParser parser = new QueryParser(field, analyzer);
		while (true) {
			String line = queryString;
			line = line.trim();
			// 查询
			Query query = parser.parse(line);
			System.out.println("Searching for: " + query.toString(field));

			if (repeat > 0) { // repeat & time as benchmark
				Date start = new Date();
				for (int i = 0; i < repeat; i++) {
					searcher.search(query, 1000);
				}
				Date end = new Date();
				System.out.println("Time: " + (end.getTime() - start.getTime())
						+ "ms");
			}

			doPagingSearch(bufferedReader, searcher, query, hitsPerPage, raw,
					queries == null && queryString == null);

			if (queryString != null) {
				break;
			}
		}
		reader.close();
	}

	/**
	 * This demonstrates a typical paging search scenario, where the search
	 * engine presents pages of size n to the user. The user can then go to the
	 * next page if interested in the next hits.
	 * 
	 * When the query is executed for the first time, then only enough results
	 * are collected to fill 5 result pages. If the user wants to page beyond
	 * this limit, then the query is executed another time and all hits are
	 * collected.
	 * 
	 */
	public static void doPagingSearch(BufferedReader in,
			IndexSearcher searcher, Query query, int hitsPerPage, boolean raw,
			boolean interactive) throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);
		hits = searcher.search(query, numTotalHits).scoreDocs;
		System.out.println("hits = " + hits.length);
		end = Math.min(hits.length, start + hitsPerPage);
		for (int i = start; i < end; i++) {
			if (raw) { // output raw format
				System.out.println("doc=" + hits[i].doc + " score="
						+ hits[i].score);
				continue;
			}

			Document doc = searcher.doc(hits[i].doc);
			String path = doc.get("path");
			if (path != null) {
				// System.out.println((i+1) + ". " + path);
				String title = doc.get("title");
				if (title != null) {
					System.out.println("   Title: " + doc.get("title"));
				}
			} else {
				System.out
						.println((i + 1) + ". " + "No path for this document");
			}
		}

	}
}
