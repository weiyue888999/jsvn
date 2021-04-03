package com.github.jsvn.impl;

import java.util.Map;

import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import com.github.jsvn.SVNLogEntryReturnResultFilter;

/**
 * 匹配基础路径的filter
 * 
 * @author weiguangyue
 */
public class SVNLogEntrySvnBasePathMatcherFilter implements SVNLogEntryReturnResultFilter {
	
	private String svnBasePath;
	
	public SVNLogEntrySvnBasePathMatcherFilter(String svnBasePath) {
		super();
		this.svnBasePath = svnBasePath;
	}

	@Override
	public SVNLogEntry getFilteResult(SVNLogEntry logEntry) {
		
		for (Object o : logEntry.getChangedPaths().entrySet()) {
			Map.Entry<String, SVNLogEntryPath> e = (Map.Entry<String, SVNLogEntryPath>) o;
			SVNLogEntryPath svnLogEntryPath = e.getValue();
			String path = svnLogEntryPath.getPath();
			if(path.startsWith(this.svnBasePath)) {
				return logEntry;
			}
		}
		return null;
	}
}
