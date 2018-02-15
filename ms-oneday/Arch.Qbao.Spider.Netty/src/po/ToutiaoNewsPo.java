package po;

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.util.HTMLSpirit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.qbao.search.util.FileUtil;

public class ToutiaoNewsPo {
	private String newsId;
	private String newsWebsite;// 新闻网站
	private String title;// 新闻标题
	private String newsUrl;// 新闻链接
	private String news_type;// 新闻类型
	private String news_time;// 新闻发布时间
	private String news_origin;// 新闻来源
	private String content;// html格式的content
	private String content_fenci;// 过滤分词的content
	private String news_tags;// 新闻标签
	private String crawl_date_time;// 爬取日期
	private String image_url;//图片链接
	private Long hot_time;//热点时间
	private String imageUrls;//图片链接
	private String newsAbstract;
	private static String[] newsTimeREG = { "div.articleInfo","span.time" };
	private static String[] contentREG = { "div.article-content" };

	public void parser(String html) {
		try {
			Elements elemets = Jsoup.parse(html).getAllElements();
			newsWebsite = "今日头条";
			Elements script = elemets.select("script");
			for(Element data:script){
				if(data.html().contains("content:")) //注意这里一定是html(), 而不是text()
				{
					String str = data.html().replace("\n", ""); //这里是为了解决 无法多行匹配的问题
					String newTime = str;
					int start = str.indexOf("content: '")+10;
					int end = str.indexOf("&gt;',")+4;
					str = str.substring(start,end);
					str = str.replace("&lt;","<");
					str = str.replace("&#x3D;","=");
					str = str.replace("&gt;",">");
					str = str.replace("&quot;","\"");
					content = str;
					content_fenci = HTMLSpirit.delHTMLTag(str);
					start = newTime.indexOf("time: '")+7;
					newTime = newTime.substring(start,start+16);
					news_time = newTime;
					break;
				}
			}
		} catch (Exception e) {
		}

	}

	public String getNews_id() {
		return newsId;
	}

	public void setNews_id(String news_id) {
		this.newsId = news_id;
	}

	public String getNews_website() {
		return newsWebsite;
	}

	public void setNews_website(String news_website) {
		this.newsWebsite = news_website;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNews_url() {
		return newsUrl;
	}

	public void setNews_url(String news_url) {
		this.newsUrl = news_url;
	}

	public String getNews_type() {
		return news_type;
	}

	public void setNews_type(String news_type) {
		this.news_type = news_type;
	}

	public String getNews_time() {
		return news_time;
	}

	public void setNews_time(String news_time) {
		this.news_time = news_time;
	}

	public String getNews_origin() {
		return news_origin;
	}

	public void setNews_origin(String news_origin) {
		this.news_origin = news_origin;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent_fenci() {
		return content_fenci;
	}

	public void setContent_fenci(String content_fenci) {
		this.content_fenci = content_fenci;
	}

	public String getNews_tags() {
		return news_tags;
	}

	public void setNews_tags(String news_tags) {
		this.news_tags = news_tags;
	}

	public String getCrawl_date_time() {
		return crawl_date_time;
	}

	public void setCrawl_date_time(String crawl_date_time) {
		this.crawl_date_time = crawl_date_time;
	}

	public Long getHot_time() {
		return hot_time;
	}

	public void setHot_time(Long hot_time) {
		this.hot_time = hot_time;
	}

	private String removeHtml(String htmlStr){
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		String regEx_html = "<[^>]+>";
		p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll("");
		return htmlStr;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	@Override
	public String toString() {
		return "{" +
				"news_website='" + newsWebsite +'\'' +
				", title='" + title + '\'' +
				", news_id='" + newsId + '\'' +
				", news_url='" + newsUrl + '\'' +
				", news_type='" + news_type +'\'' +
				", news_time='" + news_time +'\'' +
				", news_origin='" + news_origin +'\'' +
				", content='" + content +'\'' +
				", content_fenci='" + content_fenci +'\'' +
				", news_tags='" + news_tags + '\'' +
				", crawl_date_time='" + crawl_date_time + '\'' +
				", image_url='" + image_url + '\'' +
				", hot_time=" + hot_time +
				'}';
	}
}
