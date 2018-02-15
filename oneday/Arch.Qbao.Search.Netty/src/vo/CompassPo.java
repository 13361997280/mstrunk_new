package vo;

import java.util.List;
import java.util.Map;

/**
 * @author song.j
 * @create 2017-09-15 09:09:38
 **/
public class CompassPo {


    /**
     * success : true
     * responseCode : 1000
     * message : ok
     * data : {"currentTime":"09:39:49","timeSort":[{"time":"00:00:00","moduleType":"video,weather,news,shopping,baoy,goodThings,sign,research,rates"}],"currentDate":"2017.09.15","currentWeek":"星期五"}
     */

    private boolean success;
    private int responseCode;
    private String message;
    private DataBean data;


    public CompassPo() {
    }

    public CompassPo(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static CompassPo faild(String message){
        return new CompassPo(false,message);
    }

    public CompassPo(boolean success, DataBean data) {
        this.success = success;
        this.data = data;
    }


    public static CompassPo success(DataBean dataBean){
        return new CompassPo(true,dataBean);
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * currentTime : 09:39:49
         * timeSort : [{"time":"00:00:00","moduleType":"video,weather,news,shopping,baoy,goodThings,sign,research,rates"}]
         * currentDate : 2017.09.15
         * currentWeek : 星期五
         */

        private String currentTime;
        private String currentDate;
        private String currentWeek;
        private List<TimeSort> timeSort;

        public String getCurrentTime() {
            return currentTime;
        }

        public void setCurrentTime(String currentTime) {
            this.currentTime = currentTime;
        }

        public String getCurrentDate() {
            return currentDate;
        }

        public void setCurrentDate(String currentDate) {
            this.currentDate = currentDate;
        }

        public String getCurrentWeek() {
            return currentWeek;
        }

        public void setCurrentWeek(String currentWeek) {
            this.currentWeek = currentWeek;
        }

        public List<TimeSort> getTimeSort() {
            return timeSort;
        }

        public void setTimeSort(List<TimeSort> timeSort) {
            this.timeSort = timeSort;
        }

    }
}
