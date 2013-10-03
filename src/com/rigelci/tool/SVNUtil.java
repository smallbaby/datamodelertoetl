package com.rigelci.tool;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNUtil {

	 private String svnRoot;

	    private String userName;

	    private String password;

	    private SVNRepository repository;

	   

	    /***

	     * 构造方法

	     * @param svnRoot

	     *             svn根目录

	     */

	    public SVNUtil(String svnRoot) {

	       this.svnRoot=svnRoot;

	    }

	    /***

	     * 构造方法

	     * @param svnRoot

	     *             svn根目录

	     * @param userName

	     *             登录用户名

	     * @param password

	     *             登录密码

	     */

	    public SVNUtil(String svnRoot, String userName, String password) {

	       this.svnRoot=svnRoot;

	       this.userName=userName;

	       this.password=password;

	    }

	/***

	     * 通过不同的协议初始化版本库

	     */

	    private static void setupLibrary() {

	       // 对于使用http://和https：//

	       DAVRepositoryFactory.setup();

	       //对于使用svn：/ /和svn+xxx：/ /

	       SVNRepositoryFactoryImpl.setup();

	       //对于使用file://

	       FSRepositoryFactory.setup();

	}

	//每次连接库都进行登陆验证

	    /***

	     * 登录验证

	     * @return

	     */

	    public boolean login(){

	       setupLibrary();

	       try{

	           //创建库连接

	          repository=SVNRepositoryFactory.create(SVNURL.parseURIEncoded(this.svnRoot));

	           //身份验证

	           ISVNAuthenticationManager authManager = SVNWCUtil

	           .createDefaultAuthenticationManager(this.userName,

	                  this.password);
	           //创建身份验证管理器
	           repository.setAuthenticationManager(authManager);
	           return true;
	       } catch(SVNException svne){
	           svne.printStackTrace();
	           return false;
	       }
	    }
}