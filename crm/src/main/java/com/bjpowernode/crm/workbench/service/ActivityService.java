package com.bjpowernode.crm.workbench.service;


import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    int insertActivity(Activity activity);

    /**
     * 根据传入的数据进行查询出市场活动列表
     * @param map
     * @return
     */
    List<Activity> selectActivityAll(Map<String,Object> map);

    /**
     * 根据条件查询市场活动列表所有符合条件的市场活动列表总数
     * @param map
     * @return
     */
    int selectActivityAllCount(Map<String,Object> map);

    /**
     * 根据id数删除所有符合条件的市场活动
     * @param ids
     * @return
     */
    int deleteActivityById(String[] ids);

    Activity selectActivityById(String id);

    /**
     * 根据id对相对于的市场活动进行修改
     * @param activity
     * @return
     */
    int updateActivityById(Activity activity);

    /**
     * 导出市场活动时查询所有市场活动列表
     * @return
     */
    List<Activity> selectAllActivityDow();

    /**
     * 导入市场活动列表
     * @param activityList
     * @return
     */
    int insertActivityByList(List<Activity> activityList);

    /**
     * 为了在线索详细信息页面上的市场活动列表上体现，根据id去查
     * @param id
     * @return
     */
    List<Activity> selectActivityForClueById(String id);
}
