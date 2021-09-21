package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterFeatureDetailModel;

import java.util.List;

public interface BcssMemCloudwalkPersonRegisterFeatureDetailDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    int insertSelective(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    BcssMemCloudwalkPersonRegisterFeatureDetailModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    int updateByPrimaryKey(BcssMemCloudwalkPersonRegisterFeatureDetailModel record);

    List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> selectByCorpID2FaceID(@Param("corpId") String corpId, @Param("faceId") String faceId);

    List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> selectByCorpID2FaceID2PersonFeatureType(@Param("corpId") String corpId, @Param("faceId") String faceId,@Param("personFeatureType") String personFeatureType);

    int deleteByCorpId2FaceId(@Param("corpId") String corpId, @Param("faceId") String faceId);

    int deleteByCorpId2FaceId2Type(@Param("corpId") String corpId, @Param("faceId") String faceId,@Param("personFeatureType") String personFeatureType);
}