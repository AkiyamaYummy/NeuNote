package userTransactionClasses;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import net.sf.json.JSONObject;
import toolClasses.C;
import toolClasses.CharacterSetTools;
public class getStudentMessage{
	public static String get(String stuid,String password) throws IOException{
		JSONObject data = new JSONObject();
		data.put("student_id",stuid);
		data.put("password",password);
		String dataStr = data.toString();
		String jsonresstr = doPostWithJson(C.getStudentMessageApiUrl,dataStr);
		return jsonresstr;
	}
	public static String doPostWithJson(String url, String jsonDataStr) throws IOException {
		Document document = Jsoup.connect(url).
				timeout(100000).
				ignoreContentType(true).
				data("data",jsonDataStr).
				post();
		String jsonresstr = document.body().toString();
		jsonresstr = jsonresstr.substring(6,jsonresstr.length()-7);
		return CharacterSetTools.unicodeToString(jsonresstr);
	}
}