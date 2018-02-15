package com.qianwang.service.user.impl;

import com.qianwang.dao.aspect.RecentData;
import com.qianwang.dao.domain.authority.Menu;
import com.qianwang.dao.domain.city.Area;
import com.qianwang.dao.domain.city.City;
import com.qianwang.dao.domain.city.Province;
import com.qianwang.dao.domain.user.UserRegist;
import com.qianwang.dao.domain.user.Users;
import com.qianwang.dao.util.DataSourceContextHolder;
import com.qianwang.mapper.city.AreaMapper;
import com.qianwang.mapper.city.CityMapper;
import com.qianwang.mapper.city.ProvinceMapper;
import com.qianwang.mapper.user.*;
import com.qianwang.service.user.CityService;
import com.qianwang.service.user.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityServiceImpl implements CityService{
	protected static final Log LOG = LogFactory.getLog(CityServiceImpl.class);

	@Autowired
	private ProvinceMapper provinceMapper;

	@Autowired
	private CityMapper cityMapper;

	@Autowired
	private AreaMapper areaMapper;

	@Override
	@RecentData(dataSourceName= DataSourceContextHolder.DW_65)
	public List<Province> getAllProvince() {
		//Map<String,Object> params = new HashMap<String,Object>();
		return provinceMapper.selectAll();
	}

	@Override
	public List<City> getAllCity() {
		return cityMapper.selectAll();
	}

	@Override
	public List<Area> getAllArea() {
		return areaMapper.selectAll();
	}
}
