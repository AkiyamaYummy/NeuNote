package toolClasses;

import java.io.IOException;
import java.sql.SQLException;

import beans.StuInfo;
import databaseTransactionClasses.CheckConnection;
import databaseTransactionClasses.StudentBasicInfoConnection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import userTransactionClasses.TransformJSON;
import userTransactionClasses.getStudentMessage;

public class LoginOrVerify {
	public static String login(String c_code, String studentId, String password) throws IOException, ClassNotFoundException, SQLException {
		if(CheckConnection.toBelogined(c_code)) {//是待登录状态
			String verifyPassword=CheckConnection.returnPasswordFromMySQL(studentId);//调用得到密码的接口
			if(password.equals(verifyPassword)) {//密码正确
				if(!C.students.containsKey(studentId)) {//学号不在map中
					StuInfo stu0=CheckConnection.returnStudentObject(studentId);//调用 建立学生信息对象 接口
					C.debug.println(stu0.getStu_name());
					C.students.put(studentId, stu0);//将学生信息对象存入map中
				}
				CheckConnection.updateStu_id(c_code,studentId);//调用 更新学号 接口
				return("Login successed.");
			}
			else if(verifyPassword==null) {//接口返回的密码为null
				String jsonString=getStudentMessage.get(studentId, password);//调用zhjw_login_api获得json字符串
				JSONObject jsonObject0 = JSONObject.fromObject(jsonString);//字符串转JSON对象
				JSONArray array0=jsonObject0.getJSONArray("information");//获取information中的具体值
				
				// 修改 张格皓 改为判断information中有没有“login successed.”
				StringBuffer statusInfo = new StringBuffer();//存储返回的登录状态信息
				boolean is_successed = false;
				for(Object json_obj : array0){
					String obj_str = json_obj.toString(); 
					if(obj_str.equals("Login successed.")){
						statusInfo.append(obj_str+"\r\n");
						is_successed = true;
					}
				}
				if(is_successed) {//代表登录成功
					StuInfo student1=TransformJSON.transformIntoBean(jsonString, password);//将密码加上得到的json字符串转为学生信息对象
					try {
						if(!C.students.containsKey(studentId)) {//学号不在map中
							C.students.put(studentId, student1);//将学生信息对象存入C类中map
						}
						StudentBasicInfoConnection.loadStuInfoToMySQL(student1);//将学生信息存入数据库	
						CheckConnection.updateStu_id(c_code,studentId);//调用 更新学号 接口
						return("Login successed.");//返回登录成功
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {//代表登录失败
					return("Login falied, "+statusInfo);
				}
			}
			else {//密码错误
				return("Login failed, wrong password.");
			}
		}
		else {//不是待登录状态
			return("Login failed, c_code status has problems");
		}
		return null;
	}
	public static String verify(String c_code) throws ClassNotFoundException, SQLException {
		String res = CheckConnection.checkC_codeInCookies(c_code);
		if(res!=null) {//验证成功
			CheckConnection.updateOvertime(c_code, 60);//调用时间更新接口
			if(!C.students.containsKey(res)) {//学号不在map中
				StuInfo stu0=CheckConnection.returnStudentObject(res);//调用 建立学生信息对象 接口
				C.debug.println("name : "+stu0.getStu_name());
				C.students.put(res, stu0);//将学生信息对象存入map中
			}
			return(res);//返回学号
		}
		else {
			return(null);//返回null
		}
	}
}
