/**
 * 描述: 
 * Lucene5Test.java
 * 
 * @author qye.zheng
 *  version 1.0
 */
package com.hua.test.lucene;

// 静态导入
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Ignore;
import org.junit.Test;

import com.hua.constant.Constant;
import com.hua.test.BaseTest;
import com.hua.util.ProjectUtil;

/**
 * 描述:
 * 
 * @author qye.zheng Lucene5Test
 */
public final class Lucene5Test extends BaseTest {

	private String sourcePath = ProjectUtil.getAbsolutePath("/doc/source/",
			true);

	private String indexPath = ProjectUtil.getAbsolutePath("/doc/index/", true);

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testLucene5() {
		try {
			// source目录: 存放待索引的文件.
			File sourceFile = new File(sourcePath);

			// index目录: 存放索引文件.
			Path indexFilePath = Paths.get(indexPath);

			Directory dir = FSDirectory.open(indexFilePath);
			Reader reader = new InputStreamReader(new FileInputStream(
					indexFilePath.toFile()));
			// Version.LUCENE_5_1_0
			Analyzer analyzer = new StandardAnalyzer(reader);

			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			iwc.setOpenMode(OpenMode.CREATE);

			IndexWriter indexWriter = new IndexWriter(dir, iwc);

			File[] textFiles = sourceFile.listFiles();
			long startTime = new Date().getTime();

			// 增加document到索引去
			for (int i = 0; i < textFiles.length; i++) {
				if (textFiles[i].isFile()
						&& textFiles[i].getName().endsWith(".txt")) {
					System.out.println("File "
							+ textFiles[i].getCanonicalPath() + "正在被索引....");
					String temp = FileReaderAll(
							textFiles[i].getCanonicalPath(), Constant.CHART_SET_UTF_8);
					System.out.println(temp);
					Document document = new Document();

					Field fieldPath = new Field("path", "abc", new FieldType());
					Field fieldBody = new Field("body", "hello",
							new FieldType());
					// Field fieldPath = new Field("path",
					// textFiles[i].getPath(), Field.Store.YES, Field.Index.NO);
					// Field fieldBody = new Field("body", temp,
					// Field.Store.YES, Field.Index.ANALYZED,
					// Field.TermVector.WITH_POSITIONS_OFFSETS);

					document.add(fieldPath);
					document.add(fieldBody);
					indexWriter.addDocument(document);
				}
			}
			indexWriter.close();

			// 测试一下索引的时间
			long endTime = new Date().getTime();
			System.out.println("这花费了" + (endTime - startTime)
					+ " 毫秒来把文档增加到索引里面去!" + sourceFile.getPath());

		} catch (Exception e) {
			log.error("testLucene5 =====> ", e);
		}
	}

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testCreateIndex() {
		try {
			// source目录: 存放待索引的文件.
			Path sourceFile = Paths.get(sourcePath);

			// index目录: 存放索引文件.
			Path indexFilePath = Paths.get(indexPath);
			
			Directory dir = FSDirectory.open(indexFilePath);
			//Reader reader = new InputStreamReader(new FileInputStream(indexFilePath.toFile()));
			// Version.LUCENE_5_1_0
			//Analyzer analyzer = new StandardAnalyzer(reader);
			Analyzer analyzer = new StandardAnalyzer();

			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			iwc.setOpenMode(OpenMode.CREATE);

			IndexWriter indexWriter = new IndexWriter(dir, iwc);
			Iterator<Path> it = sourceFile.iterator();
			Path path = null;
			// 增加document到索引去
			while (it.hasNext())
			{
				path = it.next();
				Document document = new Document();

				//Field fieldPath = new StringField("path", path.toString(), Field.Store.YES);
				Field fieldBody = new StringField("body", path.toString(), Field.Store.YES);

				//document.add(fieldPath);
				document.add(fieldBody);
				indexWriter.addDocument(document);				
			}
			/*for (int i = 0; i < textFiles.length; i++) {
				if (textFiles[i].isFile()
						&& textFiles[i].getName().endsWith(".txt")) {
					System.out.println("File "
							+ textFiles[i].getCanonicalPath() + "正在被索引....");
					String temp = FileReaderAll(
							textFiles[i].getCanonicalPath(), Constant.CHART_SET_UTF_8);
					System.out.println(temp);
					Document document = new Document();

					Field fieldPath = new Field("path", "珠江新城", new FieldType());
					new StringField("path", file.toString(), Field.Store.YES);
					Field fieldBody = new Field("body", "珠江新城",
							new FieldType());
					// Field fieldPath = new Field("path",
					// textFiles[i].getPath(), Field.Store.YES, Field.Index.NO);
					// Field fieldBody = new Field("body", temp,
					// Field.Store.YES, Field.Index.ANALYZED,
					// Field.TermVector.WITH_POSITIONS_OFFSETS);

					document.add(fieldPath);
					document.add(fieldBody);
					indexWriter.addDocument(document);
				}
			}*/
			indexWriter.close();
		} catch (Exception e) {
			log.error("testCreateIndex =====> ", e);
		}
	}

