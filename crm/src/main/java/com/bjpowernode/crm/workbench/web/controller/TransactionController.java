package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.Transaction;
import com.bjpowernode.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping("/workbench/transaction/selectTranSanction.do")
    public @ResponseBody Object selectTranSanction(String owner, String name, String customerId, String stage, String type, String source, String contactsId, int pageNo, int pageSize){
        //收集数据，并封装到map中，
        Map<String,Object> map = new HashMap<>();

        map.put("owner",owner);
        map.put("name",name);
        map.put("customerId",customerId);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsId",contactsId);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        //封装好了之后，就要调用service方法，查询
        List<Transaction> transactionList = transactionService.selectTranSanctionAll(map);
        int totalRows = transactionService.selectTranSanctionAllCount(map);

        //查询回来的数据就封装到map中
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("transactionList",transactionList);
        retMap.put("totalRows",totalRows);

        return retMap;
    }
}
