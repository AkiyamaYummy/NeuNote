package toolClasses;

import java.io.PrintStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import beans.StuInfo;

// 用于储存各种常量
public class C {
	// 教务处储存学生信息的网站域名
	static public final String aaoUrl = "https://zhjw.neu.edu.cn/";
	// 教务处学生信息展示框架页面
	static public final String loginUrl = "ACTIONLOGON.APPPROCESS";
	// 个人信息页面
	static public final String personalInformation = "ACTIONFINDSTUDENTINFO.APPPROCESS";
	// 登录验证码的文件名前缀
	static public final String verificationCodeImgSrcPrefix = "ACTIONVALIDATERANDOMPICTURE.APPPROCESS?id=";
	// System.out,用于debug
	static public final PrintStream debug = System.out;
	//
	static public final String getStudentMessageApiUrl = "http://120.78.194.94:1999/";
	// JDBC数据库驱动
	static public final String JDBC_DIRVER = "com.mysql.jdbc.Driver";
	// 数据库NeuNote的URL 设置useSSL=false 否则会有警告
	static public final String DB_URL = "jdbc:mysql://localhost:3306/neunote?useUnicode=true&characterEncoding=utf-8&useSSL=false";
	// 用于该项目的用户名和密码
	static public final String USER = "nn_user";
	static public final String PASS = "nn_user";
	// 储存（或者作为数据库的缓存）所有已经登录的学生的学生信息
	static public Map<String, StuInfo> students = new HashMap<String ,StuInfo>();
	// 数据库连接太多了，没想到的事。也许我设计的太烂了。干脆改成一个单例。
	static public Connection connection = null;
	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",  
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",  
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",  
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",  
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",  
            "W", "X", "Y", "Z" };//刘斌添加，用来生成笔记id
	public static String filepath = "C:/NeuNote";
}
/*
Delete from connection where 1=1;
Delete from deleted_notes where 1=1;
Delete from notes where 1=1;
Delete from student where 1=1;
 */
