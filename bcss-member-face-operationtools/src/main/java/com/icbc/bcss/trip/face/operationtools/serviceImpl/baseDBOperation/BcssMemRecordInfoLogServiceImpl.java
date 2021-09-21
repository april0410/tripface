package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemRecordInfoLogDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemRecordInfoLogModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemRecordInfoLogBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("bcssMemRecordInfoLogBaseService")
public class BcssMemRecordInfoLogServiceImpl implements BcssMemRecordInfoLogBaseService {
    @Resource
    private BcssMemRecordInfoLogDao bcssMemRecordInfoLogDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemRecordInfoLogDao.deleteByPrimaryKey(seqId);
    }

    @Override
    public int insert(BcssMemRecordInfoLogModel record) {
        return bcssMemRecordInfoLogDao.insert(record);
    }

    @Override
    public int insertSelective(BcssMemRecordInfoLogModel record) {
        return bcssMemRecordInfoLogDao.insertSelective(record);
    }

    @Override
    public BcssMemRecordInfoLogModel selectByPrimaryKey(Long seqId) {
        return bcssMemRecordInfoLogDao.selectByPrimaryKey(seqId);
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemRecordInfoLogModel record) {
        return bcssMemRecordInfoLogDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKeyWithBLOBs(BcssMemRecordInfoLogModel record) {
        return bcssMemRecordInfoLogDao.updateByPrimaryKeyWithBLOBs(record);
    }

    @Override
    public int updateByPrimaryKey(BcssMemRecordInfoLogModel record) {
        return bcssMemRecordInfoLogDao.updateByPrimaryKey(record);
    }
}
