package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.mapper.ClueRemarkMapper;
import com.bjpowernode.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("clueRemarkService")
public class ClueRemarkByIdImpl implements ClueRemarkService {

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;

    /**
     * 根据id查询出与这条线索有关的线索备注
     * @param id
     * @return
     */
    @Override
    public List<ClueRemark> selectClueRemarkForById(String id) {
        return clueRemarkMapper.selectClueRemarkForById(id);
    }

}
