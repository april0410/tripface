package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderInfoModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BcssMemOrderInfoDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderInfoModel record);

    int insertSelective(BcssMemOrderInfoModel record);

    BcssMemOrderInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderInfoModel record);

    int updateByPrimaryKey(BcssMemOrderInfoModel record);

    BcssMemOrderInfoModel selectByCorpId2Trxseqno(@Param("corpId") String corpId, @Param("trxseqno") String trxseqno) ;

    BcssMemOrderInfoModel selectByCorpId2MerOrderNo(@Param("corpId") String corpId,@Param("merorderno") String merorderno) ;

    //按seqId升序
    List<BcssMemOrderInfoModel> selectByCorpId2Trxtime2DealStatus(@Param("corpId")String corpId,  @Param("trxtimeStart") Date trxtimeStart,@Param("trxtimeEnd") Date trxtimeEnd,  @Param("dealStatus") String dealStatus, @Param("startNum")  BigDecimal startNum, @Param("endNum")  BigDecimal endNum);

    int selectByCorpId2Trxtime2DealStatusCount(@Param("corpId")String corpId, @Param("trxtimeStart") Date trxtimeStart,@Param("trxtimeEnd") Date trxtimeEnd , @Param("dealStatus")  String dealStatus);

    int deleteByCorpId2Trxseqno(@Param("corpId")String corpId,@Param("trxseqno")String trxseqno);
}