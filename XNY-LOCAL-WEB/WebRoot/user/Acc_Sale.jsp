<%@ page contentType="text/html; charset=gb2312" %>
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.math.*" %>
<%@ page import="java.text.*" %>
<%@ page import="com.alibaba.fastjson.JSON" %>
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
		<script type="text/javascript" src="../skin/js/day.js"></script>
		<script type="text/javascript" src="../skin/js/jquery.min.js"></script>
		
		<script type="text/javascript">
				function divScroll(scrollDiv){
				    var scrollLeft = scrollDiv.scrollLeft;
				    document.getElementById("tableDiv_title").scrollLeft = scrollLeft;
				    document.getElementById("tableDiv_body").scrollLeft = scrollLeft;        
				}
				function divYScroll(scrollYDiv){
				    var scrollTop = scrollYDiv.scrollTop;
				    document.getElementById("tableDiv_y").scrollTop = scrollTop;    
				}
				function onwheel(event){
				    var evt = event||window.event;
				    var bodyDivY = document.getElementById("tableDiv_y");
				    var scrollDivY = document.getElementById("scrollDiv_y");
				    if (bodyDivY.scrollHeight>bodyDivY.offsetHeight){
				        if (evt.deltaY){
				            bodyDivY.scrollTop = bodyDivY.scrollTop + evt.deltaY*7;
				            scrollDivY.scrollTop = scrollDivY.scrollTop + evt.deltaY*7;
				        }else{
				            bodyDivY.scrollTop = bodyDivY.scrollTop - evt.wheelDelta/5;
				            scrollDivY.scrollTop = scrollDivY.scrollTop - evt.wheelDelta/5;
				        }
				    }
				}
				
				// select
				function doSelect()
				{
					var month = doSaleTables.Month.value
					if (month < 10) {
						month = '0'+ month;
					}
					doSaleTables.Cpm_Id.value = doSaleTables.Func_Cpm_Id.value;
					doSaleTables.BTime.value  = doSaleTables.Year.value + '-' + month + '-01 00:00:00';
				  doSaleTables.ETime.value  = new Date().format("yyyy-MM-dd");
				  doSaleTables.submit();
				}
				//isBrowser();
				function isBrowser(){
						var Sys={};
						var ua=navigator.userAgent.toLowerCase();
						var s;
						(s=ua.match(/msie ([\d.]+)/))?Sys.ie=s[1]:
						(s=ua.match(/firefox\/([\d.]+)/))?Sys.firefox=s[1]:
						(s=ua.match(/chrome\/([\d.]+)/))?Sys.chrome=s[1]:
						(s=ua.match(/opera.([\d.]+)/))?Sys.opera=s[1]:
						(s=ua.match(/version\/([\d.]+).*safari/))?Sys.safari=s[1]:0;
						if(Sys.ie){//Js判断为IE浏览器
								alert("当前浏览器为IE , 为了有更好的图表体验 , 请更换Chrome核心的浏览器!!!");
								if(Sys.ie=='9.0'){//Js判断为IE 9
								}else if(Sys.ie=='8.0'){//Js判断为IE 8
								}else{
								}
						}
						if(Sys.firefox){//Js判断为火狐(firefox)浏览器
								//alert("firefox");
						}
						if(Sys.chrome){//Js判断为谷歌chrome浏览器
								//alert("chrome");
						}
						if(Sys.opera){//Js判断为opera浏览器
								//alert("opera");
						}
						if(Sys.safari){//Js判断为苹果safari浏览器
								//alert("safari");
						}
				}
		</script>
		<style type="text/css" mce_bogus="1">  
					  table th{
					     white-space: nowrap;
					  }
					  table td{
					     white-space: nowrap;
					     word-break:keep-all;
					  }
					  body,table{
					     font-size:12px;
					  }
					  table{
			         empty-cells:show; 
			         border-collapse: collapse;
			         margin:0 auto;
			         
					  }
					 
					  h1,h2,h3{
						   font-size:12px;
						   margin:0;
						   padding:0;
					  }
					  table.tab_css_1{
						   border:1px solid #cad9ea;
						   color:#000000;
					  }
					  table.tab_css_1 th {
						   
						   background-repeat:repeat-x;
						   height:30px;
					  }
					  table.tab_css_1 td,table.tab_css_1 th{
						   border:1px solid #cad9ea;
						   padding:0 1em 0;
						   word-break:keep-all;
						   
					  }
					  table.tab_css_1 tr.tr_css{
						   background-color:#f5fafe;
						   height:30px;
					  }
        </style>
</head>
<%
		String       Sid                = CommUtil.StrToGB2312(request.getParameter("Sid"));
		ArrayList    User_FP_Role       = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
		UserInfoBean UserInfo           = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
		ArrayList    User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
		ArrayList    User_Device_Detail = (ArrayList)session.getAttribute("User_Device_Detail_" + Sid);
		//功能权限
		String       FpId               = UserInfo.getFp_Role();
		String       FpList             = "";
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
		//管理权限
		String     ManageId    = UserInfo.getManage_Role();
		String     Manage_List = "";
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
		CurrStatus currStatus = (CurrStatus)session.getAttribute("CurrStatus_" + Sid);
		String   BDate  = currStatus.getVecDate().get(0).toString().substring(0,10);
		String   EDate  = currStatus.getVecDate().get(1).toString().substring(0,10);
		int 		 Year  = Integer.parseInt(BDate.substring(0,4));
	  int      Month = Integer.parseInt(BDate.substring(5,7));
	  int thatMonthDays = CommUtil.getDaysOfMonth(BDate);
		int cnt = 0;
		int sn = 0;
	  
	  Map<String, Map>    CpmMap    = (Map)session.getAttribute("CpmMap_" + Sid);
	  Map<String, JSON>   GraphMaps = (Map)session.getAttribute("GraphMaps_" + Sid);                                                                       
