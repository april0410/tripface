package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import java.util.List;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterFeatureDetailModel;

public interface BcssMemCloudwalkPersonRegisterFeatureDetailBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    int insertSelective(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    BcssMemCloudwalkPersonRegisterFeatureDetailModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    int updateByPrimaryKey(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> selectByCorpID2FaceID(String corpId,String faceId);

    List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> selectByCorpID2FaceID2PersonFeatureType(String corpId,String faceId,String personFeatureType);

    //根据corpId和faceId以及特征类型删除所有制
    int deleteByCorpId2FaceId(String corpId,String faceId);

    int deleteByCorpId2FaceId2Type(String corpId,String faceId,String personFeatureType);
}
