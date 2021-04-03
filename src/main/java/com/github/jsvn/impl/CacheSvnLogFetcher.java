package com.github.jsvn.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.github.jsvn.SVNLogFetcher;

/**
 * 缓存SvnLog的CacheSvnLogFetcher
 * 当拉取svn日志变得更慢的时候，需要使用这个类来缓存，避免多次http请求
 * 开发这个程序，需要对接教务部门的svn代码库，将尽300M的代码量
 * 日志提交量也是相当多的。。。
 * @author 魏广跃
 */
class CacheSvnLogFetcher extends AbstractSvnLogFetcher{
	
	private Map<CacheKey,List<SVNLogEntry>> cache = new HashMap<CacheKey,List<SVNLogEntry>>();
	
	private SVNLogFetcher svnLogFetcher;
	
	public CacheSvnLogFetcher(SVNLogFetcher svnLogFetcher) {
		super();
		this.svnLogFetcher = svnLogFetcher;
	}

	@Override
	public List<SVNLogEntry> fetche(String svnChildPath, long startRevision, long endRevision) {
		
		CacheKey cacheKey = new CacheKey(svnChildPath, Long.valueOf(startRevision), Long.valueOf(endRevision));
		List<SVNLogEntry> list = cache.get(cacheKey);
		if(list == null) {
			list = this.svnLogFetcher.fetche(svnChildPath, startRevision, endRevision);
			cache.put(cacheKey, list);
		}
		if(list.isEmpty()) {
			return list;
		}
		return this.doFilter(list);
	}

	@Override
	public List<SVNLogEntry> fetche(String svnChildPath, long revision) {
		return this.fetche(svnChildPath, revision,revision);
	}
	
	/**
	 * 缓存的key对象
	 * @author 		：魏广跃（1571）
	 */
	private static class CacheKey {
		String svnChildPath;
		Long startRevision;
		Long endRevision;
		
		public CacheKey(String svnChildPath, Long startRevision, Long endRevision) {
			super();
			this.svnChildPath = svnChildPath;
			this.startRevision = startRevision;
			this.endRevision = endRevision;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((endRevision == null) ? 0 : endRevision.hashCode());
			result = prime * result + ((startRevision == null) ? 0 : startRevision.hashCode());
			result = prime * result + ((svnChildPath == null) ? 0 : svnChildPath.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey other = (CacheKey) obj;
			if (endRevision == null) {
				if (other.endRevision != null)
					return false;
			} else if (!endRevision.equals(other.endRevision))
				return false;
			if (startRevision == null) {
				if (other.startRevision != null)
					return false;
			} else if (!startRevision.equals(other.startRevision))
				return false;
			if (svnChildPath == null) {
				if (other.svnChildPath != null)
					return false;
			} else if (!svnChildPath.equals(other.svnChildPath))
				return false;
			return true;
		}
	}
}
