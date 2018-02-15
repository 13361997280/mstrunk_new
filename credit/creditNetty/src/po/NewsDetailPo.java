package po;

public class NewsDetailPo {
	private String newsId;
	private String title;// 新闻标题
	private String newsType;// 新闻类型
	private String newsOrigin;// 新闻来源
	private String content;// html格式的content
	private String newsTags;// 新闻标签
	private String imageUrl;//图片链接
	private Integer thumb;//点赞状态  1表示已点赞，0无点赞，2，看过了，3，内容太水，4，不感兴趣
	private String newsTime;
	private String advTitle;//广告标题
	private String advUrl;//广告链接
	private String advImg;//广告图片链接
	private String advTime;//广告发布时间
	private String advDesc;//广告描述
	private String advId;//广告Id

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getNewsType() {
		return newsType;
	}

	public void setNewsType(String newsType) {
		this.newsType = newsType;
	}

	public String getNewsOrigin() {
		return newsOrigin;
	}

	public void setNewsOrigin(String newsOrigin) {
		this.newsOrigin = newsOrigin;
	}

	public String getAdvUrl() {
		return advUrl;
	}

	public void setAdvUrl(String advUrl) {
		this.advUrl = advUrl;
	}

	public String getAdvTitle() {
		return advTitle;
	}

	public void setAdvTitle(String advTitle) {
		this.advTitle = advTitle;
	}

	public String getAdvImg() {
		return advImg;
	}

	public void setAdvImg(String advImg) {
		this.advImg = advImg;
	}

	public String getAdvTime() {
		return advTime;
	}

	public void setAdvTime(String advTime) {
		this.advTime = advTime;
	}

	public String getAdvDesc() {
		return advDesc;
	}

	public void setAdvDesc(String advDesc) {
		this.advDesc = advDesc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNewsTags() {
		return newsTags;
	}

	public void setNewsTags(String newsTags) {
		this.newsTags = newsTags;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Integer getThumb() {
		return thumb;
	}

	public void setThumb(Integer thumb) {
		this.thumb = thumb;
	}

	public String getNewsTime() {
		return newsTime;
	}

	public void setNewsTime(String newsTime) {
		this.newsTime = newsTime;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	@Override
	public String toString() {
		return "{" +
				"newsId='" + newsId +'\'' +
				", title='" + title + '\'' +
				", newsType='" + newsType + '\'' +
				", newsOrigin='" + newsOrigin + '\'' +
				", content='" + content +'\'' +
				", newsTags='" + newsTags +'\'' +
				", imageUrl='" + imageUrl +'\'' +
				", thumb=" + thumb +
				'}';
	}
}
