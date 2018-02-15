package vo;

import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.util.CommonUtil;

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
	
	private String action="";

	public CommandPo(HttpRequest httpRequest) {
		super();
		try {
			type = CommonUtil.getParam(httpRequest, "type");
			date = CommonUtil.getParam(httpRequest, "date");
			action = CommonUtil.getParam(httpRequest, "action");

		} catch (Exception e) {
			// TODO: handle exception
		}

		if (type == null || type.isEmpty())
			type = "";
		if (date == null || date.isEmpty())
			date = "";
		if (action == null || action.isEmpty())
			action = "";


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
	
	

}
