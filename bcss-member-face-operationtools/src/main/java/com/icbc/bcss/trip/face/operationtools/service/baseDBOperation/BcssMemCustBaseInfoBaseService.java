package com.icbc.bcss.trip.face.operationtools.service.baseDBOperation;

import java.util.List;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCustBaseInfoModel;

public interface BcssMemCustBaseInfoBaseService {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCustBaseInfoModel record);

    int insertSelective(BcssMemCustBaseInfoModel record);

    BcssMemCustBaseInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCustBaseInfoModel record);

    int updateByPrimaryKey(BcssMemCustBaseInfoModel record);

    BcssMemCustBaseInfoModel selectByCorpId2PersonId2SeqNo(String corpId,String personId,String seqNo);

    BcssMemCustBaseInfoModel selectByFaceId(String  faceId);

    int deleteByCorpId2PersonId2SeqNo(String corpId, String personId, String seqNo) ;

    int deleteByFaceId(String faceId) ;

    BcssMemCustBaseInfoModel selectByCorpId2FaceId(String corpId,String  faceId);

    List<BcssMemCustBaseInfoModel> selectByCorpId2MemCardNo(String corpId,String  memCardNo);

}
