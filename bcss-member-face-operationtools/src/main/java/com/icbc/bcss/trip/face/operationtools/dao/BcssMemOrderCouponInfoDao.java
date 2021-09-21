package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderCouponInfoModel;

import java.util.List;

public interface BcssMemOrderCouponInfoDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderCouponInfoModel record);

    int insertSelective(BcssMemOrderCouponInfoModel record);

    BcssMemOrderCouponInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderCouponInfoModel record);

    int updateByPrimaryKey(BcssMemOrderCouponInfoModel record);

    List<BcssMemOrderCouponInfoModel> selectByCorpId2Trxseqno(@Param("corpId") String corpId, @Param("trxseqno") String trxseqno);

    int deleteByCorpId2Trxseqno(@Param("corpId") String corpId , @Param("trxseqno") String trxseqno);

    BcssMemOrderCouponInfoModel selectByCorpId2Trxseqno2Seqno(@Param("corpId") String corpId, @Param("trxseqno") String trxseqno , @Param("seqno") int seqno);

    List<BcssMemOrderCouponInfoModel> selectByCorpId2CouponNo(@Param("corpId") String corpId, @Param("couponno") String couponno) ;
}