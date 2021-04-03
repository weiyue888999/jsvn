package com.github.jsvn;

import java.io.File;

import org.tmatesoft.svn.core.SVNException;

/**
 * @author weiguangyue
 */
public interface SVNCheckoutFetcher {
	
	/**
	 * @description	： 检出代码
	 * @param revesion svn版本号
	 * @param targetDir 目标目录
	 * @throws SVNException
	 */
	void checkout(long revesion,File targetDir)  throws SVNException;
	
}
