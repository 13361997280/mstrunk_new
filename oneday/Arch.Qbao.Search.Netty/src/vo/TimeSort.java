package vo;

import com.qbao.dto.Advertisement;

/**
 * @author song.j
 * @create 2017-09-20 17:17:38
 **/
public class TimeSort {
    private int hour;

    private boolean hasCurrent = false;

    private String moduleType;
    private String moduleName;

    private Advertisement advertisement;

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public TimeSort(Object hour, String moduleType) {
        this.hour = Integer.valueOf(hour.toString());
        this.moduleType = moduleType;
    }

    public TimeSort() {
    }

    public TimeSort(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return "{" +
                "hour=" + hour +
                ", hasCurrent=" + hasCurrent +
                ", moduleType='" + moduleType + '\'' +
                '}';
    }

    public boolean isHasCurrent() {
        return hasCurrent;
    }

    public void setHasCurrent(boolean hasCurrent) {
        this.hasCurrent = hasCurrent;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getModuleType() {
        return moduleType;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }
}
