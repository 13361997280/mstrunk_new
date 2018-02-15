package po;

import java.util.Comparator;

/** 搜索日志聚合类 **/
public class SearchFineVo {
	private String searchwd;
	private long searchSum;
	private long createdTime;
	private String Date;
	private long logEventId;

	public SearchFineVo(String searchwd, long searchSum, String date) {
		super();
		this.searchwd = searchwd;
		this.searchSum = searchSum;
		Date = date;
	}
	


	public String getSearchwd() {
		return searchwd;
	}

	public void setSearchwd(String searchwd) {
		this.searchwd = searchwd;
	}

	public long getSearchSum() {
		return searchSum;
	}

	public void setSearchSum(long searchSum) {
		this.searchSum = searchSum;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public long getLogEventId() {
		return logEventId;
	}

	public void setLogEventId(long logEventId) {
		this.logEventId = logEventId;
	}



	/**
	 * DateStr排序比较器
	 * @author hz
	 */
	public static class comparatorDate implements Comparator<SearchFineVo> {
		public int compare(SearchFineVo o1, SearchFineVo o2) {
			SearchFineVo s1 = (SearchFineVo) o1;
			SearchFineVo s2 = (SearchFineVo) o2;
			int result = s1.Date.compareTo(s2.Date);
			return result;
		}
	}
	
	/**
	 * DateStr排序比较器
	 * @author hz
	 */
	public static class comparatorSearchSum implements Comparator<SearchFineVo> {
		public int compare(SearchFineVo o1, SearchFineVo o2) {
			SearchFineVo s1 = (SearchFineVo) o1;
			SearchFineVo s2 = (SearchFineVo) o2;
			int result = (int)(s2.searchSum-s1.searchSum);
			return result;
		}
	}
}
