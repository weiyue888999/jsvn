package com.github.jsvn.impl;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.github.jsvn.SVNLogEntryReturnResultFilter;

import java.util.Date;

/**
 * @author weiguangyue
 */
public class SVNLogEntryCommitTimeFilter implements SVNLogEntryReturnResultFilter {

	/**
	 * 开始时间
	 */
    private Date startCommitTime;
    /**
     * 结束时间
     */
    private Date endCommitTime;

    public SVNLogEntryCommitTimeFilter(Date startCommitTime, Date endCommitTime) {
        this.startCommitTime = startCommitTime;
        this.endCommitTime = endCommitTime;
    }

    @Override
    public SVNLogEntry getFilteResult(SVNLogEntry logEntry) {
        Date commitDate = logEntry.getDate();
        long t = commitDate.getTime();
        if(this.startCommitTime.getTime() <= t && t <= this.endCommitTime.getTime()){
            return logEntry;
        }
        return null;
    }
}
