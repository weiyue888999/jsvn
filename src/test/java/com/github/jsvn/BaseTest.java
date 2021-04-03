package com.github.jsvn;

import com.github.jsvn.SVNFetcherFactory;
import com.github.jsvn.impl.DefaultSvnFetcherFactory;

public class BaseTest {

    protected String svnRepositoryUrl = "http://127.0.0.1/svn/xx";
    protected String path = "/xx/xx/branches/V-1.0/";
    protected String username = "weiguangyue";
    protected String password = "weiguangyue";

    protected SVNFetcherFactory fetcherFactory = new DefaultSvnFetcherFactory(svnRepositoryUrl, path, username, password);
}
