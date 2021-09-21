package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemCustBaseInfoDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCustBaseInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCustBaseInfoBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bcssMemCustBaseInfoBaseService")
public class BcssMemCustBaseInfoBaseServiceImpl implements BcssMemCustBaseInfoBaseService {
    @Resource
    private BcssMemCustBaseInfoDao bcssMemCustBaseInfoDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemCustBaseInfoDao.deleteByPrimaryKey(seqId);
        //        return 0;
    }

    @Override
    public int insert(BcssMemCustBaseInfoModel record) {
        return bcssMemCustBaseInfoDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemCustBaseInfoModel record) {
        return bcssMemCustBaseInfoDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemCustBaseInfoModel selectByPrimaryKey(Long seqId) {
//        return null;
        return  bcssMemCustBaseInfoDao.selectByPrimaryKey(seqId);
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemCustBaseInfoModel record) {
        return bcssMemCustBaseInfoDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemCustBaseInfoModel record) {
        return bcssMemCustBaseInfoDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public BcssMemCustBaseInfoModel selectByCorpId2PersonId2SeqNo(String corpId, String personId, String seqNo) {
        return bcssMemCustBaseInfoDao.selectByCorpId2PersonId2SeqNo(corpId,personId,seqNo);
    }

    @Override
    public BcssMemCustBaseInfoModel selectByFaceId(String faceId) {
        return bcssMemCustBaseInfoDao.selectByFaceId(faceId);
    }

    @Override
    public int deleteByCorpId2PersonId2SeqNo(String corpId, String personId, String seqNo) {
        return bcssMemCustBaseInfoDao.deleteByCorpId2PersonId2SeqNo(corpId,personId,seqNo);
    }

    @Override
    public int deleteByFaceId(String faceId) {
        return bcssMemCustBaseInfoDao.deleteByFaceId(faceId);
    }

    @Override
    public BcssMemCustBaseInfoModel selectByCorpId2FaceId(String corpId, String faceId) {
        return bcssMemCustBaseInfoDao.selectByCorpId2FaceId(corpId,faceId);
    }

    @Override
    public List<BcssMemCustBaseInfoModel> selectByCorpId2MemCardNo(String corpId, String memCardNo) {
        return bcssMemCustBaseInfoDao.selectByCorpId2MemCardNo(corpId,memCardNo);
    }


}
