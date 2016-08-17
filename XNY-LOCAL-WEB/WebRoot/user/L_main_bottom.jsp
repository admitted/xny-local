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
<script type="text/javascript" src="../skin/js/des.js"></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
		String p1 = CommUtil.StrToGB2312(request.getParameter("p1"));
		String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
%>
<body>
</body>
<script language="javascript">
DES.init(DesKey, '<%=p1%>');
var D_LinkStr = DES.Encrypt("Decrypt");
location = D_LinkStr;
</script>
</html>