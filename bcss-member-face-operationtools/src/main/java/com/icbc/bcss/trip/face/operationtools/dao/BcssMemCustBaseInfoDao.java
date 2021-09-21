package com.icbc.bcss.trip.face.operationtools.dao;

import org.apache.ibatis.annotations.Param;

import com.icbc.bcss.trip.face.operationtools.model.BcssMemCustBaseInfoModel;

import java.util.List;

public interface BcssMemCustBaseInfoDao {
    int deleteByPrimaryKey(Long seqId);

    int insert(BcssMemCustBaseInfoModel record);

    int insertSelective(BcssMemCustBaseInfoModel record);

    BcssMemCustBaseInfoModel selectByPrimaryKey(Long seqId);

    int updateByPrimaryKeySelective(BcssMemCustBaseInfoModel record);

    int updateByPrimaryKey(BcssMemCustBaseInfoModel record);

    BcssMemCustBaseInfoModel selectByCorpId2PersonId2SeqNo(@Param("corpId")String corpId, @Param("personId") String personId, @Param("seqNo")String seqNo);

    BcssMemCustBaseInfoModel selectByFaceId(@Param("faceId") String faceId);

    int deleteByCorpId2PersonId2SeqNo(@Param("corpId")String corpId, @Param("personId") String personId, @Param("seqNo")String seqNo) ;

    int deleteByFaceId(@Param("faceId") String faceId) ;


    BcssMemCustBaseInfoModel selectByCorpId2FaceId(@Param("corpId")String corpId,@Param("faceId") String faceId);

    List<BcssMemCustBaseInfoModel> selectByCorpId2MemCardNo(@Param("corpId")String corpId, @Param("memcardno")String memcardno);

}