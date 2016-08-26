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
import java.util.Iterator;

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
import util.*;

/** 
 * 销售数据处理bean(每天的销售额) 
 * AccDataBean数据处理bean
 * @author cui
 * 
 */
public class AccSaleBean extends RmiBean 
{	
	public final static long serialVersionUID = RmiBean.RMI_ACC_SALE;
	
	public long getClassId()
	{
		return serialVersionUID;
	}
	
	public AccSaleBean()
	{
		super.className = "AccSaleBean";
	}
	
	/** 销售 查询
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
		
		switch(currStatus.getCmd())
		{
		    case 0://各站点：销售列表
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
		    	request.getSession().setAttribute("Acc_Sale_Sta_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Sale_Sta.jsp?Sid=" + Sid);	
		    	break;
		    	
		    case 1://日销售总表
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		    	currStatus.setTotalRecord(msgBean.getCount());
		    	request.getSession().setAttribute("Acc_Sale_Day_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Sale_Day.jsp?Sid=" + Sid);
		    	break;
		    	
		    case 2://月销售总表
		    	msgBean = pRmi.RmiExec(currStatus.getCmd(), this, currStatus.getCurrPage());
		    	currStatus.setTotalRecord(msgBean.getCount());
		    	request.getSession().setAttribute("Acc_Sale_Month_" + Sid, ((Object)msgBean.getMsg()));
		    	currStatus.setJsp("Acc_Sale_Month.jsp?Sid=" + Sid);
		    	break;
		}
		
		request.getSession().setAttribute("CurrStatus_" + Sid, currStatus);
	   	response.sendRedirect(currStatus.getJsp());
	   
	}
	
	/**
	 * 导出Excel表格
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
			String SheetName = "站点销售统计表";
			String UPLOAD_NAME = SimFormat.format(new Date());
			System.out.println("SheetName [" + SheetName + "]");
			
			int    Cmd        = currStatus.getCmd();
			double Value_All  = 0.0;
			double Salary_All = 0.0;
			String BTime      = "";
			String StringTime = "";
			
			msgBean = pRmi.RmiExec(Cmd, this, 0);
			switch(Cmd)
			{
				case 2:
					StringTime = "月份";
					BTime = currStatus.getVecDate().get(0).toString().substring(0, 7);
					break;
				default:
					StringTime = "日期";
					break;
			}
			
			ArrayList<?> tempList = (ArrayList<?>) msgBean.getMsg();
			int row_Index = 0;
			Label cell = null;
			if (null != tempList)
			{
				WritableWorkbook book = Workbook.createWorkbook(new File(UPLOAD_PATH + UPLOAD_NAME + ".xls"));
				WritableSheet sheet = book.createSheet(SheetName, 0); // 生成名为"第一页"的工作表，参数0表示这是第一页

				/*******  字体格式1  ********/
				WritableFont wf = new WritableFont(WritableFont.createFont("normal"), 14, WritableFont.BOLD, false);
				WritableCellFormat font1 = new WritableCellFormat(wf);
				// wf.setColour(Colour.BLACK);                        // 字体颜色
				font1.setAlignment(Alignment.CENTRE);                 // 设置居中
				font1.setVerticalAlignment(VerticalAlignment.CENTRE); // 设置为垂直居中
				font1.setBorder(Border.ALL, BorderLineStyle.THIN);    // 设置边框线
                font1.setBackground(jxl.format.Colour.TURQUOISE);     // 设置单元格的背景颜色
				
				/*******  字体格式2  ********/
				WritableFont wf2 = new WritableFont(WritableFont.createFont("normal"), 10, WritableFont.NO_BOLD, false);
				WritableCellFormat font2 = new WritableCellFormat(wf2);
				wf2.setColour(Colour.BLACK);                          // 字体颜色
				font2.setAlignment(Alignment.CENTRE);                 // 设置居中
				font2.setVerticalAlignment(VerticalAlignment.CENTRE); // 设置为垂直居中
				font2.setBorder(Border.ALL, BorderLineStyle.THIN);    // 设置边框线
				
				/*******  字体格式3  ********/
				WritableFont wf3 = new WritableFont(WritableFont.createFont("normal"), 10, WritableFont.BOLD, false);
				WritableCellFormat font3 = new WritableCellFormat(wf3);
				wf3.setColour(Colour.RED);                            // 字体颜色
				font3.setAlignment(Alignment.CENTRE);                 // 设置居中
				font3.setVerticalAlignment(VerticalAlignment.CENTRE); // 设置为垂直居中
				font3.setBorder(Border.ALL, BorderLineStyle.THIN);    // 设置边框线
				font3.setBackground(jxl.format.Colour.YELLOW);        // 设置单元格的背景颜色

				sheet.setRowView(row_Index, 450);
				sheet.setColumnView(row_Index, 25);
				cell = new Label(0, 0, "站点名称", font1);
				sheet.addCell(cell);
				cell = new Label(1, 0, StringTime, font1);
				sheet.addCell(cell);
				cell = new Label(2, 0, "单价", font1);
				sheet.addCell(cell);
				cell = new Label(3, 0, "用气量", font1);
				sheet.addCell(cell);
				cell = new Label(4, 0, "金额", font1);
				sheet.addCell(cell);
				cell = new Label(5, 0, "备注", font1);
				sheet.addCell(cell);
				
