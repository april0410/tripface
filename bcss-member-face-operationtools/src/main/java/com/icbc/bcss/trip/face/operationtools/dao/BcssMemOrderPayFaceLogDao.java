package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderInfoModel;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderPayFaceLogModel;

import java.util.List;

public interface BcssMemOrderPayFaceLogDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderPayFaceLogModel record);

    int insertSelective(BcssMemOrderPayFaceLogModel record);

    BcssMemOrderPayFaceLogModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderPayFaceLogModel record);

    int updateByPrimaryKeyWithBLOBs(BcssMemOrderPayFaceLogModel record);

    int updateByPrimaryKey(BcssMemOrderPayFaceLogModel record);

    BcssMemOrderPayFaceLogModel selectByCorpId2Trxseqno(@Param("corpId") String corpId, @Param("trxseqno") String trxseqno);

    int deleteByCorpId2Trxseqno(@Param("corpId") String corpId, @Param("trxseqno") String trxseqno);

    //二维码
    List<BcssMemOrderPayFaceLogModel> selectByCorpId2FeatureType2RecordDate(@Param("corpId") String corpId, @Param("featuretype") String featuretype, @Param("recordDate") String recordDate);
}