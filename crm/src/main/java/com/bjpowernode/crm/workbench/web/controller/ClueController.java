package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {

    @Autowired
    private ClueService clueService;

    @RequestMapping("/workbench/clue/selectActivityAll.do")
    public @ResponseBody Object selectActivityAll(String fullname,String company,String phone,String mphone,String source,String owner,String state,int pageNo,int pageSize){

        //这里就要封装参数,将参数封装到map中
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("company",company);
        map.put("phone",phone);
        map.put("mphone",mphone);
        map.put("source",source);
        map.put("owner",owner);
        map.put("state",state);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //这里已经将数据封装好了

        //将封装数据，调用service方法，查询符合条件的列表
        List<Clue> clueList = clueService.selectActivityAll(map);
        int totalRows = clueService.selectClueAllCount(map);

        //查询出来的数据还需要将它们封装起来
        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("totalRows",totalRows);

        return retMap;
    }
}
