package com.icbc.bcss.trip.face.operationtools.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ProductInfoDto implements Serializable {

    private String productId;
    private String productName;
    private BigDecimal productNum;
    private BigDecimal productAmt;
    private String productDetail;
    private String productType;
    private String groupNo;
    private String distributeDate;
    private String productRemark;
    private String periodid;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductNum() {
        return productNum;
    }

    public void setProductNum(BigDecimal productNum) {
        this.productNum = productNum;
    }

    public BigDecimal getProductAmt() {
        return productAmt;
    }

    public void setProductAmt(BigDecimal productAmt) {
        this.productAmt = productAmt;
    }

    public String getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(String productDetail) {
        this.productDetail = productDetail;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getDistributeDate() {
        return distributeDate;
    }

    public void setDistributeDate(String distributeDate) {
        this.distributeDate = distributeDate;
    }

    public String getProductRemark() {
        return productRemark;
    }

    public void setProductRemark(String productRemark) {
        this.productRemark = productRemark;
    }

    public String getPeriodid() {
        return periodid;
    }

    public void setPeriodid(String periodid) {
        this.periodid = periodid;
    }

}