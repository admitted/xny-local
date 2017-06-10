<%@ page contentType="text/html; charset=gb2312" %>  
<%@ page import="java.util.*" %>
<%@ page import="bean.*" %>
<%@ page import="util.*" %>
<%@ taglib uri="/WEB-INF/limitvalidatetag.tld" prefix="Limit"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>������ԴLNG��˾����Ϣ������ƽ̨</title>
<meta http-equiv="x-ua-compatible" content="ie=7"/>
<link type="text/css" href="../skin/css/style.css" rel="stylesheet"/>
<script type="text/javascript" src="../skin/js/util.js"></script>
<script type="text/javascript" src="../skin/js/day.js"></script>
<%

	String       Sid                = CommUtil.StrToGB2312(request.getParameter("Sid"));
	ArrayList    User_FP_Role       = (ArrayList)session.getAttribute("User_FP_Role_" + Sid);
	UserInfoBean UserInfo           = (UserInfoBean)session.getAttribute("UserInfo_" + Sid);
	ArrayList    User_Manage_Role   = (ArrayList)session.getAttribute("User_Manage_Role_" + Sid);
	
	//����Ȩ��
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
	
	//����Ȩ��
	String ManageId = UserInfo.getManage_Role();
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
	
	String p3 = CommUtil.StrToGB2312(request.getParameter("p3"));
	if(null == p3)
	{
		p3 = "1";
	}		
	ArrayList User_User_Info  = (ArrayList)session.getAttribute("User_User_Info_" + Sid);
	String SYS_List = "";
	if( null != User_User_Info )
	{
		Iterator iterator = User_User_Info.iterator();
		while(iterator.hasNext())
		{
			UserInfoBean usertBean = (UserInfoBean)iterator.next();		
			String sys = 	usertBean.getSys_Id();							
			SYS_List  = SYS_List+sys;
		
		}
	}
	
										
%>
</head>
<body style="background:#0B80CC;">
<div id="PARENT" >
	<ul id="nav">
		<li id="li01" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='01' ctype='1'/>"><a id="a0101"  href="#" onClick="doGIS();DoDisplay(this.id);"            >GIS���</a></li>
		<li id="li02" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='02' ctype='1'/>"><a id="a0201"  href="#" onClick="doData();DoDisplay(this.id);"           >����ͳ��</a></li>
		<li id="li03" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='03' ctype='1'/>"><a id="a0301"  href="#" onClick="doSale();DoDisplay(this.id);"         	 >����ͳ��</a></li>
		<!--
		<li id="li02" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='02' ctype='1'/>"><a href="#" onClick="DoMenu('UserMenu2')">����ͳ��</a></li>
			 <ul id="UserMenu2" class="collapsed">
				 <li id="Display0201"><a href="#" onClick="doAcc_Sale_Sta()"		 	  style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0201' ctype='1'/>">վ�����۱�</a></li>
				 <li id="Display0202"><a href="#" onClick="doAcc_Sale_Day()"			  style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0202' ctype='1'/>">�������ܱ�</a></li>
				 <li id="Display0203"><a href="#" onClick="doAcc_Sale_Month()"			style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0203' ctype='1'/>">�������ܱ�</a></li>			
	   	 </ul>		
		<li id="li03" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='03' ctype='1'/>"><a href="#" onClick="DoMenu('UserMenu3')">����ͳ��</a></li>	 
			 <ul id="UserMenu3" class="collapsed">
				 <li id="Display0301"><a href="#" onClick="doAcc_Data_Sta()"		 	  style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0301' ctype='1'/>">վ��������</a></li>
				 <li id="Display0302"><a href="#" onClick="doAcc_Data_Day()"			  style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0302' ctype='1'/>">�������ܱ�</a></li>
				 <li id="Display0303"><a href="#" onClick="doAcc_Data_Month()"			style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0303' ctype='1'/>">�������ܱ�</a></li>			
	   	 </ul>
	  -->
 	 	
 	 	<li id="li04" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='04' ctype='1'/>"><a href="#" onClick="DoMenu('UserMenu4')">��������</a></li>
			 <ul id="UserMenu4" class="collapsed">
				 <li id="Display0401"><a id="a0401" href="#" onClick="doEnv();DoDisplay(this.id);"                 style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0401' ctype='1'/>">ʵʱ����</a></li>
	   		 <li id="Display0402"><a id="a0402" href="#" onClick="doHis();DoDisplay(this.id);"                 style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0402' ctype='1'/>">��ʷ����</a></li>
				<!-- <li id="Display0403"><a href="#" onClick="doGra()"                                                                                                >����ͼ��</a></li>-->
			 </ul>
		
    <li id="li05" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='05' ctype='1'/>"><a id="a0501" href="#" onClick="doAlert_Info();DoDisplay(this.id);"    >�澯����</a></li>
	   <!-- <ul id="UserMenu5" class="collapsed">
				<li id="Display0501"><a href="#" onClick="dolinkage_Info()"         style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0501' ctype='1'/>" >�澯��־</a></li>
	   		<li id="Display0502"><a href="#" onClick="doAlert_Info()"           style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='0502' ctype='1'/>" >������־</a></li> 
	   	</ul>-->  
	  <li id="li06" style="display:<Limit:limitValidate userrole='<%=FpList%>' fpid='06' ctype='1'/>"><a id="a0601" href="#" onClick="doControl();DoDisplay(this.id);" >�豸����</a></li>
	
	</ul>
