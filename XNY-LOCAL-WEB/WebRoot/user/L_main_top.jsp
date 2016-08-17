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

	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
	ArrayList User_FP_Role = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
	ArrayList User_Manage_Role = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	UserInfoBean UserInfo = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
	String CName = UserInfo.getCName();
	String FpId = UserInfo.getFp_Role();
	String ManageId = UserInfo.getManage_Role();
	String FpIdName = "无";
	String ManageName = "无";
	
	if(null != FpId && FpId.length() > 0 && null != User_FP_Role)
	{
		Iterator roleiter = User_FP_Role.iterator();
		while(roleiter.hasNext())
		{
			UserRoleBean roleBean = (UserRoleBean)roleiter.next();
			if(roleBean.getId().equals(FpId))
			{
				FpIdName = roleBean.getCName();
			}
		}
	}
	
	if(null != ManageId && ManageId.length() > 0 && null != User_Manage_Role)
	{
		Iterator roleiter = User_Manage_Role.iterator();
		while(roleiter.hasNext())
		{
			UserRoleBean roleBean = (UserRoleBean)roleiter.next();
			if(roleBean.getId().equals(ManageId))
			{
				ManageName = roleBean.getCName();
			}
		}
	}
	
	String p2 = CommUtil.StrToGB2312(request.getParameter("p2"));
	String p3 = CommUtil.StrToGB2312(request.getParameter("p3"));
	if(null == p3)
	{
		p3 = "1";
	}
	
%>
<body>
	<div class="top">
		<div class="logo"><img src="../skin/images/logo_sub_application_manage.gif" /></div>
		<div class="home" style="cursor:hand"><img onClick="ReturnMain()" src="../skin/images/home_return.gif" /></div>
		<div class="exit" style="cursor:hand"><img onClick="doExit()"     src="../skin/images/exit.gif" /></div>
		<div class="bar">
			<div class="ren"><img src="../skin/images/ren.gif" width="13" height="15" /></div>
			<div class="js">您好: <%=CName%> [功能权限: <font color=green><%=FpIdName%></font> | 管理权限: <font color=green><%=ManageName%></font>]</div>
			<div class="line"><img src="../skin/images/bar_line.gif" /> </div>
			<div class="js2" id="time"></div>
			<div id="banner_r">
				<font color=#ffffff>当前位置:&nbsp;<%=p2%>&nbsp;</font>
			</div>
		</div>
	</div>
</body>
<SCRIPT LANGUAGE=javascript>
//setInterval("time.innerHTML= new Date().toLocaleString()+' 星期'+'日一二三四五六'.charAt(new Date().getDay());",1000);
setInterval("time.innerHTML= new Date().toLocaleString().substring(0, new Date().toLocaleString().indexOf('日')+1)",1000);

function doExit()
{
    if(confirm("确定要安全退出本系统?"))
    {
      alert("谢谢您的使用!");
			location = "ILogout.do?Sid=<%=Sid%>";
		}
}

function ReturnMain()
{
	switch(parseInt('<%=p3%>'))
	{
		case 1:
				window.parent.location = "MapMain.jsp?Sid=<%=Sid%>&p3=<%=p3%>";
			break;
		case 2:
				window.parent.location = "MapFullScreen.jsp?Sid=<%=Sid%>&p3=<%=p3%>";
			break;
		case 3:
				window.parent.location = "MapMain.jsp?Sid=<%=Sid%>&p3=<%=p3%>";
			break;
		default:
				window.parent.location = "MapMain.jsp?Sid=<%=Sid%>&p3=<%=p3%>";
			break;
	}
}
</SCRIPT>
</html>