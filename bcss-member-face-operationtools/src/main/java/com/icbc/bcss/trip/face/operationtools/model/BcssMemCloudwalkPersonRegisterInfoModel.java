package com.icbc.bcss.trip.face.operationtools.model;

import java.io.Serializable;
import java.util.Date;

public class BcssMemCloudwalkPersonRegisterInfoModel implements Serializable {
    private static final long serialVersionUID = -1036707743816364419L;
    private Long seqId;

    private String corpId;

    private String faceId;

    private String applicationId;

    private String businessId;

    private String flownum;

    private String serverRegisterInfoCode;

    private String channelCode;

    private String serviceModelVersion;

    private String orgcode;

    private String peopletype;

    private String viplevel;

    private String certtype;

    private String certno;

    private String name;

    private String sex;

    private String birthday;

    private String duty;

    private String position;

    private String telephone;

    private String email;

    private String address;

    private String remark;

    private String province;

    private String city;

    private String area;

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

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId == null ? null : applicationId.trim();
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId == null ? null : businessId.trim();
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

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode == null ? null : channelCode.trim();
    }

    public String getServiceModelVersion() {
        return serviceModelVersion;
    }

    public void setServiceModelVersion(String serviceModelVersion) {
        this.serviceModelVersion = serviceModelVersion == null ? null : serviceModelVersion.trim();
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode == null ? null : orgcode.trim();
    }

    public String getPeopletype() {
        return peopletype;
    }

    public void setPeopletype(String peopletype) {
        this.peopletype = peopletype == null ? null : peopletype.trim();
    }

    public String getViplevel() {
        return viplevel;
    }

    public void setViplevel(String viplevel) {
        this.viplevel = viplevel == null ? null : viplevel.trim();
    }

    public String getCerttype() {
        return certtype;
    }

    public void setCerttype(String certtype) {
        this.certtype = certtype == null ? null : certtype.trim();
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno == null ? null : certno.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? null : birthday.trim();
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty == null ? null : duty.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
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