package com.github.jsvn;

import org.tmatesoft.svn.core.SVNLogEntry;

import java.util.List;

/**
 * @author weiguangyue
 */
public interface SVNLogFetcher {
	/**
	 * 年代久远的提交日志标识
	 */
    public static final long LONG_LONG_AGO_REVISION = 0;
    /**
     * 最新的提交日志标识
     */
    public static final long LASTED_REVISION = -1;

    /**
     * @description	： 获得所有顾虑器
     * @return
     */
    List<SVNLogEntryReturnResultFilter> getFilters();
    
    /**
     * @param filter 要追加的过滤器
     * @return
     */
    boolean addFilter(SVNLogEntryReturnResultFilter filter);

    /**
     * @param filter 要移除过滤器
     * @return
     */
    boolean removeFilter(SVNLogEntryReturnResultFilter filter);
    
    /**
     * 拉取日志SVNLogEntry对象列表
     * @param svnChildPath svn子路径
     * @param startRevision 开始版本号
     * @param endRevision 结束版本号
     * @return
     */
    List<SVNLogEntry> fetche(String svnChildPath,long startRevision,long endRevision);

    /**
     * 拉取日志SVNLogEntry对象列表
     * @param svnChildPath svn子路径
     * @param revision 版本号
     * @return
     */
    List<SVNLogEntry> fetche(String svnChildPath,long revision);
}
