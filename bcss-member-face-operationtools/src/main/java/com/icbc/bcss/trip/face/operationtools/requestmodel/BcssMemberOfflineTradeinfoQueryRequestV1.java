package com.icbc.bcss.trip.face.operationtools.requestmodel;

import com.icbc.api.AbstractIcbcRequest;
import com.icbc.api.BizContent;
import com.icbc.api.internal.util.fastjson.annotation.JSONField;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberOfflineTradeinfoQueryResponseV1;

import java.io.Serializable;
import java.util.List;

public class BcssMemberOfflineTradeinfoQueryRequestV1 extends AbstractIcbcRequest<BcssMemberOfflineTradeinfoQueryResponseV1> {
    public Class<BcssMemberOfflineTradeinfoQueryResponseV1> getResponseClass() {
        return BcssMemberOfflineTradeinfoQueryResponseV1.class;
    }


    public boolean isNeedEncrypt() {
        return false;
    }

    public String getMethod() {
        return "POST";
    }

    public Class<? extends BizContent> getBizContentClass() {
        return (Class) BcssMemberOfflineTradeInfoQueryRequestBizV1.class;
    }

    public static class BcssMemberOfflineTradeInfoQueryRequestBizV1 implements BizContent {

        @JSONField(name = "timeStamp")
        private String timeStamp;

        @JSONField(name = "clientTransNo")
        private String clientTransNo;


        @JSONField(
                name = "operType"
        )
        private String operType;
        @JSONField(
                name = "startDate"
        )
        private String startDate;
        @JSONField(
                name = "endDate"
        )
        private String endDate;

        @JSONField(name = "corpId")
        private String corpId;

        @JSONField(
                name = "startNum"
        )
        private String startNum;
        @JSONField(
                name = "endNum"
        )
        private String endNum;

        @JSONField(name = "merNo")
        private String merNo;


        @JSONField(name = "busiList")
        private List<BusiInfoDto> busiList;

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getClientTransNo() {
            return clientTransNo;
        }

        public void setClientTransNo(String clientTransNo) {
            this.clientTransNo = clientTransNo;
        }

        public String getOperType() {
            return operType;
        }

        public void setOperType(String operType) {
            this.operType = operType;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getCorpId() {
            return corpId;
        }

        public void setCorpId(String corpId) {
            this.corpId = corpId;
        }

        public String getStartNum() {
            return startNum;
        }

        public void setStartNum(String startNum) {
            this.startNum = startNum;
        }

        public String getEndNum() {
            return endNum;
        }

        public void setEndNum(String endNum) {
            this.endNum = endNum;
        }

        public String getMerNo() {
            return merNo;
        }

        public void setMerNo(String merNo) {
            this.merNo = merNo;
        }

        public List<BusiInfoDto> getBusiList() {
            return busiList;
        }

        public void setBusiList(List<BusiInfoDto> busiList) {
            this.busiList = busiList;
        }

        /*
         * */
        public static class BusiInfoDto implements Serializable {
            @JSONField(name = "merNo")
            private String merNo;

            @JSONField(name = "busiNo")
            private String busiNo;

            @JSONField(name = "docType")
            private String  docType;

            public String getMerNo() {
                return merNo;
            }

            public void setMerNo(String merNo) {
                this.merNo = merNo;
            }

            public String getBusiNo() {
                return busiNo;
            }

            public void setBusiNo(String busiNo) {
                this.busiNo = busiNo;
            }

            public String getDocType() {
                return docType;
            }

            public void setDocType(String docType) {
                this.docType = docType;
            }
        }

    }
}