package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemOrderPayFaceLogDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderPayFaceLogModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrderPayFaceLogBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bcssMemOrderPayFaceLogBaseService")
public class BcssMemOrderPayFaceLogBaseServiceImpl implements BcssMemOrderPayFaceLogBaseService {
    @Resource
    private BcssMemOrderPayFaceLogDao bcssMemOrderPayFaceLogDao;


    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemOrderPayFaceLogDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemOrderPayFaceLogModel record) {
        return bcssMemOrderPayFaceLogDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemOrderPayFaceLogModel record) {
        return bcssMemOrderPayFaceLogDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemOrderPayFaceLogModel selectByPrimaryKey(Long seqId) {
        return bcssMemOrderPayFaceLogDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemOrderPayFaceLogModel record) {
        return bcssMemOrderPayFaceLogDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(BcssMemOrderPayFaceLogModel record) {
        return bcssMemOrderPayFaceLogDao.updateByPrimaryKeyWithBLOBs(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemOrderPayFaceLogModel record) {
        return bcssMemOrderPayFaceLogDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public BcssMemOrderPayFaceLogModel selectByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrderPayFaceLogDao.selectByCorpId2Trxseqno(corpId,trxseqno);
    }

    @Override
    public int deleteByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrderPayFaceLogDao.deleteByCorpId2Trxseqno(corpId,trxseqno);
    }

    @Override
    public List<BcssMemOrderPayFaceLogModel> selectByCorpId2FeatureType2RecordDate(String corpId, String featuretype, String recordDate) {
        return null;
    }


}
