package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import java.util.List;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderCouponInfoModel;

public interface BcssMemOrdercouponInfoBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderCouponInfoModel record);

    int insertSelective(BcssMemOrderCouponInfoModel record);

    BcssMemOrderCouponInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderCouponInfoModel record);

    int updateByPrimaryKey(BcssMemOrderCouponInfoModel record);

    List<BcssMemOrderCouponInfoModel> selectByCorpId2Trxseqno(String corpId, String trxseqno);

    int deleteByCorpId2Trxseqno(String corpId,String trxseqno);

    BcssMemOrderCouponInfoModel selectByCorpId2Trxseqno2Seqno(String corpId, String trxseqno, int seqno);

    List<BcssMemOrderCouponInfoModel> selectByCorpId2CouponNo(String corpId,String couponno);
}