				Iterator<?> temp_iterator = tempList.iterator();
				while (temp_iterator.hasNext())
				{				
					AccSaleBean accSaleBean = (AccSaleBean) temp_iterator.next();					
					String Temp_Cpm_Name    = accSaleBean.getCpm_Name();
					if(2 != Cmd)
					{      
						BTime               = accSaleBean.getCTime();
					}
					String Temp_Unit_Price  = accSaleBean.getUnit_Price();		
					String Temp_Value       = accSaleBean.getValue();		
					String Temp_Salary      = accSaleBean.getSalary();		
					String Temp_Des         = accSaleBean.getDes();
					
					Value_All =  Value_All + Double.parseDouble(Temp_Value);
					Salary_All = Salary_All + Double.parseDouble(Temp_Salary);

					row_Index++;
					sheet.setRowView(row_Index, 400);
					sheet.setColumnView(row_Index, 25); // row_Index 列宽度

					cell = new Label(0, row_Index, Temp_Cpm_Name, font2);
					sheet.addCell(cell);
					cell = new Label(1, row_Index, BTime, font2);
					sheet.addCell(cell);
					cell = new Label(2, row_Index, Temp_Unit_Price, font2);
					sheet.addCell(cell);
					cell = new Label(3, row_Index, Temp_Value, font2);
					sheet.addCell(cell);
					cell = new Label(4, row_Index, Temp_Salary, font2);
					sheet.addCell(cell);
					cell = new Label(5, row_Index, Temp_Des, font2);
					sheet.addCell(cell);
				}
				
				if(0 == Cmd)
				{
					row_Index++;
					sheet.setRowView(row_Index, 400);
					sheet.setColumnView(row_Index, 25); 
					
					cell = new Label(0, row_Index, "本页合计：用气量总计为："+ new BigDecimal(Value_All).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() +"t               金额合计为："+ new BigDecimal(Salary_All).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() +"元", font3);
					sheet.addCell(cell);
					for(int i = 1;i<5;i++){
						cell = new Label(i, row_Index, "", font3);
						sheet.addCell(cell);
					}
					sheet.mergeCells(0, row_Index, 5, row_Index);
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
	
	
	/** 数据图表 Graph
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
			    case 1://时均值
			    	break;
			    case 2://日均值
			    	request.getSession().setAttribute("Month_" + Sid, Month);
			    	request.getSession().setAttribute("Year_" + Sid, Year);
			    	break;
			}
			
			msgBean = pRmi.RmiExec(currStatus.getCmd(), this, 0);
			switch(currStatus.getCmd())
			{
				case 20://数据图表
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
			case 0://累积銷售 (历史) 站点销售表
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.ctime, t.unit_price, t.value, t.salary, t.des " +
					 " from view_Acc_Sale_day t " +
					 " where instr('"+ Cpm_Id +"', t.cpm_id) > 0 " +
					 " and t.ctime >= date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					 " and t.ctime <= date_format('"+currStatus.getVecDate().get(1).toString()+"', '%Y-%m-%d %H-%i-%S')" +
					 " order by t.ctime desc ";
			   break;
			case 1://日销售总表
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.ctime, t.unit_price, t.value, t.salary, t.des " +
					 " from view_Acc_Sale_day t " +
			  	  	 " where t.ctime = date_format('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m-%d')" +
			  	  	 " order by t.cpm_id ";
			   break;
			case 2://月销售总表
			   Sql = " select t.sn, t.cpm_id, t.cpm_name, t.ctime, t.unit_price, sum(t.value) value, sum(t.salary) salary, t.des " +
					 " from (SELECT * FROM view_acc_sale_day WHERE (DATE_FORMAT(ctime, '%Y-%m') = DATE_FORMAT('"+currStatus.getVecDate().get(0).toString()+"', '%Y-%m'))  ORDER BY ctime DESC) t " + 
					 " GROUP BY CPM_ID ";
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
			setCTime(pRs.getString(4));
			setUnit_Price(pRs.getString(5));
			setValue(pRs.getString(6));
			setSalary(pRs.getString(7));
			setDes(pRs.getString(8));
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}		
		return IsOK;
	}
	
	/** 获取request页面 SN、ID、Cpm_Id、Level 等数据
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
			setCTime(CommUtil.StrToGB2312(request.getParameter("CTime")));		
			setUnit_Price(CommUtil.StrToGB2312(request.getParameter("Unit_Price")));
			setValue(CommUtil.StrToGB2312(request.getParameter("Value")));
			setSalary(CommUtil.StrToGB2312(request.getParameter("Salary")));
			setDes(CommUtil.StrToGB2312(request.getParameter("Des")));
			
			setSid(CommUtil.StrToGB2312(request.getParameter("Sid")));
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
	private String CTime;
	private String Unit_Price;
	private String Value;
	private String Salary;
	private String Des;

	private String Sid;
	private String Year;
	private String Month;
	

	public String getUnit_Price() {
		return Unit_Price;
	}

	public void setUnit_Price(String unit_Price) {
		Unit_Price = unit_Price;
	}

	public String getSalary() {
		return Salary;
	}

	public void setSalary(String salary) {
		Salary = salary;
	}

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

	

	public String getCTime()
	{
		return CTime;
	}

	public void setCTime(String cTime)
	{
		CTime = cTime;
	}

	
	public String getValue()
	{
		return Value;
	}

	public void setValue(String value)
	{
		Value = value;
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