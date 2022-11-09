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

    /**
     * 这个是查询符合条件的所有列表
     * @param map
     * @return
     */
    @Override
    public List<Clue> selectActivityAll(Map<String, Object> map) {
        //调用service方法，查询所有符合条件的列表
        List<Clue> clueList = clueMapper.selectClueAll(map);
        return clueList;
    }

    /**
     * 这个是查询符合条件的有多少条数
     * @param map
     * @return
     */
    @Override
    public int selectClueAllCount(Map<String, Object> map) {
        //调用service方法，查询所有符合条件的列表条数
        int count = clueMapper.selectClueAllCount(map);
        return count;
    }

    /**
     * 创建线索
     * @param clue
     * @return
     */
    @Override
    public int insertClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    /**
     * 根据传进来的id列表进行删除
     * @param ids
     * @return
     */
    @Override
    public int deleteClue(String[] ids) {
        int count = clueMapper.deleteClue(ids);
        return count;
    }

    /**
     * 根据id对线索进行修改
     * @param clue
     * @return
     */
    @Override
    public int updateClueById(Clue clue) {
        return clueMapper.updateClueById(clue);
    }

    @Override
    public Clue selectByIdForEdit(String id) {
        return clueMapper.selectByIdForEdit(id);
    }


}
