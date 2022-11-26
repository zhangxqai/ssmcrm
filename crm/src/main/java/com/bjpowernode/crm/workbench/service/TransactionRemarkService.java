package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.TransactionRemark;

import java.util.List;

public interface TransactionRemarkService {

    /**
     * 为了跳转页面，根据交易id查询交易备注信息
     * @param tranId
     * @return
     */
    List<TransactionRemark> selectForDetailTranRemarkById(String tranId);

    /**
     * 根据id添加备注信息
     * @param transactionRemark
     * @return
     */
    int insertTransactionRemark(TransactionRemark transactionRemark);

    /**
     * 根据id删除备注信息
     * @param id
     * @return
     */
    int deleteTransactionRemark(String id);

    /**
     * 根据id对备注进行修改
     * @param transactionRemark
     * @return
     */
    int updateTransactionRemark(TransactionRemark transactionRemark);
}
