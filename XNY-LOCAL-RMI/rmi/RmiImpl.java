/*
 * 0:查询全部 
 * 1：查询单个 
 * 2：插入Insert 
 * 3：修改update case 
 * 4: 删除 delete
 * 5: 执行函数
 * */
package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;//用于执行sql存储过程的接口

import util.*;

import java.util.*;

import net.TPCClient;
import bean.*;
import oracle.jdbc.OracleTypes;

/** RmiImpl implements Rmi 
 * @author Cui
 * 以后看到Rmi, 可以这么联想
 *  Rmi pRmi = new RmiImpl();
 *  
 * 0: 查询 
 * 1：插入,更新,删除  
 * 2：Function调用
 * 3：Package调用
 * 4: Producer调用
 */
public class RmiImpl extends UnicastRemoteObject implements Rmi
{
	public final static long serialVersionUID = 1001;
	
	private DBUtil m_DBUtil = null;	
	private TPCClient m_TPCClient = null;	
	
	/** 空参构造器
	 * @throws RemoteException
	 */
	public RmiImpl() throws RemoteException {
		
	}
	
	/** 初始化数据库连接池
	 * @param pDbUtil
	 */
	public void Init(DBUtil pDbUtil, TPCClient pTPCClient) {
		m_DBUtil = pDbUtil;
		m_TPCClient = pTPCClient;
	}
	
	/** RMI测试
	 * @see rmi.Rmi#Test()
	 */
	public boolean Test()throws RemoteException {
		return true;
	}
	
	/** 根据cmd命令 , 传入的pBean , CurrPage当前页码 
	 **case 0: 查询 ;
	 * case 1: 增删改 ;
	 * case 2: Function函数调用 ;
	 * case 3: Package调用 ;   ???oracle.jdbc???
	 * case 4: Producer调用 ;
	 * @see rmi.Rmi#RmiExec(int, rmi.RmiBean, int, int)
	 */
	public MsgBean RmiExec(int pCmd, RmiBean pBean, int CurrPage) throws RemoteException
	{
		MsgBean objBean = null;
		ArrayList<?> aList = null;
		String Sql = pBean.getSql(pCmd);		
		int recordCount = 0;
		System.out.println("ClassId:["+pBean.getClassId()+"]"+" ClassName["+pBean.getClassName()+"]"+" Cmd["+ pCmd + "]");
		System.out.println("BSql["+Sql+"]");
		switch(pCmd/10)
		{
			case 0://查询
				if(0 < CurrPage) //总数: 需要分页显示时,才会去计算recordCount值  查询记录总数
				{  
					recordCount = Integer.parseInt(doRecordCount("select count(*) as counts from (" + Sql + ")subselect"));
					System.out.println("RecordCount["+recordCount+"]");
					//分页: 有分页显示,后台RMI下 才会出现以下打印
					Sql = Sql + " LIMIT " + MsgBean.CONST_PAGE_SIZE + " OFFSET " + (CurrPage-1)*MsgBean.CONST_PAGE_SIZE;
					System.out.println("ESql["+Sql+"]");
					System.out.println("条数["+MsgBean.CONST_PAGE_SIZE+"]");
				}				
				aList = doSelect(Sql, pBean.getClassId());
				if(aList != null && aList.size() > 0) //若有数据返回  将数据封装到MsgBean中去.
				{
					objBean = new MsgBean(MsgBean.STA_SUCCESS,  aList, recordCount);
				}
				else                                 //若无数据返回  MsgBean中的集合对象则为空.
				{
					objBean = new MsgBean(MsgBean.STA_FAILED,  null, recordCount);
				}
				break;
			case 1://插入,更新,删除				
				if(doUpdate(Sql))    
				{		
					objBean = new MsgBean(MsgBean.STA_SUCCESS, null, recordCount);
				}
				else
				{
					objBean = new MsgBean(MsgBean.STA_FAILED, CommUtil.IntToStringLeftFillSpace(MsgBean.STA_FAILED, 4), recordCount);
				}	
				break;
			case 2://Function调用
				String rst = doFunction(Sql); 
				CommUtil.PRINT(rst);
				if(rst != null && rst.substring(0, 4).equals("0000"))    
				{		
					objBean = new MsgBean(MsgBean.STA_SUCCESS, rst, recordCount);
				}
				else
				{
					objBean = new MsgBean(Integer.parseInt(rst.substring(0, 4)), rst.substring(0, 4), recordCount);
				}	
				break;
			case 3://Package调用
				aList = Do_Package(Sql, pBean.getClassId());
				if(aList != null && aList.size() > 0)
				{
					objBean = new MsgBean(MsgBean.STA_SUCCESS,  aList, recordCount);
				}
				else
				{
					objBean = new MsgBean(MsgBean.STA_FAILED,  null, recordCount);
				}
				break;
			case 4://Producer调用
				doProducer(Sql); 
				rst = "0000";
				CommUtil.PRINT(rst);
				if(rst != null && rst.substring(0, 4).equals("0000"))    
				{		
					objBean = new MsgBean(MsgBean.STA_SUCCESS, rst, recordCount);
				}
				else
				{
					objBean = new MsgBean(Integer.parseInt(rst.substring(0, 4)), rst.substring(0, 4), recordCount);
				}	
				break;
		}			
		return objBean;
	}
	
