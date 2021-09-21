package com.icbc.bcss.trip.face.operationtools.requestmodel;


import com.icbc.api.AbstractIcbcRequest;
import com.icbc.api.BizContent;
import com.icbc.api.internal.util.fastjson.annotation.JSONField;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;

public class BcssMemberFeatureQuerymemberinfoRequestV1 extends AbstractIcbcRequest<BcssMemberFeatureQuerymemberinfoResponseV1> {

    public Class<? extends BizContent> getBizContentClass() {
        return BcssMemberFeatureQuerymemberinfoRequestBizV1.class;
    }

    public String getMethod() {
        return "POST";
    }

    public Class<BcssMemberFeatureQuerymemberinfoResponseV1> getResponseClass() {
        return BcssMemberFeatureQuerymemberinfoResponseV1.class;
    }

    public boolean isNeedEncrypt() {
        return false;
    }

    public static class BcssMemberFeatureQuerymemberinfoRequestBizV1 implements BizContent {
        @JSONField(
                name = "timeStamp"
        )
        private String timeStamp;
        @JSONField(
                name = "clientTransNo"
        )
        private String clientTransNo;
        @JSONField(
                name = "corpId"
        )
        private String corpId;
        @JSONField(
                name = "operType"
        )
        private String operType;
        @JSONField(
                name = "featureType"
        )
        private String featureType;
        @JSONField(
                name = "startDate"
        )
        private String startDate;
        @JSONField(
                name = "endDate"
        )
        private String endDate;
        @JSONField(
                name = "channel"
        )
        private String channel;
        @JSONField(
                name = "startNum"
        )
        private String startNum;
        @JSONField(
                name = "endNum"
        )
        private String endNum;
        @JSONField(
                name = "operInfo"
        )
        private String operInfo;

        public BcssMemberFeatureQuerymemberinfoRequestBizV1() {
        }

        public String getTimeStamp() {
            return this.timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getClientTransNo() {
            return this.clientTransNo;
        }

        public void setClientTransNo(String clientTransNo) {
            this.clientTransNo = clientTransNo;
        }

        public String getCorpId() {
            return this.corpId;
        }

        public void setCorpId(String corpId) {
            this.corpId = corpId;
        }

        public String getOperType() {
            return this.operType;
        }

        public void setOperType(String operType) {
            this.operType = operType;
        }

        public String getStartDate() {
            return this.startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return this.endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getChannel() {
            return this.channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getStartNum() {
            return this.startNum;
        }

        public void setStartNum(String startNum) {
            this.startNum = startNum;
        }

        public String getEndNum() {
            return this.endNum;
        }

        public void setEndNum(String endNum) {
            this.endNum = endNum;
        }

        public String getOperInfo() {
            return this.operInfo;
        }

        public void setOperInfo(String operInfo) {
            this.operInfo = operInfo;
        }

        public String getFeatureType() {
            return this.featureType;
        }

        public void setFeatureType(String featureType) {
            this.featureType = featureType;
        }
    }
}
