package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderInfoModel;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderPayFaceLogModel;

public interface BcssMemOrderPayFaceLogBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderPayFaceLogModel record);

    int insertSelective(BcssMemOrderPayFaceLogModel record);

    BcssMemOrderPayFaceLogModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderPayFaceLogModel record);

    int updateByPrimaryKeyWithBLOBs(BcssMemOrderPayFaceLogModel record);

    int updateByPrimaryKey(BcssMemOrderPayFaceLogModel record);

    BcssMemOrderPayFaceLogModel selectByCorpId2Trxseqno(String corpId, String trxseqno);

    int deleteByCorpId2Trxseqno(String corpId,String trxseqno);

    //查询二维码是否使用
    List<BcssMemOrderPayFaceLogModel> selectByCorpId2FeatureType2RecordDate(String corpId,String featuretype,String recordDate);
}
