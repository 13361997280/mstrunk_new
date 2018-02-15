package com.qianwang.service.assistant;

import com.qianwang.dao.domain.advertisement.Customer;

import java.util.List;

/**
 * Created by chenghaijiang on 2017/10/23.
 */
public interface CustomerService {
    List<Customer> selectAll();
}
