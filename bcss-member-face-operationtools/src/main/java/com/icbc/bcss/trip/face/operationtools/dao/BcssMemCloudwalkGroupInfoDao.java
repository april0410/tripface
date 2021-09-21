package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupInfoModel;

import java.util.List;

public interface BcssMemCloudwalkGroupInfoDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkGroupInfoModel record);

    int insertSelective(BcssMemCloudwalkGroupInfoModel record);

    BcssMemCloudwalkGroupInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkGroupInfoModel record);

    int updateByPrimaryKey(BcssMemCloudwalkGroupInfoModel record);

    List<BcssMemCloudwalkGroupInfoModel> selectBycorpId(@Param("corpId") String corpId);

    BcssMemCloudwalkGroupInfoModel selectByGroupCode(@Param("groupcode") String groupcode);

    int replaceByGroupcode(BcssMemCloudwalkGroupInfoModel record);
}