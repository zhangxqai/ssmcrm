package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {

    /**
     * 在添加交易，根据联系人名称查询出相对于的信息
     * @param fullname
     * @return
     */
    List<Contacts> selectFroSavaTranByNameContacts(String fullname);
}
