<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery1.10.1.js"></script>
<script type="text/javascript" src="js/image.js"></script>

<link rel="stylesheet" type="text/css" href="js/shadowbox-3.0.3/shadowbox.css">
<script type="text/javascript" src="js/shadowbox-3.0.3/shadowbox.js"></script>

<script type="text/javascript">

	Shadowbox.init({
   	 	overlayOpacity: 0.8
	}, setupDemos);

	function sleep(n) { 
		var start = new Date().getTime(); 
		while(true)  
			if(new Date().getTime()-start > n) 
				break; 
	}
	
	function showLog(id,log,color) {
		$("#"+id).html($("#"+id).html()+"<font color='" + color + "'><h4>" + log + "</h3></font>");
	}

	$(function() {
		
		$("#createBtn").click(function() {
			$("#createBtn").attr("disabled", true);
			$("#log").html("");
			$("#ctsql").val("");
		 	$("#meta").val("");
		 	$("#sql").val("");
		 	$("#yaml").val("");
		    $(".cshow").hide();
		    if($("#tableId").val() == "") {
		    	alert("表对象ID不符合预期，请重新填写...");
		    	return;
		    }
			// 开始打印一些提示信息
			 showLog("log","正在从SVN获取最新版本.....","red");
			 $.post("CS",{dbName:$("#dbName").val(),modelName:$("#modelName").val(), project:$("#project").val(),tableId:$("#tableId").val()},function(res){
				 	showLog("log","SUCCESS.........","red");
				 	var re = res.split("^^^^^^^");
				 	var ctsql=re[0];
				 	var meta=re[1];
				 	var sql=re[2];
				 	var yaml=re[3];
				 	$("#ctsql").val(ctsql);
				 	$("#meta").val(meta);
				 	$("#sql").val(sql);
				 	$("#yaml").val(yaml);
				    $(".cshow").show(3000);
				    $("#createBtn").removeAttr("disabled");
			 },'text');
			 sleep(3000);// 3秒
			 showLog("log","获取最新版本成功...","red");
			 sleep(2000);
			 showLog("log","正在生成建表语句meta.........","red");
			 sleep(2000);
			 showLog("log","生成成功正在返回.........","red");
		});
	});
</script>
</head>
<body  bgcolor="#F0F0F0">
<h2>dmap前戏<h4>请使用opera浏览器...</h4><h5>显示屏要大...</h5></h2>
<form action="CS">
	<div>
		工程名:<select name="project" id="project">
				<option selected="selected" value="etl_task">etl_task</option>
				<option value="cdc_cust_feature">cdc_cust_feature</option>
				<option value="cdc_dm">cdc_dm</option>
				<option value="cdc_publish">cdc_publish</option>
			  </select>
		模型层名称:<select name="modelName" id="modelName"  style="WIDTH: 100px">
				<option selected="selected" value="DataFlow">DataFlow</option>
				<option>DWA</option>
				<option>DWD</option>
				<option>ODS</option>
			  </select>
		数据库名称:<select name="dbName" id="dbName"  style="WIDTH: 70px">  
		<option selected="selected" value="dwd"> dwd </option>
		<option value="dwa"> dwa </option> 
		<option value="ods"> ods </option>
		<option value="pub"> pub </option>
	  </select>
			  <br/><br/>
		表ID:<input type="text" name="tableId" id="tableId" size="50" value="48F1DAB7-42AA-22E7-0564-EBABF3E4504D"/>
		获取表iD的方法【点击查看大图】:
		<a rel="shadowbox" href="a.png"><img alt="Tiger" width="100px" height="100px" class="border" src="a.png"></a>
	</div>
	<br/><br/>
	<input type="button" id="createBtn" value="生成"/>
	<br/>
</form>
<div id="log"></div>
<div class="cshow" style="display:none">
	<span><textarea id="ctsql"  cols="40" rows="70"></textarea></span> &nbsp;
	<span><textarea id="meta"  cols="40" rows="70"></textarea></span>&nbsp;
	<span><textarea id="sql"  cols="40" rows="70"></textarea></span>
	<span><textarea id="yaml"  cols="40" rows="70"></textarea></span>
	</div>
</body>
</html>