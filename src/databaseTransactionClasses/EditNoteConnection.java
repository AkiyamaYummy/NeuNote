package databaseTransactionClasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import beans.NoteInfo;
import toolClasses.C;

public class EditNoteConnection {
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		/*
		JSONObject json = EditNoteConnection.getTree("20164929");
		JSONArray array = json.getJSONArray("value");
		for (Object o : array) {
			C.debug.println(o.toString());
		}
		C.debug.println(json.getString("key"));
		EditNoteConnection.updatePage("rqjNqrnw", 3);
		*/
	}

	public static void setC_codeInStu_info(String c_code, String stu_id) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备更新student表中的editing列");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "update student set editing='" + c_code + "'" + "where stu_id=" + "'" + stu_id + "'";
			statement.executeUpdate(strSQL);
			C.debug.println("更新editing列成功！");
			statement.close();
		}
	}

	public static String getLink(String stu_id) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备查找student表中对应的c_code");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "select editing from student where stu_id='" + stu_id + "';";
			ResultSet rs = statement.executeQuery(strSQL);
			while (rs.next()) {
				String c_code_existed = rs.getString("editing");
				if (c_code_existed != null) {
					statement.close();
					return c_code_existed;
				}
			}
			statement.close();
			return null;
		}
	}

	public static String getTree(String stu_id) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备查找student表中对应的tree列");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "select tree from student where stu_id='" + stu_id + "';";
			ResultSet rs = statement.executeQuery(strSQL);
			// 张格皓 修改 后端把tree当做普通字符串就好，不用解析
			while (rs.next()) {
				String res = rs.getString("tree");
				statement.close();
				return res;
				/*
				JSONObject tree_existed = JSONObject.fromObject(rs.getString("tree"));
				if (tree_existed != null)
					return tree_existed;
				*/
			}
			statement.close();
			return null;
		}
	}

	public static void updateTree(String stu_id, String tree) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备更新student表中对应的tree列");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "update student set tree='" + tree + "'" + "where stu_id='" + stu_id + "';";
			statement.executeUpdate(strSQL);
			C.debug.println("更新成功！");
			statement.close();
		}
	}

	public static NoteInfo getNote(String noteID) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备检索Notes表");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "select * from notes where noteID='" + noteID + "';";
			ResultSet rs = statement.executeQuery(strSQL);
			while (rs.next()) {
				String noteID_existed = rs.getString("noteID");
				String title_existed = rs.getString("title");
				String author_exited = rs.getString("author");
				String releaseTime_existed = rs.getString("releaseTime");
				int pages_existed = rs.getInt("pages");
				String lastEditTime_existed = rs.getString("lastEditTime");
				NoteInfo noteinfo = new NoteInfo(noteID_existed, title_existed, author_exited, releaseTime_existed,
						pages_existed, lastEditTime_existed);
				statement.close();
				return noteinfo;
			}
			statement.close();
			return null;
		}
	}

	public static String createNewNote(String stu_id) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			String noteID = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备新建笔记");
			}
			statement = connection.createStatement();
			StringBuffer shortBuffer = new StringBuffer();
			String uuid = UUID.randomUUID().toString().replace("-", "");
			for (int i = 0; i < 8; i++) {
				String str = uuid.substring(i * 4, i * 4 + 4);
				int x = Integer.parseInt(str, 16);
				shortBuffer.append(C.chars[x % 0x3E]);
			} // 生成八位UUID 比之前的C_CODE要短，作为noteID，完全随机，不会出现一样的情况
			noteID = shortBuffer.toString();
			String strSQL = "";
			strSQL = "insert ignore into notes values ('" + noteID + "','" + "新笔记" + "','" + stu_id + "',now(),1,now())";
			statement.executeUpdate(strSQL);
			C.debug.println("新建笔记成功！");
			statement.close();
			return noteID;
		}
	}// 创建新笔记

	public static void renameNote(String noteID, String newTitle) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备重命名note");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "update notes set title='" + newTitle + "', lastEditTime=now() where noteID='" + noteID + "';";
			statement.executeUpdate(strSQL);
			C.debug.println("重命名笔记成功！");
			statement.close();
		}
	}// 重命名笔记

	public static void updateEditTime(String noteID) throws SQLException, ClassNotFoundException{
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，准备重命名note");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "update notes set lastEditTime=now() where noteID='" + noteID + "';";
			statement.executeUpdate(strSQL);
			statement.close();
		}
	}
	
	public static void deleteNote(String noteID) throws ClassNotFoundException, SQLException {
		if(C.connection == null){
			Class.forName(C.JDBC_DIRVER);// 注册JDBC驱动
			C.connection = DriverManager.getConnection(C.DB_URL, C.USER, C.PASS);// 连接数据库
		}
		synchronized(C.connection){
			Statement statement = null;
			System.out.println("连接数据库...");
			Connection connection = C.connection;
			if (connection != null) {
				System.out.println("数据库连接成功，删除note");
			}
			statement = connection.createStatement();
			String strSQL = "";
			strSQL = "select noteID from notes";
			C.debug.println("正在检索表中存在的noteID");
			ResultSet rs = statement.executeQuery(strSQL);
			while (rs.next()) {
				String noteID_existed = rs.getString("noteID");
				if (noteID_existed == null) {
					C.debug.println("noteID为空");
				}
				if (noteID_existed.equals(noteID)) {
					strSQL = "delete from notes where noteID='" + noteID_existed + "';";
					statement.executeUpdate(strSQL);
					strSQL = "insert ignore into deleted_notes values ('" + noteID_existed + "');";
					statement.executeUpdate(strSQL);
					C.debug.println("删除note成功，并加入到了deleted_notes中");
					statement.close();
					return;
				} else C.debug.println("noteID不存在");
			}
			statement.close();
		}
	}// 删除笔记

	public static void updateNote(String noteID) throws ClassNotFoundException, SQLException {
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
				System.out.println("数据库连接成功，更新note");
			}
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			String strSQL = "";
			strSQL = "select noteID from notes";
			C.debug.println("正在检索表中存在的noteID");
			ResultSet rs = statement1.executeQuery(strSQL);
			while (rs.next()) {
				String noteID_existed = rs.getString("noteID");
				if (noteID_existed == null) {
					C.debug.println("noteID为空");
				}
				if (noteID_existed.equals(noteID)) {
					strSQL = "update notes set lastEditTime=now() where noteID='" + noteID_existed + "';";
					statement2.executeUpdate(strSQL);
					C.debug.println("更新时间成功！");
				} else C.debug.println("noteID不存在");
			}
			statement1.close();
			statement2.close();
		}
	}// 更新笔记

	public static void updatePage(String noteID, int newPage) throws ClassNotFoundException, SQLException {
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
				System.out.println("数据库连接成功，更新note");
			}
			statement1 = connection.createStatement();
			statement2 = connection.createStatement();
			String strSQL = "";
			strSQL = "select noteID from notes";
			C.debug.println("正在检索表中存在的noteID");
			ResultSet rs = statement1.executeQuery(strSQL);
			while (rs.next()) {
				String noteID_existed = rs.getString("noteID");
				if (noteID_existed == null) {
					C.debug.println("noteID不存在");
				}
				if (noteID_existed.equals(noteID)) {
					strSQL = "update notes set lastEditTime=now(),pages=" + newPage + " where noteID='" + noteID_existed
							+ "';";
					statement2.executeUpdate(strSQL);
					C.debug.println("更新时间成功！");
				}
			}
			statement1.close();
			statement2.close();
		}
	}// 更新页码和时间
}
