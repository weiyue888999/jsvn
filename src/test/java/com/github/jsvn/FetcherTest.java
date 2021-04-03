package com.github.jsvn;

import org.junit.Test;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

import com.github.jsvn.SVNLogFetcher;
import com.github.jsvn.Utils;
import com.github.jsvn.impl.SVNLogEntryCommitTimeFilter;

import java.util.List;
import java.util.Map;

public class FetcherTest extends BaseTest{

	private void displaySvnLog(List<SVNLogEntry> list1) {
		System.out.println(list1.size());
		for (SVNLogEntry entry : list1) {
			System.out.println(entry.getDate());
			for (Object o : entry.getChangedPaths().entrySet()) {
				
				Map.Entry<String, SVNLogEntryPath> e = (Map.Entry<String, SVNLogEntryPath>) o;
				SVNLogEntryPath svnLogEntryPath = e.getValue();
				char type = svnLogEntryPath.getType();
				String path = svnLogEntryPath.getPath();
				System.out.println(path);
			}
		}
	}

    //@Test
    public void testGetAllSvnLog(){
        SVNLogFetcher fetcher = fetcherFactory.newLogFetcher(false);
        List<SVNLogEntry> list1 = fetcher.fetche("",SVNLogFetcher.LONG_LONG_AGO_REVISION, SVNLogFetcher.LASTED_REVISION);
        displaySvnLog(list1);
    }

   //@Test
    public void testGetCurrentDaySvnLog() {
        SVNLogFetcher fetcher = fetcherFactory.newLogFetcher(false);
        fetcher.addFilter(new SVNLogEntryCommitTimeFilter(Utils.getBeforeDayStartTime(0), Utils.getBeforeDayEndTime(0)));
        List<SVNLogEntry> list1 = fetcher.fetche("",SVNLogFetcher.LONG_LONG_AGO_REVISION, SVNLogFetcher.LASTED_REVISION);
        this.displaySvnLog(list1);
    }
    
    //@Test
    public void testGetLastDaySvnLog() {

        SVNLogFetcher fetcher = fetcherFactory.newLogFetcher(false);
        List<SVNLogEntry> list1 = fetcher.fetche("",SVNLogFetcher.LONG_LONG_AGO_REVISION, SVNLogFetcher.LASTED_REVISION);

        System.out.println(list1.size());
        for (SVNLogEntry entry : list1) {
            System.out.println(entry);
        }
    }
}
