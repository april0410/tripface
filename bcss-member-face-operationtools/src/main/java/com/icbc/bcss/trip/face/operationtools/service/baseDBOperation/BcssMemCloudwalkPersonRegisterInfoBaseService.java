package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterInfoModel;

public interface BcssMemCloudwalkPersonRegisterInfoBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkPersonRegisterInfoModel record);

    int insertSelective(BcssMemCloudwalkPersonRegisterInfoModel record);

    BcssMemCloudwalkPersonRegisterInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkPersonRegisterInfoModel record);

    int updateByPrimaryKey(BcssMemCloudwalkPersonRegisterInfoModel record);

    BcssMemCloudwalkPersonRegisterInfoModel selectByCorpID2FaceID(String corpId,String faceId);

    int deleteByCorpID2FaceID(String corpId,String faceId);
}
