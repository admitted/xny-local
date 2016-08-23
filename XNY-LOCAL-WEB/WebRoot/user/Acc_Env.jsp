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
<script type='text/javascript' src='../skin/js/zDrag.js'   charset='gb2312'></script>
<script type='text/javascript' src='../skin/js/zDialog.js' charset='gb2312'></script>
</head>
<%
	String       Sid          = CommUtil.StrToGB2312(request.getParameter("Sid"));
	CurrStatus   currStatus   = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
	ArrayList    Acc_Env          = (ArrayList)session.getAttribute("Acc_Env_" + Sid);
	UserInfoBean UserInfo     = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);	
	ArrayList    User_FP_Role = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);

	String FpId = UserInfo.getFp_Role();
	String FpList = "";
	if (null != FpId && FpId.length() > 0 && null != User_FP_Role)
	{
		Iterator roleiter = User_FP_Role.iterator();
		while (roleiter.hasNext())
		{
			UserRoleBean roleBean = (UserRoleBean) roleiter.next();
			if (roleBean.getId().equals(FpId) && null != roleBean.getPoint())
			{
				FpList = roleBean.getPoint();
			}
		}
	}

	ArrayList    Pro_R_Type = (ArrayList) session.getAttribute("Pro_R_Type_" + Sid);
	CorpInfoBean Corp_Info  = (CorpInfoBean) session.getAttribute("User_Corp_Info_" + Sid);
	String Oil_Info = "";
	if (null != Corp_Info)
	{
		Oil_Info = Corp_Info.getOil_Info();
		if (null == Oil_Info)
		{
			Oil_Info = "";
		}
	}

	ArrayList User_Manage_Role   = (ArrayList) session.getAttribute("User_Manage_Role_" + Sid);
	ArrayList User_Device_Detail = (ArrayList) session.getAttribute("User_Device_Detail_" + Sid);
	String ManageId = UserInfo.getManage_Role();

	String Manage_List = "";
	if (ManageId.length() > 0 && null != User_Manage_Role)
	{
		Iterator iterator = User_Manage_Role.iterator();
		while (iterator.hasNext())
		{
			UserRoleBean statBean = (UserRoleBean) iterator.next();
			if (statBean.getId().substring(0, 4).equals(ManageId) && statBean.getId().length() == 8)
			{
				String R_Point = statBean.getPoint();
				if (null == R_Point)
				{
					R_Point = "";
				}
				Manage_List += R_Point;
			}
		}
	}
	String Dept_Id = UserInfo.getDept_Id();
	if (Dept_Id.length() > 3)
	{
		Manage_List = Dept_Id;
	}
	ArrayList Acc_Env_Cpm  = (ArrayList) session.getAttribute("Acc_Env_Cpm_" + Sid);
	ArrayList Acc_Env_Sheb = (ArrayList) session.getAttribute("Acc_Env_Sheb_" + Sid);
