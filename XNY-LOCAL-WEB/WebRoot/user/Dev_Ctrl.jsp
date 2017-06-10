<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<%@ taglib uri="/WEB-INF/limitvalidatetag.tld" prefix="Limit"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>中海油LNG加气站公司级信息化管理平台</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type='text/javascript' src='../skin/js/browser.js' charset='gb2312'></script>
<script type="text/javascript" src="../skin/js/My97DatePicker/WdatePicker.js"></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%

	String Sid = CommUtil.StrToGB2312(request.getParameter("Sid"));
	ArrayList User_FP_Role = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
	UserInfoBean UserInfo = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
	ArrayList User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	ArrayList User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
	String ManageId = UserInfo.getManage_Role();
	String FpId = UserInfo.getFp_Role();
	String FpList = "";
	if(null != FpId && FpId.length() > 0 && null != User_FP_Role)
	{
		Iterator roleiter = User_FP_Role.iterator();
		while(roleiter.hasNext())
		{
			UserRoleBean roleBean = (UserRoleBean)roleiter.next();
			if(roleBean.getId().equals(FpId) && null != roleBean.getPoint())
			{
				FpList = roleBean.getPoint();
			}
		}
	}
	
  CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  
  ArrayList Dev_Ctrl = (ArrayList)session.getAttribute("Dev_Ctrl_" + Sid);
	
  ArrayList Alert_Info = (ArrayList)session.getAttribute("Alert_Info_" + Sid);         
  int sn = 0;
  String Manage_List = "";
  if(ManageId.length() > 0 && null != User_Manage_Role)
	{
			Iterator iterator = User_Manage_Role.iterator();
			while(iterator.hasNext())
			{
					UserRoleBean statBean = (UserRoleBean)iterator.next();
					if(statBean.getId().equals(ManageId))
					{
							String R_Point = statBean.getPoint();
							if(null == R_Point){R_Point = "";}
							Manage_List += R_Point;
					}
			}
	}						
	String Dept_Id = UserInfo.getDept_Id();
	if(Dept_Id.length()>3){Manage_List = Dept_Id; }
  
%>
<body style=" background:#CADFFF">
<form name="Dev_Ctrl"  action="Dev_Ctrl.do" method="post" target="mFrame">
<div id="down_bg_2">	
	<table width="100%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
		<tr height='25px' class='sjtop'>
			<td width='70%' align='left'>
					加气站点:
				<select  name='Func_Cpm_Id' style='width:100px;height:20px' onChange="doSelect()" >			
					<option value="<%=Manage_List%>" <%=currStatus.getFunc_Cpm_Id().equals(Manage_List)?"selected":""%>>全部站点</option>		
						<%								
								if(Manage_List.length() > 0 && null != User_Device_Detail)
								{
									Iterator iterator = User_Device_Detail.iterator();
									while(iterator.hasNext())
									{
										DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();
										if(Manage_List.contains(statBean.getId()))
										{
								%>
											<option value='<%=statBean.getId()%>' <%=currStatus.getFunc_Cpm_Id().equals(statBean.getId())?"selected":""%>><%=statBean.getBrief()%></option>
								<%
										}
									}
								}
								%>
				</select>				
				</td>
			<td width='30%' align='right'>				
				<img id="img1" src="../skin/images/mini_button_search.gif"  onClick='doSelect()' style='cursor:hand;'>
				<img src="../skin/images/mini_button_sync.gif" style='cursor:hand;' onClick='syncAll()'>
			</td>
		</tr>
		<tr height='30' valign='middle'>
			<td width='100%' align='center' colspan=2>
				<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
					<tr height='25' valign='middle'>
						<td width='5%'  align='center' class="table_deep_blue">序号</td>
						<td width='10%'  align='center' class="table_deep_blue">站点</td>
						<td width='10%'  align='center' class="table_deep_blue">设备</td>
						<td width='10%'  align='center' class="table_deep_blue">名称</td>
						<td width='10%'  align='center' class="table_deep_blue">状态</td>
						<td width='10%'  align='center' class="table_deep_blue">操作</td>
					</tr>
					<%
					if(null != Dev_Ctrl)
					{
						Iterator iterator = Dev_Ctrl.iterator();
						while(iterator.hasNext())
						{
							DevCtrlBean bean = (DevCtrlBean)iterator.next();
							String cpm_Id = bean.getCpm_Id();
							String cpm_Brief = bean.getCpm_Brief();
							String dev_Type = bean.getDev_Type();
							String dev_Id = bean.getDev_Id();
							String dev_Name = bean.getDev_Name();
							String attr_Id = bean.getAttr_Id();
							String attr_Name = bean.getAttr_Name();
							String strValue = "";
							if(bean.getValue().equals("1.00")){strValue = "闭合";}
							else{strValue = "断开";}
							String[] action = bean.getAction().split(";");
							sn ++;
					%>
				  <tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>
				  	<td align=center><%=sn%></td>
						<td align=center><%=cpm_Brief%>&nbsp;</td>
				    <td align=center><%=dev_Name%>&nbsp;</td>
				    <td align=center><%=attr_Name%></td>
				    <td align=center><%=strValue%>&nbsp;</td>
						<td align=center>
						<%
						for(int i = 0; i < action.length; i ++)
						{
							String act_Id = action[i].split(",")[0];
							String act_Name = action[i].split(",")[1].substring(4);
						%>
							<a style="cursor:hand;color:blue " onmouseout="this.style.color='blue';" onmouseover="this.style.color='#FF0000';"  title="点击进行人工处理" onClick="doControl('1','<%=cpm_Id%>','<%=dev_Type%>','<%=dev_Id%>','<%=act_Id%>')"><%=act_Name%></a>&nbsp
						<%
						}
						%>
						</td>
					</tr>
					<%	
						}
					}
					for(int i=0;i<(MsgBean.CONST_PAGE_SIZE - sn);i++)
					{
						if(sn % 2 != 0)
					  {
					%>					  
				      <tr <%=((i%2)==0?"class='table_blue'":"class='table_white_l'")%>>
				      	</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
				      </tr>
					<%
						}
					  else
					  {
					%>					
		          <tr <%=((i%2)==0?"class='table_white_l'":"class='table_blue'")%>>
			          </td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
			        </tr>	        
					<%
       			}
     			}
					%> 
     		 	<tr>
						<td colspan="10" class="table_deep_blue" >
				 			<table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" >
			    			<tr valign="bottom">
			      			<td nowrap><%=currStatus.GetPageHtml("Dev_Ctrl")%></td>
			    			</tr>			    		
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
<input type="hidden" name="Cmd"   value="0">
<input type="hidden" name="Sid"   value="<%=Sid%>">
<input type="hidden" name="CurrPage" value="<%=currStatus.getCurrPage()%>">
<input type="button" id="CurrButton" onClick="doSelect()" style="display:none">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
//ipad禁掉导出
if(1 == fBrowserRedirect() || 2 == fBrowserRedirect())
{
	document.getElementById('img2').style.display = 'none';
}

