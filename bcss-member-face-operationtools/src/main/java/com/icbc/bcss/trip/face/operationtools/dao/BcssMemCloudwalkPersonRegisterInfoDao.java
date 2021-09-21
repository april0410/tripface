package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterInfoModel;

public interface BcssMemCloudwalkPersonRegisterInfoDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkPersonRegisterInfoModel record);

    int insertSelective(BcssMemCloudwalkPersonRegisterInfoModel record);

    BcssMemCloudwalkPersonRegisterInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkPersonRegisterInfoModel record);

    int updateByPrimaryKey(BcssMemCloudwalkPersonRegisterInfoModel record);

    BcssMemCloudwalkPersonRegisterInfoModel selectByCorpID2FaceID(@Param("corpId") String corpId, @Param("faceId") String faceId) ;

    int deleteByCorpID2FaceID(@Param("corpId") String corpId, @Param("faceId") String faceId);
}