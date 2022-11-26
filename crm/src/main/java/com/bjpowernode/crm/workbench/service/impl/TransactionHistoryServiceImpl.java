package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.TransactionHistory;
import com.bjpowernode.crm.workbench.domain.TransactionRemark;
import com.bjpowernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.bjpowernode.crm.workbench.service.TransactionHistoryService;
import com.bjpowernode.crm.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("transactionHistoryService")
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public List<TransactionHistory> selectForTranHistoryById(String tranId) {
        return transactionHistoryMapper.selectForTranHistoryById(tranId);
    }
}
