package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sat Nov 05 21:18:02 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sat Nov 05 21:18:02 CST 2022
     */
    int insert(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sat Nov 05 21:18:02 CST 2022
     */
    int insertSelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sat Nov 05 21:18:02 CST 2022
     */
    ActivityRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sat Nov 05 21:18:02 CST 2022
     */
    int updateByPrimaryKeySelective(ActivityRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_activity_remark
     *
     * @mbggenerated Sat Nov 05 21:18:02 CST 2022
     */
    int updateByPrimaryKey(ActivityRemark record);

    /**
     * 根据市场活动的id查询有关这条市场活动的备注全部备注消息
     * @param id
     * @return
     */
    List<ActivityRemark> selectActivityRemarkById(String id);

    /**
     * 添加市场活动的备注
     * @param activityRemark
     * @return
     */
    int insertRemark(ActivityRemark activityRemark);

    /**
     * 根据id对市场活动备注进行删除
     * @param id
     * @return
     */
    int deleteRemark(String id);

    /**
     * 对市场活动备注进行修改
     * @param activityRemark
     * @return
     */
    int updateRemark(ActivityRemark activityRemark);


}