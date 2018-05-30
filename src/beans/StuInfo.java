package beans;

import java.io.Serializable;

public class StuInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String stu_id;//ѧ��
	 private String stu_name;//����
	 private String sex;//�Ա�
	 private String college;//Ժϵ
	 private String major;//רҵ
	 private String stu_class;//�༶
	 private String grade;//�꼶
	 private String level;//�������
	 private double gpa;//ƽ��ѧ�ּ���
	 private String password;//����
	 private String nickname;
	 private String introduction;
	 private String headphoto;
	 
	public StuInfo() {
		
	}
	public StuInfo(String stu_id) {
		this.stu_id=stu_id;
	}
	public StuInfo(String stu_id, String stu_name, String sex, String college, String major, String stu_class,
			String grade, String level, double gpa, String password,String nickname,String introduction,
			String headphoto) {
		super();
		this.stu_id = stu_id;
		this.stu_name = stu_name;
		this.sex = sex;
		this.college = college;
		this.major = major;
		this.stu_class = stu_class;
		this.grade = grade;
		this.level = level;
		this.gpa = gpa;
		this.password = password;
		this.nickname=nickname;
		this.introduction=introduction;
		this.headphoto=headphoto;
	}
	public String getStu_id() {
		return stu_id;
	}
	public void setStu_id(String stu_id) {
		this.stu_id = stu_id;
	}
	public String getStu_name() {
		return stu_name;
	}
	public void setStu_name(String stu_name) {
		this.stu_name = stu_name;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getCollege() {
		return college;
	}
	public void setCollege(String college) {
		this.college = college;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getStu_class() {
		return stu_class;
	}
	public void setStu_class(String stu_class) {
		this.stu_class = stu_class;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getHeadphoto() {
		return headphoto;
	}
	public void setHeadphoto(String headphoto) {
		this.headphoto = headphoto;
	}
	public void setGPA(double gpa) {
		this.gpa = gpa;
	}

	public double getGPA() {
		return gpa;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}