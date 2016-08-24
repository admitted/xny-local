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
  String BDate = currStatus.getVecDate().get(0).toString().substring(0,10);
	String EDate = currStatus.getVecDate().get(1).toString().substring(0,10);
	
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
<form name="Alert_Info"  action="Alert_Info.do" method="post" target="mFrame">
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
				告警类型:
				<select name='Func_Sub_Id' style='width:90px;height:20px' onChange="doSelect()">
					<option value='9' <%=currStatus.getFunc_Sub_Id() == 9 ? "selected":""%>>全部</option>
					<option value='1' <%=currStatus.getFunc_Sub_Id() == 1 ? "selected":""%>>系统告警</option>
					<option value='2' <%=currStatus.getFunc_Sub_Id() == 2 ? "selected":""%>>数据告警</option>
				</select>
				记录状态:
				<select name='Func_Sel_Id' style='width:90px;height:20px' onChange="doSelect()">
					<option value='9' <%=currStatus.getFunc_Sel_Id() == 9 ? "selected":""%>>全部</option>
					<option value='0' <%=currStatus.getFunc_Sel_Id() == 0 ? "selected":""%>>未处理</option>
					<option value='1' <%=currStatus.getFunc_Sel_Id() == 1 ? "selected":""%>>人工已处理</option>
					<option value='2' <%=currStatus.getFunc_Sel_Id() == 2 ? "selected":""%>>系统已处理</option>
				</select>
				<input id='BDate' name='BDate' type='text' style='width:90px;height:18px;display:none;' value='<%=BDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
				-
				<input id='EDate' name='EDate' type='text' style='width:90px;height:18px;display:none;' value='<%=EDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
			</td>
			<td width='30%' align='right'>				
				<img id="img1" src="../skin/images/mini_button_search.gif"  onClick='doSelect()' style='cursor:hand;'>
				<img id="img2" src="../skin/images/excel.gif"               onClick='doExport()' style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='070202' ctype='1'/>">
			</td>
		</tr>
		<tr height='30' valign='middle'>
			<td width='100%' align='center' colspan=2>
				<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
					<tr height='25' valign='middle'>
						<td width='3%'  align='center' class="table_deep_blue">序号</td>
						<td width='8%'  align='center' class="table_deep_blue">站点</td>
						<td width='8%'  align='center' class="table_deep_blue">设备</td>
						<td width='8%'  align='center' class="table_deep_blue">类型</td>
						<td width='12%' align='center' class="table_deep_blue">时间</td>
						<td width='8%'  align='center' class="table_deep_blue">级别</td>
						<td width='16%' align='center' class="table_deep_blue">描述</td>
						<td width='8%'  align='center' class="table_deep_blue">状态</td>
						<td width='12%' align='center' class="table_deep_blue">恢复时间</td>
						<td width='8%'  align='center' class="table_deep_blue">操作员</td>
					</tr>
					<%
					 if(Alert_Info != null)
					 {
						Iterator iterator = Alert_Info.iterator();
						while(iterator.hasNext())
						{
							AlertInfoBean Bean = (AlertInfoBean)iterator.next();
							String Seq = Bean.getSN();
							String Cpm_Id = Bean.getCpm_Id();
							String Id = Bean.getId();
							String Attr_Id = Bean.getAttr_Id();						
							String Cpm_Name = Bean.getCpm_Name();
							String CName  = Bean.getCName();
							String Level  = Bean.getLevel();
							String CTime  = Bean.getCTime();
							String Lev    = Bean.getLev();
							String CData  = Bean.getCData();
							String Status = Bean.getStatus();
							String ETime  = Bean.getETime();
							String Operator = Bean.getOperator();
							
							if(null == Cpm_Id){Cpm_Id = "";}
							if(null == Id){Id = "";}
							if(null == Attr_Id){Attr_Id = "";}
							if(null == CName){CName = "";}
							if(null == CData){CData = "";}
							if(null == Lev){Lev = "";}
							if(null == ETime){ETime = "";}
							if(null == Operator){Operator = "";}
							
							String str_Level = "";
							switch(Integer.parseInt(Level))
							{
								case 1:
										str_Level = "系统告警";
									break;
								case 2:
										str_Level = "数据告警";
									break;
							}
							
							String str_Status = "";
							switch(Integer.parseInt(Status))
							{
								case 0:
										str_Status = "未处理";
									break;
								case 1:
										str_Status = "人工已处理";
									break;
								case 2:
										str_Status = "系统已处理";
									break;
							}
							
							sn ++;
					%>
				  <tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>
				  	<td align=center><%=sn%></td>
						<td align=center><%=Cpm_Name%>&nbsp;</td>
				    <td align=center><%=CName%>&nbsp;</td>
				    <td align=center><%=str_Level%></td>
				    <td align=center><%=CTime%></td>
				    <td align=center><%=Lev%>&nbsp;</td>
						<td align=left><%=CData%>&nbsp;</td>
						<%
						if(Status.equals("0"))
						{
						%>
							<td align=center style="cursor:hand " onmouseout="this.style.color='#000000';" onmouseover="this.style.color='#FF0000';"  title="点击进行人工处理" onClick="doDeal('<%=Seq%>', '<%=Cpm_Id%>', '<%=Id%>', '<%=Attr_Id%>')"><%=str_Status%></td>
						<%
						}
						else
						{
						%>
							<td align=center><%=str_Status%></td>
						<%
						}
						%>
						<td align=center><%=ETime%>&nbsp;</td>
						<td align=center><%=Operator%>&nbsp;</td>
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
				      	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
				      </tr>
					<%
						}
					  else
					  {
					%>					
		          <tr <%=((i%2)==0?"class='table_white_l'":"class='table_blue'")%>>
			          <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
			        </tr>	        
					<%
       			}
     			}
					%> 
     		 	<tr>
						<td colspan="10" class="table_deep_blue" >
				 			<table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" >
			    			<tr valign="bottom">
			      			<td nowrap><%=currStatus.GetPageHtml("Alert_Info")%></td>
			    			</tr>			    		
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
<input type="hidden" name="Cmd" value="0">
<input type="hidden" name="Sid" value="<%=Sid%>">
<input type="hidden" name="Id"    value="">
<input type="hidden" name="Level" value="">
<input type="hidden" name="BTime" value="">
<input type="hidden" name="ETime" value="">
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

