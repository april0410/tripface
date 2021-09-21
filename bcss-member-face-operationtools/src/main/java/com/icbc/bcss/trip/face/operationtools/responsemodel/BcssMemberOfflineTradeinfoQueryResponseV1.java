package com.icbc.bcss.trip.face.operationtools.responsemodel;

import com.icbc.api.IcbcResponse;
import com.icbc.api.internal.util.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class BcssMemberOfflineTradeinfoQueryResponseV1 extends IcbcResponse {

    @JSONField(name = "clientTransNo")
    private String clientTransNo;

    @JSONField(name = "memCount")
    private BigDecimal memCount;

    @JSONField(name="dataList")
    private List<DataInfo> dataList;


    public String getClientTransNo() {
        return this.clientTransNo;
    }

    public void setClientTransNo(String clientTransNo) {
        this.clientTransNo = clientTransNo;
    }

    public BigDecimal getMemCount() {
        return this.memCount;
    }

    public void setMemCount(BigDecimal memCount) {
        this.memCount = memCount;
    }


    public List<DataInfo> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<DataInfo> dataList) {
        this.dataList = dataList;
    }


    public static class DataInfo implements Serializable {
        @JSONField(name = "memOrderNo")
        private String memOrderNo;

        @JSONField(name="docType")
        private String docType;


        @JSONField(name = "terminalNo")
        private String terminalNo;

        @JSONField(name = "merNo")
        private String merNo;

        @JSONField(name = "setDate")
        private String setDate;

        @JSONField(name = "dealDate")
        private String dealDate;

        @JSONField(name = "trxTime")
        private String trxTime;

        @JSONField(name = "clientType")
        private String clientType;

        @JSONField(name = "totalAmt")
        private BigDecimal totalAmt;

        @JSONField(name = "payAmt")
        private BigDecimal payAmt;


        @JSONField(name = "status")
        private String status;



        @JSONField(name = "statusDesc")
        private String statusDesc;

        @JSONField(name = "dealRlt")
        private String dealRlt;

        @JSONField(name = "execTimes")
        private String execTimes;

        @JSONField(name = "payType")
        private String payType;

        @JSONField(name = "orderSrc")
        private String ordersrc;

        @JSONField(name = "dataSrc")
        private String dataSrc;


        @JSONField(name = "industry")
        private String industry;

        @JSONField(name = "deviceName")
        private String deviceName;

        @JSONField(name = "memCardNo")
        private String memCardNo;

        @JSONField(name = "operUser")
        private String operUser;

        public String getMemOrderNo() {
            return memOrderNo;
        }

        public void setMemOrderNo(String memOrderNo) {
            this.memOrderNo = memOrderNo;
        }

        public String getDocType() {
            return docType;
        }

        public void setDocType(String docType) {
            this.docType = docType;
        }

        public String getTerminalNo() {
            return terminalNo;
        }

        public void setTerminalNo(String terminalNo) {
            this.terminalNo = terminalNo;
        }

        public String getMerNo() {
            return merNo;
        }

        public void setMerNo(String merNo) {
            this.merNo = merNo;
        }

        public String getSetDate() {
            return setDate;
        }

        public void setSetDate(String setDate) {
            this.setDate = setDate;
        }

        public String getDealDate() {
            return dealDate;
        }

        public void setDealDate(String dealDate) {
            this.dealDate = dealDate;
        }

        public String getTrxTime() {
            return trxTime;
        }

        public void setTrxTime(String trxTime) {
            this.trxTime = trxTime;
        }

        public String getClientType() {
            return clientType;
        }

        public void setClientType(String clientType) {
            this.clientType = clientType;
        }

        public BigDecimal getTotalAmt() {
            return totalAmt;
        }

        public void setTotalAmt(BigDecimal totalAmt) {
            this.totalAmt = totalAmt;
        }

        public BigDecimal getPayAmt() {
            return payAmt;
        }

        public void setPayAmt(BigDecimal payAmt) {
            this.payAmt = payAmt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusDesc() {
            return statusDesc;
        }

        public void setStatusDesc(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        public String getDealRlt() {
            return dealRlt;
        }

        public void setDealRlt(String dealRlt) {
            this.dealRlt = dealRlt;
        }

        public String getExecTimes() {
            return execTimes;
        }

        public void setExecTimes(String execTimes) {
            this.execTimes = execTimes;
        }

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public String getOrdersrc() {
            return ordersrc;
        }

        public void setOrdersrc(String ordersrc) {
            this.ordersrc = ordersrc;
        }

        public String getDataSrc() {
            return dataSrc;
        }

        public void setDataSrc(String dataSrc) {
            this.dataSrc = dataSrc;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getMemCardNo() {
            return memCardNo;
        }

        public void setMemCardNo(String memCardNo) {
            this.memCardNo = memCardNo;
        }

        public String getOperUser() {
            return operUser;
        }

        public void setOperUser(String operUser) {
            this.operUser = operUser;
        }
    }
}
