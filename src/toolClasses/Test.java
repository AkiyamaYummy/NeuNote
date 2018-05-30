package toolClasses;

import java.util.Map.Entry;

import beans.StuInfo;

public class Test {
	static public String getCMap(){ 
		StringBuffer sb = new StringBuffer();
		for (Entry<String, StuInfo> entry : C.students.entrySet()) {
			sb.append(entry.getKey()+"&nbsp;"+entry.getValue().getStu_name()+"<br>");
		}
		return sb.toString();
	}
}
