package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    /**
     * 查询与本条市场活动的市场备注表
     * @param id
     * @return
     */
    List<ActivityRemark> selectActivityRemarkById(String id);

    /**
     * 添加市场活动备注
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
     * 市场活动备注修改
     * @param activityRemark
     * @return
     */
    int updateRemark(ActivityRemark activityRemark);

}
