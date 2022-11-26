package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.TransactionRemark;
import com.bjpowernode.crm.workbench.mapper.TransactionRemarkMapper;
import com.bjpowernode.crm.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("transactionRemarkService")
public class TransactionRemarkServiceImpl implements TransactionRemarkService {

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public List<TransactionRemark> selectForDetailTranRemarkById(String tranId) {
        return transactionRemarkMapper.selectForDetailTranRemarkById(tranId);
    }

    /**
     * 根据id添加备注信息
     * @param transactionRemark
     * @return
     */
    @Override
    public int insertTransactionRemark(TransactionRemark transactionRemark) {
        return transactionRemarkMapper.insertTransactionRemark(transactionRemark);
    }

    /**
     * 根据id删除备注信息
     * @param id
     * @return
     */
    @Override
    public int deleteTransactionRemark(String id) {
        return transactionRemarkMapper.deleteTransactionRemark(id);
    }

    @Override
    public int updateTransactionRemark(TransactionRemark transactionRemark) {
        return transactionRemarkMapper.updateTransactionRemark(transactionRemark);
    }
}
