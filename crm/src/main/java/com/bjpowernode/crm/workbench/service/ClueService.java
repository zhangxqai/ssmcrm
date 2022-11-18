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

    /**
     * 根据id对线索进行修改
     * @param clue
     * @return
     */
    int updateClueById(Clue clue);

    /**
     * 根据id查询相对于的线索信息，为了修改模态窗口
     * @param id
     * @return
     */
    Clue selectByIdForEdit(String id);

    /**
     * 跳转线索详细信息表
     * @param id
     * @return
     */
    Clue selectForEditById(String id);

    /**
     * 根据id为转换查询线索信息
     * @param id
     * @return
     */
    Clue selectForConVersionById(String id);

    /**
     * 根据封装好的参数去查询相对应的线索，然后添加客户和联系人
     * @param map
     */
    void insertCustomerAndContacts(Map<String,Object> map);
}