	public static String FileReaderAll(String FileName, String charset)
			throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(FileName), charset));
		String line = new String();
		String temp = new String();

		while ((line = reader.readLine()) != null) {
			temp += line;
		}
		reader.close();
		return temp;
	}

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testQuery() {
		String queryString = "三溪"; // 搜索的关键词

		String usage = "Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
		if (queryString.length() > 0
				&& ("-h".equals(queryString) || "-help".equals(queryString))) {
			System.out.println(usage);
			System.exit(0);
		}

		String field = "body";
		String queries = queryString;
		int repeat = 0;
		boolean raw = false;
		int hitsPerPage = 10;

		IndexReader reader;
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths
					.get(indexPath)));
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new StandardAnalyzer();
			
			BufferedReader in = null;
			if (queries != null) {
				in = Files.newBufferedReader(Paths.get(queries),
						StandardCharsets.UTF_8);
			}
			QueryParser parser = new QueryParser(field, analyzer);
			while (true) {
				if (queries == null && queryString == null) { // prompt the user
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
				System.out.println("Searching for: " + query.toString(field));

				if (repeat > 0) { // repeat & time as benchmark
					Date start = new Date();
					for (int i = 0; i < repeat; i++) {
						searcher.search(query, 100);
					}
					Date end = new Date();
					System.out.println("Time: " + (end.getTime() - start.getTime())
							+ "ms");
				}

				doPagingSearch(in, searcher, query, hitsPerPage, raw,
						queries == null && queryString == null);

				if (queryString != null) {
					break;
				}
			}
			reader.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		
	}

	public static void doPagingSearch(BufferedReader in,
			IndexSearcher searcher, Query query, int hitsPerPage, boolean raw,
			boolean interactive) throws IOException {

		// Collect enough docs to show 5 pages
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		int numTotalHits = 0;
		//int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		int start = 0;
		int end = Math.min(numTotalHits, hitsPerPage);

		while (true) {
			if (end > hits.length) {
				System.out
						.println("Only results 1 - " + hits.length + " of "
								+ numTotalHits
								+ " total matching documents collected.");
				System.out.println("Collect more (y/n) ?");
				String line = in.readLine();
				if (line.length() == 0 || line.charAt(0) == 'n') {
					break;
				}

				hits = searcher.search(query, numTotalHits).scoreDocs;
			}

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
					System.out.println((i + 1) + ". " + path);
					String title = doc.get("title");
					if (title != null) {
						System.out.println("   Title: " + doc.get("title"));
					}
				} else {
					System.out.println((i + 1) + ". "
							+ "No path for this document");
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
					System.out
							.println("(q)uit or enter number to jump to a page.");

					String line = in.readLine();
					if (line.length() == 0 || line.charAt(0) == 'q') {
						quit = true;
						break;
					}
					if (line.charAt(0) == 'p') {
						start = Math.max(0, start - hitsPerPage);
						break;
					} else if (line.charAt(0) == 'n') {
						if (start + hitsPerPage < numTotalHits) {
							start += hitsPerPage;
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
				if (quit)
					break;
				end = Math.min(numTotalHits, start + hitsPerPage);
			}
		}
	}

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void test() {
		try {

		} catch (Exception e) {
			log.error("test =====> ", e);
		}
	}

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testTemp() {
		try {

		} catch (Exception e) {
			log.error("testTemp=====> ", e);
		}
	}

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testCommon() {
		try {

		} catch (Exception e) {
			log.error("testCommon =====> ", e);
		}
	}

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testSimple() {
		try {

		} catch (Exception e) {
			log.error("testSimple =====> ", e);
		}
	}

	/**
	 * 
	 * 描述:
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testBase() {
		try {

		} catch (Exception e) {
			log.error("testBase =====> ", e);
		}
	}

	/**
	 * 
	 * 描述: 解决ide静态导入消除问题
	 * 
	 * @author qye.zheng
	 * 
	 */
	@Ignore("解决ide静态导入消除问题 ")
	private void noUse() {
		String expected = null;
		String actual = null;
		Object[] expecteds = null;
		Object[] actuals = null;
		String message = null;

		assertEquals(expected, actual);
		assertEquals(message, expected, actual);
		assertNotEquals(expected, actual);
		assertNotEquals(message, expected, actual);

		assertArrayEquals(expecteds, actuals);
		assertArrayEquals(message, expecteds, actuals);

		assertFalse(true);
		assertTrue(true);
		assertFalse(message, true);
		assertTrue(message, true);

		assertSame(expecteds, actuals);
		assertNotSame(expecteds, actuals);
		assertSame(message, expecteds, actuals);
		assertNotSame(message, expecteds, actuals);

		assertNull(actuals);
		assertNotNull(actuals);
		assertNull(message, actuals);
		assertNotNull(message, actuals);

		assertThat(null, null);
		assertThat(null, null, null);

		fail();
		fail("Not yet implemented");

	}

}
