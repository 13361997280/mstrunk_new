

import java.util.ArrayList;
import java.util.Date;

public class ElasticLabel2SchemaTools {

	
	public static void main(String[] args) {
		MakeupPo();
	}

	
	public static void esDataInsertRadom(){
		
		
		
	}
	
	
	
	
	
	//从一个属性表，生成mapping jason
	public static void EsMapping() {
		ArrayList<String> lst = util.FileUtil.getText("/Users/viv/Desktop/user_label_elasticseach_Map");
		ArrayList<String> outlist= new ArrayList<>();
		for (String string : lst) {
			string = "               \""+string+"\"       :                 {\"type\":\"\"}, ";
			System.out.println(string);
			outlist.add(string);
		}
		util.FileUtil.inputTXT("/Users/viv/Desktop/user_label_elasticseach_Map-1", outlist);
	}
	
	
	// 从一个属性表，生成userlabelPo
		public static void  MakeupPo() {
			ArrayList<String> lst = util.FileUtil.getTxt2List("/Users/viv/Desktop/user_label_elasticseach_Map-index.txt");
			ArrayList<String> outlist= new ArrayList<>();
			for (String string : lst) {
				outlist.add("//"+string.toString());
				String[] str = string.split("\"");
			
				if (string.indexOf("string")>0) {
					outlist.add("private String "+str[1]+";");
				}
				if (string.indexOf("integer")>0) {
					outlist.add("private Integer "+str[1]+";");
					
					
				}
				
				if (string.indexOf("float")>0) {
					outlist.add("private float "+str[1]+";");
					
					
				}
				
				if (string.indexOf("date")>0) {
					outlist.add("private String "+str[1]+";");
					
					
				}
				outlist.add(" ");
				
	
			}
			util.FileUtil.inputTXT("/Users/viv/Desktop/user_label_elasticseach_Map-2", outlist);
		}
		
	
	
	
	
	
	
	
	
	
	
	
	
}
