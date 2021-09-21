package com.icbc.bcss.trip.face.operationtools.model;

import java.io.Serializable;
import java.util.Date;

public class BcssMemOrderProdInfoModel implements Serializable {
    private static final long serialVersionUID = -8875176719114276968L;
    private Long seqId;

    private String corpId;

    private String corgno;

    private String trxseqno;

    private Integer seqno;

    private String productid;

    private String productname;

    private Long productnum;

    private Long productamt;

    private String productdetail;

    private String producttype;

    private String groupno;

    private String distributedate;

    private String productremark;

    private String periodid;

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

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    public Long getProductnum() {
        return productnum;
    }

    public void setProductnum(Long productnum) {
        this.productnum = productnum;
    }

    public Long getProductamt() {
        return productamt;
    }

    public void setProductamt(Long productamt) {
        this.productamt = productamt;
    }

    public String getProductdetail() {
        return productdetail;
    }

    public void setProductdetail(String productdetail) {
        this.productdetail = productdetail == null ? null : productdetail.trim();
    }

    public String getProducttype() {
        return producttype;
    }

    public void setProducttype(String producttype) {
        this.producttype = producttype == null ? null : producttype.trim();
    }

    public String getGroupno() {
        return groupno;
    }

    public void setGroupno(String groupno) {
        this.groupno = groupno == null ? null : groupno.trim();
    }

    public String getDistributedate() {
        return distributedate;
    }

    public void setDistributedate(String distributedate) {
        this.distributedate = distributedate == null ? null : distributedate.trim();
    }

    public String getProductremark() {
        return productremark;
    }

    public void setProductremark(String productremark) {
        this.productremark = productremark == null ? null : productremark.trim();
    }

    public String getPeriodid() {
        return periodid;
    }

    public void setPeriodid(String periodid) {
        this.periodid = periodid == null ? null : periodid.trim();
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