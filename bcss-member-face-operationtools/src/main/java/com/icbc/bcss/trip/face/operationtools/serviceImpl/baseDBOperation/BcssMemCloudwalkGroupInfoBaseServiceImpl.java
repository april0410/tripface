package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemCloudwalkGroupInfoDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemCloudwalkGroupInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemCloudwalkGroupInfoBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bcssMemCloudwalkGroupInfoBaseService")
public class BcssMemCloudwalkGroupInfoBaseServiceImpl implements BcssMemCloudwalkGroupInfoBaseService {
    @Resource
    private BcssMemCloudwalkGroupInfoDao bcssMemCloudwalkGroupInfoDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkGroupInfoDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemCloudwalkGroupInfoModel record) {
        return bcssMemCloudwalkGroupInfoDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemCloudwalkGroupInfoModel record) {
        return bcssMemCloudwalkGroupInfoDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemCloudwalkGroupInfoModel selectByPrimaryKey(Long seqId) {
        return bcssMemCloudwalkGroupInfoDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemCloudwalkGroupInfoModel record) {
        return bcssMemCloudwalkGroupInfoDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemCloudwalkGroupInfoModel record) {
        return bcssMemCloudwalkGroupInfoDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public List<BcssMemCloudwalkGroupInfoModel> selectBycorpId(String corpId) {
        return bcssMemCloudwalkGroupInfoDao.selectBycorpId(corpId);
    }

    @Override
    public BcssMemCloudwalkGroupInfoModel selectByGroupCode(String groupcode) {
        return bcssMemCloudwalkGroupInfoDao.selectByGroupCode(groupcode);
    }

    @Override
    public int replaceByGroupcode(BcssMemCloudwalkGroupInfoModel record) {
        return bcssMemCloudwalkGroupInfoDao.replaceByGroupcode(record);
    }

}
