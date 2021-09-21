package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupInfoModel;

import java.util.List;

public interface BcssMemCloudwalkGroupInfoBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCloudwalkGroupInfoModel record);

    int insertSelective(BcssMemCloudwalkGroupInfoModel record);

    BcssMemCloudwalkGroupInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCloudwalkGroupInfoModel record);

    int updateByPrimaryKey(BcssMemCloudwalkGroupInfoModel record);

    List<BcssMemCloudwalkGroupInfoModel> selectBycorpId(String corpId);

    BcssMemCloudwalkGroupInfoModel selectByGroupCode(String groupcode);

    int replaceByGroupcode(BcssMemCloudwalkGroupInfoModel record);
}
