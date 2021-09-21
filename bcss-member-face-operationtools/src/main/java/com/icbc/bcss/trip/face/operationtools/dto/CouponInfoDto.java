package com.icbc.bcss.trip.face.operationtools.dto;

import java.math.BigDecimal;

public class CouponInfoDto {
    private String couponNo;
    private BigDecimal couponAmt;
    private String couponType;
    private BigDecimal couponNum;
    private String couponDetail1;
    private String couponDetail2;


    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public BigDecimal getCouponAmt() {
        return couponAmt;
    }

    public void setCouponAmt(BigDecimal couponAmt) {
        this.couponAmt = couponAmt;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public BigDecimal getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(BigDecimal couponNum) {
        this.couponNum = couponNum;
    }

    public String getCouponDetail1() {
        return couponDetail1;
    }

    public void setCouponDetail1(String couponDetail1) {
        this.couponDetail1 = couponDetail1;
    }

    public String getCouponDetail2() {
        return couponDetail2;
    }

    public void setCouponDetail2(String couponDetail2) {
        this.couponDetail2 = couponDetail2;
    }
}