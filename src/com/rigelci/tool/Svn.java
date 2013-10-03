package com.rigelci.tool;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class Svn {
	
	public static Map<String, String> modelM = new HashMap<String, String>();
	public static final String PATH = "/home/work/zhangkai05/xml/";
	public static final String URL_ROOT = "https://svn.baidu.com/app/ecom/shifen/sf-crm/trunk/onedata/02_数据模型/onedata/rel/";
	public static final String USERNAME = "zhangkai05";
	public static final String PASSWORD = "zhangkai05";
	public static void main(String[] args) throws Exception {
		//testSvnHeaderVersion();
		// 根据传入的table xml  ID先获取最新版本到本地
		checkOutFromSvn("435EB821-FCAA-0FC6-D107-31D1A93F339B.xml",null,null);
	}
	
	static {
		modelM.put("DataFlow", "6C4237561-9B5EAAA16246");
		modelM.put("cdc_cust_feature", "$db_cdc_cust_feature");
		modelM.put("dwa", "$db_dwa");
		modelM.put("dwd", "$db_dwd");
		modelM.put("ods", "$db_ods");
		SVNRepositoryFactoryImpl.setup();
		// 初始化支持https://协议的库。 必须先执行此操作。
		DAVRepositoryFactory.setup();
		//初始化支持file:///协议的库。 必须先执行此操作。
		FSRepositoryFactory.setup();
	}
	
	public static String checkOutFromSvn(String tableId,String project, String modelName)  {
		//获取SVN驱动选项
				ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
				// 实例化客户端管理类
				SVNClientManager ourClientManager = SVNClientManager.newInstance((DefaultSVNOptions) options, USERNAME, PASSWORD);
		SVNURL repositoryURL = null;
		// 需要循环seg_*
		try {
			// 通过客户端管理类获得updateClient类的实例。
			SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
			int is_h=0;
			
			for (int i = 0; i < 100; i++) {
				String seg="seg_"+i;
				String url=URL_ROOT + modelM.get("DataFlow") + "/table/";
				url+=seg;
				String filepath = PATH + seg;
				del(filepath);
				repositoryURL = SVNURL.parseURIEncoded(url);
				boolean b = isURLExist(repositoryURL, "zhangkai05", "zhangkai05");
				if(b) {
					is_h++;
					updateClient.setIgnoreExternals(true);
					// 执行check out 操作，返回工作副本的版本号。
					long workingVersion = updateClient.doCheckout(repositoryURL, new File(filepath),SVNRevision.HEAD, SVNRevision.HEAD, SVNDepth.INFINITY,false);
					//System.out.println(workingVersion + "版本获得成功!");
				}
			}
			
			//is_h
			String file_path = getFile(tableId, PATH, is_h);
			return file_path;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getFile(String file_name, String path, int is_h) {
		for (int i = 0; i < is_h; i++) {
			File file = new File(path + "/seg_" + i);
			File fs[] = file.listFiles();
			  for (int j = 0; j < fs.length; j++) {
				  if(file_name.equals(fs[j].getName())) {
					  return path + "/seg_" + i + "/" + file_name;
				  }
			  }
		}
		
		  return null;
	}
	
	
	public static void del(String filepath) throws Exception{  
		File f = new File(filepath);//定义文件路径         
		if(f.exists() && f.isDirectory()){//判断是文件还是目录  
		    if(f.listFiles().length==0){//若目录下没有文件则直接删除  
		        f.delete();  
		    }else{//若有则把文件放进数组，并判断是否有下级目录  
		        File delFile[]=f.listFiles();  
		        int i =f.listFiles().length;  
		        for(int j=0;j<i;j++){  
		            if(delFile[j].isDirectory()){  
		                    del(delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径  
		            }  
		            delFile[j].delete();//删除文件  
		        }  
		    }  
		}      
		}  
	
    /** 
     * 确定path是否是一个工作空间 
     * @param path 
     * @return 
     */  
    public static boolean isWorkingCopy(File path){  
        if(!path.exists()){  
            return false;  
        }  
        try {  
            if(null == SVNWCUtil.getWorkingCopyRoot(path, false)){  
                return false;  
            }  
        } catch (Exception e) {  
        }  
        return true;  
    }  
      
    /** 
     * 确定一个URL在SVN上是否存在 
     * @param url 
     * @return 
     */  
    public static boolean isURLExist(SVNURL url,String username,String password){  
        try {  
            SVNRepository svnRepository = SVNRepositoryFactory.create(url);  
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);  
            svnRepository.setAuthenticationManager(authManager);  
            SVNNodeKind nodeKind = svnRepository.checkPath("", -1);  
            return nodeKind == SVNNodeKind.NONE ? false : true;   
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return false;  
    }  
	
}
