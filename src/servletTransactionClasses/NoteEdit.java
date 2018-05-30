package servletTransactionClasses;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.NoteInfo;
import beans.StuInfo;
import databaseTransactionClasses.EditNoteConnection;
import toolClasses.C;
import toolClasses.FileTools;
import toolClasses.LoginOrVerify;


public class NoteEdit extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public NoteEdit() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//张格皓 修改 这里……设置一下response的编码形式为utf8，不然会乱码的很露骨
		//这句话的意思，是让浏览器用utf8来解析返回的数据  
		response.setHeader("Content-type", "text/html;charset=UTF-8");  
		//这句话的意思，是告诉servlet用UTF-8转码，而不是用默认的ISO8859  
		response.setCharacterEncoding("UTF-8");  
		
		PrintWriter out=response.getWriter();
		String c_code=null;//request传来的c_code
		String idFromVerify=null;//从验证接口得到的学号
		String type=null;//从request传来的请求类型
		String editCode;//保存编辑模式链接的c_code
		response.setCharacterEncoding("utf-8");
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
				// 张格皓 添加 判断type是否为null
				if(type == null){
					out.println("Please specify the request type.");
					return;
				}
				if(type.equals("tree")) {
					String tree;
					try {
						tree = EditNoteConnection.getTree(idFromVerify);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException i=new IOException();
						throw i;
					}
					out.println(tree);//返回tree内容
				}
				else if(type.equals("note")) {
					String noteId=request.getParameter("note_id");
					NoteInfo note;
					try {
						note = EditNoteConnection.getNote(noteId);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException i=new IOException();
						throw i;
					}
					if(note==null||!note.getAuthor().equals(idFromVerify)) {//笔记不存在，或不属于该用户
						out.println("Note doesn't exist.");
					}
					else {//存在
						out.println(note.toJson());
					}
						
				}
				else if(type.equals("page")) {
					String noteId=request.getParameter("note_id");
					int number=Integer.parseInt(request.getParameter("number"));//请求笔记的页码
					NoteInfo note;
					try {
						note = EditNoteConnection.getNote(noteId);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException i=new IOException();
						throw i;
					}
					String pageJson=null;//存储得到的页面JSON字符
					if(note==null||!note.getAuthor().equals(idFromVerify)) {//笔记不存在，或不属于该用户
						out.println("Note doesn't exist.");
					}
					else {//存在
						if(number>note.getPages()) {//请求的页码超过笔记的页码
							out.println("This page doesn't exist.");
						}
						else {
							pageJson=FileTools.getJsonFromFile(noteId,number);//对文件内容进行处理
							out.println(pageJson);//文件存在，返回该页内容；不存在则返回空字符串
						}
					}
				}
				else if(type.equals("edit_mode")) {
					String side=request.getParameter("side");
					if(side.equals("true")) {//“side”内容为true
						try {
							EditNoteConnection.setC_codeInStu_info(c_code, idFromVerify);//将对应的用户行中对应的列设置为该c_code
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
							IOException a=new IOException();
							throw a;
						}
					}
					else {//内容为false
						try {
							EditNoteConnection.setC_codeInStu_info(null, idFromVerify);//将对应的列设置为null
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
							IOException a=new IOException();
							throw a;
						}
					}
				}
				
				//以下所有的编辑请求，均需要先调用 得到编辑模式链接 接口，与该c_code比对，如果不相同，返回“未进入编辑模式“
	
				else if(type.equals("tree_edit")) {//此类请求要求更新该用户的tree
					
					try {
						editCode=EditNoteConnection.getLink(idFromVerify);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					if(!c_code.equals(editCode)) {//客户的c_code和处于编辑模式的c_code不相同
						out.println("You are not in edit mode.");
					}
					else {//客户的c_code和处于编辑模式的c_code相同
						String tree=request.getParameter("tree");
						//修改 张格皓 后端没有必要理解tree的内容
						//JSONObject obj=JSONObject.fromObject(tree);//字符串转JSON对象
						try {
							EditNoteConnection.updateTree(idFromVerify, tree);//更新tree
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
						}
						out.println("Edit successfully.");//返回“编辑成功”
					}
				}
				else if(type.equals("note_create")) {//此类请求要求新建一个笔记
					try {
						editCode=EditNoteConnection.getLink(idFromVerify);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					if(!c_code.equals(editCode)) {//客户的c_code和处于编辑模式的c_code不相同
						out.println("You are not in edit mode.");
					}
					else {//客户的c_code和处于编辑模式的c_code相同
						String noteId;
						NoteInfo note;
						try {
							noteId=EditNoteConnection.createNewNote(idFromVerify);
							note=EditNoteConnection.getNote(noteId);
						}catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
							IOException a=new IOException();
							throw a;
						}
						out.println(note.toJson());
					}
				}
				
				/*以下所有的笔记编辑请求，均需要先调用 得到note 接口，如果该笔记不存在或不属于该用户，
				返回“笔记不存在”。如果属于，则我们就得到了该笔记的笔记信息对象。这个过程在下文中不再赘述。*/
				else if(type.equals("note_rename")) {
					try {
						editCode=EditNoteConnection.getLink(idFromVerify);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					if(!c_code.equals(editCode)) {//客户的c_code和处于编辑模式的c_code不相同
						out.println("You are not in edit mode.");
					}
					else {//客户的c_code和处于编辑模式的c_code相同
						String noteId=request.getParameter("note_id");//request传来的笔记id
						NoteInfo note=null;
						try {
							note = EditNoteConnection.getNote(noteId);
						} catch (ClassNotFoundException | SQLException e1) {
							e1.printStackTrace();
							IOException a=new IOException();
							throw a;
						}
						if(note==null||!note.getAuthor().equals(idFromVerify)) {//笔记不存在，或不属于该用户
							out.println("Note doesn't exist.");
						}
						else {//如果属于，则我们就得到了该笔记的笔记信息对象
							String newName=request.getParameter("new_name");//字段“new_name”，内容为笔记的新名字
							try {
								EditNoteConnection.renameNote(noteId,newName);
								note=EditNoteConnection.getNote(noteId);
							} catch (ClassNotFoundException | SQLException e) {
								e.printStackTrace();
								IOException a=new IOException();
								throw a;
							}//调用 重命名note 接口
							
							//张格皓 修改 这里有点设计失误，应当返回json数据，此时前端设计更加便捷直观
							out.println(note.toJson());//返回“编辑成功
						}
					}
				}
				else if(type.equals("note_drop")) {
					//不再赘述
					try {
						editCode=EditNoteConnection.getLink(idFromVerify);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					if(!c_code.equals(editCode)) {//客户的c_code和处于编辑模式的c_code不相同
						out.println("You are not in edit mode.");
					}
					else {//客户的c_code和处于编辑模式的c_code相同
						String noteId=request.getParameter("note_id");//request传来的笔记id
						NoteInfo note=null;
						try {
							note = EditNoteConnection.getNote(noteId);
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
							IOException a=new IOException();
							throw a;
						}
						if(note==null||!note.getAuthor().equals(idFromVerify)) {//笔记不存在，或不属于该用户
							out.println("Note doesn't exist.");
						}
						else {//笔记存在且属于该用户
							FileTools.dropNote(noteId);//删除该文件夹
							try {
								EditNoteConnection.deleteNote(noteId);//调用 删除note 接口
								out.println("Edit successfully.");//返回编辑成功
							} catch (ClassNotFoundException | SQLException e) {
								e.printStackTrace();
								IOException a=new IOException();
								throw a;
							}
						}
					}
				}
				else if(type.equals("page_edit")) {
					//不再赘述
					try {
						editCode=EditNoteConnection.getLink(idFromVerify);
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						IOException a=new IOException();
						throw a;
					}
					if(!c_code.equals(editCode)) {//客户的c_code和处于编辑模式的c_code不相同
						out.println("You are not in edit mode.");
					}
					else {//客户的c_code和处于编辑模式的c_code相同
						String noteId=request.getParameter("note_id");//request传来的笔记id
						NoteInfo note=null;
						String content;
						int number;
						try {
							note = EditNoteConnection.getNote(noteId);
						} catch (ClassNotFoundException | SQLException e) {
							e.printStackTrace();
							IOException a=new IOException();
							throw a;
						}
						if(note==null||!note.getAuthor().equals(idFromVerify)) {//笔记不存在，或不属于该用户
							out.println("Note doesn't exist.");
						}
						else {//笔记存在且属于该用户
							content=request.getParameter("content");
							//判断content是不是空串
							number=Integer.parseInt(request.getParameter("number"));
							if(content==null||content.length()==0) {//是空串									
								//比较请求更新的页码数和当前笔记的页码数
								if(number==note.getPages()) {//相等
									FileTools.deletePage(noteId, number);//删除当前的最后一页的json文件
									try {
										EditNoteConnection.updatePage(noteId, number-1);//调用 更新页码 接口，页码数-1
									} catch (ClassNotFoundException | SQLException e) {
										e.printStackTrace();
										IOException a=new IOException();
										throw a;
									}
								}
								else if(number<note.getPages()) {
									FileTools.clearPage(noteId, number);//更新对应的json文件内容，更新为空
								}
								else {
									//大于当前页码数，什么都不做
								}
							}
							else {//不是空串
								//比较请求更新的页码数和当前笔记的页码数
								if(number<=note.getPages()) {//请求更新的页码数 <= 当前笔记页码数
									FileTools.updateExistedPage(noteId, number, content);
								}
								else if(number-note.getPages()<=2) {
									try {
										EditNoteConnection.updatePage(noteId, number);//更新页码至请求更新的页码数
									} catch (ClassNotFoundException | SQLException e) {
										e.printStackTrace();
										IOException a=new IOException();
										throw a;
									}
									FileTools.createNewPage(noteId, number, content);//新建该页码对应的文件，更新文件内容
								}
								else {//请求更新的页码数-当前笔记的页码数 > 2
									out.println("Illegal operation.");//返回“非法操作“
									return;
								}
							}
							try {
								note = EditNoteConnection.getNote(noteId);
								EditNoteConnection.updateEditTime(noteId);
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
								IOException a=new IOException();
								throw a;
							} catch (SQLException e) {
								e.printStackTrace();
								IOException a=new IOException();
								throw a;
							}
							out.println(note.toJson());
							// 张格皓 修改 此处返回note的json字符串。
						}
					}
				}
			}
		}			
	}
}
