/**
 * 描述: 
 * EntityTest.java
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

import com.hua.entity.Article;
import com.hua.test.BaseTest;


/**
 * 描述: 
 * 
 * @author qye.zheng
 * EntityTest
 */
public final class EntityTest extends BaseTest {

	/**
	 * 
	 * 描述: 
	 * @author qye.zheng
	 * 
	 */
	@Test
	public void testEntity() {
		try {
			
			/* 文档数据源 */
			Article entity = new Article();
			entity.setId(12);
			entity.setTitle("古老遥远的传说");
			entity.setAuthor("Jaco angle");
			entity.setContent("Haha: 去一次规划了许久，但迟迟没有到达过的地方。出发去一次西藏，"
					+ "去雪域高原感受神秘的藏文化、纯净湛蓝的天空、雄奇壮美的神山圣湖、"
					+ "神秘而虔诚的宗教信仰以及曼妙生花的异域风情，踏上那块陌生而神奇的土地，"
					+ "沿着朝圣者的足迹走入那古老而遥远的传说。");
			
			/* java.nio.file.Path java.nio.file.Paths */
			/* 索引的存放路径 */
			/*  ./article/ 是在当前工程根目录下
			 *  /article 表示当前磁盘目录下，例如: E:/article
			 *  
			 *  */
			//Path path = Paths.get("./article/");
			Path path = Paths.get(INDEX_PATH_PREFIX + "/article");
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
			Document document = new Document();
			/* org.apache.lucene.document.Field */
			document.add(new TextField("id", entity.getId().toString(), Field.Store.YES));
			document.add(new TextField("title", entity.getTitle(), Field.Store.YES));
			document.add(new TextField("author", entity.getAuthor(), Field.Store.YES));
			document.add(new TextField("content", entity.getContent(), Field.Store.YES));
			
			// 写入磁盘形成 索引库
			writer.addDocument(document);
			
			// 关闭
			writer.close();
		} catch (Exception e) {
			log.error("testEntity =====> ", e);
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
