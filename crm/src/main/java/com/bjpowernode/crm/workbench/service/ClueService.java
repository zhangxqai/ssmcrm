package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueService {

    /**
     * 这个是查询符合条件的所有列表
     * @param map
     * @return
     */
    List<Clue> selectActivityAll(Map<String,Object>map);

    /**
     * 这个是查询符合条件的有多少条数
     * @param map
     * @return
     */
    int selectClueAllCount(Map<String,Object>map);

    /**
     * 创建线索
     * @param clue
     * @return
     */
    int insertClue(Clue clue);

    /**
     * 根据传进来的id列表进行删除
     * @param ids
     * @return
     */
    int deleteClue(String[] ids);
}
