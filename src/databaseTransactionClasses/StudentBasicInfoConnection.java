package databaseTransactionClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import beans.StuInfo;
import toolClasses.C;

public class StudentBasicInfoConnection {

	//修改 张格皓 把这里的四个数据库相关的字符串挪到了C.java
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		
		
	}
	public static void loadStuInfoToMySQL(StuInfo stu) throws ClassNotFoundException, SQLException {
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
			/* 数据库连接成功后，接下来的操作就是将岑哲栋解析好的JSON对象存到数据库中 */
			// 这里 张格皓 改动，因为stu是需要参数传入的
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "insert ignore into student (stu_id,stu_name,sex,college,major,stu_class,grade,level,gpa,password)"
					+ "values ('" + stu.getStu_id() + "','" + stu.getStu_name() + "','"
					+ stu.getSex() + "','" + stu.getCollege() + "','" + stu.getMajor() + "','" + stu.getStu_class() + "','"
					+ stu.getGrade() + "','" + stu.getLevel() + "'," + stu.getGPA() + ",'" + stu.getPassword() + "');";
			/*
			 * 这里的sql语句 注意 ！！一定要加ignore ，避免重复插入，也是线程安全的一种体现，
			 * 现在暂时这样处理没什么不妥，如果以后更多线程的话还会在下面继续修改
			 */
	
			C.debug.println(strSQL);
			
			statement.executeUpdate(strSQL);// 执行sql语句
			System.out.println("插入成功！请转到数据库界面查看（已忽略重复数据）");
	
			statement.close();
		}
		
		//改动 张格皓 不捕获异常，改为抛出
	}
	public static boolean modifyStuInfo(String stu_id,String nickname,String password,
			String introduction,String headphoto) throws ClassNotFoundException, SQLException{
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
				System.out.println("数据库连接成功，准备修改学生信息");
			}
			statement1 = connection.createStatement();
			statement2=connection.createStatement();
			String strSQL = "";
			strSQL="select stu_id from student";
			C.debug.println("正在检索表中存在的学号");
			ResultSet rs1 = statement1.executeQuery(strSQL);
			while(rs1.next()) {
				String stu_id_existed=rs1.getString("stu_id");
				if(stu_id_existed==null) {
					C.debug.println("表中没有学号！");
					return false;
				}
				if(stu_id_existed.equals(stu_id)) {
					strSQL="update student set nickname='"+nickname+"', password='"
							+ password+"',headphoto='"+headphoto+"',introduction='"+introduction+"' where stu_id='"
							+stu_id+"';";
					statement2.executeUpdate(strSQL);
					C.debug.println("成功更改已有信息");
					statement1.close();
					statement2.close();
					return true;
				}
				
			}
			C.debug.println("学号不存在");
			statement1.close();
			statement2.close();
			return false;
		}
	}
}
