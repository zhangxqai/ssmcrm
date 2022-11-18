package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Transaction;
import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    //调用mapper方法，要注入
    @Autowired
    private TransactionMapper transactionMapper;
    @Override
    public List<Transaction> selectTranSanctionAll(Map<String, Object> map) {
        return transactionMapper.selectTranSanctionAll(map);
    }

    @Override
    public int selectTranSanctionAllCount(Map<String, Object> map) {
        return transactionMapper.selectTranSanctionAllCount(map);
    }
}
