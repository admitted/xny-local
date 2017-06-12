package XNYLOCALAppSvr;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.PropertyConfigurator;

import container.ActionContainer;
import net.MsgCtrl;
import net.TcpSvr;
import util.*;

public class Main extends Thread 
{
	private static Main      objMain    = null;
	
	private DBUtil m_DBUtil = null;				//数据库
	private TcpSvr m_TcpSvr = null;				//Svr	
	private MsgCtrl m_MsgCtrl = null;			//Svr消息控制
	
	public static void main(String[] args) 
	{
		objMain = new Main();  
		objMain.init();
	}

	public void init() 
	{		
		try 
		{
			PropertyConfigurator.configure("log4j.properties");//加载properties文件
			//指令回复容器
			if(!ActionContainer.Initialize())
			{
				//System.exit(-1)  非0值表示异常退出     是指所有程序（方法，类等）停止，系统停止运行。
				System.exit(-1);
			}			
			//数据库组件
			m_DBUtil = new DBUtil();
			if(!m_DBUtil.init())
			{
				System.exit(-1);
			}

			m_TcpSvr = new TcpSvr(m_DBUtil);
			if(!m_TcpSvr.init())
			{
				System.out.println("m_PlatSvr Failed======");
				System.exit(-3);
			}
			
			//消息控制初始化
			m_MsgCtrl = new MsgCtrl(m_TcpSvr, m_DBUtil);
			if(!m_MsgCtrl.Initialize())
			{
				System.out.println("m_MsgCtrl Failed======");
				System.exit(0);
			}
			
			this.start();
			Runtime.getRuntime().addShutdownHook(new Thread(){  //注册新的虚拟机来关闭钩子。
				public void run() {
					System.gc();
				}
			});
		} catch (Exception e) 
		{
			e.printStackTrace();
			Runtime.getRuntime().exit(0);
		}

	}

	public void run() 
	{
		System.out.println("Start..........................................");
		String inputCmd = null;
		boolean test = true;
		while (!interrupted()) //interrupted()如果当前线程已经中断，则返回 true；否则返回 false  ：当线程没有中断时执行循环
		{     //第一次执行 interrupted()，线程没有中断，返回true，接着将状态置为false 于是第二次执行会返回false
			try 
			{
				sleep(1000);	
				if(test)
					continue;
				BufferedReader bufReader = new BufferedReader(new InputStreamReader(System.in));
				inputCmd = bufReader.readLine().toLowerCase();
				if (inputCmd.equals("t")) 
				{
					
				}
			} 
			catch (Exception exp) 
			{
				exp.printStackTrace();
			}
		}
	}
}
