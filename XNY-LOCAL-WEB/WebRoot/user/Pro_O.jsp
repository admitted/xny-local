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
<title>中新能源LNG公司级信息化管理平台</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type='text/javascript' src='../skin/js/browser.js' charset='gb2312'></script>
<script type="text/javascript" src="../skin/js/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="../skin/js/util.js"></script>
<script type='text/javascript' src='../skin/js/zDrag.js'   charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/zDialog.js' charset='gb2312'></script>

</head>
<%
	
	String    	 Sid                = CommUtil.StrToGB2312(request.getParameter("Sid"));
	ArrayList    User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
	ArrayList    User_FP_Role       = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
	ArrayList    User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	UserInfoBean UserInfo           = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
	ArrayList    Pro_Status         = (ArrayList)session.getAttribute("Pro_Status" + Sid);	

	String ManageId = UserInfo.getManage_Role();
	String Operator = UserInfo.getId();
	String FpId     = UserInfo.getFp_Role(); //001
	String FpList   = ""; //FpId对应的管理权限
	//System.out.println("FpId:" + FpId);
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
	
	ArrayList Pro_R_Type            = (ArrayList)session.getAttribute("Pro_R_Type_" + Sid);
	CorpInfoBean Corp_Info          = (CorpInfoBean)session.getAttribute("User_Corp_Info_" + Sid);
	String Oil_Info = "";
	String Car_Info = "";
	if(null != Corp_Info)
	{
		Oil_Info = Corp_Info.getOil_Info();
		Car_Info = Corp_Info.getCar_Info();
		if(null == Oil_Info){Oil_Info = "";}
		if(null == Car_Info){Car_Info = "";}
	}
	
	CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
  String BDate = currStatus.getVecDate().get(0).toString().substring(0,10);
	String EDate = currStatus.getVecDate().get(1).toString().substring(0,10);		
  ArrayList Pro_O = (ArrayList)session.getAttribute("Pro_O_" + Sid);
  ArrayList Pro_O_ALL = (ArrayList)session.getAttribute("Pro_O_ALL" + Sid);
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
	String Oil_CType       = "";
	String Oil_CName       = "无";		
	String Cpm_Name        = "";
	String Unit            = "";			
	double Value_O_All     = 0.0;			
													
