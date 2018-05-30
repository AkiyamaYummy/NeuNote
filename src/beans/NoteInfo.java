package beans;

import java.io.Serializable;
import net.sf.json.JSONObject;

public class NoteInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String noteId;//笔记ID
	private String title;//标题
	private String author;//作者
	private String releaseTime;//发布时间
	private int pages;//页数
	private String lastEditTime;//最后编辑时间
	
	public NoteInfo(String noteId, String title, String author, String releaseTime, int pages, String lastEditTime) {
		super();
		this.noteId = noteId;
		this.title = title;
		this.author = author;
		this.releaseTime = releaseTime;
		this.pages = pages;
		this.lastEditTime = lastEditTime;
	}
	public NoteInfo() {
		
	}
	public String getNoteId() {
		return noteId;
	}
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getLastEditTime() {
		return lastEditTime;
	}
	public void setLastEditTime(String lastEditTime) {
		this.lastEditTime = lastEditTime;
	}
	public String toJson() {
		JSONObject jsonObj = JSONObject.fromObject(this);
		return jsonObj.toString();
	}
}
