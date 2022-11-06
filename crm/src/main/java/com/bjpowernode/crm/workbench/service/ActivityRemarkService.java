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
}
