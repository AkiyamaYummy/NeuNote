package databaseTransactionClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import beans.NoteInfo;
import toolClasses.C;

public class ShareNoteConnection {

	public static void setSharedNotes(String noteID) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Connection connection = C.connection;
			Statement statement1 = connection.createStatement();
			Statement statement2 = connection.createStatement();
			String strSQL = "select * from notes where noteID='"+noteID+"';";
			ResultSet rs1 = statement1.executeQuery(strSQL);
			while (rs1.next()) {
				String author_existed = rs1.getString("author");
				String noteID_existed = rs1.getString("noteID");
				String title_existed = rs1.getString("title");
				int pages_existed = rs1.getInt("pages");
				String lastEditTime_existed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(rs1.getTimestamp("lastEditTime").getTime());
				
				strSQL = "select * from shared_notes where noteID='"+noteID+"';";
				ResultSet rs2 = statement2.executeQuery(strSQL);
				while(rs2.next()){
					statement1.executeUpdate("update shared_notes set "+
							"title='"+title_existed+
							"',lastEditTime='"+
							lastEditTime_existed+
							"',pages="+pages_existed+
							" where noteID='"+noteID+"';");							
					rs1.close();
					rs2.close();
					statement2.close();
					statement1.close();
					return;
				}
				
				statement1.executeUpdate("insert into shared_notes(noteID,title,author,pages,sharedtime,lastEditTime)"+
						" values('"+noteID_existed+"','"+title_existed+"','"+author_existed+"',"+pages_existed+",NOW(),'"+lastEditTime_existed+"');");
				rs1.close();
				rs2.close();
				statement2.close();
				statement1.close();
				return;
			}
			statement2.close();
			statement1.close();
		}
	}// 设置笔记分享状态
	
	public static void deleteSharedNotes(String noteID) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Connection connection = C.connection;
			Statement statement1 = connection.createStatement();
			statement1.executeUpdate("delete from shared_notes where noteID='"+noteID+"';");
			statement1.close();
		}

	}
	
	public static NoteInfo getNoteFromSharedNotes(String noteID) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Connection connection = C.connection;
			Statement statement1 = connection.createStatement();
			String strSQL = "select * from shared_notes where noteID='"+noteID+"';";
			ResultSet rs1 = statement1.executeQuery(strSQL);
			while(rs1.next()){
				String author_existed = rs1.getString("author");
				String noteID_existed = rs1.getString("noteID");
				String title_existed = rs1.getString("title");
				int pages_existed = rs1.getInt("pages");
				String releaseTime_existed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(rs1.getTimestamp("sharedtime").getTime());
				String lastEditTime_existed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(rs1.getTimestamp("lastEditTime").getTime());
				NoteInfo noteInfo = new NoteInfo(noteID_existed, title_existed, author_existed,
						releaseTime_existed, pages_existed, lastEditTime_existed);
				statement1.close();
				return noteInfo;
			}
			statement1.close();
			return null;
		}
	}

	public static ArrayList<NoteInfo> search(int startIndex, int finishIndex, String type,String exestr)
			throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			ArrayList<NoteInfo> notes = new ArrayList<NoteInfo>();
			Statement statement1 = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备检索已分享的笔记");
			}
			statement1 = connection.createStatement();
			String strSQL = "";
			if("highGPA".equals(type)){
				strSQL = "select * from shared_notes where author in "
						+ "(select stu_id from student where gpa>3.6)";
			}else if("latestPublish".equals(type)){
				strSQL = "select * from shared_notes";
			}else if("sameMajor".equals(type)){
				strSQL = "select * from shared_notes where author in"
						+ "(select stu_id from student where major='"+exestr+"')";
			}else if("sameCollege".equals(type)){
				strSQL = "select * from shared_notes where author in"
						+ "(select stu_id from student where college='"+exestr+"')";
			}else if("user".equals(type)){
				strSQL = "select * from shared_notes where author='"+exestr+"'";
			}else if("keyword".equals(type)){
				strSQL = "select * from shared_notes where ((title like \"%"
						+ exestr + "%\") or " + "(author IN (SELECT stu_id FROM student WHERE nickname LIKE" + "\"%" + exestr
						+ "%\")))";
			}
			strSQL += " order by lastEditTime desc limit " + (startIndex - 1) + "," + (finishIndex - startIndex + 1) + ";";
			C.debug.println("sql : "+strSQL);
			ResultSet rs1 = statement1.executeQuery(strSQL);
			while (rs1.next()) {
				String author_existed = rs1.getString("author");
				String noteID_existed = rs1.getString("noteID");
				String title_existed = rs1.getString("title");
				int pages_existed = rs1.getInt("pages");
				String releaseTime_existed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(rs1.getTimestamp("sharedtime").getTime());
				String lastEditTime_existed = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(rs1.getTimestamp("lastEditTime").getTime());
				NoteInfo noteInfo = new NoteInfo(noteID_existed, title_existed, author_existed,
						releaseTime_existed, pages_existed, lastEditTime_existed);
				notes.add(noteInfo);
			}
			statement1.close();
			return notes;
		}
	}// 传入范围 显示出不同筛选条件下的数据

}
