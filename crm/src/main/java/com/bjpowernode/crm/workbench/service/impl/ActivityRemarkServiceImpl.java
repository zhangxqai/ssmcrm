package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.domain.ReturnObject;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.mapper.ActivityRemarkMapper;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {

    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    /**
     * 根据市场活动的id查询有关这条市场活动的备注全部备注消息
     * @param id
     * @return
     */
    @Override
    public List<ActivityRemark> selectActivityRemarkById(String id) {
        //调用mapper层
        List<ActivityRemark> activityRemarkList = activityRemarkMapper.selectActivityRemarkById(id);
        return activityRemarkList;
    }

    /**
     * 添加市场活动备注
     * @param activityRemark
     * @return
     */
    @Override
    public int insertRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.insertRemark(activityRemark);
    }

    /**
     * 根据id对市场活动备注进行删除
     * @param id
     * @return
     */
    @Override
    public int deleteRemark(String id) {

        return activityRemarkMapper.deleteRemark(id);
    }

    @Override
    public int updateRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.updateRemark(activityRemark);
    }


}
