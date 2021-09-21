package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemCloudwalkPersonRegisterFeatureDetailDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterFeatureDetailModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkPersonRegisterFeatureDetailBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bcssMemCloudwalkPersonRegisterFeatureDetailBaseService")
public class BcssMemCloudwalkPersonRegisterFeatureDetailBaseServiceImpl implements BcssMemCloudwalkPersonRegisterFeatureDetailBaseService {
    @Resource
    private BcssMemCloudwalkPersonRegisterFeatureDetailDao bcssMemCloudwalkPersonRegisterFeatureDetailDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemCloudwalkPersonRegisterFeatureDetailModel record) {
//        return 0;
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.insert(record);
    }

    @Override
    public int insertSelective(BcssMemCloudwalkPersonRegisterFeatureDetailModel record) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemCloudwalkPersonRegisterFeatureDetailModel selectByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemCloudwalkPersonRegisterFeatureDetailModel record) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemCloudwalkPersonRegisterFeatureDetailModel record) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> selectByCorpID2FaceID(String corpId, String faceId) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.selectByCorpID2FaceID(corpId,faceId);
    }

    @Override
    public List<BcssMemCloudwalkPersonRegisterFeatureDetailModel> selectByCorpID2FaceID2PersonFeatureType(String corpId, String faceId, String personFeatureType) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.selectByCorpID2FaceID2PersonFeatureType(corpId,faceId,personFeatureType);
    }

    @Override
    public int deleteByCorpId2FaceId(String corpId, String faceId) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.deleteByCorpId2FaceId(corpId,faceId);
    }

    @Override
    public int deleteByCorpId2FaceId2Type(String corpId, String faceId, String personFeatureType) {
        return bcssMemCloudwalkPersonRegisterFeatureDetailDao.deleteByCorpId2FaceId2Type(corpId,faceId,personFeatureType);
    }
}
