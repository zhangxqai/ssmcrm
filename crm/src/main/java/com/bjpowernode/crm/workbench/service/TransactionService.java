package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionService {

    /**
     * 根据条件进行查询所有符合条件的全部交易列表
     * @param map
     * @return
     */
    List<Transaction> selectTranSanctionAll(Map<String,Object> map);

    /**
     * 根据条件进行查询所有符合条件的总条数
     * @param map
     * @return
     */
    int selectTranSanctionAllCount(Map<String,Object> map);
}
