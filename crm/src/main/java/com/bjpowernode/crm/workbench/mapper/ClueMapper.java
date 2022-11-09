package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Tue Nov 08 14:40:28 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Tue Nov 08 14:40:28 CST 2022
     */
    int insert(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Tue Nov 08 14:40:28 CST 2022
     */
    int insertSelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Tue Nov 08 14:40:28 CST 2022
     */
    Clue selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Tue Nov 08 14:40:28 CST 2022
     */
    int updateByPrimaryKeySelective(Clue record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue
     *
     * @mbggenerated Tue Nov 08 14:40:28 CST 2022
     */
    int updateByPrimaryKey(Clue record);

    /**
     * 按条件查询出所有符合条件的线索列表
     * @param map
     * @return
     */
    List<Clue> selectClueAll(Map<String,Object> map);

    /**
     * 按条件查询出所有符合条件的总条数
     * @param map
     * @return
     */
    int selectClueAllCount(Map<String,Object> map);

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