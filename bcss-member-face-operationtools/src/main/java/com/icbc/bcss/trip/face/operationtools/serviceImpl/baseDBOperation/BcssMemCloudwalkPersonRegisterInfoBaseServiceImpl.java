package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemCloudwalkPersonRegisterInfoDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkPersonRegisterInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkPersonRegisterInfoBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("bcssMemCloudwalkPersonRegisterInfoBaseService")
public class BcssMemCloudwalkPersonRegisterInfoBaseServiceImpl implements BcssMemCloudwalkPersonRegisterInfoBaseService {
    @Resource
    private BcssMemCloudwalkPersonRegisterInfoDao bcssMemCloudwalkPersonRegisterInfoDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkPersonRegisterInfoDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemCloudwalkPersonRegisterInfoModel record) {
        return bcssMemCloudwalkPersonRegisterInfoDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemCloudwalkPersonRegisterInfoModel record) {
        return bcssMemCloudwalkPersonRegisterInfoDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemCloudwalkPersonRegisterInfoModel selectByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkPersonRegisterInfoDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemCloudwalkPersonRegisterInfoModel record) {
        return bcssMemCloudwalkPersonRegisterInfoDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemCloudwalkPersonRegisterInfoModel record) {
        return bcssMemCloudwalkPersonRegisterInfoDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public BcssMemCloudwalkPersonRegisterInfoModel selectByCorpID2FaceID(String corpId, String faceId) {
        return bcssMemCloudwalkPersonRegisterInfoDao.selectByCorpID2FaceID(corpId,faceId);
    }

    @Override
    public int deleteByCorpID2FaceID(String corpId, String faceId) {
        return bcssMemCloudwalkPersonRegisterInfoDao.deleteByCorpID2FaceID(corpId,faceId);
    }
}
