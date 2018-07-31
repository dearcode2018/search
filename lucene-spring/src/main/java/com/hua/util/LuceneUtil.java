/**
 * LuceneUtil.java
 * @author  qye.zheng
 * 	version 1.0
 */
package com.hua.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.hua.constant.LuceneConstant;

/**
 * LuceneUtil
 * 描述: Lucene - 工具类
 * @author  qye.zheng
 */
public final class LuceneUtil
{

	/**
	 * 构造方法
	 * 描述: 私有 - 禁止实例化
	 * @author  qye.zheng
	 */
	private LuceneUtil()
	{
	}

	/* ================ 创建索引 ================== */
	
	/**
	 * 
	 * @description 
	 * @param docPath
	 * @param indexPath
	 * @param create
	 * @author qye.zheng
	 */
	public static final void createIndex(final String docPath, 
			final String indexPath, final boolean create)
	{
		final Path docDir = Paths.get(docPath);
		if (!Files.isReadable(docDir))
		{
			System.out.println("Document directory '" +docDir.toAbsolutePath()+ "' does not exist or is not readable, please check the path");
		}
		 final Date start = new Date();
		 try {
			 System.out.println("Indexing to directory '" + indexPath + "'...");
			 final Directory indexDir = FSDirectory.open(Paths.get(indexPath));
			 final Analyzer analyzer = new StandardAnalyzer();
			 final IndexWriterConfig writerConfig = new IndexWriterConfig(analyzer);
			 if (create)
			 {
				 // 重新创建新的索引目录
				 writerConfig.setOpenMode(OpenMode.CREATE);
			 } else
			 {
				 // 创建或添加
				 writerConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
			 }
			 final IndexWriter writer = new IndexWriter(indexDir, writerConfig);
			 indexDocs(writer, docDir);
			 
			  writer.close();
			 
		      final Date end = new Date();
		      System.out.println(end.getTime() - start.getTime() + " total milliseconds");			 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description 
	 * @param writer
	 * @param path
	 * @author qye.zheng
	 */
	public static final void indexDocs(final IndexWriter writer, 
			final Path path)
	{
		try {
			if (Files.isDirectory(path))
			{
				// 目录
				Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file,
							BasicFileAttributes attrs) throws IOException {
						indexDoc(writer, file, attrs.lastModifiedTime().toMillis());
						
						return FileVisitResult.CONTINUE;
					}
				});
			} else
			{
				// 文件
				indexDoc(writer, path, Files.getLastModifiedTime(path).toMillis());
			}
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @description 创建文档索引
	 * @param writer
	 * @param path
	 * @param lastModified
	 * @author qye.zheng
	 */
	public static final void indexDoc(final IndexWriter writer, 
			final Path path, final long lastModified)
	{
		try (final InputStream inputStream = Files.newInputStream(path)) 
		{
			 // make a new, empty document
			final Document doc = new Document();
			
			// 路径 字段
			final Field pathField = new StringField(LuceneConstant.FIELD_PATH, path.toString(), Field.Store.YES);
			doc.add(pathField);
			
			// 路径 字段
			//final Field lastModifiedField = new LongField(LuceneConstant.FIELD_MODIFIED, lastModified, Field.Store.NO);
			//doc.add(lastModifiedField);
			
			final Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
			// 内容 字段
			final Field contentField = new TextField(LuceneConstant.FIELD_CONTENTS, reader);
			doc.add(contentField);
			
			if (OpenMode.CREATE == writer.getConfig().getOpenMode())
			{
				 System.out.println("adding " + path);
				// 重新创建模式
				writer.addDocument(doc);
			} else
			{
				System.out.println("updating " + path);
				final Term term = new Term(LuceneConstant.FIELD_PATH, path.toString());
				// 更新模式
				writer.updateDocument(term, doc);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* ================ 创建搜索引擎 ================== */

	/**
	 * 
	 * @description 
	 * 也可以从一个指定的文件中逐行读取查询关键字，
	 * 这里直接采用但一个查询关键字的方式，从文件中
	 * 逐行读取 需要设置一个循环
	 * @param indexPath
	 * @param field
	 * @param repeat
	 * @param queryString
	 * @author qye.zheng
	 */
	public static final void search(final String indexPath, final String field, 
			final int repeat, final String queryString, final int n)
	{
		try {
			// 读取 索引文件
			final IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
			// 搜索器
			final IndexSearcher searcher = new IndexSearcher(reader);
			// 分析器
			final Analyzer analyzer = new StandardAnalyzer();
			// 查询转化器
			final QueryParser queryParser = new QueryParser(field, analyzer);
			// 
			final Query query = queryParser.parse(queryString);
			 System.out.println("Searching for: " + query.toString(field));
			 if (repeat > 0)
			 {
				 for (int i = 0; i < repeat; i++)
				 {
					 // 查询出前 n 条
					final TopDocs results = searcher.search(query, n);
					 final ScoreDoc[] hits = results.scoreDocs;
					 System.out.println(hits.length + " total matching documents");
				 }
			 }
			 reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
