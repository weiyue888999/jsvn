package com.github.jsvn;

/**
 * @author weiguangyue
 */
public interface SVNFetcherFactory {

    /**
     * new一个日志拉取对象
     * @return
     */
    SVNLogFetcher newLogFetcher(boolean cache);
    
    /**
     * new一个检出对象
     * @param checkoutPath
     * @return
     */
	SVNCheckoutFetcher newCheckoutFetcher(String checkoutPath);

}
