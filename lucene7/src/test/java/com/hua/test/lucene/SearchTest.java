/**
 * 描述: 
 * SearchTest.java
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

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Ignore;
import org.junit.Test;

import com.hua.test.BaseTest;


/**
 * 描述: 
 * 
 * @author qye.zheng
 * SearchTest
 */
public final class SearchTest extends BaseTest {

	/**
	 * 
	 * 描述: 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testSearch() {
		try {
			
			
		} catch (Exception e) {
			log.error("testSearch =====> ", e);
		}
	}
	
	/**
	 * 
	 * 描述: 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testSearchEntity() {
		try {
			// 搜索关键字，不能为空
			String queryString = "传说";
			Path path = Paths.get(INDEX_PATH_PREFIX + "/article");
			/* org.apache.lucene.store.Directory org.apache.lucene.store.FSDirectory */
			Directory directory = FSDirectory.open(path);
			// 分析器
			Analyzer analyzer = new StandardAnalyzer();
			// 索引读入器
			IndexReader reader = DirectoryReader.open(directory);
			// 索引检索器
			IndexSearcher searcher = new IndexSearcher(reader);
			
			// 单条件
			String indexName = "title";
			QueryParser queryParster = new QueryParser(indexName, analyzer);
			// 根据关键字转化为查询对象
			Query query = queryParster.parse(queryString);
			// 前N个文档
			TopDocs topDocs = searcher.search(query, 10);
			System.out.println("检索总条数: " + topDocs.totalHits);
			// 文档得分
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc e : scoreDocs)
			{
				Document document = searcher.doc(e.doc);
				System.out.println( "value: " + document.get(indexName) + ", 相关度: " + e.score);
			}
			
		} catch (Exception e) {
			log.error("testSearchEntity =====> ", e);
		}
	}
	
	/**
	 * 
	 * 描述: 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testSearchEntity2() {
		try {
			// 搜索关键字，不能为空
			String queryString = "传说";
			Path path = Paths.get(INDEX_PATH_PREFIX + "/article");
			/* org.apache.lucene.store.Directory org.apache.lucene.store.FSDirectory */
			Directory directory = FSDirectory.open(path);
			// 分析器
			Analyzer analyzer = new StandardAnalyzer();
			// 索引读入器
			IndexReader reader = DirectoryReader.open(directory);
			// 索引检索器
			IndexSearcher searcher = new IndexSearcher(reader);
			
			// 多条件
			String[] fields = {"title", "author", "content"};
			QueryParser queryParster = new MultiFieldQueryParser(fields, analyzer);
			// 根据关键字转化为查询对象
			Query query = queryParster.parse(queryString);
			// 前N个文档
			TopDocs topDocs = searcher.search(query, 10);
			System.out.println("检索总条数: " + topDocs.totalHits);
			// 文档得分
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc e : scoreDocs)
			{
				//Document document = searcher.doc(e.doc);
				System.out.println("相关度: " + e.score);
			}
			
		} catch (Exception e) {
			log.error("testSearchEntity2 =====> ", e);
		}
	}
	
	/**
	 * 
	 * 描述: 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testSearchFile() {
		try {
			/*
			 * 搜索关键字，不能为空
			 * 天河路: 关键字中有 路，而title.txt也含有 路，所以被匹配出来了，只是匹配分值较低
			 * 
			 * 天河: 此时只能匹配 content.txt中的词语.
			 * 
			 */
			String queryString = "天河路";
			 queryString = "天河";
			Path path = Paths.get(INDEX_PATH_PREFIX + "/file");
			/* org.apache.lucene.store.Directory org.apache.lucene.store.FSDirectory */
			Directory directory = FSDirectory.open(path);
			// 分析器
			Analyzer analyzer = new StandardAnalyzer();
			// 索引读入器
			IndexReader reader = DirectoryReader.open(directory);
			// 索引检索器
			IndexSearcher searcher = new IndexSearcher(reader);
			
			// 单条件
			String indexName = "keyword";
			QueryParser queryParster = new QueryParser(indexName, analyzer);
			// 根据关键字转化为查询对象
			Query query = queryParster.parse(queryString);
			// 前N个文档
			TopDocs topDocs = searcher.search(query, 10);
			System.out.println("检索总条数: " + topDocs.totalHits);
			// 文档得分
			ScoreDoc[] scoreDocs = topDocs.scoreDocs;
			for (ScoreDoc e : scoreDocs)
			{
				Document document = searcher.doc(e.doc);
				System.out.println( "value: " + document.get(indexName) + ", 相关度: " + e.score);
			}

		} catch (Exception e) {
			log.error("testSearchFile =====> ", e);
		}
	}
	
	/**
	 * 
	 * 描述: 
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