%>
<body style="background:#CADFFF">
<form name="Pro_O"  action="Pro_O.do" method="post" target="mFrame">
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
				燃料类型:
				<select name='Func_Corp_Id' style='width:100px;height:20px' onChange="doSelect()">				
					<%
					if(null != Pro_R_Type)
					{
						Iterator typeiter = Pro_R_Type.iterator();
						while(typeiter.hasNext())
						{
							ProRBean typeBean = (ProRBean)typeiter.next();
							String type_Id = typeBean.getOil_CType();
							String type_Name = "无";
													
							if(Oil_Info.trim().length() > 0)
							{
							  String[] List = Oil_Info.split(";");
							  for(int i=0; i<List.length && List[i].length()>0; i++)
							  {
							  	String[] subList = List[i].split(",");
							  	if(type_Id.equals(subList[0]))
							  	{
							  		type_Name = subList[1];
							  		break;
							  	}
					  		}
					  	}
					%>
					  	<option value='<%=type_Id%>' <%=currStatus.getFunc_Corp_Id().equals(type_Id)?"selected":""%>><%=type_Id%>|<%=type_Name%></option>
					<%				  	
						}
					}
					%>
				</select>
				<!--记录状态:
				<select name='Func_Sub_Id' style='width:90px;height:20px' onChange="doSelect()">
					<option value='9' <%=currStatus.getFunc_Sub_Id() == 9 ? "selected":""%>>全部记录</option>
					<option value='0' <%=currStatus.getFunc_Sub_Id() == 0 ? "selected":""%>>审核有效</option>
					<option value='1' <%=currStatus.getFunc_Sub_Id() == 1 ? "selected":""%>>审核无效</option>
				</select>
				-->
				<input name='BDate' type='text' style='width:90px;height:18px;' value='<%=BDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
				<input name='EDate' type='text' style='width:90px;height:18px;' value='<%=EDate%>' onClick='WdatePicker({readOnly:true})' class='Wdate' maxlength='10'>
				单号:
				<input type='text' name='Func_Type_Id' style='width:100px;height:16px;' value='<%=currStatus.getFunc_Type_Id()%>'>							
			</td>
			<td width='30%' align='right'>						
				<img id="img1" src="../skin/images/mini_button_search.gif" onClick='doSelect()' style='cursor:hand;'>
				<img id="img1" src="../skin/images/scrb.gif" onClick='doTJ()' style='cursor:hand;' style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='020304' ctype='1'/>">
				<img id="img2" src="../skin/images/excel.gif"              onClick='doExport()' style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='020302' ctype='1'/>">
				<img id="img3" src="../skin/images/mini_button_add.gif"    onClick='doAdd()'    style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='020303' ctype='1'/>">
				<img id="img3" src="../skin/images/shanchu.gif"    onClick='doDel()'    style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='020303' ctype='1'/>">
			</td>
		</tr>
		<tr height='30' valign='middle'>
			<td width='100%' align='center' colspan=2>
				<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
					<tr height='25' valign='middle'>
						<td width='10%'  align='center' class="table_deep_blue">序号</td>
						<td width='12%' align='center' class="table_deep_blue">加注站点</td>
						<td width='12%' align='center' class="table_deep_blue">燃料类型</td>
						<td width='10%' align='center' class="table_deep_blue">单号</td>
						<td width='10%' align='center' class="table_deep_blue">加注时间</td>
						<td width='10%' align='center' class="table_deep_blue">加注数量</td>
						<td width='10%' align='center' class="table_deep_blue">车辆牌号</td>
						<td width='16%' align='center' class="table_deep_blue">所属单位</td>
						<td width='10%' align='center' class="table_deep_blue">明细查询</td>								
					</tr>
					<!--查询总数-->
					<%
						if(Pro_O_ALL != null)
						{
							Iterator itall = Pro_O_ALL.iterator();
							while(itall.hasNext())
							{
							ProOBean pbean =(ProOBean)itall.next();
							String value_all = pbean.getValue();
							if(value_all == null ){ value_all = "0.00";}
							
					%>		
						<tr height='25' valign='middle'>						
						<td colspan='2' align='center' ><strong>总计</strong></td>
						
						<td colspan='3' align='center' ><strong>加注数量</strong></td>
						<td colspan='3' align='center' >&nbsp;<strong><font color='red'><%=value_all%>kg</font></strong></td>
						<td align='center' ><strong><a href="#" onclick="DeSn()">全选</a></strong></td>							
					</tr>
					<%		
							}
						}
					%>
					
					
					<%
					 if(Pro_O != null)
					 {					 
						Iterator iterator = Pro_O.iterator();
						while(iterator.hasNext())
						{
							ProOBean Bean = (ProOBean)iterator.next();
							String SN        = Bean.getSN();
							String pCpm_Id   = Bean.getCpm_Id();
							
							if( null != User_Device_Detail)
								{
									Iterator Uiterator = User_Device_Detail.iterator();
									while(Uiterator.hasNext())
									{
										DeviceDetailBean sBean = (DeviceDetailBean)Uiterator.next();
										if(sBean.getId().equals(pCpm_Id))
										{
											Cpm_Name = sBean.getBrief();
										}
									}
								}
							
							
							Oil_CType = Bean.getOil_CType();
							String CTime     = Bean.getCTime();
							String str_stime = CTime.substring(0,10);
							String Memo      = Bean.getMemo();
							
							//加注信息
							String Value     = Bean.getValue();
							Value_O_All     = Value_O_All + Double.parseDouble(Value);
							String Value_Gas = Bean.getValue_Gas();
							String Price     = Bean.getPrice();
						  String Amt       = Bean.getAmt();
							String Worker    = Bean.getWorker();
							
							//客户信息
							String Unq_Flag  = Bean.getUnq_Flag();
							String Unq_Str   = Bean.getUnq_Str();
						
							String Car_CType = Bean.getCar_CType();
							String Car_Owner = Bean.getCar_Owner();
							String Car_BH 	 = Bean.getCar_BH();
							String Car_DW 	 = Bean.getCar_DW();
							String DW_ID     = Bean.getDW_ID();
							
							//审核信息
							String Status        = Bean.getStatus();
						
							
							//单号
							String Fill_Number = Bean.getFill_Number();
							
							if(null == Oil_CType || Oil_CType.trim().length() < 1){Oil_CType = "1000";}
							if(null == Memo){Memo = "";}
							if(null == Value){Value = "";}
							if(null == Value_Gas){Value_Gas = "";}
							if(null == Price){Price = "";}
							if(null == Amt){Amt = "";}
							if(null == Worker){Worker = "";}
							if(null == Unq_Flag){Unq_Flag = "0";}
							if(null == Unq_Str){Unq_Str = "";}
							
							if(null == Car_CType){Car_CType = "";}
							if(null == Car_Owner){Car_Owner = "";}
							if(null == Car_BH){Car_BH = "";}
							if(null == Car_DW){Car_DW = "";}
													
							if(Oil_Info.trim().length() > 0)
							{
							  String[] List = Oil_Info.split(";");
							  for(int i=0; i<List.length && List[i].length()>0; i++)
							  {
							  	String[] subList = List[i].split(",");
							  	if(subList[0].equals(Oil_CType))
							  	{
							  		Oil_CName = subList[0]+"|"+subList[1];
							  		break;
							  	}
							  }
							}
							
							String Oil_Detail = "";
							try
							{
								switch(Integer.parseInt(Oil_CType))
								{
									default:
									case 1000://汽油
									case 1010://90#汽油
									
											Oil_Detail =  Value + "L";
											Unit  = "L";
										break;
									case 3001://CNG
									case 3002://LNG
											Oil_Detail =  Value + "kg";
											Unit  = "kg";
										break;
								}
							}
							catch(Exception Exp)
							{
								Oil_Detail =  Value + "kg";
								Exp.printStackTrace();
							}
							
							String Car_CType_Name = "未知";
							if(Car_Info.trim().length() > 0)
							{
								String[] List = Car_Info.split(";");
							  for(int i=0; i<List.length && List[i].length()>0; i++)
							  {
							  	String[] subList = List[i].split(",");
							  	if(subList[0].equals(Car_CType))
							  	{
							  		Car_CType_Name = subList[1];
							  		break;
							  	}
							  }
							}
							
							String Car_Detail = "";
							switch(Integer.parseInt(Unq_Flag))
							{
								case 0:
										Car_Detail =  Unq_Str;
									break;
								case 1:
										Car_Detail =  Unq_Str;
									break;
							}														
							String str_car = Car_DW;
							if(str_car.equals("无数据")){str_car = "<font color='red'>本车暂未录入系统</font>";}
							String Rcd_Detail = "";
							switch(Integer.parseInt(Status))
							{
								case 0:
										Rcd_Detail =   "<a href='#' onClick=\"doAbandon('"+SN+"', '"+CTime+"', '1')\"><U>有效</U></a>";
									break;
								case 1:
										Rcd_Detail =   "<a href='#' onClick=\"doAbandon('"+SN+"', '"+CTime+"', '0')\"><U>无效</U></a>";
									break;
							
							}
							
							sn++;
					%>
				  <tr <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%> title='<%=Memo%>'>						  
							<td align=center> <%=sn%></td>
							<td align=center><%=Cpm_Name%></td>
							<td align=center><%=Oil_CName%></td>
							<td align=center><%=Fill_Number%></td>
							<td align=center><%=CTime.substring(0,10)%></td>
							<td align=center><%=Oil_Detail%></td>
							<td align=center>	<%=Car_Detail%>	</td>
							<td align=center>
						
																
								
								<%	if(Car_CType_Name.equals("未知"))
								{
						%>
							<font color='red'><%=str_car%></font>		
						<%
							}else
								{
					%>
									<%=str_car%>	
					<%			
								}
					%>														
								</td>			
					<%	
								boolean afs = false;
							if(null != Pro_Status)
							{
								Iterator Sit = Pro_Status.iterator();								
								while(Sit.hasNext())
								{
									ProLBean plBean =(ProLBean)Sit.next();
									String SCpm_Id = plBean.getCpm_Id();
									String Sctime  = plBean.getCTime();
									if(SCpm_Id.equals(pCpm_Id) && Sctime.equals(CTime.substring(0,10)))
									{
										afs = true;
								%>
								<td  align=center>报表已生成</td>
								<%	
									}
								}
							}	
							if(!afs)
							{
						%>
						<td align=center><input name="SN_C" type="checkbox" value="<%=SN%>" /><a href="#" onClick="doPro_O_Detail('<%=SN%>')"><font color="red"><U>明细查询</U></font></a></td>	
						<%	
							}						
						%>											
										
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
				      	<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
				      </tr>
					<%
						}
					  else
					  {
					%>				
	          <tr <%=((i%2)==0?"class='table_white_l'":"class='table_blue'")%>>
		          <td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
		        </tr>	        
					<%
       			}
     			}
					%> 
     		 	<tr>
						<td colspan=90" class="table_deep_blue" >
				 			<table width="100%" height="20"  border="0" cellpadding="0" cellspacing="0" >
			    			<tr valign="bottom">
			      			<td nowrap><%=currStatus.GetPageHtml("Pro_O")%></td>
			    			</tr>			    		
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</div>
<input name="Cmd"      type="hidden" value="0">
<input name="Sid"      type="hidden" value="<%=Sid%>">
<input name="Cpm_Id"   type="hidden" value=""/>
<input name="SN"       type="hidden" value=""/>
<input name="Status"   type="hidden" value=""/>
<input name="Checker"  type="hidden" value="<%=Operator%>"/>
<input name="BTime"    type="hidden" value="">
<input name="ETime"    type="hidden" value="">
<input name="CurrPage" type="hidden" value="<%=currStatus.getCurrPage()%>">
<input type="button"   id="CurrButton" onClick="doSelect()" style="display:none">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
if(<%=currStatus.getResult().length()%> > 0)
   alert("<%=currStatus.getResult()%>");
