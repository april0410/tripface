package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupPersonBindInfoModel;

public interface BcssMemCloudwalkGroupPersonBindInfoBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkGroupPersonBindInfoModel record);

    int insertSelective(BcssMemCloudwalkGroupPersonBindInfoModel record);

    BcssMemCloudwalkGroupPersonBindInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkGroupPersonBindInfoModel record);

    int updateByPrimaryKey(BcssMemCloudwalkGroupPersonBindInfoModel record);

    BcssMemCloudwalkGroupPersonBindInfoModel selectByCorpId2FaceId2GroupCode(String corpId,String faceId,String groupcode);

    int deleteByCorpId2faceId2Groupcode(String corpId,String faceId,String groupcode);
}