switch(parseInt(<%=currStatus.getFunc_Sel_Id()%>))
{
	case 0:
			document.getElementById('BDate').style.display = 'none';
			document.getElementById('EDate').style.display = 'none';
		break;
	case 1:
	case 2:
	case 9:
			document.getElementById('BDate').style.display = '';
			document.getElementById('EDate').style.display = '';
		break;
}

function doSelect()
{
	/**var days = new Date(Alert_Info.EDate.value.replace(/-/g, "/")).getTime() - new Date(Alert_Info.BDate.value.replace(/-/g, "/")).getTime();
	var dcnt = parseInt(days/(1000*60*60*24));
	if(dcnt < 0)
	{
		alert('截止日期需大于开始日期');
		return;
	}
	if((dcnt + 1) > 31)
	{
		alert('日期跨越不超过31天');
		return;
	}**/
	
	Alert_Info.Id.value = Alert_Info.Func_Cpm_Id.value;
	Alert_Info.Level.value = Alert_Info.Func_Sub_Id.value;
	Alert_Info.BTime.value = Alert_Info.BDate.value + " 00:00:00";
	Alert_Info.ETime.value = Alert_Info.EDate.value + " 23:59:59";
	Alert_Info.submit();
}

function GoPage(pPage)
{
/**	var days = new Date(Alert_Info.EDate.value.replace(/-/g, "/")).getTime() - new Date(Alert_Info.BDate.value.replace(/-/g, "/")).getTime();
	var dcnt = parseInt(days/(1000*60*60*24));
	if(dcnt < 0)
	{
		alert('截止日期需大于开始日期');
		return;
	}
	if((dcnt + 1) > 31)
	{
		alert('日期跨越不超过31天');
		return;
	}
	**/
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
	Alert_Info.Id.value = Alert_Info.Func_Cpm_Id.value;
	Alert_Info.Level.value = Alert_Info.Func_Sub_Id.value;
	Alert_Info.BTime.value = Alert_Info.BDate.value + " 00:00:00";
	Alert_Info.ETime.value = Alert_Info.EDate.value + " 23:59:59";
	Alert_Info.CurrPage.value = pPage;
	Alert_Info.submit();
}

var req = null;
function doExport()
{	
	/**var days = new Date(Alert_Info.EDate.value.replace(/-/g, "/")).getTime() - new Date(Alert_Info.BDate.value.replace(/-/g, "/")).getTime();
	var dcnt = parseInt(days/(1000*60*60*24));
	if(dcnt < 0)
	{
		alert('截止日期需大于开始日期');
		return;
	}
	if((dcnt + 1) > 31)
	{
		alert('日期跨越不超过31天');
		return;
	}
	**/
	if(0 == <%=sn%>)
	{
		alert('当前无记录!');
		return;
	}
	if(65000 <= <%=currStatus.getTotalRecord()%>)
	{
		alert('记录过多，请分批导出!');
		return;
	}
	if(confirm("确定导出?"))
  {
		if(window.XMLHttpRequest)
	  {
			req = new XMLHttpRequest();
		}
		else if(window.ActiveXObject)
		{
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}		
		//设置回调函数
		req.onreadystatechange = callbackForName;
		var url = "Alert_Info_Export.do?Sid=<%=Sid%>&CurrPage=<%=currStatus.getCurrPage()%>&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>&Func_Sel_Id=<%=currStatus.getFunc_Sel_Id()%>&BTime="+Alert_Info.BDate.value+" 00:00:00"+"&ETime="+Alert_Info.EDate.value+" 23:59:59"+"&Id="+Alert_Info.Func_Cpm_Id.value+"&Level="+Alert_Info.Func_Sub_Id.value+"&currtime="+new Date();
		req.open("post",url,true);
		req.send(null);
		return true;
	}
}
function callbackForName()
{
	var state = req.readyState;
	if(state==4)
	{
		var resp = req.responseText;			
		var str = "";
		if(resp != null)
		{
			location.href = "../../files/excel/" + resp + ".xls";
		}
	}
}

var reqAct = null;
function doDeal(pSN, pCpm_Id, pId, pAttr_Id)
{
	if('<Limit:limitValidate userrole='<%=FpList%>' fpid='070102' ctype='1'/>' == 'none')
	{
		alert('您无权限处理告警!');
		return;
	}
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
		var url = "Alert_Deal.do?Sid=<%=Sid%>&CurrPage=<%=currStatus.getCurrPage()%>&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>&Func_Sel_Id=<%=currStatus.getFunc_Sel_Id()%>&Operator=<%=UserInfo.getId()%>&SN="+pSN+"&Cpm_Id="+pCpm_Id+"&Id="+pId+"&Attr_Id="+pAttr_Id+"&BTime="+Alert_Info.BDate.value+" 00:00:00"+"&ETime="+Alert_Info.EDate.value+" 23:59:59"+"&Level="+Alert_Info.Func_Sub_Id.value+"&currtime="+new Date();
		reqAct.open("post",url,true);
		reqAct.send(null);
		return true;
	}
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
</SCRIPT>
</html>