<%
currStatus.setResult("");
session.setAttribute("CurrStatus_" + Sid, currStatus);
%>

//ipad禁掉导出
if(1 == fBrowserRedirect() || 2 == fBrowserRedirect())
{
	document.getElementById('img2').style.display = 'none';
}


function doSelect()
{
	/**var days = new Date(Pro_O.EDate.value.replace(/-/g, "/")).getTime() - new Date(Pro_O.BDate.value.replace(/-/g, "/")).getTime();
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
	
	Pro_O.Cpm_Id.value = Pro_O.Func_Cpm_Id.value;
	Pro_O.BTime.value = Pro_O.BDate.value + " 00:00:00";
	Pro_O.ETime.value = Pro_O.EDate.value + " 23:59:59";
	Pro_O.submit();
}


function GoPage(pPage)
{
	/**var days = new Date(Pro_O.EDate.value.replace(/-/g, "/")).getTime() - new Date(Pro_O.BDate.value.replace(/-/g, "/")).getTime();
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
	Pro_O.Cpm_Id.value = Pro_O.Func_Cpm_Id.value;
	Pro_O.BTime.value = Pro_O.BDate.value + " 00:00:00";
	Pro_O.ETime.value = Pro_O.EDate.value + " 23:59:59";
	Pro_O.CurrPage.value = pPage;
	Pro_O.submit();
}

function doAbandon(pSN, pCTime, pStatus)
{
	if('<Limit:limitValidate userrole='<%=FpList%>' fpid='020304' ctype='1'/>' == 'none')
	{
		alert('您无权限审核加注记录!');
		return;
	}
	
	var TDay = new Date().format("yyyy-MM-dd");
	if(pCTime.substring(0,10) != TDay)
	{
		alert('只可操作当天流水记录!');
		return;
	}
	
	if(confirm('确认要改变当前审核?'))
	{
		Pro_O.Cmd.value = 11;
		Pro_O.Cpm_Id.value = Pro_O.Func_Cpm_Id.value;
		Pro_O.SN.value = pSN;
		Pro_O.Status.value = pStatus;
		Pro_O.BTime.value = Pro_O.BDate.value + " 00:00:00";
		Pro_O.ETime.value = Pro_O.EDate.value + " 23:59:59";
		Pro_O.submit();
	}
}

var req = null;
function doExport()
{	  
	/**var days = new Date(Pro_O.EDate.value.replace(/-/g, "/")).getTime() - new Date(Pro_O.BDate.value.replace(/-/g, "/")).getTime();
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
		var url = "Pro_O_Export.do?Sid=<%=Sid%>";	
		req.open("post",url,true);
		req.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
		req.send("Cpm_Id="+Pro_O.Func_Cpm_Id.value+"&BTime="+Pro_O.BDate.value+" 00:00:00"+"&ETime="+Pro_O.EDate.value+" 23:59:59"+"&Func_Sub_Id=<%=currStatus.getFunc_Sub_Id()%>&Func_Corp_Id=<%=currStatus.getFunc_Corp_Id()%>&Func_Type_Id=<%=currStatus.getFunc_Type_Id()%>&CurrPage=<%=currStatus.getCurrPage()%>" );
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
			location.href = "../files/excel/" + resp + ".xls";
		}
	}
}

function doAdd()
{
	location = "Pro_O_Add_New.jsp?Sid=<%=Sid%>&BTime="+Pro_O.BDate.value;
}

function doPro_O_Detail(pSN)
{
	location = "Pro_O_Detail.jsp?Sid=<%=Sid%>&SN="+pSN;
}
function doTJ()
{
	var Pdiag = new Dialog();
	Pdiag.Top = "50%";
	Pdiag.Width = 600;
	Pdiag.Height = 260;
	Pdiag.Title = "生成日报数据";
	Pdiag.URL = 'Pro_O_File.jsp?Sid=<%=Sid%>&Cpm_Id='+Pro_O.Func_Cpm_Id.value;
	Pdiag.show();
}

function DeSn()	
{
	var SN_C = document.forms[0].SN_C;
	var str ='';	
	if(SN_C != null)
	{
	if(SN_C[0].checked == true)
	{
		for (i=0;i<SN_C.length;++ i)
		{
			SN_C[i].checked =false;
		}	
	}else	
	{
		for (i=0;i<SN_C.length;++ i)
		{
			SN_C[i].checked =true;
		}	
		}
	}
}


function doDel()
{
	var SN_C = document.forms[0].SN_C;
	var str ='';
	for (i=0;i<SN_C.length;++ i)
	{
		if(SN_C[i].checked)
		{
			str =SN_C[i].value + ';' + str;
		}
	}
	if(str.length >0)
	{
		if(confirm("是否删除本条记录?"))
		{
			location="Pro_O_Del.do?Sid=<%=Sid%>&JiJiu="+str+"&Cpm_Id="+Pro_O.Func_Cpm_Id.value+"&Func_Sub_Id=9&Func_Corp_Id="+Pro_O.Func_Corp_Id.value+"&BTime="+Pro_O.BDate.value + ' 00:00:00'+"&ETime="+Pro_O.EDate.value + " 23:59:59";
		}
	}		
}
</SCRIPT>
</html>