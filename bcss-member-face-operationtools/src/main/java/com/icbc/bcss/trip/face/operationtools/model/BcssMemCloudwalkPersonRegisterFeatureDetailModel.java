package com.icbc.bcss.trip.face.operationtools.model;

import java.io.Serializable;
import java.util.Date;

public class BcssMemCloudwalkPersonRegisterFeatureDetailModel implements Serializable {
    private static final long serialVersionUID = -8274392037686400032L;
    private Long seqId;

    private String corpId;

    private String faceId;

    private String flownum;

    private String serverRegisterInfoCode;

    private String personFeatureType;

    private String personFeatureSubType;

    private String personFeatureId;

    private String personFeatureSavePath;

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

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId == null ? null : faceId.trim();
    }

    public String getFlownum() {
        return flownum;
    }

    public void setFlownum(String flownum) {
        this.flownum = flownum == null ? null : flownum.trim();
    }

    public String getServerRegisterInfoCode() {
        return serverRegisterInfoCode;
    }

    public void setServerRegisterInfoCode(String serverRegisterInfoCode) {
        this.serverRegisterInfoCode = serverRegisterInfoCode == null ? null : serverRegisterInfoCode.trim();
    }

    public String getPersonFeatureType() {
        return personFeatureType;
    }

    public void setPersonFeatureType(String personFeatureType) {
        this.personFeatureType = personFeatureType == null ? null : personFeatureType.trim();
    }

    public String getPersonFeatureSubType() {
        return personFeatureSubType;
    }

    public void setPersonFeatureSubType(String personFeatureSubType) {
        this.personFeatureSubType = personFeatureSubType == null ? null : personFeatureSubType.trim();
    }

    public String getPersonFeatureId() {
        return personFeatureId;
    }

    public void setPersonFeatureId(String personFeatureId) {
        this.personFeatureId = personFeatureId == null ? null : personFeatureId.trim();
    }

    public String getPersonFeatureSavePath() {
        return personFeatureSavePath;
    }

    public void setPersonFeatureSavePath(String personFeatureSavePath) {
        this.personFeatureSavePath = personFeatureSavePath == null ? null : personFeatureSavePath.trim();
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