package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderProdInfoModel;

import java.util.List;

public interface BcssMemOrderProdInfoDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderProdInfoModel record);

    int insertSelective(BcssMemOrderProdInfoModel record);

    BcssMemOrderProdInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderProdInfoModel record);

    int updateByPrimaryKey(BcssMemOrderProdInfoModel record);

    List<BcssMemOrderProdInfoModel> selectByCorpId2Trxseqno(@Param("corpId") String corpId, @Param("trxseqno") String trxseqno);

    int deleteByCorpId2Trxseqno(@Param("corpId") String corpId , @Param("trxseqno") String trxseqno);

    BcssMemOrderProdInfoModel selectByCorpId2Trxseqno2Seqno(@Param("corpId") String corpId , @Param("trxseqno") String trxseqno, @Param("seqno") int seqno);
}