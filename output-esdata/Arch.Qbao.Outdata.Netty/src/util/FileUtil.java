package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import com.qbao.search.logging.ESLogger;
import com.qbao.search.logging.Loggers;

public class FileUtil {
	private static final ESLogger logger = Loggers.getLogger(FileUtil.class);

	/**
	 * 按行插入字符串到指定文件 ,文件在DIC目录中,原文件更新
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 **/
	public static boolean inputTXT(String basePath, String fileName, List<String> textlines, boolean append) {
		int count = 0;
		FileWriter writer;
		try {
			writer = new FileWriter(basePath + fileName, append);
			for (int i = 0; i < textlines.size(); i++) {
				writer.write(textlines.get(i) + "\r\n");
				count++;
				if (count % 5000 == 0) {
					writer.flush();
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			logger.error(e);
		}
		return true;
	}

	/**
	 * 存入到txt
	 * **/
	public static void inputTxt(String path, String sb) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			}
			OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f, true), "utf-8");
			BufferedWriter writer = new BufferedWriter(write);
			writer.write(sb);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** txt按行读到map **/
	public static Map<String, String> getTxtByLine2Map(String path) throws Exception {
		int count = 0;
		Map<String, String> dicMap = new HashMap<String, String>();
		FileInputStream fis = new FileInputStream(path);
		InputStreamReader isr = new InputStreamReader(fis, "utf-8");
		BufferedReader br = new BufferedReader(isr);
		String line = "";
		while ((line = br.readLine()) != null) {
			dicMap.put(line.trim(), "");
			count++;
		}
		br.close();
		isr.close();
		fis.close();
		System.out.println(" 总共获得文本条数：  = " + count);
		return dicMap;
	}

	/** txt按行读到LIST **/
	public static ArrayList<String> getTxtByLine2List(String path) {
		int count = 0;
		ArrayList<String> list = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				list.add(line.trim());
				count++;
			}
			br.close();
			isr.close();
			fis.close();
			logger.info(" 总共获得文本条数：  = " + count);
		} catch (Exception e) {
			logger.info(e);
		}

		return list;
	}

	/**
	 * List排重
	 ***/
	public static ArrayList<String> dupilateClear(ArrayList<String> list) {
		ArrayList<String> oList = new ArrayList<String>();
		Map<String, String> check = new HashMap<String, String>();
		if (list != null && list.size() > 0) {
			for (String str : list) {
				if (check.get(str) == null) {
					oList.add(str);
					check.put(str, "");
				}

			}
		}
		return oList;
	}

	/** get text lines **/
	public static ArrayList<String> getText(String path) {
		ArrayList<String> lists = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#") || line.equals("") || line.equals("\t"))
					continue;
				if (line.indexOf(" ") < 0) {
					lists.add(line.trim());
					continue;
				} else {
					lists.add(line.substring(0, line.indexOf(" ", 0)).trim());
					continue;
				}
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return lists;
	}

	/** 从文件中得到每行存入LIST **/
	public static ArrayList<String> getTxt2List(String pathFile) {
		ArrayList<String> lists = new ArrayList<String>();
		try {
			FileInputStream fis = new FileInputStream(pathFile);
			InputStreamReader isr = new InputStreamReader(fis, "utf-8");
			BufferedReader br = new BufferedReader(isr);
			String line = "";
			while ((line = br.readLine()) != null) {
				lists.add(line.trim());
			}
			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			System.out.println(e.toString());
		}

		return lists;
	}

	/** 按行插入字符串到指定文件 ,文件在DIC目 **/
	public static boolean inputTXT(String pathFile, List textlines) {
		boolean ok = false;
		int count = 0;
		try {
			FileWriter writer = new FileWriter(pathFile);
			for (int i = 0; i < textlines.size(); i++) {
				writer.write(textlines.get(i).toString() + "\r\n");
				count++;
				if (count % 500 == 0) {
					writer.flush();
				}
			}
			writer.flush();
			writer.close();
			ok = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ok;
	}

	/**
	 * 复合排序器排序 可自定义排序字段 排序字段为键值对，如果值为空是顺序，值不为空是逆序 例如： sortNames.add(new
	 * KeyValue("count", "re")); sortNames.add(new KeyValue("wordLen", ""));
	 * sortNames.add(new KeyValue("word", ""));
	 * **/
	@SuppressWarnings("unchecked")
	public static void sortMapbyParam(ArrayList<Object> wordList, ArrayList<KeyValue> sortNames) {
		// 创建一个排序规则
		Comparator<Object> reverseComp = ComparableComparator.getInstance();
		reverseComp = ComparatorUtils.nullLowComparator(reverseComp); // 允许null
		reverseComp = ComparatorUtils.reversedComparator(reverseComp); // 逆序
		// 声明要排序的对象的属性，并指明所使用的排序规则，如果不指明，则用默认排序
		ArrayList<Object> sortFields = new ArrayList<Object>();
		for (KeyValue sortKV : sortNames) {
			String name = (String) sortKV.getKey();
			String sortBy = (String) sortKV.getValue();
			if (sortBy.isEmpty()) {
				sortFields.add(new BeanComparator(name));
			} else {
				sortFields.add(new BeanComparator(name, reverseComp));
			}

		}

		// 创建一个排序链
		ComparatorChain multiSort = new ComparatorChain(sortFields);
		// 开始真正的排序，按照先主，后副的规则
		Collections.sort(wordList, multiSort);

	}

	public static void main(String[] args) {
		String file = "F:\\workspace\\dooioo-solr-ground\\dic\\dooioo\\dic_prex.txt";

		ArrayList<String> list = getTxt2List(file);

		ArrayList<String> listclear = dupilateClear(list);

		inputTXT("F:\\workspace\\dooioo-solr-ground\\dic\\dooioo\\dic_prex-clear.txt", listclear);

	}
}
