package data.spider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qbao.search.conf.Config;
import com.qbao.search.conf.LoadValues;
import com.qbao.search.util.HttpClientUtil;
import data.service.ToutiaoEsInputService;
import org.apache.commons.lang3.StringUtils;
import po.ToutiaoNewsPo;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.ConsolePipeline;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.pipeline.Pipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.DelayQueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import us.codecraft.webmagic.monitor.SpiderMonitor;

/**
 * 抓取今日关注热点新闻
 * 
 */
public class ToutiaoApiProcesser implements PageProcessor {
	private static ToutiaoApiProcesser ToutiaoApiProcesser;
	private static Site site = Site.me().setDomain("toutiao.com");
	private static Spider spider;
	private static SimpleDateFormat df;

	public final static ToutiaoApiProcesser getInstance() {// 单例模式
		try {
			if (ToutiaoApiProcesser == null) {
				synchronized (ToutiaoApiProcesser.class) {
					if (ToutiaoApiProcesser == null) {
						ToutiaoApiProcesser = new ToutiaoApiProcesser();
						ToutiaoApiProcesser.getSite().setCharset("utf-8");
						df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return ToutiaoApiProcesser;
	}

	@Override
	public void process(Page page) {

		long nowTime = System.currentTimeMillis();
		// 得到新闻链接list
		JSONObject jsonObject = JSON.parseObject(page.getRawText());
		JSONArray jsonArray = jsonObject.getJSONArray("data");
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject1 = jsonArray.getJSONObject(i);
			String imageUrl = jsonObject1.getString("image_url");
			if ("".equals(imageUrl)||null==imageUrl) continue;
			String newsOrigin = jsonObject1.getString("source");
			if (!"".equals(newsOrigin)&&newsOrigin.contains("头条")) continue;
			String newsUrl = jsonObject1.getString("source_url");
			if ("".equals(newsUrl)||null==newsUrl) continue;
			String news_type = jsonObject1.getString("chinese_tag");
			Long hotTime = jsonObject1.getLong("behot_time") * 1000;
			String title = jsonObject1.getString("title");
			if ("".equals(title)||null==title
					||title.contains("钱宝")
					||title.contains("钱旺")
					||title.contains("张小雷")
					||title.contains("非法集资")
					||title.contains("庞氏骗局")
					) continue;
			String abstract_content = jsonObject1.getString("abstract");
			String news_tags = jsonObject1.getString("label");
			if (news_tags == null)
				continue;
			if (news_tags != null) {
				news_tags = news_tags.replaceAll("\"", "").replaceAll("\\[", "").replaceAll("\\]", "");
			}
			if (!newsUrl.contains("group"))
				continue;
			if (news_type == null || news_type.contains("图片"))
				continue;
			newsUrl = newsUrl.substring(0, newsUrl.length() - 1);
			newsUrl = newsUrl.replaceAll("group/", "a");
			String crawl_date_time = df.format(new Date());
			String newsId = newsUrl.substring(newsUrl.lastIndexOf("/") + 1, newsUrl.length());
			Map<String, Object> ret = new HashMap<String, Object>();
			ret.put("news_id", newsId);
			ret.put("image_url", imageUrl);
			ret.put("news_url", newsUrl);
			ret.put("news_origin", newsOrigin);
			ret.put("title", title);
			ret.put("news_type", news_type);
			ret.put("news_tags", news_tags);
			ret.put("news_tags_fenci", news_tags);
			ret.put("abstract_content", abstract_content);
			ret.put("abstract_content_fenci", abstract_content);
			ret.put("crawl_date_time", crawl_date_time);
			ret.put("hot_time", df.format(new Date(hotTime)));

			// 得到新闻详情页
			spiderDetail(ret);
			if (ret.get("content") == null || "".equals(ret.get("content"))) {
				continue;
			}else {
				String content = ret.get("content").toString();
				if (content.contains("钱宝") || content.contains("钱旺") || content.contains("张小雷")|| content.contains("非法集资")|| content.contains("庞氏骗局"))
					continue;
			}
			if (ret.get("news_time") == null || "".equals(ret.get("news_time")))
				continue;
			if(ToutiaoEsInputService.getInstance().isExist(newsId,title,imageUrl)) continue;
			try {
				// 新闻详情解析后存储到es
				ToutiaoEsInputService.getInstance().importEsFromSpider(ret);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("++++++++++++++++++ 本批次抓取和存储耗时 =   " + (System.currentTimeMillis() - nowTime) + "");
	}

	public void spiderDetail(Map<String, Object> ret) {
		String contentUrl = ret.get("news_url").toString();
		String hrmlStr = HttpClientUtil.getPage("http://www.toutiao.com" + contentUrl);

		if (StringUtils.isNotEmpty(hrmlStr)) {
			ToutiaoNewsPo po = new ToutiaoNewsPo();
			po.parser(hrmlStr);
			ret.put("news_website", po.getNews_website());
			ret.put("news_time", po.getNews_time());
			ret.put("content", po.getContent());
			ret.put("content_fenci", po.getContent_fenci());
			int length = 0;
			if(po.getContent()!=null)length=po.getContent().length();
			System.out.println("++++get page detail  :" + ",内容长度＝" + length);
		}
	}

	@Override
	public Site getSite() {
		return site;
	}

	public void doProcess() {
		if (this.getSpider() != null) {
			this.getSpider().stop();// 先停止正在抓取的spider
		}
		String seedUrls = Config.getBase().get(LoadValues.SPIDER_URL).trim();
		
		String[] seedLinks =seedUrls.split("\\|=\\|");
		
		
		ToutiaoApiProcesser process = new ToutiaoApiProcesser();
		String projectPath = Config.getBase().get(LoadValues.SPIDER_FOLDRE).trim();
		Pipeline filePipe = new FilePipeline(projectPath);

		// Scheduler scheduler = new FileCacheQueueScheduler(projectPath);
		Scheduler scheduler = new DelayQueueScheduler(250, TimeUnit.SECONDS);
		spider = Spider.create(process).setExitWhenComplete(true).scheduler(scheduler).addUrl(seedLinks)
				.addPipeline(filePipe).addPipeline(new ConsolePipeline()).thread(16);
		spider.getSite().addHeader("Host", "www.toutiao.com");

		spider.start();
	}

	public void restart() {

		close();
		ToutiaoApiProcesser.getInstance().doProcess();

	}

	public void close() {
		
		ToutiaoApiProcesser=null;

	}

	/**
	 * @return the spider
	 */
	public Spider getSpider() {
		return spider;
	}

	/**
	 * @param spider
	 *            the spider to set
	 */
	public static void setSpider(Spider spider) {
		ToutiaoApiProcesser.spider = spider;
	}

	/**
	 * @param site
	 *            the site to set
	 */
	public static void setSite(Site site) {
		ToutiaoApiProcesser.site = site;
	}

	public static void main(String[] args) {
		ToutiaoApiProcesser.getInstance().doProcess();
	}
}
