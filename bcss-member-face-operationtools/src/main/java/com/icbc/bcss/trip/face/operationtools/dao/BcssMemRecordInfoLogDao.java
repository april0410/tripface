package com.icbc.bcss.trip.face.operationtools.dao;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemRecordInfoLogModel;

public interface BcssMemRecordInfoLogDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemRecordInfoLogModel record);

    int insertSelective(BcssMemRecordInfoLogModel record);

    BcssMemRecordInfoLogModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemRecordInfoLogModel record);

    int updateByPrimaryKeyWithBLOBs(BcssMemRecordInfoLogModel record);

    int updateByPrimaryKey(BcssMemRecordInfoLogModel record);
}