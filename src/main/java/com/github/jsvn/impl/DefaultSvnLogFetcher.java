package com.github.jsvn.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.io.SVNRepository;

import java.util.*;

/**
 * @author weiguangyue
 */
class DefaultSvnLogFetcher extends AbstractSvnLogFetcher{

    private static final Logger log = LoggerFactory.getLogger(DefaultSvnLogFetcher.class);

    private SVNRepository svnRepository;

    public DefaultSvnLogFetcher(SVNRepository svnRepository) {
        this.svnRepository = svnRepository;
    }

    /**
     *拉取日志Handler
     */
    private class FetcherHandler implements ISVNLogEntryHandler {
    	
    	private final List<SVNLogEntry> svnLogEntryList;
    	
        public FetcherHandler(List<SVNLogEntry> svnLogEntryList) {
			super();
			this.svnLogEntryList = svnLogEntryList;
		}

		@Override
        public void handleLogEntry(SVNLogEntry logEntry) throws SVNException {
			this.svnLogEntryList.add(logEntry);
        }
    }

	@Override
	public List<SVNLogEntry> fetche(String svnChildPath, long startRevision, long endRevision) {
		final List<SVNLogEntry> svnLogEntryList = new LinkedList<SVNLogEntry>();
        try {
            this.svnRepository.log(new String[]{svnChildPath},startRevision,endRevision,true,true,Integer.MAX_VALUE,new FetcherHandler(svnLogEntryList));
            return this.doFilter(svnLogEntryList);
        } catch (SVNException e) {
            log.error("fetch log for svnChildPath[{}] error",svnChildPath,e);
        }
        return null;
	}

	@Override
	public List<SVNLogEntry> fetche(String svnChildPath, long revision) {
		return this.fetche(svnChildPath, revision, revision);
	}
}
