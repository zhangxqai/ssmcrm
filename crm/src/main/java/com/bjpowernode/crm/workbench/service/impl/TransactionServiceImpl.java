package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateUtils;
import com.bjpowernode.crm.commons.utils.UUIDUtils;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service("transactionService")
public class TransactionServiceImpl implements TransactionService {

    //调用mapper方法，要注入
    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public List<Transaction> selectTranSanctionAll(Map<String, Object> map) {
        return transactionMapper.selectTranSanctionAll(map);
    }

    @Override
    public int selectTranSanctionAllCount(Map<String, Object> map) {
        return transactionMapper.selectTranSanctionAllCount(map);
    }

    /**
     * 在交易模块中添加一个交易信息
     * @param map
     * @return
     */
    @Override
    public void insertTransactionOne(Map<String, Object> map) {

        //在添加之前先获取参数，看看有没有需要先处理的业务
        //添加参数之前需要，查看一下客户在数据库中有没有存在，没有就需要先添加客户
        //先将客户的名称信息拿到，在到数据库中查询
        String customerName = (String) map.get("customerName");
        User user = (User) map.get(Contants.SESSION_USER);

        //查询有没有相对于的客户，如果没有就要新创建客户
        Customer customer = customerMapper.selectCustomerByIdOne(customerName);

        //判断有没有数据
        if (customer == null){
            //如果是空的就说明没有这个，就要新创建一个客户
            //创建之前需要封装数据
            customer = new Customer();
            customer.setId(UUIDUtils.getUUID());
            customer.setOwner(user.getId());
            customer.setName(customerName);
            customer.setCreateBy(user.getId());
            customer.setCreateTime(DateUtils.formateDateTime(new Date()));

            //这里只是才创建一个客户，不需要太多数据，传进来的数据只是创建交易的数据，而不是客户的数据
            //到这里就调用mapper层方法创建一个客户
            customerMapper.insertCustomer(customer);

        }

        //创建好客户之后就要创建交易了，创建实体类
        Transaction transaction = new Transaction();
        transaction.setId(UUIDUtils.getUUID());
        transaction.setName((String) map.get("name"));
        transaction.setOwner(user.getId());
        transaction.setMoney((String) map.get("money"));
        transaction.setExpectedDate((String) map.get("expectedDate"));
        transaction.setCustomerId(customer.getId());
        transaction.setStage((String) map.get("stage"));
        transaction.setType((String) map.get("type"));
        transaction.setActivityId((String) map.get("activityId"));
        transaction.setContactsId((String) map.get("contactsId"));
        transaction.setCreateBy(user.getId());
        transaction.setCreateTime(DateUtils.formateDateTime(new Date()));
        transaction.setDescription((String) map.get("description"));
        transaction.setContactSummary((String) map.get("contactSummary"));
        transaction.setNextContactTime((String) map.get("nextContactTime"));

        //封装好了之后就要调用mapper层，创建一个交易
        transactionMapper.insertTransaction(transaction);

        //创建好了之后纪要创建一个交易历史记录
        //创建之前需要创建一个实体类
        TransactionHistory transactionHistory = new TransactionHistory();
        transactionHistory.setId(UUIDUtils.getUUID());
        transactionHistory.setStage(transaction.getStage());
        transactionHistory.setExpectedDate(transaction.getExpectedDate());
        transactionHistory.setMoney(transaction.getMoney());
        transactionHistory.setCreateBy(user.getId());
        transactionHistory.setCreateTime(DateUtils.formateDateTime(new Date()));
        transactionHistory.setTranId(transaction.getId());

        //封装好了之后就要调用mapper层方法，创建交易历史记录
        transactionHistoryMapper.insertTransactionHistory(transactionHistory);

    }

    /**
     * 看交易详情信息，根据id查询这个信息
     * @param id
     * @return
     */
    @Override
    public Transaction selectForDetailById(String id) {
        return transactionMapper.selectForDetailById(id);
    }

    /**
     * 查询交易表中的各个阶段的交易量
     * @return
     */
    @Override
    public List<FunnelVo> selectCountOfTranGroupByStage() {
        return transactionMapper.selectCountOfTranGroupByStage();
    }
}
