package test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import aPredict.predictModel;
import organicsController.BaseTest;
import sketch.geo.gPoint;



/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/HelloServlet")
public class HelloServlet extends HttpServlet 
{
	
	private static final long serialVersionUID = 1L;

    public HelloServlet() 
    {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		if(request.getParameter("name")!=null) 
		{
			String str=request.getParameter("name").toString();
			str=str.replaceAll("<sub>", "");
			str=str.replaceAll("</sub>","");
			System.out.println(str);
			String inputType="按分子式";
			if(BaseTest.getCheck(str,inputType)) 
			{
				String jsonMoles = JSON.toJSONString(BaseTest.getMoles(str, inputType));
				String jsonBonds = JSON.toJSONString(BaseTest.getBonds(str, inputType));
				JSONObject data = new JSONObject();
				data.put("moles", jsonMoles);
				data.put("bonds", jsonBonds);
				response.getWriter().append(JSON.toJSONString(data));
				response.getWriter().flush();
				response.getWriter().close();
			}else 
			{
				response.getWriter().append("非有机物");
				response.getWriter().flush();
				response.getWriter().close();
			}
		}
		else 
		if(request.getParameter("recog")!=null) 
		{
        	PrintWriter out = response.getWriter();
        	String res="";
        	
        	//kert2019
        	String recogType =  (request.getParameter("recog"));
            char respons = 0;
            if (recogType.equals("CHN"))
            {
	            try 
	            {
					respons=callSample(null, null, 1);//识别中文字符
				} 
	            catch (Exception e)
	            {
					e.printStackTrace();
				}   
	            
            }
            else
            {
            	try
            	{
					respons=callSample(null, null, 2);//识别特殊字符
				} catch (Exception e) 
            	{
					e.printStackTrace();
				}   	
            }
        	res=""+respons;

        	System.out.println("识别的结果是"+res);
            out.append(new String(res.getBytes("utf-8"),"iso-8859-1"));
            out.flush();   
            out.close();
            ink.removeAllElements();
        }
		else
		{
			
		}
	}

	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json; charset=utf-8");
        response.setHeader("cache-control", "no-cache");  
        System.out.println("坐标点"+request.getParameter("coodi"));
        JSONArray tableData = JSONArray.parseArray(request.getParameter("coodi"));

        int len=tableData.size();
        int []x=new int[len];
        int []y=new int[len];
        
        for(int i=0;i<len;i++) 
        {
        	Coodi tmp=tableData.getObject(i, Coodi.class);
        	x[i]=tmp.getX();
        	System.out.print("---x"+(x[i]-825)+"  ");
        	y[i]=tmp.getY();
        	System.out.print("---y"+(y[i]-75)+"  ");

        }         
        //kert2019
        if(request.getParameter("times").equals("1")) 
        {
        	System.out.println("-----------------第一次使用----------------------------");
        }
        try 
        {
			callSample(x,y,0);
		}
        catch (Exception e)
        {
			e.printStackTrace();
		}
         
	}

	
	
	
	//0代表载入笔画 
	//1代表调用中文识别
	//2代表化学识别
	private static Vector<gPoint> ink= new Vector<gPoint>();
	private static predictModel p1=new predictModel();
	
	private static char callSample(int[] x, int[] y, int status) throws Exception
	{
		char a1 = 0;
		if (status==0)
		{//状态为0代表往里面加点
			for (int i=0;i<x.length-1;i++) 
			{
				int disatancex=x[i+1]-x[i];
				int distancey=y[i+1]-y[i];
				ink.add(new gPoint(x[i], y[i]));
				ink.add(new gPoint(x[i]+0.3*disatancex, y[i]+0.3*distancey));
				ink.add(new gPoint(x[i]+0.6*disatancex, y[i]+0.6*distancey));
			}
			ink.add(new gPoint(x[x.length-1], y[x.length-1]));
			
			
			return 0;
		}
		
		if (status==1)
		{
			ListIterator<gPoint> li = ink.listIterator();
			//predictModel p1 = new predictModel();
			a1 = p1.predictChinese(li);
			System.out.println("识别的是中文字符"+ink.size());
			//ink.clear();
		}
			
		if (status==2)
		{
			ListIterator<gPoint> li = ink.listIterator();
			//predictModel p1 = new predictModel();
			a1 = p1.predictSpecialChar(li);
			System.out.println("识别的是特殊字符"+ink.size());
			//ink.clear();
		}
			
		if (status!=0) 
		{
			return a1;
		}else 
		{
			return 0;
		}	
	}

	//kert2019
	
	@Override
	protected Object clone() throws CloneNotSupportedException
	{
		return super.clone();
	}
	
	
	
	
	
}
