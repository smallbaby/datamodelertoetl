package com.rigelci.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class CS
 */
public class CS extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public CS() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("content-type","text/html;charset=UTF-8");
		response.setContentType("text/xml");  
        response.setHeader("Cache-Control", "no-cache");  
        PrintWriter out = response.getWriter();  
        
		String project = request.getParameter("project");
		String modelName = request.getParameter("modelName");
		String tableId = request.getParameter("tableId");
		String dbName = request.getParameter("dbName");
		
		try {
			 String filepath = Svn.checkOutFromSvn(tableId + ".xml",project, modelName); // 从svn获得最新代码
			 Map<String,String> m = AutoCreateJet.autoCreateMeta(filepath, project, modelName,dbName);
			 out.write(m.get("ctsql") + "^^^^^^^" + m.get("meta") + "^^^^^^^" + m.get("sql") + "^^^^^^^" + m.get("yaml")); 
			} catch (Exception e) {
				out.write("转换失败");
			}
		
		
		
	}
}
