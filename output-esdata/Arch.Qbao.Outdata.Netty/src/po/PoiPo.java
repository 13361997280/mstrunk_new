package po;

import java.util.Comparator;

public class PoiPo {
	// [百度quid,或者x+Y+type]
	private String poi_id;
	private Double x_b;
	// 百度
	private Double y_b;
	// 百度
	private Double x_g;
	// 高德
	private Double y_g;
	// 高德
	private String shen;
	// 省
	private String shen_code;
	// 省code
	private String shi;
	// 市
	private String shi_code;
	// 市code
	private String qu;
	// 区县
	private String qu_code;
	// 区县code
	private String poi_name;
	// poi名称
	private String poi_name_fenci;
	// poi名称分词
	private String road_name;
	// 道路名[地址]
	private String road_name_fenci;
	// 道路名[地址] 分词
	private String poi_type;
	// poi类型
	private String poi_type_code;
	// poi类型标签
	private String update;
	// 更新时间
	//和原点的排序距离
	private float distanc;
	
	
	
	/**
	 * 距离排序比较器
	 * @author hz
	 */
	public static class comparatorDistanc implements Comparator<PoiPo> {
		public int compare(PoiPo o1, PoiPo o2) {
			PoiPo s1 = (PoiPo) o1;
			PoiPo s2 = (PoiPo) o2;
			int result = (int)(s2.distanc-s1.distanc);
			return result;
		}
	}
	
	
	
}
