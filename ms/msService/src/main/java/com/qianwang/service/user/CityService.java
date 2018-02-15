package com.qianwang.service.user;

import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.city.Area;
import com.qianwang.dao.domain.city.City;
import com.qianwang.dao.domain.city.Province;
import com.qianwang.dao.domain.user.UserRegist;
import com.qianwang.dao.domain.user.Users;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CityService {
    /**
     * 查询所有
     * @return 省份信息集合
     */
    List<Province> getAllProvince();
    /**
     * 查询所有
     * @return 市信息集合
     */
    List<City> getAllCity();
    /**
     * 查询所有
     * @return 区信息集合
     */
    List<Area> getAllArea();

}
