package com.github.jsvn.impl;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.util.DefaultSVNDebugLogger;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.github.jsvn.SVNCheckoutFetcher;
import com.github.jsvn.SVNFetcherFactory;
import com.github.jsvn.SVNLogFetcher;

/**
 * @author weiguangyue
 */
public class DefaultSvnFetcherFactory implements SVNFetcherFactory {
    
    private static final Logger log = LoggerFactory.getLogger(DefaultSvnFetcherFactory.class);
    
	static {
		Properties properties = System.getProperties();  
	    properties.setProperty("svnkit.http.methods", "Basic,Digest,NTLM"); 
	}

    private String svnRepositoryUrl;
    private SVNURL svnUrl;
    private String path;
    private String username;
    private String password;
    private SVNRepository svnRepository;

    public DefaultSvnFetcherFactory(String svnRepositoryUrl, String path, String username, String password) {
    	
        this.svnRepositoryUrl = svnRepositoryUrl;
        this.path = path;
        this.username = username;
        this.password = password;
        
        log.info("init");
        log.info("svnRepositoryUrl:"+svnRepositoryUrl);
        log.info("path:"+path);
        
        DAVRepositoryFactory.setup();
        try {
        	this.svnUrl = SVNURL.parseURIEncoded(svnRepositoryUrl);
            this.svnRepository = SVNRepositoryFactory.create(this.svnUrl);
            ISVNAuthenticationManager authenticationManager = SVNWCUtil.createDefaultAuthenticationManager(username,password);
            this.svnRepository.setAuthenticationManager(authenticationManager);
            this.svnRepository.setDebugLog(new DefaultSVNDebugLogger());
            this.svnRepository.testConnection();

        } catch (SVNException e) {
            log.error("init svn repository error",e);
        }
    }

    @Override
    public SVNLogFetcher newLogFetcher(boolean cache) {
    	SVNLogFetcher svnLogFetcher = new DefaultSvnLogFetcher(this.svnRepository);
    	if(cache) {
    		return new CacheSvnLogFetcher(svnLogFetcher);
    	}else {
    		return svnLogFetcher;    		
    	}
    }

	@Override
	public SVNCheckoutFetcher newCheckoutFetcher(String checkoutPath) {
		SVNURL checkoutSvnUrl = null;
		try {
			checkoutSvnUrl = SVNURL.parseURIEncoded(checkoutPath);
		} catch (SVNException e) {
			log.error("parse svn url error",e);
		}
		return new DefaultSvnCheckoutFetcher(checkoutSvnUrl,this.username,this.password);
	}
}
