package com.bjpowernode.crm.workbench.service;

import java.util.List;

public interface CustomerService {

    /**
     * 根据name模糊查询符合的数据，返回去的只是客户名称，但是可能会有很多客户
     * @param name
     * @return
     */
    List<String> selectCustomerById(String name);
}
