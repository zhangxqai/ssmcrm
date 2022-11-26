package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.FunnelVo;
import com.bjpowernode.crm.workbench.domain.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction
     *
     * @mbggenerated Sun Nov 13 20:35:09 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction
     *
     * @mbggenerated Sun Nov 13 20:35:09 CST 2022
     */
    int insert(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction
     *
     * @mbggenerated Sun Nov 13 20:35:09 CST 2022
     */
    int insertSelective(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction
     *
     * @mbggenerated Sun Nov 13 20:35:09 CST 2022
     */
    Transaction selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction
     *
     * @mbggenerated Sun Nov 13 20:35:09 CST 2022
     */
    int updateByPrimaryKeySelective(Transaction record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction
     *
     * @mbggenerated Sun Nov 13 20:35:09 CST 2022
     */
    int updateByPrimaryKey(Transaction record);

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
     * 在线索中转换中，为交易创建信息
     * @param transaction
     * @return
     */
    int insertTransaction(Transaction transaction);

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