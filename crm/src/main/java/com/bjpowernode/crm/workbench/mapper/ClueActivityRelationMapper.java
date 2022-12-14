package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sun Nov 13 12:31:53 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sun Nov 13 12:31:53 CST 2022
     */
    int insert(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sun Nov 13 12:31:53 CST 2022
     */
    int insertSelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sun Nov 13 12:31:53 CST 2022
     */
    ClueActivityRelation selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sun Nov 13 12:31:53 CST 2022
     */
    int updateByPrimaryKeySelective(ClueActivityRelation record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_clue_activity_relation
     *
     * @mbggenerated Sun Nov 13 12:31:53 CST 2022
     */
    int updateByPrimaryKey(ClueActivityRelation record);

    /**
     *根据关联列表中的activityId去绑定市场活动和线索
     * @param clueActivityRelationList
     * @return
     */
    int insertClueActivityRelation(List<ClueActivityRelation> clueActivityRelationList);

    /**
     * 根据id解除市场活动和线索的关联
     * @param clueActivityRelation
     * @return
     */
    int deleteClueActivityRelationById(ClueActivityRelation clueActivityRelation);

    /**
     * 根据clueID查询相关连的数据
     * @param clueId
     * @return
     */
    List<ClueActivityRelation> selectClueActivityRemarkForContacts(String clueId);

    /**
     * 根据clueId对市场活动关联表进行删除
     * @param clueId
     * @return
     */
    int deleteClueActivityRelationForConversionByClueId(String clueId);

}