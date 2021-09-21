package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupPersonBindInfoModel;

public interface BcssMemCloudwalkGroupPersonBindInfoDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkGroupPersonBindInfoModel record);

    int insertSelective(BcssMemCloudwalkGroupPersonBindInfoModel record);

    BcssMemCloudwalkGroupPersonBindInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkGroupPersonBindInfoModel record);

    int updateByPrimaryKey(BcssMemCloudwalkGroupPersonBindInfoModel record);

    BcssMemCloudwalkGroupPersonBindInfoModel selectByCorpId2FaceId2GroupCode(@Param("corpId") String corpId, @Param("faceId") String faceId, @Param("groupcode") String groupcode);

    int deleteByCorpId2faceId2Groupcode(@Param("corpId") String corpId, @Param("faceId") String faceId, @Param("groupcode") String groupcode);
}