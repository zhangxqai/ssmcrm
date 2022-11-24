package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityService implements com.bjpowernode.crm.workbench.service.ActivityService {

    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public int insertActivity(Activity activity) {
        return activityMapper.insertActivity(activity);
    }

    @Override
    public List<Activity> selectActivityAll(Map<String, Object> map) {
        List<Activity> activityList = activityMapper.selectActivityAll(map);
        return activityList;
    }

    @Override
    public int selectActivityAllCount(Map<String, Object> map) {
        return activityMapper.selectActivityAllCount(map);
    }

    @Override
    public int deleteActivityById(String[] ids) {
        return activityMapper.deleteActivityById(ids);
    }

    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }


    @Override
    public int updateActivityById(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }

    @Override
    public List<Activity> selectAllActivityDow() {
        return activityMapper.selectAllActivityDow();
    }

    @Override
    public int insertActivityByList(List<Activity> activityList) {
        return activityMapper.insertActivityByList(activityList);
    }

    @Override
    public List<Activity> selectActivityForClueById(String id) {
        return activityMapper.selectActivityForClueById(id);
    }

    @Override
    public List<Activity> selectForClueRelationActivityByName(Map<String, Object> map) {
        return activityMapper.selectForClueRelationActivityByName(map);
    }

    @Override
    public int selectForClueRelationActivityCount(Map<String, Object> map) {
        return activityMapper.selectForClueRelationActivityCount(map);
    }

    /**
     * 根据id数组查询市场活动信息
     * @param ids
     * @return
     */
    @Override
    public List<Activity> selectForClueRelationByIds(String[] ids) {
        return activityMapper.selectForClueRelationByIds(ids);
    }

    /**
     * 为了转换线索查询市场活动，根据市场活动的名称查询符合条件的市场活动
     * @param map
     * @return
     */
    @Override
    public List<Activity> selectActivityForConversion(Map<String,Object> map) {
        return activityMapper.selectActivityForConversion(map);
    }

    /**
     * 在添加交易时，根据名称查询市场活动源
     * @param name
     * @return
     */
    @Override
    public List<Activity> selectFroSavaTranByNameActivity(String name) {
        return activityMapper.selectFroSavaTranByNameActivity(name);
    }


}
