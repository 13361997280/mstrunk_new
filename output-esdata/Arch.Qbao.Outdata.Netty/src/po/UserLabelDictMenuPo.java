package po;

import java.util.ArrayList;

/**
 * Created by chenghaijiang on 2017/5/18.
 */
public class UserLabelDictMenuPo {
    private String name;
    private String icon;
    private ArrayList<UserLabelDictPo> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public ArrayList<UserLabelDictPo> getList() {
        return list;
    }

    public void setList(ArrayList<UserLabelDictPo> list) {
        this.list = list;
    }
}
