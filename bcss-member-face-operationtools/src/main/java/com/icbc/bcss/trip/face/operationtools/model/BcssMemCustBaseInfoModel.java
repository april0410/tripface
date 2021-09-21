package com.icbc.bcss.trip.face.operationtools.model;

import java.io.Serializable;
import java.util.Date;

public class BcssMemCustBaseInfoModel implements Serializable {
    private static final long serialVersionUID = 8997422031410019637L;
    private Long seqId;

    private String faceId;

    private String corpId;

    private String personId;

    private String seqno;

    private String faceIndustry;

    private String memcardno;

    private String memname;

    private String memno;

    private String memprodno;

    private String memproddesc;

    private Byte memCardstatus;

    private String memdetail1;

    private String memdetail2;

    private String memdetail3;

    private String dataSource;

    private Date cloudFaceCreatdate;

    private Date cloudFaceModifieddate;

    private String serverRegisterInfoCode;

    private String backup1;

    private String backup2;

    private String backup3;

    private String backup4;

    private String backup5;

    private String backup6;

    private String backup7;

    private String backup8;

    private String backup9;

    private String backup10;

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

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId == null ? null : faceId.trim();
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId == null ? null : corpId.trim();
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId == null ? null : personId.trim();
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno == null ? null : seqno.trim();
    }

    public String getFaceIndustry() {
        return faceIndustry;
    }

    public void setFaceIndustry(String faceIndustry) {
        this.faceIndustry = faceIndustry == null ? null : faceIndustry.trim();
    }

    public String getMemcardno() {
        return memcardno;
    }

    public void setMemcardno(String memcardno) {
        this.memcardno = memcardno == null ? null : memcardno.trim();
    }

    public String getMemname() {
        return memname;
    }

    public void setMemname(String memname) {
        this.memname = memname == null ? null : memname.trim();
    }

    public String getMemno() {
        return memno;
    }

    public void setMemno(String memno) {
        this.memno = memno == null ? null : memno.trim();
    }

    public String getMemprodno() {
        return memprodno;
    }

    public void setMemprodno(String memprodno) {
        this.memprodno = memprodno == null ? null : memprodno.trim();
    }

    public String getMemproddesc() {
        return memproddesc;
    }

    public void setMemproddesc(String memproddesc) {
        this.memproddesc = memproddesc == null ? null : memproddesc.trim();
    }

    public Byte getMemCardstatus() {
        return memCardstatus;
    }

    public void setMemCardstatus(Byte memCardstatus) {
        this.memCardstatus = memCardstatus;
    }

    public String getMemdetail1() {
        return memdetail1;
    }

    public void setMemdetail1(String memdetail1) {
        this.memdetail1 = memdetail1 == null ? null : memdetail1.trim();
    }

    public String getMemdetail2() {
        return memdetail2;
    }

    public void setMemdetail2(String memdetail2) {
        this.memdetail2 = memdetail2 == null ? null : memdetail2.trim();
    }

    public String getMemdetail3() {
        return memdetail3;
    }

    public void setMemdetail3(String memdetail3) {
        this.memdetail3 = memdetail3 == null ? null : memdetail3.trim();
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public Date getCloudFaceCreatdate() {
        return cloudFaceCreatdate;
    }

    public void setCloudFaceCreatdate(Date cloudFaceCreatdate) {
        this.cloudFaceCreatdate = cloudFaceCreatdate;
    }

    public Date getCloudFaceModifieddate() {
        return cloudFaceModifieddate;
    }

    public void setCloudFaceModifieddate(Date cloudFaceModifieddate) {
        this.cloudFaceModifieddate = cloudFaceModifieddate;
    }

    public String getServerRegisterInfoCode() {
        return serverRegisterInfoCode;
    }

    public void setServerRegisterInfoCode(String serverRegisterInfoCode) {
        this.serverRegisterInfoCode = serverRegisterInfoCode == null ? null : serverRegisterInfoCode.trim();
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

    public String getBackup5() {
        return backup5;
    }

    public void setBackup5(String backup5) {
        this.backup5 = backup5 == null ? null : backup5.trim();
    }

    public String getBackup6() {
        return backup6;
    }

    public void setBackup6(String backup6) {
        this.backup6 = backup6 == null ? null : backup6.trim();
    }

    public String getBackup7() {
        return backup7;
    }

    public void setBackup7(String backup7) {
        this.backup7 = backup7 == null ? null : backup7.trim();
    }

    public String getBackup8() {
        return backup8;
    }

    public void setBackup8(String backup8) {
        this.backup8 = backup8 == null ? null : backup8.trim();
    }

    public String getBackup9() {
        return backup9;
    }

    public void setBackup9(String backup9) {
        this.backup9 = backup9 == null ? null : backup9.trim();
    }

    public String getBackup10() {
        return backup10;
    }

    public void setBackup10(String backup10) {
        this.backup10 = backup10 == null ? null : backup10.trim();
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