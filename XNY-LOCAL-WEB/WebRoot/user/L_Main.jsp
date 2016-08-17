<%@ page contentType="text/html; charset=gb2312"%>
<%@ page import="java.util.*"%>
<%@ page import="bean.*"%>
<%@ page import="util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中新能源LNG公司级信息化管理平台</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
		String p1  = CommUtil.StrToGB2312(request.getParameter("p1"));
		String p2  = CommUtil.StrToGB2312(request.getParameter("p2"));
		String p3  = CommUtil.StrToGB2312(request.getParameter("p3"));
		String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
%>

<frameset rows="111,*" cols="*" frameborder="NO" border="0" framespacing="0px">
 	<frame src="L_main_top.jsp?Sid=<%=Sid%>&p2=<%=p2%>&p3=<%=p3%>" name="tFrame" scrolling="NO" noResize frameborder="no">
	<frame src="L_main_bottom.jsp?Sid=<%=Sid%>&p1=<%=p1%>"         name="mFrame" scrolling="NO" noResize frameborder="no">
</frameset>

</html>