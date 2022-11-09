package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.mapper.ClueMapper;
import com.bjpowernode.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("clueService")
public class ClueServiceImpl implements ClueService {

    @Autowired
    private ClueMapper clueMapper;

    @Override
    public List<Clue> selectActivityAll(Map<String, Object> map) {
        //调用service方法，查询所有符合条件的列表
        List<Clue> clueList = clueMapper.selectClueAll(map);
        return clueList;
    }

    @Override
    public int selectClueAllCount(Map<String, Object> map) {
        //调用service方法，查询所有符合条件的列表条数
        int count = clueMapper.selectClueAllCount(map);
        return count;
    }

    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }
}
