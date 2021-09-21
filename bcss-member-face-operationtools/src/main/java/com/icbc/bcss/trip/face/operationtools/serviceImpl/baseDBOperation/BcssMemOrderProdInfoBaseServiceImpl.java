package com.icbc.bcss.trip.face.operationtools.serviceImpl.baseDBOperation;

import com.icbc.bcss.trip.face.operationtools.dao.BcssMemOrderProdInfoDao;
import com.icbc.bcss.trip.face.operationtools.model.BcssMemOrderProdInfoModel;
import com.icbc.bcss.trip.face.operationtools.service.baseDBOperation.BcssMemOrderProdInfoBaseService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("bcssMemOrderProdInfoBaseService")
public class BcssMemOrderProdInfoBaseServiceImpl implements BcssMemOrderProdInfoBaseService {
    @Resource
    private BcssMemOrderProdInfoDao bcssMemOrderProdInfoDao;

    @Override
    public int deleteByPrimaryKey(Long seqId) {
        return bcssMemOrderProdInfoDao.deleteByPrimaryKey(seqId);
//        return 0;
    }

    @Override
    public int insert(BcssMemOrderProdInfoModel record) {
        return bcssMemOrderProdInfoDao.insert(record);
//        return 0;
    }

    @Override
    public int insertSelective(BcssMemOrderProdInfoModel record) {
        return  bcssMemOrderProdInfoDao.insertSelective(record);
//        return 0;
    }

    @Override
    public BcssMemOrderProdInfoModel selectByPrimaryKey(Long seqId) {
        return bcssMemOrderProdInfoDao.selectByPrimaryKey(seqId);
//        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(BcssMemOrderProdInfoModel record) {
        return bcssMemOrderProdInfoDao.updateByPrimaryKeySelective(record);
//        return 0;
    }

    @Override
    public int updateByPrimaryKey(BcssMemOrderProdInfoModel record) {
        return bcssMemOrderProdInfoDao.updateByPrimaryKey(record);
//        return 0;
    }

    @Override
    public List<BcssMemOrderProdInfoModel> selectByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrderProdInfoDao.selectByCorpId2Trxseqno(corpId,trxseqno);
    }

    @Override
    public int deleteByCorpId2Trxseqno(String corpId, String trxseqno) {
        return bcssMemOrderProdInfoDao.deleteByCorpId2Trxseqno(corpId,trxseqno);
    }

    @Override
    public BcssMemOrderProdInfoModel selectByCorpId2Trxseqno2Seqno(String corpId, String trxseqno, int seqno) {
        return bcssMemOrderProdInfoDao.selectByCorpId2Trxseqno2Seqno(corpId,trxseqno,seqno);
    }

}
