package com.icbc.bcss.trip.face.operationtools.model;

import java.util.Date;

public class BcssMemOrderPayFaceLogModel {
    private Long seqId;

    private String corpId;

    private String faceId;

    private String personId;

    private String corgno;

    private String trxseqno;

    private String featuretype;

    private String featureno;

    private String featurecorgno;

    private String faceSim;

    private String factFaceSim;

    private String recordDate;

    private String recordTime;

    private String backup1;

    private String backup2;

    private String backup3;

    private String backup4;

    private String setuser;

    private Date setdate;

    private String lstmuser;

    private Date lstmdate;

    private String feature;

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

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId == null ? null : faceId.trim();
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId == null ? null : personId.trim();
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

    public String getFeaturetype() {
        return featuretype;
    }

    public void setFeaturetype(String featuretype) {
        this.featuretype = featuretype == null ? null : featuretype.trim();
    }

    public String getFeatureno() {
        return featureno;
    }

    public void setFeatureno(String featureno) {
        this.featureno = featureno == null ? null : featureno.trim();
    }

    public String getFeaturecorgno() {
        return featurecorgno;
    }

    public void setFeaturecorgno(String featurecorgno) {
        this.featurecorgno = featurecorgno == null ? null : featurecorgno.trim();
    }

    public String getFaceSim() {
        return faceSim;
    }

    public void setFaceSim(String faceSim) {
        this.faceSim = faceSim == null ? null : faceSim.trim();
    }

    public String getFactFaceSim() {
        return factFaceSim;
    }

    public void setFactFaceSim(String factFaceSim) {
        this.factFaceSim = factFaceSim == null ? null : factFaceSim.trim();
    }

    public String getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(String recordDate) {
        this.recordDate = recordDate == null ? null : recordDate.trim();
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime == null ? null : recordTime.trim();
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

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature == null ? null : feature.trim();
    }
}