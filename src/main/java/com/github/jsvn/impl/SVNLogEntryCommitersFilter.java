package com.github.jsvn.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.github.jsvn.SVNLogEntryReturnResultFilter;

public class SVNLogEntryCommitersFilter implements SVNLogEntryReturnResultFilter{
	/**
	 * 提交作者
	 */
    private Set<String> authors = new HashSet<String>();

    public SVNLogEntryCommitersFilter(String[] authors) {
        this.authors.addAll(Arrays.asList(authors));
    }

    @Override
    public SVNLogEntry getFilteResult(SVNLogEntry logEntry) {
        String author = logEntry.getAuthor();
        if(this.authors.contains(author)){
            return logEntry;
        }
        return null;
    }
}
