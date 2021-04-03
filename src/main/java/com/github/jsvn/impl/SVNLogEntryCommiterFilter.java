package com.github.jsvn.impl;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.github.jsvn.SVNLogEntryReturnResultFilter;

/**
 * @author weiguangyue
 */
public class SVNLogEntryCommiterFilter implements SVNLogEntryReturnResultFilter {

	/**
	 * 提交作者
	 */
    private String author;

    public SVNLogEntryCommiterFilter(String author) {
        this.author = author;
    }

    @Override
    public SVNLogEntry getFilteResult(SVNLogEntry logEntry) {
        String author = logEntry.getAuthor();
        if(this.author.equals(author)){
            return logEntry;
        }
        return null;
    }
}
