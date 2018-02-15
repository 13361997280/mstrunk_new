package po;

import org.jboss.netty.handler.codec.http.HttpRequest;

import com.qbao.search.util.CommonUtil;

/**
 * Created by hz on 2017/7/21.
 */
public class ToutiaoCmdPo {
    /**
     * cmd=start
     * cmd=restart
     * cmd=stop
     * cmd=status 获取spider最新状态
     * 
     * **/
    private String cmd="";
    
    public ToutiaoCmdPo(HttpRequest httpRequest) {
        super();
        try {
         
        	cmd = CommonUtil.getParam(httpRequest, "cmd");
           
        } catch (Exception e) {
           e.printStackTrace();
        }
    }

	/**
	 * @return the cmd
	 */
	public String getCmd() {
		return cmd;
	}

	/**
	 * @param cmd the cmd to set
	 */
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

    
    
}
