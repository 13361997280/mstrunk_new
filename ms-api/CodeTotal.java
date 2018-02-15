import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CodeTotal {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String path="E:\\project\\workspace\\mstrunk\\ms-api";
		List<File> list=total(path);
		System.out.println("file size"+list.size());
		
		
		Integer row=0;
		
		for (File file : list) {
			System.out.println(file.getName());
			FileReader fr=new FileReader(file);
            BufferedReader in=new BufferedReader(fr);
            String line="";
            while((line=in.readLine()) != null) {
            	row++;
            }
		}

		System.out.println("line s"+row);
	}
	
	public static List<File> total(String path){
		List<File> files=new ArrayList<File>();
		File file=new File(path);
		File []files2=file.listFiles();
		for (File file3 : files2) {
			if(file3.isFile()){
				files.add(file3);
			}else {
				files.addAll(files.size(), total(file3.getPath()));
			}
		}
		return files;
	}

}