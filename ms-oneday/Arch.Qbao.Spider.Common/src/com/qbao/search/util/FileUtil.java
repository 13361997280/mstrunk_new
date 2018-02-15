package com.qbao.search.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import us.codecraft.webmagic.Page;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {

	/**
	 * 链接持久化到文本 考虑到性能，会先缓冲在List 中 ，然后分批存储
	 * 
	 * **/
	public void storeLinks(List<String> productLinks, String path, int blockNum, List<String> cashLinks) {
		cashLinks.addAll(productLinks);
		if (cashLinks.size() >= blockNum) {
			StringBuffer sb = new StringBuffer();
			for (String link : cashLinks) {
				sb.append(link).append("\r\n");
			}
			inputTxt(path, sb.toString(), true);
			cashLinks.clear();
			System.out.println("cashlinks size = " + cashLinks.size());
		}

	}

	/**
	 * 存入到txt
	 * **/
	public void inputTxt(String path, String sb, boolean append) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				f.createNewFile();
			}
			if (!append) {
				f.delete();
				f = new File(path);
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

	/**
	 * 按行插入字符串到指定文件 ,文件在DIC目录中,原文件更新
	 * 
	 * @throws Exception
	 * 
	 * @throws Exception
	 **/
	public boolean inputTXT(String pathName, List<String> textlines, boolean append) {
		int count = 0;
		FileWriter writer;
		try {
			writer = new FileWriter(pathName, append);
			for (int i = 0; i < textlines.size(); i++) {
				writer.write(textlines.get(i) + "\r\n");
				count++;
				if (count % 500 == 0) {
					writer.flush();
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * List排重
	 ***/
	public List<String> dupilateClear(List<String> list) {
		List<String> oList = new ArrayList<String>();
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

	/**
	 * Txt排除
	 * 
	 * @throws Exception
	 * **/
	public void TxtFileClear(String path, String newFilePath) throws Exception {
		File f = new File(path);
		if (!f.exists())
			return;
		File newFile = new File(newFilePath);
		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		Map<String, Integer> linksMap = new HashMap<String, Integer>();
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		String link;
		int all = 0;
		while ((link = br.readLine()) != null) {
			//System.out.println(all + "   : " + link);
			if (linksMap.get(link) == null) {
				linksMap.put(link, 1);
			} else {
				linksMap.put(link, linksMap.get(link) + 1);
			}
			all++;
		}
		br.close();
		int sum = 0;
		int count = 0;
		StringBuffer sb = new StringBuffer();
		for (String url : linksMap.keySet()) {
			if (count < 1000) {
				sb.append(url).append("\r\n");
				count++;
			} else {
				inputTxt(newFilePath, sb.toString(), true);
				sum = sum + count;
				System.out.println("Input into file links count = " + count + " ;  Total links sum = " + sum + "  : all = " + all);
				count = 0;
				sb.delete(0, sb.length());
			}
		}
		if (count > 0) {
			inputTxt(newFilePath, sb.toString(), true);
			sum = sum + count;
			System.out.println("Input into file links count = " + count + " ;  Total links sum = " + sum + "  : all = " + all);
		}

	}

	/**
	 * ebayItemId去重
	 * 
	 * @throws Exception
	 * **/
	public void EbayItemClearTxtFile(String path, String newFilePath) throws Exception {
		File f = new File(path);
		if (!f.exists())
			return;
		File newFile = new File(newFilePath);
		if (!newFile.exists()) {
			newFile.createNewFile();
		}
		Map<String, String> linksMap = new HashMap<String, String>();
		FileReader fr = new FileReader(path);
		BufferedReader br = new BufferedReader(fr);
		String link;
		String itemID;
		int index;
		int all = 0;
		while ((link = br.readLine()) != null) {
			//System.out.println(all + "   : " + link);
			index = link.indexOf("?_trksid=") >= 0 ? link.indexOf("?_trksid=") : link.indexOf("?pt=") >= 0 ? link.indexOf("?pt=") : link.indexOf("?rt=");
			if (index > 12) {
				itemID = link.substring(index - 12, index);
				linksMap.put(itemID, link);
				all++;
			}

		}
		br.close();
		int sum = 0;
		int count = 0;
		StringBuffer sb = new StringBuffer();
		for (String url : linksMap.values()) {
			if (count < 1000) {
				sb.append(url).append("\r\n");
				count++;
			} else {
				inputTxt(newFilePath, sb.toString(), true);
				sum = sum + count;

				System.out.println("Input into file links count = " + count + " ;  Total links sum = " + sum + "  : all = " + all);

				count = 0;
				sb.delete(0, sb.length());
			}
		}

		if (count > 0) {
			inputTxt(newFilePath, sb.toString(), true);
			sum = sum + count;
			System.out.println("Input into file links count = " + count + " ;  Total links sum = " + sum + "  : all = " + all);

		}

	}

	public ArrayList<String> jsoupParser(String rawText, String... regexs) {
		ArrayList<String> links = new ArrayList<String>();
		try {
			Document html = Jsoup.parse(rawText);
			Elements elemets = html.getAllElements();
			for (String regex : regexs) {
				elemets = elemets.select(regex);
			}
			if (!elemets.isEmpty()) {
				for (Element element : elemets) {
					links.add(element.attr("href"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return links;
	}

	public ArrayList<String> jsoupParserHtml(String rawText, String... regexs) {
		ArrayList<String> links = new ArrayList<String>();
		try {
			Document html = Jsoup.parse(rawText);
			Elements elemets = html.getAllElements();
			for (String regex : regexs) {
				elemets = elemets.select(regex);
			}
			if (!elemets.isEmpty()) {
				for (Element element : elemets) {
					links.add(element.html());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return links;
	}

	public Elements jsoupParser(Elements elemets, String... regexs) {

		Elements elemt = new Elements();
		try {
			for (String regex : regexs) {
				elemt = elemets.select(regex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return elemt;
	}

	// People who viewed this item also viewed :extract url
	// See what other people are watching
	// Alternatives for you to consider
	public static final String[] extracturlAlsoViewed = { "li>a[href^=http://www.ebay.com/itm/]" };

//	public ArrayList<String> getExtracturlAlsoViewed(Page page) {
//		return jsoupParser(page.getRawText(), FileUtil.extracturlAlsoViewed);
//	}

	// ebay搜索页面总的搜索结果数
	public static final String[] ebaySearchPageResults = { "span.rcnt" };

	public String getEbaySearchPageResults(String html, String[] regxs) {
		try {
			Document doc = Jsoup.parse(html);
			Elements elemets = doc.getAllElements();
			for (String regex : regxs) {
				elemets = elemets.select(regex);
			}
			if (!elemets.isEmpty()) {
				return elemets.get(0).html();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	// ebay搜索页面的排序links
	public static final String[] ebaySearchSortLinks = { "a[href^=http://www.ebay.com/sch/i.html?_nkw=]", "a[href*=_sop]" };

	public ArrayList<String> getEbaySearchSortLinks(String html, String[] regxs) {
		ArrayList<String> links = new ArrayList<String>();
		try {
			Document doc = Jsoup.parse(html);
			Elements elemets = doc.getAllElements();
			// fileUtil.inputTxt("d:\\demo.txt", elemets.html(), true);
			for (String regex : regxs) {
				elemets = elemets.select(regex);
			}
			if (!elemets.isEmpty()) {
				for (Element e : elemets) {
					links.add(e.attr("href"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return links;
	}

	/** get text lines **/
	public ArrayList<String> getText(String path) {
		ArrayList<String> lists = new ArrayList<String>();
		File file = new File(path);
		if (!file.exists() || !file.isFile())
			return lists;
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
			e.printStackTrace();
			
		}

		return lists;
	}

	/**
	 * 删除单个文件
	 * 
	 * @param pathName
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	public boolean deleteFile(String pathName) {
		boolean flag = false;
		File file = new File(pathName);
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param path
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	public boolean deleteDirectory(String path) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!path.endsWith(File.separator)) {
			path = path + File.separator;
		}
		File dirFile = new File(path);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i].getAbsolutePath());
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	public static void main(String[] args) throws Exception {

		// fileUtil.TxtFileClear("D:\\spiderpath\\productLinksClip.txt",
		// "D:\\spiderpath\\productLinksClipClear.txt");

		// fileUtil.EbayItemClearTxtFile("D:\\spiderpath\\productLinksClipNew.txt",
		// "D:\\spiderpath\\productLinksClipNewClear.txt");
	}

}
