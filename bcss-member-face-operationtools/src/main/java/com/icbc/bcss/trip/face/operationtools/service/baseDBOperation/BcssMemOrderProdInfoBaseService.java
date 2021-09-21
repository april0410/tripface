package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import java.util.List;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderPayFaceLogModel;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderProdInfoModel;

public interface BcssMemOrderProdInfoBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderProdInfoModel record);

    int insertSelective(BcssMemOrderProdInfoModel record);

    BcssMemOrderProdInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderProdInfoModel record);

    int updateByPrimaryKey(BcssMemOrderProdInfoModel record);

    List<BcssMemOrderProdInfoModel> selectByCorpId2Trxseqno(String corpId, String trxseqno);

    int deleteByCorpId2Trxseqno(String corpId,String trxseqno);

    BcssMemOrderProdInfoModel selectByCorpId2Trxseqno2Seqno(String corpId, String trxseqno,int seqno);
}
