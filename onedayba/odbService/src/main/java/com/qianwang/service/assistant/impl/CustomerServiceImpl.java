package com.qianwang.service.assistant.impl;

import com.qianwang.dao.domain.advertisement.Advertisement;
import com.qianwang.dao.domain.advertisement.Customer;
import com.qianwang.mapper.advertisement.AdvertisementMapper;
import com.qianwang.mapper.customer.CustomerMapper;
import com.qianwang.service.assistant.AdvertisementService;
import com.qianwang.service.assistant.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by songjie on 17/4/13.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

    protected static final Logger LOG = LoggerFactory.getLogger(CustomerServiceImpl.class);

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public List<Customer> selectAll() {
        return customerMapper.selectAll();
    }

}