	/** 数据库查询操作
	 * @param pSql
	 * @param pClass
	 *    bean的类值
	 * @return
	 */
	public ArrayList<?> doSelect(String pSql, long pClass) //Bean Type
	{
		ArrayList<Object> alist = new ArrayList<Object>();
		RmiBean rmiBean = null;
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		try 
		{
			conn = m_DBUtil.objConnPool.getConnection();
			conn.setAutoCommit(false);
			pStmt = conn.prepareStatement(pSql);
			rs = pStmt.executeQuery();
			while(rs.next())
			{
				switch((int)pClass)
				{
				/******************system****************/
				/******************admin*****************/
					case RmiBean.RMI_CORP_INFO:
						rmiBean = new CorpInfoBean();
						break;
					case RmiBean.RMI_USER_INFO:
						rmiBean = new UserInfoBean();
						break;
					case RmiBean.RMI_USER_ROLE:
						rmiBean = new UserRoleBean();
						break;
					case RmiBean.RMI_DEVICE_DETAIL:
				 		rmiBean = new DeviceDetailBean();
				 		break;
					case RmiBean.RMI_USER_POSITION:
						rmiBean = new UserPositionBean();
						break;
				/******************user*****************/
					case RmiBean.RMI_DATA:
						rmiBean = new DataBean();
						break;
					case RmiBean.RMI_DATA_NOW:
						rmiBean = new DataNowBean();
						break;
					case RmiBean.RMI_LINKAGE:
						rmiBean = new LinkageInfoBean();
						break;
					case RmiBean.RMI_ALERT:
						rmiBean = new AlertInfoBean();
						break;
					case RmiBean.RMI_ACC_DATA:
						rmiBean = new AccDataBean();
						break;
					case RmiBean.RMI_ACC_SALE:
						rmiBean = new AccSaleBean();
						break;
					case RmiBean.RMI_DEV_CTRL:
						rmiBean = new DevCtrlBean();
						break;
				}
				rmiBean.getData(rs);
				alist.add(rmiBean);
			}
		} catch (SQLException sqlExp)
		{
			sqlExp.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs != null)
				{				
					rs.close();
					rs = null;
				}
				if(pStmt != null)
				{				
					pStmt.close();
					pStmt = null;
				}
				if(conn != null)
				{
					conn.close();	
					conn = null;
				}
			}catch(Exception ex)
			{ex.printStackTrace();}
		}
		return alist;
	}
	
	/** 数据库的 增\删\改 操作
	 * @param pSql
	 * @return
	 *     返回boolean,表示是否操作成功
	 */
	public boolean doUpdate(String pSql)
	{
		boolean IsOK = false;
		Connection conn = null;
		PreparedStatement pStmt = null;
		try 
		{
			conn = m_DBUtil.objConnPool.getConnection();
			conn.setAutoCommit(false);
			pStmt = conn.prepareStatement(pSql);
			if (pStmt.executeUpdate() > 0) 
			{
				IsOK = true;
				conn.commit();
			} 
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}
		finally
		{
			try
			{
				if(pStmt != null)
				{				
					pStmt.close();
					pStmt = null;
				}
				if(conn != null)
				{
					conn.close();	
					conn = null;
				}
			}catch(Exception ex)
			{ex.printStackTrace();}
		}
		return IsOK;
	}

	/** 数据库的Function函数调用
	 * @param pSql
	 * @return 
	 *     返回数据库函数的? 
	 */
	public String doFunction(String pSql)
	{
		String rslt = null;
		Connection conn = null;
		CallableStatement cstat = null;
		try 
		{
			conn = m_DBUtil.objConnPool.getConnection();
			conn.setAutoCommit(false);
			cstat = conn.prepareCall(pSql);
			cstat.registerOutParameter(1, java.sql.Types.VARCHAR);
			cstat.execute();
			rslt = cstat.getString(1);
			conn.commit();
			//System.out.println("rslt:" + rslt);
		}
		catch(SQLException sqlExp)
		{
			sqlExp.printStackTrace();
			return  CommUtil.IntToStringLeftFillSpace(MsgBean.STA_FAILED, 4);
		}
		finally
		{
			try
			{
				if(cstat != null)
				{				
					cstat.close();
					cstat = null;
				}
				if(conn != null)
				{
					conn.close();	
					conn = null;
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return rslt;
	}
	
	/** 数据库的Producer存储过程调用
	 * @param pSql
	 * @return 
	 *     返回数据库存储过程的?返回值? 
	 */
	public String doProducer(String pSql)
	{
		String rslt = null;
		Connection conn = null;
		CallableStatement cstat = null;
		try 
		{
			conn = m_DBUtil.objConnPool.getConnection();
			conn.setAutoCommit(false);
			cstat = conn.prepareCall(pSql);
			cstat.execute();
			conn.commit();
			//System.out.println("rslt:" + rslt);
		}
		catch(SQLException sqlExp)
		{
			sqlExp.printStackTrace();
			return  CommUtil.IntToStringLeftFillSpace(MsgBean.STA_FAILED, 4);
		}
		finally
		{
			try
			{
				if(cstat != null)
				{				
					cstat.close();
					cstat = null;
				}
				if(conn != null)
				{
					conn.close();	
					conn = null;
				}
			}catch(Exception ex)
			{
				ex.printStackTrace();
			}
		}
		return rslt;
	}
	
	/** 数据库  记录总数查询
	 * @param pSql
	 * @return String
	 *   将int值转成String类型返回
	 */
	public String doRecordCount(String pSql)
	{
		String rslt = null;
		Connection conn = null;
		PreparedStatement pStmt = null;
		ResultSet rs = null;
		try 
		{
			conn = m_DBUtil.objConnPool.getConnection();
			conn.setAutoCommit(false);
			pStmt = conn.prepareStatement(pSql);
			rs = pStmt.executeQuery();		
			while(rs.next())
			{
				rslt = rs.getString("counts");
			}
		} 
		catch (SQLException sqlExp) 
		{
			sqlExp.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs != null)
				{				
					rs.close();
					rs = null;
				}
				if(pStmt != null)
				{				
					pStmt.close();
					pStmt = null;
				}
				if(conn != null)
				{
					conn.close();	
					conn = null;
				}
			}catch(Exception ex)
			{ex.printStackTrace();}
		}
		return rslt;
	}
	
	/** oracle.jdbc
	 * @param pSql
	 * @param pClass
	 * @return
	 */
	public ArrayList<?> Do_Package(String pSql, long pClass) 
	{
		ArrayList<Object> alist = new ArrayList<Object>();
		RmiBean rmiBean = null;
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		try 
		{
			conn = m_DBUtil.objConnPool.getConnection();
			cstmt = conn.prepareCall(pSql);
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			cstmt.execute();
			rs = (ResultSet) cstmt.getObject(1);
			while(rs.next())
			{
				switch((int)pClass)
				{
					//case RmiBean.RMI_SURROUNDINGS_INFO:
							//rmiBean = new SurroundingsInfoBean();
						//break;
				}
				rmiBean.getData(rs);						
				alist.add(rmiBean);	
			}
		} catch (SQLException sqlExp) 
		{
		sqlExp.printStackTrace();
		}
		finally
		{
			try
			{
				if(rs != null)
				{				
					rs.close();
					rs = null;
				}
				if(cstmt != null)
				{				
					cstmt.close();
					cstmt = null;
				}
				if(conn != null)
				{
					conn.close();	
					conn = null;
				}
			}catch(Exception ex)
			{ex.printStackTrace();}
		}
		return alist;
	}
	
	@Override
	public String Client(int pCmd, String pClientId, String pOprator, String pData) throws RemoteException
	{
		// TODO Auto-generated method stub
		System.out.println("pCmd["+pCmd+"]\npClientId["+pClientId+"]");
		String ret = "9999";
		switch(pCmd)
		{
			case Cmd_Sta.CMD_DEVICE_CTRL:	// 远程控制
			{	
				String SendData = CommUtil.StrBRightFillSpace(" ", 20)
						+ "0000"
						+ Cmd_Sta.CMD_DEVICE_CTRL
						+ CommUtil.StrBRightFillSpace(pClientId, 10)
						+ CommUtil.StrBRightFillSpace(pOprator, 10)
						+ pData;
				if(m_TPCClient.SetSendMsg(SendData, 1))
				{
					ret = "0000";
				}
				break;
			}
			case Cmd_Sta.CMD_DEVICE_3003:	// 设备同步
			{
				String SendData = CommUtil.StrBRightFillSpace(" ", 20)
						+ "0000"
						+ Cmd_Sta.CMD_DEVICE_3003
						+ CommUtil.StrBRightFillSpace(pClientId, 10);
				if(m_TPCClient.SetSendMsg(SendData, 1))
				{
					ret = "0000";
				}
				break;
			}
			case Cmd_Sta.CMD_DEVICE_3004:	// 属性同步
			{
				String SendData = CommUtil.StrBRightFillSpace(" ", 20)
						+ "0000"
						+ Cmd_Sta.CMD_DEVICE_3004
						+ CommUtil.StrBRightFillSpace(pClientId, 10);
				if(m_TPCClient.SetSendMsg(SendData, 1))
				{
					ret = "0000";
				}
				break;
			}
			case Cmd_Sta.CMD_DEVICE_3005:	// 动作同步
			{
				String SendData = CommUtil.StrBRightFillSpace(" ", 20)
						+ "0000"
						+ Cmd_Sta.CMD_DEVICE_3005
						+ CommUtil.StrBRightFillSpace(pClientId, 10);
				if(m_TPCClient.SetSendMsg(SendData, 1))
				{
					ret = "0000";
				}
				break;
			}
		}	
		return ret;
	}
}
