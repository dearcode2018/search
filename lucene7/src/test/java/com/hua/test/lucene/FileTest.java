/**
 * 描述: 
 * FileTest.java
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
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Ignore;
import org.junit.Test;

import com.hua.test.BaseTest;
import com.hua.util.FileUtil;
import com.hua.util.ProjectUtil;


/**
 * 描述: 
 * 
 * @author qye.zheng
 * FileTest
 */
public final class FileTest extends BaseTest {

	/**
	 * 
	 * 描述: 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testFile() {
		try {

			/* java.nio.file.Path java.nio.file.Paths */
			/* 索引的存放路径 */
			/*  ./article/ 是在当前工程根目录下
			 *  /article 表示当前磁盘目录下，例如: E:/article
			 *  
			 *  */
			//Path path = Paths.get("./article/");
			Path path = Paths.get(INDEX_PATH_PREFIX + "/file");
			/* org.apache.lucene.store.Directory org.apache.lucene.store.FSDirectory */
			Directory directory = FSDirectory.open(path);
			// 分析器
			Analyzer analyzer = new StandardAnalyzer();
			// 索引写入器配置
			IndexWriterConfig config = new IndexWriterConfig(analyzer);
			// 打开模式: 创建(若存在删除再创建新的，不是追加)
			config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
			// 索引写入器
			IndexWriter writer = new IndexWriter(directory, config);
			
			/* org.apache.lucene.document.Document */
			// 将数据源构建为文档对象
			Document document = null;
			/* org.apache.lucene.document.Field */
			String filePath = null;
			String indexName = "keyword";
			filePath = ProjectUtil.getAbsolutePath("/doc/source/title.txt", true);
			document = new Document();
			document.add(new TextField(indexName, FileUtil.getString(filePath), Field.Store.YES));
			// 写入磁盘形成 索引库
			writer.addDocument(document);
			
			filePath = ProjectUtil.getAbsolutePath("/doc/source/content.txt", true);
			document = new Document();
			document.add(new TextField(indexName, FileUtil.getString(filePath), Field.Store.YES));
			// 写入磁盘形成 索引库
			writer.addDocument(document);
			
			// 关闭
			writer.close();			
			
		} catch (Exception e) {
			log.error("testFile =====> ", e);
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
