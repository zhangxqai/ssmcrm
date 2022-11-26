package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.FunnelVo;
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

    /**
     * 在交易模块中添加一个交易信息
     * @param map
     * @return
     */
    void insertTransactionOne(Map<String,Object> map);

    /**
     * 查看交易详情信息，根据id查询这个信息
     * @param id
     * @return
     */
    Transaction selectForDetailById(String id);

    /**
     * 查询交易表中的各个阶段的交易量
     * @return
     */
    List<FunnelVo> selectCountOfTranGroupByStage();
}