%>
<body style="background:#CADFFF">
<form name="Acc_Env"  action="Acc_Env.do" method="post" target="mFrame">
<div id="down_bg_2">
	<table width='100%' style='margin:auto' cellpadding='0' cellspacing='0' border='0' width='100%' bordercolor="#3491D6" borderColorDark="#ffffff">
			<tr height='25px' class='sjtop'>
					<td height='25px' width='100%' align='left'>
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
							排列模式:
							<select name="Func_Sub_Id" style="width:100px;height:20px;" onchange="doSelect()">
								<option value='1' <%=currStatus.getFunc_Sub_Id() == 1?"selected":""%>>单元模式</option>
								<option value='2' <%=currStatus.getFunc_Sub_Id() == 2?"selected":""%>>记录模式</option>
							</select>			
					</td>
				<td><img id="img1" src="../skin/images/tupianshangchuan.gif" onClick='doFile()' style="cursor:hand;display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0504' ctype='1'/>"></td>
			</tr>
	</table>
		
	<table width='100%' style='margin:auto' cellpadding='0' cellspacing='0' border='0'  bordercolor="#3491D6" borderColorDark="#ffffff">	
		<tr height='30' align="center">
			<td width='100%'>
				<%
				switch(currStatus.getFunc_Sub_Id())
				{
					case 1:
				%>
				        <table style='margin:auto' cellpadding='0' cellspacing='0' border='0' width='100%' bordercolor="#3491D6" borderColorDark="#ffffff">
						        <tr>
			          <%
								String Cpm_Id = "";
								String Id     = "";
								if(null != Acc_Env_Cpm)
								{
										Iterator cpmiter = Acc_Env_Cpm.iterator();
										while(cpmiter.hasNext())
										{
												DataBean dBean    = (DataBean)cpmiter.next();
												String   Cpm_Name = dBean.getCpm_Name();
												Cpm_Id            = dBean.getCpm_Id();
					      %>						
				                 <td valign="top" width='30%' align="left" >					
					                   <table style='margin:10px' cellpadding='1' cellspacing='1' border='1' width='30%' bordercolor="#3491D6" borderColorDark="#ffffff">	
						                      <tr >
							                       <td colspan="2"  >																	
						                             <div style='width:100%;height:128px;' >
							                               <img src='../skin/images/CPM_TU/<%=Cpm_Id %>.jpg' style='width:300px;height:128px;' >		
						                             </div>
						                             <div style='width:30%;height:22px;'><font color='FFOOFF'><strong>场站:<%=Cpm_Name%></strong></font></div>	
					                           </td>	
					                        </tr>			
						          <%
												if(null != Acc_Env_Sheb)
												{
														Iterator shebiter = Acc_Env_Sheb.iterator();
														while(shebiter.hasNext())
														{
																DataBean daBean = (DataBean)shebiter.next();
																if(Cpm_Id.equals(daBean.getCpm_Id()))
																{
																		Id = daBean.getId();
						           %>
						                        <tr height=50>
							                         <td  colspan="2" align="center"><font color='#3491D6'><strong>
							                         <%=daBean.getCName()%></strong></font></td>
						                        </tr>				
											
		                   <%				
																	  if(null != Acc_Env)
																	  {
																				Iterator curriter = Acc_Env.iterator();
																				while(curriter.hasNext())
																				{
																						DataBean currBean = (DataBean)curriter.next();
																						if(currBean.getCpm_Id().equals(Cpm_Id) && currBean.getId().equals(Id))
																						{
																								String curr_Attr_Name = currBean.getAttr_Name();								
																								String curr_Value = currBean.getValue();
																								String curr_Unit = currBean.getUnit();
																								if(null == curr_Value){curr_Value = "";}
																								if(null == curr_Unit){curr_Unit = "";}
		                   %>
		                   
																								<tr height=30 >
																									 <td  width='20%' height='30px' align="center" ><%=curr_Attr_Name%></td>
																									 <td  width='60%' height='30px' align="center" ><%=curr_Value+curr_Unit%></td>
																								</tr>
			  <%
										                        }
									                       }
						
								                    }				
								                }
						                 }										
					               }				
		                   %>										
			                  </table>
				              </td>		
			        <%				
						        }					
					      }	  	
			        %>			  	
						</tr>
						</table>
						
						
				<%
					break;
						
					case 2:
				%>
						<table style='margin:auto' cellpadding='0' cellspacing='0' border='1' width='100%' bordercolor="#3491D6" borderColorDark="#ffffff">
							<tr height='25'>
								<td width='5%'  class='table_deep_blue'>序号</td>
								<td width='10%' class='table_deep_blue'>站点</td>
								<td width='10%' class='table_deep_blue'>设备</td>
								<td width='10%' class='table_deep_blue'>参数</td>
								<td width='15%' class='table_deep_blue'>时间</td>
								<td width='10%' class='table_deep_blue'>数值</td>
								<td width='10%' class='table_deep_blue'>级别</td>
								<td width='15%' class='table_deep_blue'>描述</td>
							</tr>
							<%
							int i = 0;
							if(null != Acc_Env)
							{
								Iterator iterator = Acc_Env.iterator();
								while(iterator.hasNext())
								{
									i++;
									DataBean Bean = (DataBean)iterator.next();
									String Cpm_Name = Bean.getCpm_Name();
									String CName = Bean.getCName();
									String Attr_Name = Bean.getAttr_Name();
									String CTime = Bean.getCTime();
									String Value = Bean.getValue();
									String Unit = Bean.getUnit();
									String Lev = Bean.getLev();
									String Des = Bean.getDes();
									
									if(null == Value){Value = "";}
									if(null == Unit){Unit = "";}
									if(null == Lev){Lev = "";}
									if(null == Des){Des = "";}
									
									String str_Lev = "无";
									String str_Des = "无";
									if(Lev.length() > 0)
									{
										str_Lev = Lev;
									}
									if(Des.length() > 0)
									{
										str_Des = Des;
									}
							%>
									<tr height='40'>
										<%
										if((Lev.length() < 1 && Des.length() < 1) || Lev.equals("N"))
										{
										%>
											<td width='5%'  align=center><%=i%></td>
											<td width='10%' align=center><%=Cpm_Name%></td>
											<td width='10%' align=center><%=CName%></td>
											<td width='10%' align=center><%=Attr_Name%></td>
											<td width='15%' align=center><%=CTime%></td>			
											<td width='10%' align=center><%=Value+Unit%></td>			
											<td width='10%' align=center><%=str_Lev%></td>
											<td width='15%' align=center><%=str_Des%></td>
										<%
										}
										else
										{
										%>
											<td width='5%'  align=center><font color=red><%=i%></font></td>
											<td width='10%' align=center><font color=red><%=Cpm_Name%></font></td>
											<td width='10%' align=center><font color=red><%=CName%></font></td>
											<td width='10%' align=center><font color=red><%=Attr_Name%></font></td>
											<td width='15%' align=center><font color=red><%=CTime%></font></td>			
											<td width='10%' align=center><font color=red><%=Value+Unit%></font></td>			
											<td width='10%' align=center><font color=red><%=str_Lev%></font></td>
											<td width='15%' align=center><font color=red><%=str_Des%></font></td>					
										<%
										}
										%>
									</tr>
							<%
								}
							}
							if(i == 0)
							{
							%>
								<tr height='30'>
									<td width='100%' colspan=8 align=center>无</td>
								</tr>
							<%
							}
							%>
						</table>
				<%
						break;
				}
				%>
				
			</td>
		</tr>
	</table>
