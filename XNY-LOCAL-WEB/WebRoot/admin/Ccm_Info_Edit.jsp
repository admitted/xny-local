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
<script type="text/javascript" src="../skin/js/util.js"></script>
<script language=javascript>document.oncontextmenu=function(){window.event.returnValue=false;};</script>
</head>
<%
	
	String Sid    = CommUtil.StrToGB2312(request.getParameter("Sid"));
	String Crm_Id = CommUtil.StrToGB2312(request.getParameter("Crm_Id"));
	String Cmd    = CommUtil.StrToGB2312(request.getParameter("Cmd"));
 	String Id     = CommUtil.StrToGB2312(request.getParameter("Id"));
 	
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
 	
  String CType  = "";
	String Owner  = "";
	String Bottle = "";
	if(Ccm_Info != null && Cmd.equals("11"))
	{
		Iterator iterator = Ccm_Info.iterator();
		while(iterator.hasNext())
		{
			CcmInfoBean statBean = (CcmInfoBean)iterator.next();
			if(statBean.getId().equals(Id))
			{
				CType  = statBean.getCType();
				Owner  = statBean.getOwner();
				Bottle = statBean.getBottle();
			}
		}
 	}
 	
%>
<body style="background:#CADFFF">
<form name="Ccm_Info_Edit" action="Ccm_Info.do" method="post" target="_self">
	<br><br><br>
	<table width="80%" style='margin:auto;' border=0 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">
		<tr height='30'>
			<td width='100%' align='center'>
				<table width="100%" border=1 cellPadding=0 cellSpacing=0 bordercolor="#3491D6" borderColorDark="#ffffff">		
					<tr height='40px'>
						<td width='20%' align='center'>车辆牌号</td>
						<td width='30%' align='left'>
							<%
							if(Cmd.equals("10"))
							{
							%>
								<select name="Car_Id" style="width:65%;height:20px">
									<option value='-1'>====广东省(粤)====</option>
									<option value='粤A-'>粤A-</option>
									<option value='粤B-'>粤B-</option>
									<option value='粤C-' selected>粤C-</option>
									<option value='粤D-'>粤D-</option>
									<option value='粤E-'>粤E-</option>
									<option value='粤F-'>粤F-</option>
									<option value='粤G-'>粤G-</option>
									<option value='粤H-'>粤H-</option>
									<option value='粤J-'>粤J-</option>
									<option value='粤K-'>粤K-</option>
									<option value='粤L-'>粤L-</option>
									<option value='粤M-'>粤M-</option>
									<option value='粤N-'>粤N-</option>
									<option value='粤P-'>粤P-</option>
									<option value='粤Q-'>粤Q-</option>
									<option value='粤R-'>粤R-</option>
									<option value='粤S-'>粤S-</option>
									<option value='粤T-'>粤T-</option>
									<option value='粤U-'>粤U-</option>
									<option value='粤V-'>粤V-</option>
									<option value='-1'>====河北省(冀)====</option>
									<option value='冀A-'>冀A-</option>
									<option value='冀B-'>冀B-</option>
									<option value='冀C-'>冀C-</option>
									<option value='冀D-'>冀D-</option>
									<option value='冀E-'>冀E-</option>
									<option value='冀F-'>冀F-</option>
									<option value='冀G-'>冀G-</option>
									<option value='冀H-'>冀H-</option>
									<option value='冀J-'>冀J-</option>
									<option value='冀K-'>冀K-</option>
									<option value='冀L-'>冀L-</option>
									<option value='冀N-'>冀N-</option>
									<option value='冀P-'>冀P-</option>
									<option value='冀Q-'>冀Q-</option>
									<option value='冀R-'>冀R-</option>
									<option value='冀S-'>冀S-</option>
									<option value='冀T-'>冀T-</option>
									<option value='-1'>====河南省(豫)====</option>
									<option value='豫A-'>豫A-</option>
									<option value='豫B-'>豫B-</option>
									<option value='豫C-'>豫C-</option>
									<option value='豫D-'>豫D-</option>
									<option value='豫E-'>豫E-</option>
									<option value='豫F-'>豫F-</option>
									<option value='豫G-'>豫G-</option>
									<option value='豫H-'>豫H-</option>
									<option value='豫J-'>豫J-</option>
									<option value='豫K-'>豫K-</option>
									<option value='豫L-'>豫L-</option>
									<option value='豫M-'>豫M-</option>
									<option value='豫N-'>豫N-</option>
									<option value='豫P-'>豫P-</option>
									<option value='豫Q-'>豫Q-</option>
									<option value='豫R-'>豫R-</option>
									<option value='豫S-'>豫S-</option>
									<option value='-1'>====云南省(云)====</option>
									<option value='云A-'>云A-</option>
									<option value='云B-'>云B-</option>
									<option value='云C-'>云C-</option>
									<option value='云D-'>云D-</option>
									<option value='云E-'>云E-</option>
									<option value='云F-'>云F-</option>
									<option value='云G-'>云G-</option>
									<option value='云H-'>云H-</option>
									<option value='云J-'>云J-</option>
									<option value='云K-'>云K-</option>
									<option value='云L-'>云L-</option>
									<option value='云M-'>云M-</option>
									<option value='云N-'>云N-</option>
									<option value='云P-'>云P-</option>
									<option value='云Q-'>云Q-</option>
									<option value='云R-'>云R-</option>
									<option value='云S-'>云S-</option>								
									<option value='-1'>====辽宁省(辽)====</option>
									<option value='辽A-'>辽A-</option>
									<option value='辽B-'>辽B-</option>
									<option value='辽C-'>辽C-</option>
									<option value='辽D-'>辽D-</option>
									<option value='辽E-'>辽E-</option>
									<option value='辽F-'>辽F-</option>
									<option value='辽G-'>辽G-</option>
									<option value='辽H-'>辽H-</option>
									<option value='辽J-'>辽J-</option>
									<option value='辽K-'>辽K-</option>
									<option value='辽L-'>辽L-</option>
									<option value='辽M-'>辽M-</option>
									<option value='辽N-'>辽N-</option>
									<option value='辽P-'>辽P-</option>
									<option value='-1'>====黑龙江省(黑)====</option>
									<option value='黑A-'>黑A-</option>
									<option value='黑B-'>黑B-</option>
									<option value='黑C-'>黑C-</option>
									<option value='黑D-'>黑D-</option>
									<option value='黑E-'>黑E-</option>
									<option value='黑F-'>黑F-</option>
									<option value='黑G-'>黑G-</option>
									<option value='黑H-'>黑H-</option>
									<option value='黑J-'>黑J-</option>
									<option value='黑K-'>黑K-</option>
									<option value='黑L-'>黑L-</option>
									<option value='黑M-'>黑M-</option>
									<option value='黑N-'>黑N-</option>
									<option value='黑P-'>黑P-</option>
									<option value='-1'>====湖南省(湘)====</option>
									<option value='湘A-'>湘A-</option>
									<option value='湘B-'>湘B-</option>
									<option value='湘C-'>湘C-</option>
									<option value='湘D-'>湘D-</option>
									<option value='湘E-'>湘E-</option>
									<option value='湘F-'>湘F-</option>
									<option value='湘G-'>湘G-</option>
									<option value='湘H-'>湘H-</option>
									<option value='湘J-'>湘J-</option>
									<option value='湘K-'>湘K-</option>
									<option value='湘L-'>湘L-</option>
									<option value='湘M-'>湘M-</option>
									<option value='湘N-'>湘N-</option>
									<option value='湘P-'>湘P-</option>
									<option value='-1'>====安徽省(皖)====</option>
									<option value='皖A-'>皖A-</option>
									<option value='皖B-'>皖B-</option>
									<option value='皖C-'>皖C-</option>
									<option value='皖D-'>皖D-</option>
									<option value='皖E-'>皖E-</option>
									<option value='皖F-'>皖F-</option>
									<option value='皖G-'>皖G-</option>
									<option value='皖H-'>皖H-</option>
									<option value='皖J-'>皖J-</option>
									<option value='皖K-'>皖K-</option>
									<option value='皖L-'>皖L-</option>
									<option value='皖M-'>皖M-</option>
									<option value='皖N-'>皖N-</option>
									<option value='皖P-'>皖P-</option>
									<option value='皖Q-'>皖Q-</option>
									<option value='皖R-'>皖R-</option>
									<option value='-1'>====山东省(鲁)====</option>
									<option value='鲁A-'>鲁A-</option>
									<option value='鲁B-'>鲁B-</option>
									<option value='鲁C-'>鲁C-</option>
									<option value='鲁D-'>鲁D-</option>
									<option value='鲁E-'>鲁E-</option>
									<option value='鲁F-'>鲁F-</option>
									<option value='鲁G-'>鲁G-</option>
									<option value='鲁H-'>鲁H-</option>
									<option value='鲁J-'>鲁J-</option>
									<option value='鲁K-'>鲁K-</option>
									<option value='鲁L-'>鲁L-</option>
									<option value='鲁M-'>鲁M-</option>
									<option value='鲁N-'>鲁N-</option>
									<option value='鲁P-'>鲁P-</option>
									<option value='鲁Q-'>鲁Q-</option>
									<option value='鲁R-'>鲁R-</option>
									<option value='鲁S-'>鲁S-</option>
									<option value='-1'>====新疆维吾尔(新)====</option>
									<option value='新A-'>新A-</option>
									<option value='新B-'>新B-</option>
									<option value='新C-'>新C-</option>
									<option value='新D-'>新D-</option>
									<option value='新E-'>新E-</option>
									<option value='新F-'>新F-</option>
									<option value='新G-'>新G-</option>
									<option value='新H-'>新H-</option>
									<option value='新J-'>新J-</option>
									<option value='新K-'>新K-</option>
									<option value='新L-'>新L-</option>
									<option value='新M-'>新M-</option>
									<option value='新N-'>新N-</option>
									<option value='新P-'>新P-</option>
									<option value='新Q-'>新Q-</option>
									<option value='新R-'>新R-</option>
									<option value='-1'>====江苏省(苏)====</option>
									<option value='苏A-'>苏A-</option>
									<option value='苏B-'>苏B-</option>
									<option value='苏C-'>苏C-</option>
									<option value='苏D-'>苏D-</option>
									<option value='苏E-'>苏E-</option>
									<option value='苏F-'>苏F-</option>
									<option value='苏G-'>苏G-</option>
									<option value='苏H-'>苏H-</option>
									<option value='苏J-'>苏J-</option>
									<option value='苏K-'>苏K-</option>
									<option value='苏L-'>苏L-</option>
									<option value='-1'>====浙江省(浙)====</option>
									<option value='浙A-'>浙A-</option>
									<option value='浙B-'>浙B-</option>
									<option value='浙C-'>浙C-</option>
									<option value='浙D-'>浙D-</option>
									<option value='浙E-'>浙E-</option>
									<option value='浙F-'>浙F-</option>
									<option value='浙G-'>浙G-</option>
									<option value='浙H-'>浙H-</option>
									<option value='浙J-'>浙J-</option>
									<option value='浙K-'>浙K-</option>
									<option value='浙L-'>浙L-</option>
									<option value='-1'>====江西省(赣)====</option>
									<option value='赣A-'>赣A-</option>
									<option value='赣B-'>赣B-</option>
									<option value='赣C-'>赣C-</option>
									<option value='赣D-'>赣D-</option>
									<option value='赣E-'>赣E-</option>
									<option value='赣F-'>赣F-</option>
									<option value='赣G-'>赣G-</option>
									<option value='赣H-'>赣H-</option>
									<option value='赣J-'>赣J-</option>
									<option value='赣K-'>赣K-</option>
									<option value='赣L-'>赣L-</option>
									<option value='-1'>====湖北省(鄂)====</option>
									<option value='鄂A-'>鄂A-</option>
									<option value='鄂B-'>鄂B-</option>
									<option value='鄂C-'>鄂C-</option>
									<option value='鄂D-'>鄂D-</option>
									<option value='鄂E-'>鄂E-</option>
									<option value='鄂F-'>鄂F-</option>
									<option value='鄂G-'>鄂G-</option>
									<option value='鄂H-'>鄂H-</option>
									<option value='鄂J-'>鄂J-</option>
									<option value='鄂K-'>鄂K-</option>
									<option value='鄂L-'>鄂L-</option>
									<option value='鄂M-'>鄂M-</option>
									<option value='鄂N-'>鄂N-</option>
									<option value='鄂P-'>鄂P-</option>
									<option value='鄂Q-'>鄂Q-</option>
									<option value='-1'>====广西壮族(桂)====</option>
									<option value='桂A-'>桂A-</option>
									<option value='桂B-'>桂B-</option>
									<option value='桂C-'>桂C-</option>
									<option value='桂D-'>桂D-</option>
									<option value='桂E-'>桂E-</option>
									<option value='桂F-'>桂F-</option>
									<option value='桂G-'>桂G-</option>
									<option value='桂H-'>桂H-</option>
									<option value='桂J-'>桂J-</option>
									<option value='桂K-'>桂K-</option>
									<option value='桂L-'>桂L-</option>
									<option value='桂M-'>桂M-</option>
									<option value='桂N-'>桂N-</option>
									<option value='桂P-'>桂P-</option>
									<option value='-1'>====甘肃省(甘)====</option>
									<option value='甘A-'>甘A-</option>
									<option value='甘B-'>甘B-</option>
									<option value='甘C-'>甘C-</option>
									<option value='甘D-'>甘D-</option>
									<option value='甘E-'>甘E-</option>
									<option value='甘F-'>甘F-</option>
									<option value='甘G-'>甘G-</option>
									<option value='甘H-'>甘H-</option>
									<option value='甘J-'>甘J-</option>
									<option value='甘K-'>甘K-</option>
									<option value='甘L-'>甘L-</option>
									<option value='甘M-'>甘M-</option>
									<option value='甘N-'>甘N-</option>
									<option value='甘P-'>甘P-</option>
									<option value='-1'>====山西省(晋)====</option>
									<option value='晋A-'>晋A-</option>
									<option value='晋B-'>晋B-</option>
									<option value='晋C-'>晋C-</option>
									<option value='晋D-'>晋D-</option>
									<option value='晋E-'>晋E-</option>
									<option value='晋F-'>晋F-</option>
									<option value='晋G-'>晋G-</option>
									<option value='晋H-'>晋H-</option>
									<option value='晋J-'>晋J-</option>
									<option value='晋K-'>晋K-</option>
									<option value='晋L-'>晋L-</option>
									<option value='晋M-'>晋M-</option>
									<option value='-1'>====内蒙古(蒙)====</option>
									<option value='蒙A-'>蒙A-</option>
									<option value='蒙B-'>蒙B-</option>
									<option value='蒙C-'>蒙C-</option>
									<option value='蒙D-'>蒙D-</option>
									<option value='蒙E-'>蒙E-</option>
									<option value='蒙F-'>蒙F-</option>
									<option value='蒙G-'>蒙G-</option>
									<option value='蒙H-'>蒙H-</option>
									<option value='蒙J-'>蒙J-</option>
									<option value='蒙K-'>蒙K-</option>
									<option value='蒙L-'>蒙L-</option>
									<option value='-1'>====陕西省(陕)====</option>
									<option value='陕A-'>陕A-</option>
									<option value='陕B-'>陕B-</option>
									<option value='陕C-'>陕C-</option>
									<option value='陕D-'>陕D-</option>
									<option value='陕E-'>陕E-</option>
									<option value='陕F-'>陕F-</option>
									<option value='陕G-'>陕G-</option>
									<option value='陕H-'>陕H-</option>
									<option value='陕J-'>陕J-</option>
									<option value='陕K-'>陕K-</option>
									<option value='-1'>====吉林省(吉)====</option>
									<option value='吉A-'>吉A-</option>
									<option value='吉B-'>吉B-</option>
									<option value='吉C-'>吉C-</option>
									<option value='吉D-'>吉D-</option>
									<option value='吉E-'>吉E-</option>
									<option value='吉F-'>吉F-</option>
									<option value='吉G-'>吉G-</option>
									<option value='吉H-'>吉H-</option>
									<option value='-1'>====福建省(闽)====</option>
									<option value='闽A-'>闽A-</option>
									<option value='闽B-'>闽B-</option>
									<option value='闽C-'>闽C-</option>
									<option value='闽D-'>闽D-</option>
									<option value='闽E-'>闽E-</option>
									<option value='闽F-'>闽F-</option>
									<option value='闽G-'>闽G-</option>
									<option value='闽H-'>闽H-</option>
									<option value='闽J-'>闽J-</option>
									<option value='-1'>====贵州省(贵)====</option>
									<option value='贵A-'>贵A-</option>
									<option value='贵B-'>贵B-</option>
									<option value='贵C-'>贵C-</option>
									<option value='贵D-'>贵D-</option>
									<option value='贵E-'>贵E-</option>
									<option value='贵F-'>贵F-</option>
									<option value='贵G-'>贵G-</option>
									<option value='贵H-'>贵H-</option>
									<option value='贵J-'>贵J-</option>
									<option value='-1'>====四川省(川)====</option>
									<option value='川A-'>川A-</option>
									<option value='川C-'>川C-</option>
									<option value='川D-'>川D-</option>
									<option value='川E-'>川E-</option>
									<option value='川F-'>川F-</option>
									<option value='川G-'>川G-</option>
									<option value='川H-'>川H-</option>
									<option value='川J-'>川J-</option>
									<option value='川K-'>川K-</option>
									<option value='川L-'>川L-</option>
									<option value='川M-'>川M-</option>
									<option value='川N-'>川N-</option>
									<option value='川P-'>川P-</option>
									<option value='川Q-'>川Q-</option>
									<option value='川R-'>川R-</option>
									<option value='川S-'>川S-</option>
									<option value='川T-'>川T-</option>
									<option value='川U-'>川U-</option>
									<option value='川V-'>川V-</option>
									<option value='川W-'>川W-</option>
									<option value='-1'>====青海省(青)====</option>
									<option value='青A-'>青A-</option>
									<option value='青B-'>青B-</option>
									<option value='青C-'>青C-</option>
									<option value='青D-'>青D-</option>
									<option value='青E-'>青E-</option>
									<option value='青F-'>青F-</option>
									<option value='青G-'>青G-</option>
									<option value='青H-'>青H-</option>
									<option value='-1'>====西藏(藏)====</option>
									<option value='藏A-'>藏A-</option>
									<option value='藏B-'>藏B-</option>
									<option value='藏C-'>藏C-</option>
									<option value='藏D-'>藏D-</option>
									<option value='藏E-'>藏E-</option>
									<option value='藏F-'>藏F-</option>
									<option value='藏G-'>藏G-</option>
									<option value='-1'>====海南省(琼)====</option>
									<option value='琼A-'>琼A-</option>
									<option value='琼B-'>琼B-</option>
									<option value='琼C-'>琼C-</option>
									<option value='-1'>====宁夏回族(宁)====</option>
									<option value='宁A-'>宁A-</option>
									<option value='宁B-'>宁B-</option>
									<option value='宁C-'>宁C-</option>
									<option value='宁D-'>宁D-</option>
									<option value='-1'>====重庆市(渝)====</option>
									<option value='渝A-'>渝A-</option>
									<option value='渝B-'>渝B-</option>
									<option value='渝C-'>渝C-</option>
									<option value='渝F-'>渝F-</option>
									<option value='渝G-'>渝G-</option>
									<option value='渝H-'>渝H-</option>
									<option value='-1'>====北京市(京)====</option>
									<option value='京A-'>京A-</option>
									<option value='京B-'>京B-</option>
									<option value='京C-'>京C-</option>
									<option value='京D-'>京D-</option>
									<option value='-1'>====天津市(津)====</option>
									<option value='津A-'>津A-</option>
									<option value='津C-'>津C-</option>
									<option value='津D-'>津D-</option>
									<option value='津E-'>津E-</option>
									<option value='-1'>====上海市(沪)====</option>
									<option value='沪A-'>沪A-</option>
									<option value='沪B-'>沪B-</option>
									<option value='沪C-'>沪C-</option>
									<option value='沪D-'>沪D-</option>
								</select>
								<input type='text' name='Car_Id_Num' style='width:29%;height:16px;' value='' maxlength='5'>
							<%
							}
							else
							{
							%>
								<%=Id%>
							<%
							}
							%>
						</td>
						<td width='20%' align='center'>车辆类型</td>
						<td width='30%' align='left'>
							<select name="CType" style="width:99%;height:20px">
							<%
							if(Car_Info.trim().length() > 0)
							{
								String[] CarList = Car_Info.split(";");
								for(int i=0; i<CarList.length && CarList[i].length()>0; i++)
								{
									String[] subCarList = CarList[i].split(",");
							%>
									<option value='<%=subCarList[0]%>' <%=subCarList[0].equals(CType)?"selected":""%>><%=subCarList[1]%></option>
							<%
								}
							}
							%>
							</select>
						</td>
					</tr>
					<tr height='40px'>
						<td width='20%' align='center'>车辆司机</td>
						<td width='30%' align='left'>
							<input type='text' name='Owner'  style='width:97%;height:18px;' value='<%=Owner%>'  maxlength='10'>
						</td>
						<td width='20%' align='center'>车载瓶号</td>
						<td width='30%' align='left'>
							<input type='text' name='Bottle' style='width:97%;height:18px;' value='<%=Bottle%>' maxlength='20'>
						</td>
					</tr>
				</table>
			</td>
		</tr>
		<tr height='40px'>
			<td width='100%' align='center'>
				<img src="../skin/images/mini_button_submit.gif" style='cursor:hand;' onClick='doEdit()'>
				<img src="../skin/images/button10.gif"           style='cursor:hand;' onclick='doNO()'>
			</td>
		</tr>
	</table>
	<input name="Cmd"         type="hidden" value="<%=Cmd%>">
	<input name="Sid"         type="hidden" value="<%=Sid%>">
	<input name="Crm_Id"      type="hidden" value="<%=Crm_Id%>">
	<input name="Id"          type="hidden" value="<%=Id%>">
	<input name="Func_Sub_Id" type="hidden" value="<%=currStatus.getFunc_Sub_Id()%>">
