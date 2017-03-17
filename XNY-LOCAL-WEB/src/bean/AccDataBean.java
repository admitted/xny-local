package bean;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import rmi.Rmi;
import rmi.RmiBean;
import util.CommUtil;
import util.CurrStatus;

import com.alibaba.fastjson.JSON;
import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Magic;
import com.github.abel533.echarts.code.Tool;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.feature.MagicType;
import com.github.abel533.echarts.series.Line;


/** 
 * �ۻ��������ݴ���bean(ÿ�����������) 
 * AccDataBean���ݴ���bean
 * @author cui
 * 
 */
public class AccDataBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_ACC_DATA;
	
	/* ���DataBean�� serialVersionUID (non-Javadoc)
	 * @see rmi.RmiBean#getClassId()
	 */
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public AccDataBean()
	{
		super.className = "AccDataBean";
	}
	
	/** �ۻ����� ��ѯ
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @throws ServletException
	 * @throws IOException
	 */
	public void ExecCmd(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);
		System.out.println("1");
		switch(currStatus.getCmd())
		{
		    case 0://��վ�㣺�ۻ�����
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		    	request.getSession().setAttribute("Acc_Data_Sta_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Data_Sta.jsp?Sid=" + Sid);	
		    	System.out.println("2");
		    	break;
		    case 1://�������ܱ�
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		    	currStatus.setTotalRecord(msgBean.getCount());
		    	request.getSession().setAttribute("Acc_Data_Day_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Data_Day.jsp?Sid=" + Sid);
		    	break;
		    case 2://�������ܱ�
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		    	currStatus.setTotalRecord(msgBean.getCount());
		    	request.getSession().setAttribute("Acc_Data_Month_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Data_Month.jsp?Sid=" + Sid);
		    	break;
		}
		System.out.println("3");
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	   
	}
	
	//�±�����ϸ
	public void doTables(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) throws ServletException, IOException
	{
		getHtmlData(request);
		currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
		currStatus.getHtmlData(request, pFromZone);

		// ��վ��������-ÿ����ʾ
		// ������������ݵ�վ��
		msgBean = pRmi.RmiExec(6, this, 0);
		ArrayList<?> Acc_Data_Cpm = (ArrayList<?>) msgBean.getMsg();
		// ������������ݵ�վ�������ϸ����
		msgBean = pRmi.RmiExec(9, this, 0);
		ArrayList<?> Acc_Data_Cpm_Month = (ArrayList<?>) msgBean.getMsg();

		// ����ȡ����CPM_Data ���� CPMվ��ֽ�
		Map<String, Map> CpmMap = new HashMap<String, Map>();
		if (null != Acc_Data_Cpm)
		{
			Iterator<?> cpmIterator = Acc_Data_Cpm.iterator();
			while (cpmIterator.hasNext())
			{
				Map<Integer, String> daysDataMap = new HashMap<Integer, String>();
				AccDataBean CpmBean = (AccDataBean) cpmIterator.next();
				if (null != Acc_Data_Cpm_Month)
				{
					Iterator<?> cpmDataIterator = Acc_Data_Cpm_Month.iterator();
					while (cpmDataIterator.hasNext())
					{
						AccDataBean CpmDataBean = (AccDataBean) cpmDataIterator.next();
						if (CpmBean.getCpm_Id().equals(CpmDataBean.getCpm_Id()))
						{   
							daysDataMap.put(Integer.parseInt(CpmDataBean.getCTime().substring(8, 10)), (int)Math.floor(Float.parseFloat(CpmDataBean.getValue()))+"");
						}
					}
				}
				CpmMap.put(CpmBean.getCpm_Id(), daysDataMap);
			}
		}
		
		/**
	     *  GraphMaps <String, Option>  :< վ�� �� option����> 
	     *  �鵽�����ݽ��� data ���� �޴����ڵ� ��0���
 	     *  ���� GraphMaps
	     * */
		int thatMonthDays = CommUtil.getDaysOfMonth(currStatus.getVecDate().get(0).toString().substring(0,10));
		Map<String, JSON> GraphMaps = new HashMap<String, JSON>();
		
		for(String CPM : CpmMap.keySet()){
			String Cpm_Name = "";
			if (null != Acc_Data_Cpm)
			{
				Iterator<?> cpmIterator = Acc_Data_Cpm.iterator();
				while (cpmIterator.hasNext())
				{
					AccDataBean CpmBean = (AccDataBean) cpmIterator.next();
					if(CPM.equals(CpmBean.getCpm_Id()))
						Cpm_Name = CpmBean.getCpm_Name();
				}
			}
			Map<Integer, String> dataMap = CpmMap.get(CPM);
			Option option = new Option(); 
			// ���� �� legend ˵��
			option.title(Cpm_Name); 
		    option.toolbox().show(true).feature(Tool.mark, Tool.dataView, new MagicType(Magic.line, Magic.bar), Tool.restore, Tool.saveAsImage);
		    option.calculable(true);
		    option.tooltip().trigger(Trigger.axis).formatter("վ������ : <br/>{b}�� : {c}t");
		    
		    ValueAxis valueAxis = new ValueAxis();
		    valueAxis.axisLabel().formatter("{value} t");
		    option.yAxis(valueAxis);
		    
		    CategoryAxis categoryAxis = new CategoryAxis();
		    categoryAxis.axisLine().onZero(false);
//		    categoryAxis.axisLabel().formatter("{value} ��");
		    categoryAxis.boundaryGap(false);
		    
		    option.xAxis(categoryAxis);
		    Line line = new Line();
		    line.smooth(true).name("����(t)�����ڱ仯").itemStyle().normal().lineStyle().shadowColor("rgba(0,0,0,0.4)");
		    for(int i = 1; i <= thatMonthDays ; i++ ){
		    	// X����ӵ�����������
		    	categoryAxis.data(i); 
		    	// �������������
		    	if(null == dataMap.get(i))
		    		line.data("0");
		    	else
		    		line.data(dataMap.get(i));
		    }
		    option.series(line);
		    option.grid().x(80); 
		    GraphMaps.put(CPM, (JSON) JSON.toJSON(option));
		}
//		System.out.println(GraphMaps);
//		for(String s :GraphMaps.keySet()){
//			System.out.println(GraphMaps.get(s));
//		}
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		request.getSession().setAttribute("CpmMap_" + Sid, CpmMap);
		request.getSession().setAttribute("GraphMaps_" + Sid, GraphMaps);
		currStatus.setJsp("Acc_Data.jsp?Sid=" + Sid);
		response.sendRedirect(currStatus.getJsp());
	}
	
	/**
	 * ����Excel���
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 * @param pConfig
	 */
	public void ExportToExcel(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone)
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus) request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
			SimpleDateFormat SimFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String SheetName = "վ������ͳ�Ʊ�";
			String UPLOAD_NAME = SimFormat.format(new Date());
			System.out.println("SheetName [" + SheetName + "]");
			
			int    Cmd        = currStatus.getCmd();
			double Value_All  = 0.0;
			String BTime      = "";
			String StringTime = "";
			
			msgBean = pRmi.RmiExec(Cmd, this, 0);
			switch(Cmd)
			{
				case 2:
					StringTime = "�·�";
					BTime = currStatus.getVecDate().get(0).toString().substring(0, 7);
					break;
				default:
					StringTime = "����";
					break;
			}

			ArrayList<?> tempList = (ArrayList<?>) msgBean.getMsg();
			int row_Index = 0;
			Label cell = null;
			if (null != tempList)
			{
				WritableWorkbook book = Workbook.createWorkbook(new File(UPLOAD_PATH + UPLOAD_NAME + ".xls"));
				WritableSheet sheet = book.createSheet(SheetName, 0); // ������Ϊ"��һҳ"�Ĺ���������0��ʾ���ǵ�һҳ

				/*******  �����ʽ1  ********/
				WritableFont wf = new WritableFont(WritableFont.createFont("normal"), 14, WritableFont.BOLD, false);
				WritableCellFormat font1 = new WritableCellFormat(wf);
				// wf.setColour(Colour.BLACK);                        // ������ɫ
				font1.setAlignment(Alignment.CENTRE);                 // ���þ���
				font1.setVerticalAlignment(VerticalAlignment.CENTRE); // ����Ϊ��ֱ����
				font1.setBorder(Border.ALL, BorderLineStyle.THIN);    // ���ñ߿���
                font1.setBackground(jxl.format.Colour.TURQUOISE);     // ���õ�Ԫ��ı�����ɫ
				
				/*******  �����ʽ2  ********/
				WritableFont wf2 = new WritableFont(WritableFont.createFont("normal"), 10, WritableFont.NO_BOLD, false);
				WritableCellFormat font2 = new WritableCellFormat(wf2);
				wf2.setColour(Colour.BLACK);                          // ������ɫ
				font2.setAlignment(Alignment.CENTRE);                 // ���þ���
				font2.setVerticalAlignment(VerticalAlignment.CENTRE); // ����Ϊ��ֱ����
				font2.setBorder(Border.ALL, BorderLineStyle.THIN);    // ���ñ߿���
				
				/*******  �����ʽ3  ********/
				WritableFont wf3 = new WritableFont(WritableFont.createFont("normal"), 10, WritableFont.BOLD, false);
				WritableCellFormat font3 = new WritableCellFormat(wf3);
				wf3.setColour(Colour.RED);                            // ������ɫ
				font3.setAlignment(Alignment.CENTRE);                 // ���þ���
				font3.setVerticalAlignment(VerticalAlignment.CENTRE); // ����Ϊ��ֱ����
				font3.setBorder(Border.ALL, BorderLineStyle.THIN);    // ���ñ߿���
				font3.setBackground(jxl.format.Colour.YELLOW);        // ���õ�Ԫ��ı�����ɫ

				sheet.setRowView(row_Index, 450);
				sheet.setColumnView(row_Index, 25);
				cell = new Label(0, 0, StringTime, font1);
				sheet.addCell(cell);
				cell = new Label(1, 0, "վ������", font1);
				sheet.addCell(cell);
				cell = new Label(2, 0, "��ʼ����", font1);
				sheet.addCell(cell);
				cell = new Label(3, 0, "��ֹ����", font1);
				sheet.addCell(cell);
				cell = new Label(4, 0, "������", font1);
				sheet.addCell(cell);
				cell = new Label(5, 0, "��ע", font1);
				sheet.addCell(cell);
				
				Iterator<?> temp_iterator = tempList.iterator();
				while (temp_iterator.hasNext())
				{
					AccDataBean accDataBean = (AccDataBean) temp_iterator.next();
					String Temp_Cpm_Name    = accDataBean.getCpm_Name();
					String Temp_B_Value     = accDataBean.getB_Value();
					String Temp_E_Value     = accDataBean.getE_Value();
					if(2 != Cmd)
					{      
						BTime               = accDataBean.getCTime();
					}
					String Temp_Value       = accDataBean.getValue();
					String Temp_Des         = accDataBean.getDes();
					
					Value_All = Value_All + Double.parseDouble(Temp_Value);
					
					row_Index++;
					sheet.setRowView(row_Index, 400);
					sheet.setColumnView(row_Index, 25); // row_Index �п��

					cell = new Label(0, row_Index, BTime, font2);
					sheet.addCell(cell);
					cell = new Label(1, row_Index, Temp_Cpm_Name, font2);
					sheet.addCell(cell);
					cell = new Label(2, row_Index, Temp_B_Value, font2);
					sheet.addCell(cell);
					cell = new Label(3, row_Index, Temp_E_Value, font2);
					sheet.addCell(cell);
					cell = new Label(4, row_Index, Temp_Value, font2);
					sheet.addCell(cell);
					cell = new Label(5, row_Index, Temp_Des, font2);
					sheet.addCell(cell);
				}
				
				if(0 == Cmd)
				{
					row_Index++;
					sheet.setRowView(row_Index, 400);
					sheet.setColumnView(row_Index, 25); 
					
					cell = new Label(0, row_Index, "��ҳ�ϼƣ��������ܼ�Ϊ��" + new BigDecimal(Value_All).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue(), font3);
					sheet.addCell(cell);
					for(int i = 1;i<5;i++){
						cell = new Label(i, row_Index, "", font3);
						sheet.addCell(cell);
					}
					sheet.mergeCells(0, row_Index, 5, row_Index); // �ϲ����
				}
				book.write();
				book.close();
				try
				{
					PrintWriter out = response.getWriter();
					out.print(UPLOAD_NAME);
				}
				catch (Exception exp)
				{
					exp.printStackTrace();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/** ����ͼ�� Graph
	 * @param request
	 * @param response
	 * @param pRmi
	 * @param pFromZone
	 */
	public void Graph(HttpServletRequest request, HttpServletResponse response, Rmi pRmi, boolean pFromZone) 
	{
		try
		{
			getHtmlData(request);
			currStatus = (CurrStatus)request.getSession().getAttribute("CurrStatus_" + Sid);
			currStatus.getHtmlData(request, pFromZone);
		    
			switch(currStatus.getFunc_Sub_Id())
			{
			    case 1://ʱ��ֵ
			    	break;
			    case 2://�վ�ֵ
			    	request.getSession().setAttribute("Month_" + Sid, Month);
			    	request.getSession().setAttribute("Year_" + Sid, Year);
			    	break;
			}
			
			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
			switch(currStatus.getCmd())
			{
				case 20://����ͼ��
			    	request.getSession().setAttribute("Graph_" + Sid, (Object)msgBean.getMsg());
					currStatus.setJsp("Graph.jsp?Sid=" + Sid);
					break;
			}
			
			request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
		   	response.sendRedirect(currStatus.getJsp());
		}
		catch(Exception Ex)
		{
			Ex.printStackTrace();
		}
	}

	public String getSql(int pCmd)
	{
		String Sql = "";
		switch (pCmd)
		{
			case 0://�ۻ����� (��ʷ) վ��������
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.b_value, t.e_value, t.value, t.unit,  t.des " +
					 " from view_acc_data_day t " +
					 " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					 " and t.ctime >= date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					 " and t.ctime <= date_format('"+currStatus.getVecDate().get(1).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					 " order by t.ctime desc ";
			   break;
			case 1://�������ܱ�
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.b_value, t.e_value, t.value, t.unit,  t.des " +
					 " from view_acc_data_day t " +
			  	  	 " where t.ctime = date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d')" +
			  	  	 " order by t.cpm_id ";
			   break;
			case 2://�������ܱ�
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, (t.e_value - sum(t.value)) as b_value , t.e_value, sum(t.value) value , t.unit,  t.des " +
					 " from (SELECT * FROM view_acc_data_day WHERE (DATE_FORMAT(ctime, '%Y-%m') = DATE_FORMAT('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m'))  ORDER BY ctime DESC) t " + 
					 " GROUP BY CPM_ID ";
			   break;
			case 6://ĳ�������ݵ�վ������Щ��
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, (t.e_value - sum(t.value)) as b_value , t.e_value, sum(t.value) value , t.unit,  t.des " +
					 " FROM view_acc_data_day t  " + 
					 " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					 " and (DATE_FORMAT(ctime, '%Y-%m') = DATE_FORMAT('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m'))"+
			         " group by cpm_id order by t.cpm_id";
				   break;
			case 9://ĳ����ϸ����
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.id, t.cname, t.attr_id, t.attr_name, t.ctime, t.b_value , t.e_value, t.e_value - t.b_value as value , t.unit,  t.des " +
					 " FROM view_acc_data_day t  " + 
					 " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					 " and (DATE_FORMAT(ctime, '%Y-%m') = DATE_FORMAT('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m'))"+
			         " order by t.cpm_id ,t.ctime";
				   break;
			case 20://����ͼ��
			   Sql = " {? = call rmi_graph('"+ Id +"', '"+ currStatus.getFunc_Sub_Id() +"', '"+ currStatus.getVecDate().get(0).toString().substring(0,10) +"')}";
			   break;
		}
		return Sql;
	}

	public boolean getData(ResultSet pRs) 
	{
		boolean IsOK = true;
		try
		{
			setSN(pRs.getString(1));
			setCpm_Id(pRs.getString(2));		
			setCpm_Name(pRs.getString(3));		
			setId(pRs.getString(4));
			setCName(pRs.getString(5));		
			setAttr_Id(pRs.getString(6));
			setAttr_Name(pRs.getString(7));			
			setCTime(pRs.getString(8));
			setB_Value(pRs.getString(9));
			setE_Value(pRs.getString(10));
			setValue(pRs.getString(11));
			setUnit(pRs.getString(12));
			setDes(pRs.getString(13));
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}		
		return IsOK;
	}
	
	/** ��ȡrequestҳ�� SN��ID��Cpm_Id��Level ������
	 * @param request
	 * @return
	 */
	public boolean getHtmlData(HttpServletRequest request) 
	{
		boolean IsOK = true;
		try 
		{	
			setSN(CommUtil.StrToGB2312(request.getParameter("SN")));
			setCpm_Id(CommUtil.StrToGB2312(request.getParameter("Cpm_Id")));
			setCpm_Name(CommUtil.StrToGB2312(request.getParameter("Cpm_Name")));
			setId(CommUtil.StrToGB2312(request.getParameter("Id")));
			setCName(CommUtil.StrToGB2312(request.getParameter("CName")));
			setAttr_Id(CommUtil.StrToGB2312(request.getParameter("Attr_Id")));
			setAttr_Name(CommUtil.StrToGB2312(request.getParameter("Attr_Name")));		
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));		
			setB_Value(CommUtil.StrToGB2312(request.getParameter("B_Value")));
			setE_Value(CommUtil.StrToGB2312(request.getParameter("E_Value")));
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setUnit(CommUtil.StrToGB2312(request.getParameter("Unit")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
			setLevel(CommUtil.StrToGB2312(request.getParameter("Level")));
			setYear(CommUtil.StrToGB2312(request.getParameter("Year")));
			setMonth(CommUtil.StrToGB2312(request.getParameter("Month")));		
			setFunc_Cpm_Id(CommUtil.StrToGB2312(request.getParameter("Func_Cpm_Id")));
		}
		catch (Exception Exp) 
		{
			Exp.printStackTrace();
		}
		return IsOK;
	}
	
	private String SN;
	private String Cpm_Id;
	private String Cpm_Name;
	private String Id;
	private String CName;
	private String Attr_Id;
	private String Attr_Name;
	private String CTime;
	private String B_Value;
	private String E_Value;
	private String Value;
	private String Unit;
	private String Des;

	private String Sid;
	private String Level;
	private String Year;
	private String Month;
	
	private String Func_Cpm_Id;

	public String getSN()
	{
		return SN;
	}

	public void setSN(String sN)
	{
		SN = sN;
	}

	public String getCpm_Id()
	{
		return Cpm_Id;
	}

	public void setCpm_Id(String cpm_Id)
	{
		Cpm_Id = cpm_Id;
	}
	
	public String getCpm_Name()
	{
		return Cpm_Name;
	}

	public void setCpm_Name(String cpm_Name)
	{
		Cpm_Name = cpm_Name;
	}

	public String getId()
	{
		return Id;
	}

	public void setId(String id)
	{
		Id = id;
	}

	public String getCName()
	{
		return CName;
	}

	public void setCName(String cName)
	{
		CName = cName;
	}

	public String getAttr_Id()
	{
		return Attr_Id;
	}

	public void setAttr_Id(String attr_Id)
	{
		Attr_Id = attr_Id;
	}

	public String getAttr_Name()
	{
		return Attr_Name;
	}

	public void setAttr_Name(String attr_Name)
	{
		Attr_Name = attr_Name;
	}

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
	}

	public String getB_Value()
	{
		return B_Value;
	}

	public void setB_Value(String b_Value)
	{
		B_Value = b_Value;
	}

	public String getE_Value()
	{
		return E_Value;
	}

	public void setE_Value(String e_Value)
	{
		E_Value = e_Value;
	}

	public String getValue()
	{
		return Value;
	}

	public void setValue(String value)
	{
		Value = value;
	}

	public String getUnit()
	{
		return Unit;
	}

	public void setUnit(String unit)
	{
		Unit = unit;
	}

	public String getDes()
	{
		return Des;
	}

	public void setDes(String des)
	{
		Des = des;
	}

	public String getSid()
	{
		return Sid;
	}

	public void setSid(String sid)
	{
		Sid = sid;
	}

	public String getLevel()
	{
		return Level;
	}

	public void setLevel(String level)
	{
		Level = level;
	}

	public String getYear()
	{
		return Year;
	}

	public void setYear(String year)
	{
		Year = year;
	}

	public String getMonth()
	{
		return Month;
	}

	public void setMonth(String month)
	{
		Month = month;
	}

	public String getFunc_Cpm_Id()
	{
		return Func_Cpm_Id;
	}

	public void setFunc_Cpm_Id(String func_Cpm_Id)
	{
		Func_Cpm_Id = func_Cpm_Id;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	
	
	
	
}