</div>
<input type="hidden" name="Cmd" value="0"/>
<input type="hidden" name="Sid" value="<%=Sid%>"/>
<input name="Id"    type="hidden" value="">
<input type="hidden" name="Level" value=""/>
<input type="button" id="CurrButton"  onClick="doSelect()" style="display:none">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
function doSelect()
{
	Acc_Env.Id.value = Acc_Env.Func_Cpm_Id.value;
	Acc_Env.Level.value = 2;
	Acc_Env.submit();
}


/**实时数据
function doAcc_Env()
{
	window.parent.frames.mFrame.location = 'Acc_Env.do?Cmd=0&Level=2&Id='+Acc_Env.Id.value+'&Sid=<%=Sid%>&Func_Sub_Id=1';
}
//历史数据
function doHis()
{
	window.parent.frames.mFrame.location = "Acc_Env.do?Cmd=2&Sid=<%=Sid%>&Level=2&Id="+Acc_Env.Id.value;
}
//数据图表
function doGra()
{
	var TDay = new Date().format("yyyy-MM-dd");
	var Id = window.parent.frames.lFrame.document.getElementById('id').value;
	var Level = window.parent.frames.lFrame.document.getElementById('level').value;
	if('4' != Level)
	{
		Id = '';
	}
	window.parent.frames.mFrame.location = "Graph.do?Cmd=20&Sid=<%=Sid%>&Id="+Id+"&Level="+Level+"&BTime="+TDay+"&Func_Sub_Id=1";
}**/

function doFile()
{
	var Pdiag = new Dialog();
	Pdiag.Top = "50%";
	Pdiag.Width = 500;
	Pdiag.Height = 140;
	Pdiag.Title = "图片上传";
	Pdiag.URL = 'Acc_Env_File.jsp?Sid=<%=Sid%>';
	Pdiag.show();	
	
}


</SCRIPT>
</html>