package com.github.jsvn.impl;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;
import org.tmatesoft.svn.util.SVNDebugLogAdapter;
import org.tmatesoft.svn.util.SVNLogType;

import com.github.jsvn.SVNCheckoutFetcher;

/**
 * @author weiguangyue
 */
class DefaultSvnCheckoutFetcher implements SVNCheckoutFetcher {
	
	private static final Logger log = LoggerFactory.getLogger(DefaultSvnCheckoutFetcher.class);
	
	private SVNURL svnUrl;
	private static SVNClientManager ourClientManager;
	private ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
	private SVNUpdateClient updateClient;
	private SVNWCClient wcclient;
	
	public DefaultSvnCheckoutFetcher(SVNURL svnUrl,String name,String password) {
		super();
		this.svnUrl = svnUrl;
		this.ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, name, password);
		this.updateClient = ourClientManager.getUpdateClient();
		this.wcclient = this.ourClientManager.getWCClient();
	}

	@Override
	public void checkout(long revesion, File workDir) throws SVNException {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		/**
		log.info("清理目录:"+workDir);
		try {
			FileUtils.cleanDirectory(workDir);
			log.info("清理目录:{}完成",workDir);
		} catch (IOException e2) {
			log.error("清理目录{}异常",workDir);
		}
		**/
		
		log.info("检出svn代码reversion[{}]到目录[{}]",revesion,workDir);
		this.updateClient.setIgnoreExternals(false);
		this.updateClient.setDebugLog(new ConsoleSVNDebugLogger());
		SVNRevision svnRevision = SVNRevision.create(revesion);
		try {
			this.updateClient.doCheckout(svnUrl, workDir, svnRevision, svnRevision, SVNDepth.INFINITY,true);			
		}catch (SVNException e) {
			log.error("发生异常"+e.getMessage()+",清空目录[{}]并重试",workDir);
			try {
				FileUtils.cleanDirectory(workDir);
			} catch (IOException e1) {
				log.error("",e1);
			}
			this.updateClient.doCheckout(svnUrl, workDir, svnRevision, svnRevision, SVNDepth.INFINITY,true);
		}
		stopWatch.stop();
		log.info("检出代码完成,耗时{}ms",stopWatch.getTime());
	}
	
	/**
	 * @description	： svn日志
	 * @author 		：魏广跃（1571）
	 */
	static class ConsoleSVNDebugLogger extends SVNDebugLogAdapter {
		
		private static final Logger log = LoggerFactory.getLogger(ConsoleSVNDebugLogger.class);
		private static final Logger log_client = LoggerFactory.getLogger(ConsoleSVNDebugLogger.class+"_client");
		private static final Logger log_wc = LoggerFactory.getLogger(ConsoleSVNDebugLogger.class+"_wc");
		private static final Logger log_default = LoggerFactory.getLogger(ConsoleSVNDebugLogger.class+"_default");
		private static final Logger log_network = LoggerFactory.getLogger(ConsoleSVNDebugLogger.class+"_network");
		private static final Logger log_fsfs = LoggerFactory.getLogger(ConsoleSVNDebugLogger.class+"_fsfs");

		private static Logger getLogger(SVNLogType logType) {
			if (logType == SVNLogType.WC) {
				return log_wc;
			}else if(logType == SVNLogType.CLIENT) {
				return log_client;
			}else if(logType == SVNLogType.DEFAULT) {
				return log_default;
			}else if(logType == SVNLogType.NETWORK) {
				return log_network;
			}else if(logType == SVNLogType.FSFS) {
				return log_fsfs;
			}else {
				return log;
			}
		}
		
		@Override
		public void log(SVNLogType logType, Throwable th, Level logLevel) {
			//if (logType == SVNLogType.WC) {
			//	log_wc.error("",th);
			//}
		}

		@Override
		public void log(SVNLogType logType, String message, Level logLevel) {
			//if (logType == SVNLogType.WC) {
			//	log_wc.info(message);
			//}
		}

		@Override
		public void log(SVNLogType logType, String message, byte[] data) {
			//if (logType == SVNLogType.WC) {
			//	log_wc.info(message);
			//}
		}
	}
}
