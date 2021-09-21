package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderInfoModel;

public interface BcssMemOrderInfoBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemOrderInfoModel record);

    int insertSelective(BcssMemOrderInfoModel record);

    BcssMemOrderInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemOrderInfoModel record);

    int updateByPrimaryKey(BcssMemOrderInfoModel record);

    BcssMemOrderInfoModel selectByCorpId2Trxseqno(String corpId,String trxseqno);

    BcssMemOrderInfoModel selectByCorpId2MerOrderNo(String corpId,String merorderno);
    //按seqId升序
    List<BcssMemOrderInfoModel> selectByCorpId2Trxtime2DealStatus(String corpId, Date trxtimeStart, Date trxtimeEnd, String dealStatus, BigDecimal startNum, BigDecimal endNum);

    int selectByCorpId2Trxtime2DealStatusCount(String corpId, Date trxtimeStart , Date trxtimeEnd, String dealStatus);

    int deleteByCorpId2Trxseqno(String corpId,String trxseqno);
}