</div>
</body>
<script language='javascript'>
//��ʼ��
switch(parseInt('<%=p3%>'))
{
	default:
	case 1:
			if('<Limit:limitValidate userrole='<%=FpList%>' fpid='01' ctype='1'/>' == '')
			{
				window.parent.frames.mFrame.location = 'MapMain_Map.jsp?Sid=<%=Sid%>';
			}
			else
			{
				window.parent.frames.mFrame.location = 'User_Info.jsp?Sid=<%=Sid%>';
			}
		break;
	case 2:
		break;
	case 3:
			if('<Limit:limitValidate userrole='<%=FpList%>' fpid='03' ctype='1'/>' == '')
			{
				window.parent.frames.mFrame.location = 'Device_Detail.do?Cmd=1&Sid=<%=Sid%>';
			}
			else
			{
				window.parent.frames.mFrame.location = 'User_Info.jsp?Sid=<%=Sid%>';
			}
		break;
}
//�˵�Menu
var LastLeftID = "";
function DoMenu(emid)
{
	 var obj = document.getElementById(emid); 
	 obj.className = (obj.className.toLowerCase() == "expanded"?"collapsed":"expanded");
	 if((LastLeftID!="")&&(emid!=LastLeftID)) //�ر���һ��Menu
	 {
	  	document.getElementById(LastLeftID).className = "collapsed";
	 }
	 LastLeftID = emid;
}
//�˵���ɫ�仯
var LastsubID = "";
function DoDisplay(emid)
{
	 var obj = document.getElementById(emid); 
	 obj.className = (obj.className.toLowerCase() == "expanded"?"collapsed":"expanded");
	 if((LastsubID!="")&&(emid!=LastsubID)) //�ر���һ��
	 {
	  	document.getElementById(LastsubID).className = "collapsed";
	 }
	 LastsubID = emid;
}
//�˵���ɫ�仯
var LastsubID = "a0101";
DoDisplay(LastsubID);
function DoDisplay(emid)
{
    document.getElementById(emid).style.color = "red" ;
    if ((LastsubID != "") && (emid != LastsubID)) //�ر���һ��
    {
        document.getElementById(LastsubID).style.color = "" ;
    }
    LastsubID = emid;
}

/*****************************************************GISͼ*****************************************************************/
//GISͼ
function doGIS()
{
	window.parent.frames.mFrame.location = 'MapMain_Map.jsp?Sid=<%=Sid%>';
}

/*****************************************************վ��������*************************************************************/
//վ��������
function doData()
{
	window.parent.frames.mFrame.location = 'doTables.do?Cmd=9&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>';
}

//վ�����۱�
function doSale()
{
	window.parent.frames.mFrame.location = 'doSaleTables.do?Cmd=9&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>';
}

/*****************************************************����ͳ��*************************************************************/
//վ��������
function doAcc_Sale_Sta()
{
	window.parent.frames.mFrame.location = 'Acc_Sale.do?Cmd=0&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>&Func_Sub_Id=1';
}

function doAcc_Sale_Day()
{
	window.parent.frames.mFrame.location = 'Acc_Sale.do?Cmd=1&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>&Func_Sub_Id=1';
}

function doAcc_Sale_Month()
{
	var TDay = new Date().format("yyyy-MM-dd");
	window.parent.frames.mFrame.location = 'Acc_Sale.do?Cmd=2&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>&Func_Sub_Id=1';
}

/****************************************************����ͳ��*************************************************************/
//վ��������
function doAcc_Data_Sta()
{
	window.parent.frames.mFrame.location = 'Acc_Data.do?Cmd=0&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>&Func_Sub_Id=1';
}

function doAcc_Data_Day()
{
	window.parent.frames.mFrame.location = 'Acc_Data.do?Cmd=1&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>&Func_Sub_Id=1';
}

function doAcc_Data_Month()
{
	window.parent.frames.mFrame.location = 'Acc_Data.do?Cmd=2&Cpm_Id=<%=Manage_List%>&Sid=<%=Sid%>&Func_Sub_Id=1';
}

/****************************************************��վ����*************************************************************/
//ʵʱ����
function doEnv()
{
	window.parent.frames.mFrame.location = 'Env.do?Cmd=0&Id=<%=Manage_List%>&Sid=<%=Sid%>&Func_Sub_Id=1';
}
//��ʷ����
function doHis()
{
	window.parent.frames.mFrame.location = 'Env.do?Cmd=2&Sid=<%=Sid%>&Cpm_Id=<%=Manage_List%>&Id=<%=Manage_List%>&Func_Sub_Id=9&Func_Corp_Id=9999&Func_Sel_Id=9';
}
//����ͼ��
function doGra()
{
	var TDay = new Date().format("yyyy-MM-dd");
	window.parent.frames.mFrame.location = "Graph.do?Cmd=20&Id=0100000001&Sid=<%=Sid%>&Level="+'4'+"&BTime="+TDay+"&Func_Sub_Id=1";
}

/**************************************************�澯����***************************************************************/
//�澯��־
function doAlert_Info()
{
	window.parent.frames.mFrame.location = "Alert_Info.do?Cmd=0&Sid=<%=Sid%>&Cpm_Id=<%=Manage_List%>&Id=<%=Manage_List%>&Func_Sub_Id=9&Func_Corp_Id=9999&Func_Sel_Id=9";
}

//������־
function doLinkage_Info()
{
	window.parent.frames.mFrame.location = "linkage_Info.do?Cmd=0&Sid=<%=Sid%>";
}

/**************************************************�豸����***************************************************************/

function doControl()
{
	window.parent.frames.mFrame.location = "Dev_Ctrl.do?Cmd=0&Sid=<%=Sid%>&Func_Cpm_Id=<%=Manage_List%>";
}

</script>
</html>