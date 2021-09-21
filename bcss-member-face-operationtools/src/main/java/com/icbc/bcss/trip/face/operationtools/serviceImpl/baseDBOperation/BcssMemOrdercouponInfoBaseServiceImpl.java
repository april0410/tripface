package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemOrderCouponInfoDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderCouponInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrdercouponInfoBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("bcssMemOrdercouponInfoBaseService")
public class BcssMemOrdercouponInfoBaseServiceImpl implements BcssMemOrdercouponInfoBaseService {
    @Resource
    private BcssMemOrderCouponInfoDao bcssMemOrdercouponInfoDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemOrdercouponInfoDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemOrderCouponInfoModel record) {
        return bcssMemOrdercouponInfoDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemOrderCouponInfoModel record) {
        return bcssMemOrdercouponInfoDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemOrderCouponInfoModel selectByPrimaryKey(Long seqId) {
        return bcssMemOrdercouponInfoDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemOrderCouponInfoModel record) {
        return bcssMemOrdercouponInfoDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemOrderCouponInfoModel record) {
        return bcssMemOrdercouponInfoDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public List<BcssMemOrderCouponInfoModel> selectByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrdercouponInfoDao.selectByCorpId2Trxseqno(corpId,trxseqno);
    }

    @Override
    public int deleteByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrdercouponInfoDao.deleteByCorpId2Trxseqno(corpId,trxseqno);
    }

    @Override
    public BcssMemOrderCouponInfoModel selectByCorpId2Trxseqno2Seqno(String corpId, String trxseqno, int seqno) {
        return bcssMemOrdercouponInfoDao.selectByCorpId2Trxseqno2Seqno(corpId,trxseqno,seqno);
    }

    @Override
    public List<BcssMemOrderCouponInfoModel> selectByCorpId2CouponNo(String corpId, String couponno) {
        return bcssMemOrdercouponInfoDao.selectByCorpId2CouponNo(corpId,couponno);
    }


}
