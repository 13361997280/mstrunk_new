package com.qianwang.mapper.customer;

import com.qianwang.dao.domain.advertisement.Customer;

import java.util.List;

public interface CustomerMapper {
    List<Customer> selectAll();
}