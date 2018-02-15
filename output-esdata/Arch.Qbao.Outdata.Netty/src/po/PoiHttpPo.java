package po;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * A 前缀 www.lbs.tengrong.com/poi? 输入参数：［x ＝ ，y＝ ， r＝ ， OR x1 ＝ , y1= , x2 = ,
 * y2= OR poi_id = ］＋ 图形类型［type=t , r 方形或圆形］ ＋ 查询坐标类型［map= baidu，gmap ,
 * poiid］＋排序条件[sort= feild1, feild2 , …［特殊排序：按搜索原点的距离远近排序］ ＋ 过滤条件 feild1 = ,
 * feild2= , …. 输出：poi list [翻页]
 * 
 * B 前缀 www.lbs.tengrong.com/address ? 输入： poi_name= , poi_name_fenci = ,
 * road_name= , road_name_fenci = , poi_type = , poi_type_tag= , shen= , shi=,
 * qu= , 排序条件[sort= feild1, feild2 , … ， 输出: poi list [翻页]
 * ［先查本地poi库，为空再查百度poi库，并存储新poi到本地库］
 * 
 * C www.lbs.tengrong.com/geo ? 输入 电子围栏 ＋ 过滤条件 ＋排序条件[sort= feild1, feild2 , …
 * 输出： poi list [翻页] [可第二期做]
 * 
 **/
public class PoiHttpPo {

	private String x;
	private String y;
	private String x1;
	private String y1;
	private String x2;
	private String y2;
	private String type;
	private String map;
	private String sort;
	private String page = "1";// 页面数,默认为1
	private String pageSize = "10";// 页面尺寸，默认为10

	// 百度uid,或者x+Y+type
	private String poiId;
	// 百度
	private BigDecimal xB;
	// 百度
	private BigDecimal yB;
	// 高德
	private BigDecimal xG;
	// 高德
	private BigDecimal yG;
	// 百度保留3位小数
	private Float xB1;
	// 百度保留3位小数
	private Float yB1;
	// 高德保留3位小数
	private Float xG1;
	// 高德保留3位小数
	private Float yG1;
	// 省
	private String shen;
	// 省code
	private String shenCode;
	// 市
	private String shi;
	// 市code
	private String shiCode;
	// 区县
	private String qu;
	// 区县code
	private String quCode;
	// poi名称
	private String poiName;
	// 道路名[地址]
	private String roadName;
	// poi类型
	private String poiType;
	// poi类型标签
	private String poiTypeTag;
	// 更新时间 "yyyy-MM-dd HH:mm:ss"
	private String update;

	public String getX() {
		return x;
	}

	public void setX(String x) {
		this.x = x;
	}

	public String getY() {
		return y;
	}

	public void setY(String y) {
		this.y = y;
	}

	public String getX1() {
		return x1;
	}

	public void setX1(String x1) {
		this.x1 = x1;
	}

	public String getY1() {
		return y1;
	}

	public void setY1(String y1) {
		this.y1 = y1;
	}

	public String getX2() {
		return x2;
	}

	public void setX2(String x2) {
		this.x2 = x2;
	}

	public String getY2() {
		return y2;
	}

	public void setY2(String y2) {
		this.y2 = y2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public String getPoiId() {
		return poiId;
	}

	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}

	public BigDecimal getxB() {
		return xB;
	}

	public void setxB(BigDecimal xB) {
		this.xB = xB;
	}

	public BigDecimal getyB() {
		return yB;
	}

	public void setyB(BigDecimal yB) {
		this.yB = yB;
	}

	public BigDecimal getxG() {
		return xG;
	}

	public void setxG(BigDecimal xG) {
		this.xG = xG;
	}

	public BigDecimal getyG() {
		return yG;
	}

	public void setyG(BigDecimal yG) {
		this.yG = yG;
	}

	public Float getxB1() {
		
		return xB1;
	}

	public void setxB1(Float xB1) {
		this.xB1 = xB1;
	}

	public Float getyB1() {
		return yB1;
	}

	public void setyB1(Float yB1) {
		this.yB1 = yB1;
	}

	public Float getxG1() {
		return xG1;
	}

	public void setxG1(Float xG1) {
		this.xG1 = xG1;
	}

	public Float getyG1() {
		return yG1;
	}

	public void setyG1(Float yG1) {
		this.yG1 = yG1;
	}

	public String getShen() {
		return shen;
	}

	public void setShen(String shen) {
		this.shen = shen;
	}

	public String getShenCode() {
		return shenCode;
	}

	public void setShenCode(String shenCode) {
		this.shenCode = shenCode;
	}

	public String getShi() {
		return shi;
	}

	public void setShi(String shi) {
		this.shi = shi;
	}

	public String getShiCode() {
		return shiCode;
	}

	public void setShiCode(String shiCode) {
		this.shiCode = shiCode;
	}

	public String getQu() {
		return qu;
	}

	public void setQu(String qu) {
		this.qu = qu;
	}

	public String getQuCode() {
		return quCode;
	}

	public void setQuCode(String quCode) {
		this.quCode = quCode;
	}

	public String getPoiName() {
		return poiName;
	}

	public void setPoiName(String poiName) {
		this.poiName = poiName;
	}

	public String getRoadName() {
		return roadName;
	}

	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}

	public String getPoiType() {
		return poiType;
	}

	public void setPoiType(String poiType) {
		this.poiType = poiType;
	}

	public String getPoiTypeTag() {
		return poiTypeTag;
	}

	public void setPoiTypeTag(String poiTypeTag) {
		this.poiTypeTag = poiTypeTag;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public String poiInputtoString() {
		return "PoiHttpPo [poi_id=" + poiId + ", x_b=" + xB + ", y_b=" + yB + ", x_g=" + xG + ", y_g=" + yG + ", shen="
				+ shen + ", shen_code=" + shenCode + ", shi=" + shi + ", shi_code=" + shiCode + ", qu=" + qu
				+ ", qu_code=" + quCode + ", poi_name=" + poiName + ", road_name=" + roadName + ", poi_type=" + poiType
				+ ", poi_type_tag=" + poiTypeTag + "]";
	}
	
	/**
	 * 保留3位小数
	 * @param num
	 * @return
	 */
	public Float cutThreeNumber(BigDecimal num) {
		DecimalFormat fnum = new DecimalFormat("##0.000");
		String dd = fnum.format(num);
		return Float.parseFloat(dd);
	}
	
	/**
	 * 保留6位小数
	 * @param num
	 * @return
	 */
	public String cutSixNumber(BigDecimal num) {
		DecimalFormat fnum = new DecimalFormat("##0.000000");
		String dd = fnum.format(num);
		return dd;
	}
	
	public static void main(String args[]) {
		BigDecimal scale = new BigDecimal(116.34071960680832);
		DecimalFormat fnum = new DecimalFormat("##0.000000");
		String dd = fnum.format(scale);
		System.out.println(dd);
	}

}
