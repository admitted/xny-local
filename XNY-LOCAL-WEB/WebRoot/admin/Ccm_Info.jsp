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
<style type='text/css'>
html,body {height:100%; margin:0px; font-size:12px;}
.mydiv
{
	background-color: #e0e6ed;
	border: 1px solid #3491D6;
	text-align: center;
	line-height: 40px;
	font-size: 12px;
	font-weight: bold;
	z-index:999;
	width: 300px;
	height:100px;
	left:35%;
	top: 30%;
	position:fixed!important;
	position:absolute;
	_top:expression(eval(document.compatMode && document.compatMode=='CSS1Compat') ? documentElement.scrollTop + (document.documentElement.clientHeight-this.offsetHeight)/2 : document.body.scrollTop + (document.body.clientHeight - this.clientHeight)/2);
}
</style>
</head>
<%
	
	String Sid    = CommUtil.StrToGB2312(request.getParameter("Sid"));
	String Crm_Id = CommUtil.StrToGB2312(request.getParameter("Crm_Id"));
	
  CurrStatus currStatus  = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  CorpInfoBean Corp_Info = (CorpInfoBean)session.getAttribute("Corp_Info_" + Sid);
	ArrayList Ccm_Info     = (ArrayList)session.getAttribute("Ccm_Info_" + Sid);
	String Car_Info = "";
	if(null != Corp_Info)
	{
		Car_Info = Corp_Info.getCar_Info();
		if(null == Car_Info)
		{
			Car_Info = "";
		}
	}	
%>
<body style="background:#CADFFF">
<form name="Ccm_Info" action="Ccm_Info.do" method="post" target="_self">
	<table width="100%" style='margin:auto;' border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
		<tr>
			<td width="10%"  class="table_deep_blue">
				<img src='../skin/images/diyBtn1.gif' style='cursor:hand' title='点击添加' onclick="doAdd()">
				序号
			</td>
			<td width="15%" class="table_deep_blue">车辆牌号</td>
			<td width="15%" class="table_deep_blue">车辆类型</td>
			<td width="15%" class="table_deep_blue">车辆司机</td>
			<td width="15%" class="table_deep_blue">车载瓶号</td>
			<td width="30%" class="table_deep_blue">IC卡号</td>
		</tr>
		<%
		if(Ccm_Info != null)
		{
			int sn = 0;
			Iterator iterator = Ccm_Info.iterator();
			while(iterator.hasNext())
			{
				CcmInfoBean statBean = (CcmInfoBean)iterator.next();
				String Id     = statBean.getId();
				String CType  = statBean.getCType();
				String Owner  = statBean.getOwner();
				String Bottle = statBean.getBottle();
				String IC     = statBean.getIC();
				if(null == IC)
				{
					IC = "";
				}
				
				String str_CType = "";
				if(Car_Info.trim().length() > 0)
				{
					String[] List = Car_Info.split(";");
				  for(int i=0; i<List.length && List[i].length()>0; i++)
				  {
				  	String[] subList = List[i].split(",");
				  	if(subList[0].equals(CType))
				  	{
				  		str_CType = subList[1];
				  		break;
				  	}
				  }
				}
				
				sn++;
		%>
				<tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>							
					<td align=center><a href="#" title="点击编辑" onClick="doEdit('<%=Id%>')"><U><%=sn%></U></a></td>
					<td align=left>
						<%=Id%>
						<img src='../skin/images/cmddel.gif' title='删除此车' onclick="DeCar('<%=Id%>','<%=Crm_Id%>')"  >
					</td>
					<td align=left><%=str_CType%></td>
			  	<td align=left><%=Owner%></td>
			  	<td align=left><%=Bottle%></td>
			  	<td align=left>
			  		&nbsp;<img src='../skin/images/device_cmdadd.png' style='cursor:hand' title='卡号绑定' onclick="doCardAdd('<%=Id%>', '<%=IC%>')"><br>
				  	<%
				  	if(IC.length() > 0)
				  	{
				  		String[] List = IC.split(";");
				  		for(int i=0; i<List.length; i++)
				  		{
				  			String[] subList = List[i].split(",");
				  			String subStatus = "";
				  			switch(Integer.parseInt(subList[1]))
				  			{
				  				case 0:
				  						subStatus = "<font color=green>[启用]</font>";
				  					break;
				  				case 1:
				  						subStatus = "<font color=gray>[注销]</font>";
				  					break;
				  			}
				  	%>
				  			&nbsp;<img src='../skin/images/cmddel.gif' style='cursor:hand' title='卡号删除' onClick="doCardDel('<%=Id%>', '<%=IC%>', '<%=subList[0]%>')">
				  			&nbsp;<%=subList[0]%>&nbsp;<a href='#' title='状态重置' onClick="doCardSta('<%=Id%>', '<%=IC%>', '<%=subList[0]%>', '<%=subList[1]%>')"><%=subStatus%></a>
				  			<br>
				  	<%
				  		}
				  	}
				  	else
				  	{
				  	%>
				  		&nbsp;未绑定卡!
				  	<%
				  	}
				  	%>
			  	</td>
				</tr>
		<%
			}
		}
		%>
	</table>
	<div id='popDiv' class='mydiv' style='display:none;margin:auto'></div>
	<input name="Cmd"         type="hidden" value="0">
	<input name="Sid"         type="hidden" value="<%=Sid%>">
	<input name="Crm_Id"      type="hidden" value="<%=Crm_Id%>">
	<input name="Func_Sub_Id" type="hidden" value="<%=currStatus.getFunc_Sub_Id()%>">
