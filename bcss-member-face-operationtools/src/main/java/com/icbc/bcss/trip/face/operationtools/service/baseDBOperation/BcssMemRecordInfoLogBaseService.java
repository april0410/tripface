package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemRecordInfoLogModel;

public interface BcssMemRecordInfoLogBaseService {

    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemRecordInfoLogModel record);

    int insertSelective(BcssMemRecordInfoLogModel record);

    BcssMemRecordInfoLogModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemRecordInfoLogModel record);

    int updateByPrimaryKeyWithBLOBs(BcssMemRecordInfoLogModel record);

    int updateByPrimaryKey(BcssMemRecordInfoLogModel record);
}
