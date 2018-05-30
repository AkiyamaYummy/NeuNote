package databaseTransactionClasses;

import java.sql.Connection;
import java.util.Calendar;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

import beans.StuInfo;
import toolClasses.C;

public class CheckConnection {
/*刘斌 这个类中所有的前几行代码都是千篇一律 都是针对对数据库的操作  感觉比较蠢  今后有时间会改进这些冗余的一样的代码 例如 写一个连接数据库的专用方法
     将里面的connection statement设为静态变量 然后只用一次这几段代码*/
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		String b = CheckConnection.checkC_codeInCookies("4f73191c-cbef-4083-94c7-e0d143dbc3a9");
		C.debug.println(b);
	}
	public static StuInfo returnStudentObject(String stu_id) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			Statement statement1= connection.createStatement();
			String strSQL = "";
			strSQL = "select * from student where stu_id='"+stu_id+"';";
			ResultSet rs1 = statement1.executeQuery(strSQL);
			while (rs1.next()) {
				    String stu_name_existed = rs1.getString("stu_name");
				    String sex_existed = rs1.getString("sex");
					String college_existed = rs1.getString("college");
					String major_existed = rs1.getString("major");
					String stu_class_existed = rs1.getString("stu_class");	
				    String grade_existed = rs1.getString("grade");
				    String level_existed = rs1.getString("level");
				    double gpa_existed = rs1.getDouble("gpa");
				    String password_existed = rs1.getString("password");
				    String nickname_existed = rs1.getString("nickname");
				    String introduction_existed = rs1.getString("introduction");
				    String headphoto_existed = rs1.getString("headphoto");
					StuInfo stu = new StuInfo(stu_id,stu_name_existed,sex_existed,college_existed,
							major_existed,stu_class_existed,grade_existed,level_existed,gpa_existed,
							password_existed,nickname_existed,introduction_existed,headphoto_existed);
					statement1.close();
					return stu;
			}
			statement1.close();
			return null;
		}
	}//返回学生信息对象，以学号为student表的主键，查找表中的学号，如果表内学号存在，返回stu对象，不存在返回null

	public static String returnPasswordFromMySQL(String stu_id) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement1 = null;
			Statement statement2 = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，现在可以进行操作");
			}
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			String strSQL = "";
			strSQL = "select stu_id from student";
			C.debug.println("正在检索表中存在的学号");
			ResultSet rs1 = statement1.executeQuery(strSQL);//先检索学号
			while (rs1.next()) {
				String stu_id_existed = rs1.getString("stu_id");
				if (stu_id_existed.equals(stu_id)) {
					C.debug.println("数据库中存在该学号");
					strSQL = "select password from student where stu_id='" + stu_id_existed + "'";
					ResultSet rs2 = statement2.executeQuery(strSQL);//找出学号对应的密码值，然后获取，返回
					while(rs2.next()) {
						String passwordexisted = rs2.getString("password");
						statement1.close();
						statement2.close();
						return passwordexisted;
					}
	
				}
			}
	        statement1.close();
	        statement2.close();
			return null;
		}
	}//从mysql中返回密码值

	public static boolean insertNewC_codeToMySQL(String C_code) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，现在可以进行操作");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "select C_code from connection";
			C.debug.println("正在检索表中存在的C_code");
			ResultSet rs = statement.executeQuery(strSQL);
			while (rs.next()) {
				String C_code_existed = rs.getString("C_code");
	
				if (C_code_existed.equals(C_code)) {
	
					C.debug.println("数据库中已经存在该C_code");
					statement.close();
					return false;
				}
			}//如果C_code存在于connection表中，直接返回false，其实这段完全没必要。。。因为之后写的那个生成C_code的方法用了UUID类，不可能重复
			strSQL = "insert ignore into connection values('" + C_code + "'," + "null,"
					+ "date_sub(now(), interval -5 minute)" + ");";
			statement.executeUpdate(strSQL);
			C.debug.println("成功加入C_code");
	
			statement.close();
			return true;
		}
	}//检验是否成功向Mysql中插入新的C_code行

	public static String generateC_code() throws ClassNotFoundException, SQLException {
		UUID uuid = UUID.randomUUID();// 生成随机字符串的一个类
		String s = uuid.toString();// 转化为toString
		boolean flag = insertNewC_codeToMySQL(s);// 将转好的String作为参数传入建造新
		if (flag)
			return s;
		return null;

	}// 生成C_code，并以这个C_code建造新的C_code行,同时返回这个过程中生成的C_code

	public static void updateStu_id(String C_code, String stu_id) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			C.debug.println("updating...");
			Statement statement1 = null;
			Statement statement2 = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，现在可以进行操作");
			}
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			String strSQL = "";
			strSQL = "select C_code from connection";
			C.debug.println("正在检索表中存在的C_code");
			ResultSet rs1 = statement1.executeQuery(strSQL);//检索C_code
			C.debug.println("The c_code select : "+C_code);
			C.debug.println("The c_code existed : ");
			while (rs1.next()) {
				String C_code_existed = rs1.getString("C_code");
				C.debug.println(C_code_existed);
				if (C_code_existed.equals(C_code)) {
					strSQL = "select stu_id from connection where C_code='" + C_code_existed + "';";
					ResultSet rs2 = statement2.executeQuery(strSQL);//检索C_code对应的学生id
					while (rs2.next()) {
						String Stu_id_existed = rs2.getString("stu_id");
						if (Stu_id_existed == null) {
							Stu_id_existed = stu_id;//将null值改为stu_id
							strSQL="update connection set stu_id='"+Stu_id_existed+"'"+"where C_code='"
							+C_code_existed+"';";
						statement2.executeUpdate(strSQL);
							C.debug.println("修改学号成功！");
							statement1.close();
							statement2.close();
							return;
						}
					}
				}
			}
			statement1.close();
			statement2.close();
			C.debug.println("该C_code不在connection表中");
		}

	}//更新connection中的学生学号，将原先的null值更改为传入参数进来的学生学号
    
	public static boolean toBelogined(String C_code) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			
			Statement statement1 = null;
			Statement statement2=null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，现在可以进行操作");
			}
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			String strSQL = "";
			strSQL = "select C_code from connection";
			C.debug.println("正在检索表中存在的C_code");
			ResultSet rs1 = statement1.executeQuery(strSQL);
			C.debug.println("The c_code select : "+C_code);
			C.debug.println("The c_code existed : ");
			while (rs1.next()) {
				String C_code_existed = rs1.getString("C_code");
				C.debug.println(C_code_existed);
				if (C_code_existed.equals(C_code)) {
					strSQL = "select stu_id from connection where C_code='" + C_code_existed + "';";
					ResultSet rs2 = statement2.executeQuery(strSQL);
					while (rs2.next()) {
						String Stu_id_existed = rs2.getString("stu_id");
						C.debug.println("student id : "+Stu_id_existed);
						if (Stu_id_existed == null) {
							statement1.close();
							statement2.close();
							return true;//先检索C_code，再检索C_code对应的stu_id，如果为null，说明等待登录
						}
					}
				}
			}
			C.debug.println("该C_code不在connection表中");
			statement1.close();
			statement2.close();
			return false;
		}
		
	}//检验是否待登录
	public static boolean isLogining(String stu_id) throws ClassNotFoundException, SQLException{
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，现在可以进行操作");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL="select stu_id from connection";
			C.debug.println("正在检索表中存在的stu_id");
			ResultSet rs=statement.executeQuery(strSQL);
			while(rs.next()) {
				String stu_id_existed=rs.getString("stu_id");
				if(stu_id_existed==null) continue;
				if(stu_id_existed.equals(stu_id)) {
					statement.close();
					return true;//检索stu_id，如果存在，说明正在登录
				}
			}
			statement.close();
			return false;
		}
	}
	
	public static String checkC_codeInCookies(String C_code) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);
		}
		synchronized(C.connection){
			Statement statement1 = null;
			Statement statement2 = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，现在可以进行操作");
			}
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			String strSQL = "";
			strSQL = "select C_code from connection";
			C.debug.println("正在检索表中存在的C_code");
			ResultSet rs1 = statement1.executeQuery(strSQL);//首先检索c_code
			while (rs1.next()) {
				String C_code_existed = rs1.getString("C_code");
				if (C_code_existed.equals(C_code)) {
					strSQL = "select stu_id,overtime from connection where C_code=" + "'"+C_code_existed +"'"+ ";";
					ResultSet rs2 = statement2.executeQuery(strSQL);//检索C_code对应的学号和过期时间
					while (rs2.next()) {
						String Stu_id_existed = rs2.getString("stu_id");
						Timestamp overtime=rs2.getTimestamp("overtime");
						if(Stu_id_existed==null) {
							C.debug.println("学号值为空！");
							continue;
						}
						if(overtime.getTime()<System.currentTimeMillis()) {
							C.debug.println("该C_code已经过期");
							continue;
						}
						if(Stu_id_existed!=null&&overtime.getTime()>=System.currentTimeMillis()) {
							C.debug.println("返回的学号为："+Stu_id_existed);
							statement1.close();
							statement2.close();
							return Stu_id_existed;
						}//如果学号不为空且从数据库中获取的时间long值大于当前时间，返回正确值
					}
				}
			}
			statement1.close();
			statement2.close();
			return null;
		}
	}//验证工作

	public static void updateOvertime(String C_code, int time) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，现在可以进行操作");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "select C_code from connection";
			C.debug.println("正在检索表中存在的C_code");
			ResultSet rs1 = statement.executeQuery(strSQL);//首先检索c_code
			while (rs1.next()) {
				String C_code_existed = rs1.getString("C_code");
				if (C_code_existed.equals(C_code)) {
					String currentTime="";
					Calendar cal=Calendar.getInstance();
					cal.add(Calendar.MINUTE, time);
					//使用calendar可以设置时间为任意分钟数前后的某个time，如time=60，即可得到该时间一个小时后的时间
					currentTime=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTimeInMillis());
					C.debug.println(currentTime);
					
					strSQL="update connection set overtime="+"'"+currentTime+"'"+"where C_code="+"'"+C_code_existed +"'"+ ";";
					statement.executeUpdate(strSQL);
					//这里直接采用string进行更新，数据库语句对timestamp的操作和对varchar是类似的
					C.debug.println("更新时间成功！");
					statement.close();
					return;
				}
			}
			C.debug.println("未找到对应的C_code");
			statement.close();
		}
	}
	
}
