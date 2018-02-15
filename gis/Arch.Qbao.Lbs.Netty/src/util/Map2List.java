package util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.IIOException;



public class Map2List {
	public static List	getList(Map map,String keyOrValue){
		List rtn=new ArrayList();
		Iterator iter=map.entrySet().iterator();
		while(iter.hasNext()){
			Entry entry=(Entry) iter.next();
			Object obj = null;
			if(keyOrValue.equals("key")){
				obj= entry.getKey();
			}else if(keyOrValue.equals("value")){
				obj=entry.getValue();
			}else {
				System.err.println("pleae input key or value");
				break;
			}
			rtn.add(obj);
		}
		return rtn;
		
	}
}
