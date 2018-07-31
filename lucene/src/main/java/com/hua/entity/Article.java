/**
  * @filename Article.java
  * @description 
  * @version 1.0
  * @author qianye.zheng
 */
package com.hua.entity;

 /**
 * @type Article
 * @description 文章
 * @author qianye.zheng
 */
public class Article
{
	
	private Integer id;
	
	/* 标题 */
	private String title;
	
	/* 作者 */
	private String author;
	
	/* 内容 */
	private String content;

	/**
	 * @return the id
	 */
	public final Integer getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(Integer id)
	{
		this.id = id;
	}

	/**
	 * @return the title
	 */
	public final String getTitle()
	{
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public final void setTitle(String title)
	{
		this.title = title;
	}

	/**
	 * @return the author
	 */
	public final String getAuthor()
	{
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public final void setAuthor(String author)
	{
		this.author = author;
	}

	/**
	 * @return the content
	 */
	public final String getContent()
	{
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public final void setContent(String content)
	{
		this.content = content;
	}
	
}
