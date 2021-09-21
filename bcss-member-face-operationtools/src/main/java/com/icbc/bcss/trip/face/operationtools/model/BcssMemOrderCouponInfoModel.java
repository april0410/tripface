package com.icbc.bcss.trip.face.operationtools.model;

import java.io.Serializable;
import java.util.Date;

public class BcssMemOrderCouponInfoModel implements Serializable {
    private static final long serialVersionUID = -8698498502517358130L;
    private Long seqId;

    private String corpId;

    private String corgno;

    private String trxseqno;

    private Integer seqno;

    private String couponno;

    private Long couponamt;

    private String coupontype;

    private Long couponnum;

    private String coupondetail1;

    private String coupondetail2;

    private String backup1;

    private String backup2;

    private String backup3;

    private String backup4;

    private String setuser;

    private Date setdate;

    private String lstmuser;

    private Date lstmdate;

    public Long getSeqId() {
        return seqId;
    }

    public void setSeqId(Long seqId) {
        this.seqId = seqId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId == null ? null : corpId.trim();
    }

    public String getCorgno() {
        return corgno;
    }

    public void setCorgno(String corgno) {
        this.corgno = corgno == null ? null : corgno.trim();
    }

    public String getTrxseqno() {
        return trxseqno;
    }

    public void setTrxseqno(String trxseqno) {
        this.trxseqno = trxseqno == null ? null : trxseqno.trim();
    }

    public Integer getSeqno() {
        return seqno;
    }

    public void setSeqno(Integer seqno) {
        this.seqno = seqno;
    }

    public String getCouponno() {
        return couponno;
    }

    public void setCouponno(String couponno) {
        this.couponno = couponno == null ? null : couponno.trim();
    }

    public Long getCouponamt() {
        return couponamt;
    }

    public void setCouponamt(Long couponamt) {
        this.couponamt = couponamt;
    }

    public String getCoupontype() {
        return coupontype;
    }

    public void setCoupontype(String coupontype) {
        this.coupontype = coupontype == null ? null : coupontype.trim();
    }

    public Long getCouponnum() {
        return couponnum;
    }

    public void setCouponnum(Long couponnum) {
        this.couponnum = couponnum;
    }

    public String getCoupondetail1() {
        return coupondetail1;
    }

    public void setCoupondetail1(String coupondetail1) {
        this.coupondetail1 = coupondetail1 == null ? null : coupondetail1.trim();
    }

    public String getCoupondetail2() {
        return coupondetail2;
    }

    public void setCoupondetail2(String coupondetail2) {
        this.coupondetail2 = coupondetail2 == null ? null : coupondetail2.trim();
    }

    public String getBackup1() {
        return backup1;
    }

    public void setBackup1(String backup1) {
        this.backup1 = backup1 == null ? null : backup1.trim();
    }

    public String getBackup2() {
        return backup2;
    }

    public void setBackup2(String backup2) {
        this.backup2 = backup2 == null ? null : backup2.trim();
    }

    public String getBackup3() {
        return backup3;
    }

    public void setBackup3(String backup3) {
        this.backup3 = backup3 == null ? null : backup3.trim();
    }

    public String getBackup4() {
        return backup4;
    }

    public void setBackup4(String backup4) {
        this.backup4 = backup4 == null ? null : backup4.trim();
    }

    public String getSetuser() {
        return setuser;
    }

    public void setSetuser(String setuser) {
        this.setuser = setuser == null ? null : setuser.trim();
    }

    public Date getSetdate() {
        return setdate;
    }

    public void setSetdate(Date setdate) {
        this.setdate = setdate;
    }

    public String getLstmuser() {
        return lstmuser;
    }

    public void setLstmuser(String lstmuser) {
        this.lstmuser = lstmuser == null ? null : lstmuser.trim();
    }

    public Date getLstmdate() {
        return lstmdate;
    }

    public void setLstmdate(Date lstmdate) {
        this.lstmdate = lstmdate;
    }
}