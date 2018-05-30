package servletTransactionClasses;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import toolClasses.*;

public class CheckCookies extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public CheckCookies() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	@SuppressWarnings("unused")
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		String c_code=null;//request传来的c_code
		String studentId=null;//request传来的学号（用户输入的）
		String password=null;//request传来的密码（用户输入的）
		String jsonString=null;//保存从zhjw_login_api得到的json字符串
		String verifyPassword=null;//保存从数据库中取出的密码，用于验证
		Boolean boolForCode=true;//用于验证c_code是否为空
		Boolean boolForId=true;//用于验证学号是否为空
		Boolean boolForPass=true;//用于验证密码是否为空
		C.debug.println("servlet start.");
		try {
			c_code=request.getParameter("c_code");//从request得到c_code
			if(!RegexTools.verifyC_code(c_code)) {
				out.println("c_code格式错误");
			}
		}catch(Exception e) {
			//不做什么
		}
		try {
			studentId=request.getParameter("studentId");//从request得到studentId
			if(!RegexTools.verifyStudentId(studentId)) {
				out.println("学号格式错误");
			}
		}catch(Exception e) {
			//不做什么
		}
		try {
			password=request.getParameter("password");//从request得到password
			if(!RegexTools.verifyPassword(password)) {
				out.println("密码格式错误");
			}
		}catch(Exception e) {
			//不做什么
		}
		if(c_code==null||c_code.length()==0)//验证c_code是否为空
			boolForCode=false;
		if(studentId==null||studentId.length()==0)//验证学号是否为空
			boolForId=false;
		if(password==null||password.length()==0)//验证密码是否为空
			boolForPass=false;
		
		// 这里的大段注释被移动到了该文件最后 张格皓
		
		C.debug.println("servlet get : "+c_code+" "+studentId+" "+password);
		if(boolForCode&&boolForId&&boolForPass) {//三个字段都不为空
			try {
				out.println(LoginOrVerify.login(c_code, studentId, password));
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				IOException a=new IOException();
				throw a;
			}
		} else if(boolForCode&&!boolForId&&!boolForPass) {//学号密码的字段为空，c_code字段不为空，执行验证功能
			try {
				//out.println(LoginOrVerify.verify(c_code)); 
				//岑哲栋在进行核心功能开发时重新修改，让verify方法返回学号或者null而不是直接返回成功或失败的字符串，这对于NoteEdit中的部分功能很有用
				if(LoginOrVerify.verify(c_code)!=null) {//验证成功
					out.println("Verify successed.");
				}
				else {//验证失败
					out.println("Verify failed.");
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				IOException a=new IOException();
				throw a;
			}
		} else {//除了以上这 2 种，其他的情况
			out.println("Login failed, other situations");
		}
				
	}

}

/*if(!boolForCode&&boolForId&&boolForPass) {//c_code字段为空，且学号密码不为空
try {
	verifyPassword=CheckConnection.returnPasswordFromMySQL(studentId);//调用得到密码的接口
} catch (ClassNotFoundException e) {
	e.printStackTrace();
} catch (SQLException e) {
	e.printStackTrace();
}
if(verifyPassword==null) {//得到密码的接口返回的的密码为空,即user表中没有这个学号
	jsonString=getStudentMessage.get(studentId, password);//调用zhjw_login_api获得json字符串
	JSONObject jsonObject0 = JSONObject.fromObject(jsonString);//字符串转JSON对象
	JSONArray array0=jsonObject0.getJSONArray("information");//获取information中的具体值
	String statusInfo=array0.getJSONObject(0).getString("value");//存储返回的登录状态信息
	if(statusInfo.equals("Login successed.")) {//代表登录成功
		StuInfo student1=TransformJSON.transformIntoBean(jsonString, password);//将密码加上得到的json字符串转为学生信息对象
		try {
			StudentBasicInfoConnection.loadStuInfoToMySQL(student1);//将其中信息存入数据库
			C.students.put(password, student1);//将学生信息对象存入C类中map
			out.println("Login successes.");//返回验证成功的报文
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//之后都是登录失败的情况
	else if(statusInfo.startsWith("An exception occurred in the program")) {
		out.println("An exception occurred in the program");
	}
	else if(statusInfo.startsWith("Verification code recognition failed.")) {
		out.println("Verification code recognition failed.");
	}
	else if(statusInfo.startsWith("Mysterious exception, it should be your password is wrong.")) {
		out.println("Mysterious exception, it should be your password is wrong.");
	}
	else {
		out.println("Unknown exception");
	}
}
else if(verifyPassword.equals(password)) {//密码比对成功
	out.println("Login successes.");//返回验证成功的报文
}
else if(!verifyPassword.equals(password)) {//密码比对失败，即密码肯定错误
	out.println("Wrong password");
}
}*/