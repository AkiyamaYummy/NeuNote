package userTransactionClasses;

import java.sql.SQLException;
import java.util.ArrayList;

import beans.NoteInfo;
import beans.StuInfo;
import databaseTransactionClasses.CheckConnection;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


public class TransformJSON {
	//修改 张格皓 判断了登录是否成功，登陆失败则返回null
	public static StuInfo transformIntoBean(String jsonString,String password) {
		JSONObject jsonObject1 = JSONObject.fromObject(jsonString);//字符串转JSON对象
		JSONArray array1=jsonObject1.getJSONArray("pages_message");//获取pages_message中的具体值
		
		//修改 张格皓 此处判断是否登录成功
		JSONArray array0=jsonObject1.getJSONArray("information");//获取information中的具体值
		boolean is_successed = false;
		for(Object json_obj : array0){
			String obj_str = json_obj.toString(); 
			if(obj_str.equals("Login successed.")){
				is_successed = true;
				break;
			}
		}
		if(!is_successed)return null;
		StuInfo student1=new StuInfo();
		/*if(array1.size()>0) {
			for(int i=0;i<array1.size();i++) {
				JSONObject job = array1.getJSONObject(i);
				System.out.println(i+". key:"+job.get("key")+" "+"value:"+job.get("value"));
			}
		}		*///打印出来看每个属性的编号的
		student1.setPassword(password);
		student1.setStu_id(array1.getJSONObject(0).getString("value"));//设置学号
		student1.setStu_name(array1.getJSONObject(1).getString("value"));//设置姓名
		student1.setSex(array1.getJSONObject(4).getString("value"));//设置性别
		student1.setCollege(array1.getJSONObject(20).getString("value"));//设置院系
		student1.setMajor(array1.getJSONObject(22).getString("value"));//设置专业
		student1.setStu_class(array1.getJSONObject(24).getString("value"));//设置班级
		student1.setGrade(array1.getJSONObject(26).getString("value"));//设置年级
		student1.setLevel(array1.getJSONObject(27).getString("value"));//设置培养层次
		student1.setGPA(Double.parseDouble(array1.getJSONObject(28).getString("value")));//设置平均学分绩点
		return student1;
	}
	public static JSONArray getNoteAndAuthorArray(ArrayList<NoteInfo> noteArray) throws ClassNotFoundException, SQLException {
		StuInfo author;
		JSONArray allInfo=new JSONArray();//全部信息
		for(NoteInfo noteInfo:noteArray) {//for循环添加特定笔记数组中的信息到JSON数组
			author=CheckConnection.returnStudentObject(noteInfo.getAuthor());
			JSONObject noteJSON=JSONObject.fromObject(noteInfo);//笔记详细信息
			JSONObject authorJSON=new JSONObject();//作者公开信息
			JSONObject both=new JSONObject();//用于整合两者信息
			authorJSON.put("stu_id", author.getStu_id());
			authorJSON.put("nickname",author.getNickname());
			authorJSON.put("headphoto", author.getHeadphoto());
			authorJSON.put("introduction", author.getIntroduction());
			both.putAll(noteJSON);
			both.putAll(authorJSON);
			allInfo.add(both);//将整合后的信息添加到JSON数组
		}
		return allInfo;
	}
	public static JSONObject getNoteAndAuthor(NoteInfo noteInfo) throws ClassNotFoundException, SQLException{
		StuInfo author=CheckConnection.returnStudentObject(noteInfo.getAuthor());
		JSONObject noteJSON=JSONObject.fromObject(noteInfo);//笔记详细信息
		JSONObject authorJSON=new JSONObject();//作者公开信息
		JSONObject both=new JSONObject();//用于整合两者信息
		authorJSON.put("stu_id", author.getStu_id());
		authorJSON.put("nickname",author.getNickname());
		authorJSON.put("headphoto", author.getHeadphoto());
		authorJSON.put("introduction", author.getIntroduction());
		both.putAll(noteJSON);
		both.putAll(authorJSON);
		return both;
	}
	public static JSONObject getStuPublicInfo(StuInfo stu) {//将用户信息中的“可公开的个人信息”提取出来
		JSONObject stuJSON=new JSONObject();//作者公开信息
		stuJSON.put("stu_id", stu.getStu_id());
		stuJSON.put("nickname",stu.getNickname());
		stuJSON.put("headphoto", stu.getHeadphoto());
		stuJSON.put("introduction", stu.getIntroduction());
		return stuJSON;
	}
}
