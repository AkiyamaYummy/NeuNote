package servletTransactionClasses;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.StuInfo;
import databaseTransactionClasses.CheckConnection;
import databaseTransactionClasses.StudentBasicInfoConnection;
import net.sf.json.JSONObject;
import toolClasses.C;
import toolClasses.LoginOrVerify;

public class ProcessPersonalInformation extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ProcessPersonalInformation() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out=response.getWriter();
		String c_code=null;//request传来的c_code
		String idFromVerify=null;//从验证接口得到的学号
		String type=null;//从request传来的请求类型
		String nickname=null;
		String introduction=null;
		String password=null;
		String headphoto=null;
		String new_password=null;
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
			StuInfo stu=C.students.get(idFromVerify);
			synchronized(stu) {//加锁
				type=request.getParameter("type");
				if(type.equals("get")){
					try {
						stu=CheckConnection.returnStudentObject(idFromVerify);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						throw new IOException();
					}
					JSONObject jsonObj = JSONObject.fromObject(stu);
					out.println(jsonObj.toString());
				}
				else if (type.equals("set")) {
					nickname=request.getParameter("nickname");
					introduction=request.getParameter("introduction");
					password=request.getParameter("password");
					headphoto=request.getParameter("headphoto");
					new_password=request.getParameter("new_password");
					StuInfo stu0=C.students.get(idFromVerify);
					if(!stu0.getPassword().equals(password)) {
						out.println("Password error.");
					}else {
						if(new_password==null||new_password.equals("")) {
							new_password = password;
							stu0.setNickname(nickname);
							stu0.setHeadphoto(headphoto);
							stu0.setIntroduction(introduction);
						}else {
							stu0.setNickname(nickname);
							stu0.setHeadphoto(headphoto);
							stu0.setIntroduction(introduction);
							stu0.setPassword(new_password);
						}
						C.students.put(idFromVerify,stu0);
						try {
							boolean b=StudentBasicInfoConnection.modifyStuInfo(idFromVerify, nickname, new_password, introduction,headphoto);
							if(b==true) {
								JSONObject jsonObj = JSONObject.fromObject(stu0);
								out.println(jsonObj.toString());
							}
						} catch (ClassNotFoundException |SQLException e) {
							e.printStackTrace();
							throw new IOException();
						} 
						
					}
					
				}
			}
		}
	}

}
