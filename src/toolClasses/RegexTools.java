package toolClasses;

import java.util.regex.*;

public class RegexTools {
	public static Boolean verifyC_code(String c_code) {
		String regex="[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(c_code);
	    // 字符串是否与正则表达式相匹配
	    boolean rs = matcher.matches();
	    return rs;
	}
	public static Boolean verifyStudentId(String studentID) {
		String regex="201[0-9]{1}[0-9]{4}";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(studentID);
	    // 字符串是否与正则表达式相匹配
	    boolean rs = matcher.matches();
	    return rs;
	}
	public static Boolean verifyPassword(String password) {
		String regex="[^\u4e00-\u9fa5]{1,}";//不能有中文
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
	    // 字符串是否与正则表达式相匹配
	    boolean rs = matcher.matches();
	    return rs;
	}
}
