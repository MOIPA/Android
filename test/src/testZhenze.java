import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class testZhenze {
	public static void main(String[] args) {
		String str="[1234,125]";
		String reg = "\\[(\\d{1,8}\\,\\d{1,8})\\]";
		Pattern pattern = Pattern.compile(reg);
		Matcher matcher = pattern.matcher(str);
		
		while(matcher.find()){
			String msg = matcher.group();
			int start = matcher.start();
			// �õ����ƥ�������������
			int end = matcher.end();

			// �õ����ƥ�����е�����
			int groupCount = matcher.groupCount();
			// �õ�ÿ����������
			for (int i = 0; i <= groupCount; i++) {
				
				String timeStr = matcher.group(i);
				System.out.println(timeStr+"");
				if (i == 1) {
					System.out.println(timeStr+"***");
				}
			}
		}
	}
}
