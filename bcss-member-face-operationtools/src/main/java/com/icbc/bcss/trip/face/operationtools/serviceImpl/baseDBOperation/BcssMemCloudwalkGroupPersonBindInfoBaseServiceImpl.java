package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemCloudwalkGroupPersonBindInfoDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupPersonBindInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkGroupPersonBindInfoBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/*
* 基础服务
* */
@Service("bcssMemCloudwalkGroupPersonBindInfoBaseService")
public class BcssMemCloudwalkGroupPersonBindInfoBaseServiceImpl implements BcssMemCloudwalkGroupPersonBindInfoBaseService {
    @Resource
    private BcssMemCloudwalkGroupPersonBindInfoDao bcssMemCloudwalkGroupPersonBindInfoDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemCloudwalkGroupPersonBindInfoModel record) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemCloudwalkGroupPersonBindInfoModel record) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemCloudwalkGroupPersonBindInfoModel selectByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemCloudwalkGroupPersonBindInfoModel record) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemCloudwalkGroupPersonBindInfoModel record) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public BcssMemCloudwalkGroupPersonBindInfoModel selectByCorpId2FaceId2GroupCode(String corpId, String faceId, String groupcode) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.selectByCorpId2FaceId2GroupCode(corpId,faceId,groupcode);
    }

    @Override
    public int deleteByCorpId2faceId2Groupcode(String corpId, String faceId, String groupcode) {
        return bcssMemCloudwalkGroupPersonBindInfoDao.deleteByCorpId2faceId2Groupcode(corpId,faceId,groupcode);
    }
}
