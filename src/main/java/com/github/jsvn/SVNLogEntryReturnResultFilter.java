package com.github.jsvn;

import org.tmatesoft.svn.core.SVNLogEntry;

/**
 * @author weiguangyue
 */
public interface SVNLogEntryReturnResultFilter {

    /**
     * 获得过滤结果
     * @param logEntry
     * @return 结果，有可能是null
     */
    SVNLogEntry getFilteResult(SVNLogEntry logEntry);
}
