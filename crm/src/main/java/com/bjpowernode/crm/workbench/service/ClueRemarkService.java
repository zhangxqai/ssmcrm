package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {

    /**
     * 根据id查询出与这条线索有关的线索备注
     * @param id
     * @return
     */
    List<ClueRemark> selectClueRemarkForById(String id);
}
