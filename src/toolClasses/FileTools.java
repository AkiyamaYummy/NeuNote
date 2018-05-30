package toolClasses;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
/*处理文件io工作*/
public class FileTools {
	public static String getJsonFromFile(String noteId,int number) throws IOException {//从文件得到JSON字符串
		File pageInfo;
		String path=C.filepath+"/NoteLib/"+noteId+"/"+number+".json";//得到该页对应的json文件路径
		pageInfo=new File(path);
		if(pageInfo.exists()) {//文件存在
			String input;//存储读到的字符串
			input = FileUtils.readFileToString(pageInfo, "UTF-8");//要求json文件的编码为UTF-8,。有可能会受到String长度的限制
			return input;
		}
		else {
			return("");//文件不存在,
		}
	}
	public static void dropNote(String noteId) throws IOException {//删除笔记
		File note;
		String path=C.filepath+"/NoteLib/"+noteId;
		note=new File(path);//得到该笔记对应的文件夹
		FileUtils.deleteDirectory(note);
	}
	public static void deletePage(String noteId,int number) {//删除页面
		File page;
		String path=C.filepath+"/NoteLib/"+noteId+"/"+number+".json";
		page=new File(path);//得到该页面对应文件
		page.delete();
	}
	public static void clearPage(String noteId,int number) throws IOException {//清空页面文件数据
		File page;
		String path=C.filepath+"/NoteLib/"+noteId+"/"+number+".json";
		page=new File(path);//得到该页面对应文件
		page.delete();
		page.createNewFile();
		//清空文件
	}
	public static void updateExistedPage(String noteId,int number,String content) throws IOException {//更新已存在的页面内容
		File page;
		String path=C.filepath+"/NoteLib/"+noteId+"/"+number+".json";
		page=new File(path);//得到该页面对应文件
		if(!page.exists()) {
			File dir1 = new File(C.filepath+"/NoteLib/"+noteId);
			if(!dir1.exists())dir1.mkdirs();
			page.createNewFile();
		}
		FileUtils.writeStringToFile(page, content, "UTF-8",false);//写入更新的文件内容
	}
	public static void createNewPage(String noteId,int number,String content) throws IOException {//建立新页面并更新内容
		File page;
		String path=C.filepath+"/NoteLib/"+noteId+"/"+number+".json";
		page=new File(path);//得到该页面对应文件
		if(!page.exists()) {
			File dir1 = new File(C.filepath+"/NoteLib/"+noteId);
			if(!dir1.exists())dir1.mkdirs();
			page.createNewFile();
		}
		FileUtils.writeStringToFile(page, content, "UTF-8",false);//写入更新的文件内容
	}
	public static String getContentFromSharedNote(String noteId,int number) throws IOException {
		File pageInfo;
		String path=C.filepath+"/SharedNote/"+noteId+"/"+number+".json";//得到该页对应的json文件路径
		pageInfo=new File(path);
		if(pageInfo.exists()) {//文件存在
			String input;//存储读到的字符串
			input = FileUtils.readFileToString(pageInfo, "UTF-8");//要求json文件的编码为UTF-8,。有可能会受到String长度的限制
			return input;
		}
		else {
			return("");//文件不存在,
		}
	}
	public static void updateSharedNote(String noteId) throws IOException {//笔记文件覆盖到储存被分享笔记的文件夹中
		File srcDir,destDir;//来源文件夹（待分享的），目标文件夹（分享之后的）
		String srcPath,destPath;
		srcPath=C.filepath+"/NoteLib/"+noteId;//原路径
		destPath=C.filepath+"/SharedNote/"+noteId;//目标路径
		srcDir=new File(srcPath);
		destDir=new File(destPath);
		if(srcDir.exists())
			FileUtils.copyDirectory(srcDir, destDir);
	}
	public static void deleteSharedNote(String noteId) throws IOException {//删除已分享的笔记
		File note;//要删除的笔记文件夹
		String path=C.filepath+"/SharedNote/"+noteId;
		note=new File(path);
		FileUtils.deleteDirectory(note);
	}
}
