package vo;

import com.qbao.search.util.CommonUtil;
import org.apache.commons.lang.StringUtils;
import org.jboss.netty.handler.codec.http.HttpRequest;

public class CommandPo {

	/**
	 * 各种netty，es re-index ,dailymail,startup, restart, shutdown,monitor,health
	 * check，stat..
	 * ===========================================
	 * localhost:port/command?type=indexupdate
	 * localhost:port/command?type=indexupdate&action=cancel 立即停止索引更新
	 **/

	 
	private String type = "";
	
	/**更新那天的数据,例如2017-01-03**/
	private String date="";
	
	private String action = "";
	private String table = "";
	private String index = "";
	private String key = "";
	private String dt = "";
	public CommandPo(){

	}

	public CommandPo(HttpRequest httpRequest) {
		super();
		try {
			type = CommonUtil.getParam(httpRequest, "type");
			date = CommonUtil.getParam(httpRequest, "date");
			action = CommonUtil.getParam(httpRequest, "action");
			table = CommonUtil.getParam(httpRequest, "table");
			index = CommonUtil.getParam(httpRequest, "index");
			key = CommonUtil.getParam(httpRequest, "key");
			dt = CommonUtil.getParam(httpRequest, "dt");

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (type == null || type.isEmpty())
			type = "";
		if (date == null || date.isEmpty())
			date = "";
		if (action == null || action.isEmpty())
			action = "";
		if (table == null || table.isEmpty())
			table = "";
		if (index == null || index.isEmpty())
			index = "";
		if (key == null || key.isEmpty())
			key = "";
		if (dt == null || dt.isEmpty())
			dt = "";


	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getDt() {
		return dt;
	}

	public void setDt(String dt) {
		this.dt = dt;
	}

    public void check() {
        if (StringUtils.isEmpty(date))
            throw new IllegalAccessError("date not be null");

        if (StringUtils.isEmpty(table))
            throw new IllegalAccessError("table not be null");


    }
}
