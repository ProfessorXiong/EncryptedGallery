package com.xiong.lockedgallery;

import java.io.Serializable;

/**
 * 图片实体类
 * @author Administrator
 *
 */
public class Picture implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String title; //标题
	String picUrl;//图片地址
	String description;//描述
	String ctime;//创建时间
	String url;	//链接地址
}
