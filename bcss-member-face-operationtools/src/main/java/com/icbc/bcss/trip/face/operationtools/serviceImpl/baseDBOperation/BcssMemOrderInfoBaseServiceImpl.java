package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemOrderInfoDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrderInfoBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("bcssMemOrderInfoBaseService")
public class BcssMemOrderInfoBaseServiceImpl implements BcssMemOrderInfoBaseService {
    @Resource
    private BcssMemOrderInfoDao bcssMemOrderInfoDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemOrderInfoDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemOrderInfoModel record) {
        return bcssMemOrderInfoDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemOrderInfoModel record) {
        return bcssMemOrderInfoDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemOrderInfoModel selectByPrimaryKey(Long seqId) {
        return bcssMemOrderInfoDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemOrderInfoModel record) {
        return bcssMemOrderInfoDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemOrderInfoModel record) {
        return bcssMemOrderInfoDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public BcssMemOrderInfoModel selectByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrderInfoDao.selectByCorpId2Trxseqno(corpId,trxseqno);
    }

    @Override
    public BcssMemOrderInfoModel selectByCorpId2MerOrderNo(String corpId, String merorderno) {
        return bcssMemOrderInfoDao.selectByCorpId2MerOrderNo(corpId,merorderno);
    }
    //按seqId升序
    @Override
    public List<BcssMemOrderInfoModel> selectByCorpId2Trxtime2DealStatus(String corpId, Date trxtimeStart, Date trxtimeEnd, String dealStatus, BigDecimal startNum, BigDecimal endNum) {
        return bcssMemOrderInfoDao.selectByCorpId2Trxtime2DealStatus(corpId,trxtimeStart,trxtimeEnd,dealStatus,startNum,endNum);
    }

    @Override
    public int selectByCorpId2Trxtime2DealStatusCount(String corpId, Date trxtimeStart, Date trxtimeEnd, String dealStatus) {
        return bcssMemOrderInfoDao.selectByCorpId2Trxtime2DealStatusCount(corpId,trxtimeStart,trxtimeEnd,dealStatus);
    }

    @Override
    public int deleteByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrderInfoDao.deleteByCorpId2Trxseqno(corpId,trxseqno);
    }


}
