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
