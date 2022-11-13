package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {

    /**
     * 根据关联列表中的activityId去绑定市场活动和线索
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
}
