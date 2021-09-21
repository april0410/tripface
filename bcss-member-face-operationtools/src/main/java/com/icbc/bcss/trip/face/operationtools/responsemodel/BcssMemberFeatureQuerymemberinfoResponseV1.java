package com.icbc.bcss.trip.face.operationtools.responsemodel;


import com.icbc.api.IcbcResponse;
import com.icbc.api.internal.util.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

public class BcssMemberFeatureQuerymemberinfoResponseV1 extends IcbcResponse {
//    @JSONField(
//            name = "return_msg"
//    )
//    private String return_msg;
    @JSONField(
            name = "clientTransNo"
    )
    private String clientTransNo;
    @JSONField(
            name = "memCount"
    )
    private String memCount;
    @JSONField(
            name = "nextPage"
    )
    private String nextPage;
    @JSONField(
            name = "corgNo"
    )
    private String corgNo;
    @JSONField(
            name = "dataList"
    )
    private List<DataInfo> dataList;

//    public String getReturn_msg() {
//        return this.return_msg;
//    }
//
//    public void setReturn_msg(String return_msg) {
//        this.return_msg = return_msg;
//    }

    public String getClientTransNo() {
        return this.clientTransNo;
    }

    public void setClientTransNo(String clientTransNo) {
        this.clientTransNo = clientTransNo;
    }

    public String getCorgNo() {
        return this.corgNo;
    }

    public void setCorgNo(String corgNo) {
        this.corgNo = corgNo;
    }

    public String getMemCount() {
        return this.memCount;
    }

    public void setMemCount(String memCount) {
        this.memCount = memCount;
    }

    public String getNextPage() {
        return this.nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public List<DataInfo> getDataList() {
        return this.dataList;
    }

    public void setDataList(List<DataInfo> dataList) {
        this.dataList = dataList;
    }

    public static class DataInfo implements Serializable {
        @JSONField(
                name = "memNo"
        )
        private String memNo;
        @JSONField(
                name = "memName"
        )
        private String memName;
        @JSONField(
                name = "memParam"
        )
        private String memParam;
        @JSONField(
                name = "createDate"
        )
        private String createDate;
        @JSONField(
                name = "memDetail1"
        )
        private String memDetail1;
        @JSONField(
                name = "memDetail2"
        )
        private String memDetail2;
        @JSONField(
                name = "memDetail3"
        )
        private String memDetail3;
        @JSONField(
                name = "personId"
        )
        private String personId;

        @JSONField(
                name="industry"
        )
        private String industry;

        @JSONField(
                name="seqNo"
        )
        private String seqNo;

        public DataInfo() {
        }

        public String getMemNo() {
            return this.memNo;
        }

        public void setMemNo(String memNo) {
            this.memNo = memNo;
        }

        public String getMemName() {
            return this.memName;
        }

        public void setMemName(String memName) {
            this.memName = memName;
        }

        public String getMemParam() {
            return this.memParam;
        }

        public void setMemParam(String memParam) {
            this.memParam = memParam;
        }

        public String getCreateDate() {
            return this.createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getMemDetail1() {
            return this.memDetail1;
        }

        public void setMemDetail1(String memDetail1) {
            this.memDetail1 = memDetail1;
        }

        public String getMemDetail2() {
            return this.memDetail2;
        }

        public void setMemDetail2(String memDetail2) {
            this.memDetail2 = memDetail2;
        }

        public String getMemDetail3() {
            return this.memDetail3;
        }

        public void setMemDetail3(String memDetail3) {
            this.memDetail3 = memDetail3;
        }

        public String getPersonId() {
            return this.personId;
        }

        public void setPersonId(String personId) {
            this.personId = personId;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getSeqNo() {
            return seqNo;
        }

        public void setSeqNo(String seqNo) {
            this.seqNo = seqNo;
        }
    }
}