</form>
</body>
<SCRIPT LANGUAGE=javascript>
function doNO()
{
	
	location = "Ccm_Info.jsp?Sid=<%=Sid%>&Crm_Id=<%=Crm_Id%>";
}

function doEdit()
{
	
	switch(parseInt(<%=Cmd%>))
	{
		case 10:
				if(Ccm_Info_Edit.Car_Id.value.length < 1 || Ccm_Info_Edit.Car_Id.value == '-1')
			  {
			  	alert('请选择车牌所在地!');
			  	return;
			  }
			  if(Ccm_Info_Edit.Car_Id_Num.value.Trim().length != 5)
			  {
			  	alert('车牌尾数填写有误!');
			  	return;
			  }
			  Ccm_Info_Edit.Id.value = Ccm_Info_Edit.Car_Id.value + Ccm_Info_Edit.Car_Id_Num.value;
			break;
		case 11:
				Ccm_Info_Edit.Id.value = '<%=Id%>';
			break;
	}
	if(Ccm_Info_Edit.Id.value.Trim().length != 8)
	{
		alert('车辆牌号填写有误!');
		return;
	}
	if(Ccm_Info_Edit.CType.value.length < 1)
  {
  	
  	alert('请选择车辆类型!');
  	return;
  }
  if(Ccm_Info_Edit.Owner.value.Trim().length < 1)
  {
  	alert('请填写车辆司机姓名!');
  	return;
  }
  if(Ccm_Info_Edit.Bottle.value.Trim().length < 1)
  {
  	alert('请填写车载瓶号!');
  	return;
  }
  if(confirm("信息无误,确定提交?"))
  {
  	Ccm_Info_Edit.submit();
  }
}
</SCRIPT>
</html>