function GoPage(pPage)
{
	if(pPage == "")
	{
		 alert("请输入目标页面的数值!");
		 return;
	}
	if(pPage < 1)
	{
	   	alert("请输入页数大于1");
		  return;	
	}
	if(pPage > <%=currStatus.getTotalPages()%>)
	{
		pPage = <%=currStatus.getTotalPages()%>;
	}
	Dev_Ctrl.CurrPage.value = pPage;
	Dev_Ctrl.submit();
}

var reqAct = null;
function doControl(pCmd, pCpm_Id, pDev_Type, pDev_Id, pAct_Id)
{
	/*if('<Limit:limitValidate userrole='<%=FpList%>' fpid='070102' ctype='1'/>' == 'none')
	{
		alert('您无权限处理!');
		return;
	}*/
	if(confirm('确认处理?'))
	{
		if(window.XMLHttpRequest)
	  {
			reqAct = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			reqAct = new ActiveXObject("Microsoft.XMLHTTP");
		}
		//设置回调函数
		reqAct.onreadystatechange = callbackForActName;
		var url = "Control.do?Cmd="+pCmd+"&Sid=<%=Sid%>&cpm_Id="+pCpm_Id+"&dev_Type="+pDev_Type+"&dev_Id="+pDev_Id+"&act_Id="+pAct_Id+"&currtime="+new Date();
		reqAct.open("post",url,true);
		reqAct.send(null);
		return true;
	}
}
function syncAll()
{
	if(confirm('确认处理?'))
	{
		var cpm_Id = Dev_Ctrl.Func_Cpm_Id.value.split(",");
		for(var i = 0; i < cpm_Id.length; i ++)
		{
			doSync("2",cpm_Id[i]);
			doSync("3",cpm_Id[i]);
			doSync("4",cpm_Id[i]);
		}
	}
}
var reqSync = null;
function doSync(pCmd, pCpm_Id)
{
	/*if('<Limit:limitValidate userrole='<%=FpList%>' fpid='070102' ctype='1'/>' == 'none')
	{
		alert('您无权限处理!');
		return;
	}*/
	if(window.XMLHttpRequest)
  {
		reqSync = new XMLHttpRequest();
	}
	else if(window.ActiveXObject)
	{
		reqSync = new ActiveXObject("Microsoft.XMLHTTP");
	}
	var url = "synchronous.do?Cmd="+pCmd+"&cpm_Id="+pCpm_Id+"&Sid=<%=Sid%>&currtime="+new Date();
	reqSync.open("post",url,true);
	reqSync.send(null);
	return true;
}

function callbackForActName()
{
	var state = reqAct.readyState;
	if(state == 4)
	{
		if(reqAct.status == 200)
		{
			var resp = reqAct.responseText;			
			if(resp != null && resp.substring(0,4) == '0000')
			{
				alert('成功');
				location.reload();
				return;
			}
			else if(resp != null && resp.substring(0,4) == '3000')
			{
				alert('提交成功');
				location.reload();
				return;
			}
			else
			{
				alert('失败，请重新操作');
				return;
			}
		}
		else
		{
			alert("失败，请重新操作");
			return;
		}
	}
}
function doSelect()
{
	location = "Dev_Ctrl.do?Cmd=0&Sid=<%=Sid%>&Func_Cpm_Id=" + Dev_Ctrl.Func_Cpm_Id.value;
}

</SCRIPT>
</html>