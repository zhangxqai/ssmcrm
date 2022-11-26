package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TransactionHistory;

import java.util.List;

public interface TransactionHistoryService {

    /**
     * 为了跳转页面，根据交易id查询交易历史记录
     * @param tranId
     * @return
     */
    List<TransactionHistory> selectForTranHistoryById(String tranId);
}