%>
<body style=" background:#CADFFF">
<form name="doSaleTables"  action="doSaleTables.do" method="post" target="mFrame">
		<div id="down_bg_2">
				<table width="100%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
						<tr height='25px' class='sjtop'>
					  		<td width='20%' align='left'>
								    &nbsp;场站站点: 
									  <select  name='Func_Cpm_Id' style='width:100px;height:20px' onChange="doSelect()" >					
									  		<option value="<%=Manage_List%>" <%=currStatus.getFunc_Cpm_Id().equals(Manage_List)?"selected":""%>>全部站点</option>		
												    <%					
														if( Manage_List.length() > 0 && null != User_Device_Detail){
																Iterator iterator = User_Device_Detail.iterator();
																while(iterator.hasNext()){
																		DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();
																		if(Manage_List.contains(statBean.getId())){
														%>
																	      <option value='<%=statBean.getId()%>' <%=currStatus.getFunc_Cpm_Id().equals(statBean.getId())?"selected":""%>><%=statBean.getBrief()%></option>
														<%
																		}
																}
														}
														%>
									  </select>
					          &nbsp;年份月份:
					          <select name="Year" style="width:70px;height:20px;" onChange="doSelect(this.value)">
										        <%
										        for(int j=2012; j<=2030; j++){
										        %>
										          <option value="<%=j%>" <%=(Year == j?"selected":"")%> > <%=j%>年</option>
										        <%
										        }
										        %>
				        		</select>
				        		<select name="Month" id="Month" style="width:60px;height:20px;" onChange="doSelect(this.value)" >
										        <%
										        for(int k=1; k<=12; k++){
										       	%>
										       		<option value="<%=k%>" <%=(Month == k?"selected":"")%> ><%=k%>月</option>
										       	<%
										       	}
										       	%>
				        		</select>
					      </td>
								<td width='80%' align='left'>		
										<!--&nbsp;&nbsp;<img id="img1" src="../skin/images/mini_button_search.gif" onClick='doSelect()' style='cursor:hand;'>
										<img id="img2" src="../skin/images/excel.gif"              onClick='doExport()' >-->
								</td>
						</tr>
						<tr height='30' >
								<td width='100%' align='center' colspan=2>
										<table class="tab_css_1"  style="width:100%;" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff"  >
												<tr class="tr_css" height=30>
														<td   style="word-break:break-all" align=center class="table_deep_blue" ><strong>站点\日期</strong></td>
												<%
																		  for(int i = 1; i<= thatMonthDays ;i++){
												%>
																<td   style="word-break:break-all" align=center class="table_deep_blue" ><strong><%=i%></strong></td>
												<%
																			}
												%>
												</tr>
																		
												<%
												if(null != CpmMap && Manage_List.length() > 0)
												{
														for (String key : CpmMap.keySet())
														{
														   //System.out.println(key);
										    			 Map<Integer, String> CpmDaysMap  = CpmMap.get(key);
										    			 sn++;
										    			 Iterator iterator = User_Device_Detail.iterator();
															 while(iterator.hasNext())
															 {
																		DeviceDetailBean statBean = (DeviceDetailBean)iterator.next();
																		if(Manage_List.contains(statBean.getId()) && key.equals(statBean.getId()))
										    			      {
										    			 
											  %>
													  <tr class="tr_css" height='30' <%=((sn%2)==0?"class='table_blue'":"class='table_white_l'")%>>
																<td  style="word-break:break-all" align=center ><strong><%=statBean.getBrief()%></strong></td>
												<%
																			  for(int i = 1; i<= thatMonthDays ;i++)
																		    {
												%>
														    <td  style="word-break:break-all" align=center><strong><%=CpmDaysMap.get(i) == null ?"0":CpmDaysMap.get(i)%></strong></td>		  
												<% 
													  						}
												%>
												    </tr>		  
												<%
												           }
												       }
														} 
												}
								        %>
										</table>    
								</td>
						</tr>
			  </table>
			 
		</div>
		<input name="Cmd"           type="hidden" value="9">
		<input name="Sid"           type="hidden" value="<%=Sid%>">
		<input name="Cpm_Id"        type="hidden" value="">
		<input name="BTime"         type="hidden" value="">
		<input name="ETime"         type="hidden" value="">
		<input name="Func_Type_Id"  type="hidden" value="">
		<input type="button" id="CurrButton" onClick="doSelect()" style="display:none">
</form>

</body>
<SCRIPT LANGUAGE=javascript>

//ipad禁掉导出
if(1 == fBrowserRedirect() || 2 == fBrowserRedirect())
{
	  document.getElementById('img2').style.display = 'none';
}

</SCRIPT>
</html>