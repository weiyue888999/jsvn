package com.github.jsvn.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.tmatesoft.svn.core.SVNLogEntry;

import com.github.jsvn.SVNLogEntryReturnResultFilter;
import com.github.jsvn.SVNLogFetcher;

/**
 * @description	： 抽象的svnLogFetcher
 * @author 		：魏广跃（1571）
 */
abstract class AbstractSvnLogFetcher implements SVNLogFetcher{

	private final List<SVNLogEntryReturnResultFilter> svnLogFilterList  = new ArrayList<SVNLogEntryReturnResultFilter>();
	
    @Override
    public boolean addFilter(SVNLogEntryReturnResultFilter filter){
    	if(this.svnLogFilterList.contains(filter)) {
    		throw new IllegalArgumentException("can not add filter twice");
    	}
        return this.svnLogFilterList.add(filter);
    }

    @Override
    public boolean removeFilter(SVNLogEntryReturnResultFilter filter) {
        return this.svnLogFilterList.remove(filter);
    }
    
    public List<SVNLogEntryReturnResultFilter> getFilters(){
    	return Collections.unmodifiableList(this.svnLogFilterList);
    }
    
    protected List<SVNLogEntry> doFilter(List<SVNLogEntry> svnLogEntryList) {
    	
        final List<SVNLogEntryReturnResultFilter> list = this.getFilters();
        if(!list.isEmpty()){
        	
        	List<SVNLogEntry> resultSvnLogEntryList = new ArrayList<SVNLogEntry>();
        	
        	Iterator<SVNLogEntry> it_svn_log_entry = svnLogEntryList.iterator();
        	while(it_svn_log_entry.hasNext()) {
        		
        		SVNLogEntry svnLogEntry = it_svn_log_entry.next();
        		SVNLogEntry result = null;
        		
        		for(SVNLogEntryReturnResultFilter filter : list) {
        			result = filter.getFilteResult(svnLogEntry);
        			if(result == null){
        				break;
        			}
        		}
        		if(result != null) {
        			resultSvnLogEntryList.add(result);        			
        		}
        	}
        	return resultSvnLogEntryList;
        }else {
        	return svnLogEntryList;        	
        }
    }
}
