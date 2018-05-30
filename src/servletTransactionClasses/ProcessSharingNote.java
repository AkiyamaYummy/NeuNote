package servletTransactionClasses;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.NoteInfo;
import beans.StuInfo;
import databaseTransactionClasses.CheckConnection;
import databaseTransactionClasses.EditNoteConnection;
import databaseTransactionClasses.ShareNoteConnection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import toolClasses.C;
import toolClasses.FileTools;
import toolClasses.LoginOrVerify;

public class ProcessSharingNote extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ProcessSharingNote() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		String c_code=null;//request传来的c_code
		String idFromVerify=null;//从验证接口得到的学号
		String type=null;//从request传来的请求类型
		String noteId=null;//笔记id
		String pageContent=null;//笔记页面内容
		String strategies=null;//搜索策略
		String keyword=null;//搜索关键字
		String user=null;//某用户学号
		int number;//请求的页数
		int high;//获得的笔记范围的最大值
		int low;//获得的笔记范围的最小值
		c_code=request.getParameter("c_code");//从request得到c_code
		try {
			C.debug.print(c_code);
			idFromVerify=LoginOrVerify.verify(c_code);//verify接口中已经具有更新过期时间的功能，无需重复
			
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			IOException a=new IOException();
			throw a;
		}
		if(idFromVerify==null) {//验证失败
			out.println("You are not logged in.");
		}
		else {//验证成功	
			type=request.getParameter("type");//取得type字段
			NoteInfo note;//笔记信息
			if(type.equals("page")) {//得到笔记某页的内容
				noteId=request.getParameter("note_id");//得到笔记id
				number=Integer.parseInt(request.getParameter("number"));//得到请求的页数
				try {
					note=ShareNoteConnection.getNoteFromSharedNotes(noteId);//从已分享的笔记数据库中找到该笔记信息
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					IOException a=new IOException();
					throw a;
				}
				if(note==null) {//笔记未分享
					out.println("Note has not been shared.");
				}
				else if(number-note.getPages()>2) {//请求页数超过笔记总页数
					out.println("This page doesn't exist.");				
				}
				else {//正常情况，从文件读取笔记信息
					pageContent=FileTools.getContentFromSharedNote(noteId, number);//从文件读取该页内容
					out.println(pageContent);//返回给前端
				}
			}
			if(type.equals("note")) {//得到笔记信息
				noteId=request.getParameter("note_id");//得到笔记id
				try {
					note=ShareNoteConnection.getNoteFromSharedNotes(noteId);//从已分享的笔记数据库中找到该笔记信息
					if(note==null) {//笔记未分享
						out.println("Note has not been shared.");
					}else out.println(userTransactionClasses.TransformJSON.getNoteAndAuthor(note));//返回笔记信息
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					IOException a=new IOException();
					throw a;
				}
			}
			else if(type.equals("shared_mode")) {//用户主动将笔记分享出去
				noteId=request.getParameter("note_id");//得到笔记id
				try {
					note=EditNoteConnection.getNote(noteId);//从所有笔记数据库中找到该笔记信息（该笔记待分享）
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					IOException a=new IOException();
					throw a;
				}
				if(note==null||!note.getAuthor().equals(idFromVerify)) {//笔记不存在，或不属于该用户
					out.println("Note doesn't exist.");//返回笔记不存在
				}
				else {//笔记存在，且属于该用户
					try {
						ShareNoteConnection.setSharedNotes(noteId);//调用设置笔记分享状态接口
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					FileTools.updateSharedNote(noteId);//笔记文件覆盖到储存被分享笔记的文件夹中
				}
			}
			else if(type.equals("delete")) {//此类指令代表撤销分享操作
				noteId=request.getParameter("note_id");//得到笔记id
				try {
					note=EditNoteConnection.getNote(noteId);//从所有笔记数据库中找到该笔记信息（该笔记待分享）
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					IOException a=new IOException();
					throw a;
				}
				if(note==null||!note.getAuthor().equals(idFromVerify)) {//笔记不存在，或不属于该用户
					out.println("Note doesn't exist.");//返回笔记不存在
				}
				else {//笔记存在，且属于该用户
					try {
						ShareNoteConnection.deleteSharedNotes(noteId);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					FileTools.deleteSharedNote(noteId);//调用删除已分享的笔记接口
				}
			}
			else if(type.equals("search")){//此类指令代表用户希望得到一系列以某种策略搜索出来的笔记
				strategies=request.getParameter("strategies");//“strategies”字段
				low=Integer.parseInt(request.getParameter("low"));//“low”字段
				high=Integer.parseInt(request.getParameter("high"));//“high”字段
				ArrayList<NoteInfo> noteArray;//要得到的笔记数组
				JSONArray allInfo;//全部信息
				if(strategies.equals("highGPA")||strategies.equals("latestPublish")||
						strategies.equals("sameMajor")||strategies.equals("sameCollege")) {			
					try {
						StuInfo stu = CheckConnection.returnStudentObject(idFromVerify);//调用数据库的得到用户对象接口
						String exestr = "";
						if(strategies.equals("sameMajor"))exestr = stu.getMajor();
						else if(strategies.equals("sameCollege"))exestr = stu.getCollege();
						noteArray=ShareNoteConnection.search(low, high, strategies,exestr);//调用得到特定笔记接口
						allInfo=userTransactionClasses.TransformJSON.getNoteAndAuthorArray(noteArray);//循环包装成为JSONArray
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					out.println(allInfo.toString());//返回整个JSON数组字符串
				}
				else if(strategies.equals("keyword")) {
					keyword=request.getParameter("keyword");//keyword字段
					try {
						noteArray=ShareNoteConnection.search(low, high, strategies, keyword);//调用搜索接口
						allInfo=userTransactionClasses.TransformJSON.getNoteAndAuthorArray(noteArray);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					out.println(allInfo.toString());
				}
				else if(strategies.equals("user")) {
					user=request.getParameter("user");
					try {
						noteArray=ShareNoteConnection.search(low, high, "user",user);//调用得到某用户的笔记接口
						allInfo=userTransactionClasses.TransformJSON.getNoteAndAuthorArray(noteArray);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					out.println(allInfo.toString());
				}
			}
			else if(type.equals("Enter")) {
				String stuId=request.getParameter("id");
				StuInfo stu;
				JSONObject stuJSON;
				try {
					stu=CheckConnection.returnStudentObject(stuId);//调用数据库的得到用户对象接口
				} catch (ClassNotFoundException | SQLException e) {
					e.printStackTrace();
					IOException a=new IOException();
					throw a;
				}
				stuJSON=userTransactionClasses.TransformJSON.getStuPublicInfo(stu);//将用户信息中的“可公开的个人信息”提取出来
				out.println(stuJSON.toString());
			}
		}
	}

}