</form>
</body>
<SCRIPT LANGUAGE=javascript>	
if(<%=currStatus.getResult().length()%> > 0)
   alert("<%=currStatus.getResult()%>");
<%
currStatus.setResult("");
session.setAttribute("CurrStatus_" + Sid, currStatus);
%>

//车辆查询
function doSelect()
{
	Ccm_Info.submit();
}

//车辆添加
function doAdd()
{

	location = "Ccm_Info_Edit.jsp?Sid=<%=Sid%>&Crm_Id=<%=Crm_Id%>&Cmd=10";
}

//车辆编辑
function doEdit(pId)
{
	location = "Ccm_Info_Edit.jsp?Sid=<%=Sid%>&Crm_Id=<%=Crm_Id%>&Cmd=11&Id="+pId;
}

//卡号绑定
function closeDiv()
{
	document.getElementById('popDiv').style.display = 'none';
}

function doCardAdd(pId, pIC)
{
	document.getElementById('popDiv').style.display = 'block';
	var url = "Ccm_Info_Card.jsp?Sid=<%=Sid%>&Crm_Id=<%=Crm_Id%>&Id="+pId+"&IC="+pIC;
	document.getElementById('popDiv').innerHTML = "<iframe id='divFrame' name='divFrame' src='"+url+"' style='width:100%;height:100%' frameborder=0 scrolling='no'></iframe>";
}

//卡号删除
function doCardDel(pId, pIC, pDelCard)
{
	var IC = '';
	var list = pIC.split(';');
	for(var i=0; i<list.length && list[i].length>0; i++)
	{
		if(list[i].indexOf(pDelCard) < 0)
		{
			IC += list[i] + ';';
		}
	}
	if(confirm('确认删除当前卡号?'))
	{
		if(window.XMLHttpRequest)
		{
			reqEdit = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqEdit = new ActiveXObject('Microsoft.XMLHTTP');
		}
		reqEdit.onreadystatechange = function()
		{
			var state = reqEdit.readyState;
			if(state==4)
			{
				var resp = reqEdit.responseText;
				if(resp != null && resp == "0000")
				{	
					alert("删除成功!");
					doSelect();
					return;
				}
				else
				{
					alert("失败,请重新操作!");
					return;
				}
			}
		};
		var url = 'Ccm_Info_Card.do?Cmd=14&Sid=<%=Sid%>&Crm_Id=<%=Crm_Id%>&Id='+pId+'&IC='+IC+'&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>&currtime='+new Date();
		reqEdit.open("get", url);
		reqEdit.setRequestHeader('If-Modified-Since', '0');
		reqEdit.send(null);
		return true;
	}
}

//状态重置
function doCardSta(pId, pIC, pStaCard, pSta)
{
	var currFirm = '';
	var cardsta = '';
	switch(parseInt(pSta))
	{
		case 0:
				currFirm = '确认将当前卡号置为[注销]?';
				cardsta = '1';
			break;
		case 1:
				currFirm = '确认将当前卡号置为[启用]?';
				cardsta = '0';
			break;
	}
	
	var IC = '';
	var list = pIC.split(';');
	for(var i=0; i<list.length && list[i].length>0; i++)
	{
		if(list[i].indexOf(pStaCard) < 0)
		{
			IC += list[i] + ';';
		}
		else
		{
			IC += list[i].split(',')[0] + ',' + cardsta + ';';
		}
	}
	
	if(confirm(currFirm))
	{
		if(window.XMLHttpRequest)
		{
			reqEdit = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqEdit = new ActiveXObject('Microsoft.XMLHTTP');
		}
		reqEdit.onreadystatechange = function()
		{
			var state = reqEdit.readyState;
			if(state==4)
			{
				var resp = reqEdit.responseText;
				if(resp != null && resp == "0000")
				{	
					alert("重置成功!");
					doSelect();
					return;
				}
				else
				{
					alert("失败,请重新操作!");
					return;
				}
			}
		};
		var url = 'Ccm_Info_Card.do?Cmd=14&Sid=<%=Sid%>&Crm_Id=<%=Crm_Id%>&Id='+pId+'&IC='+IC+'&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>&currtime='+new Date();
		reqEdit.open("get", url);
		reqEdit.setRequestHeader('If-Modified-Since', '0');
		reqEdit.send(null);
		return true;
	}
}
function DeCar(pId,pCrm_Id)
{
	if(confirm("确定删除当前车辆?"))
	{
		location="Ccm_Info.do?Cmd=15&Sid=<%=Sid%>&Id="+pId+"&Crm_Id="+pCrm_Id;
	}		
}
</SCRIPT>
</html>