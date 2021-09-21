package com.icbc.bcss.trip.face.operationtools.dto;

import com.alibaba.fastjson.annotation.JSONField;

import java.math.BigDecimal;
import java.util.List;

public class OfflineTradeInfoDto {
	/** 厂商编号 **/
	@JSONField(name = "manufacturerId")
	private String manufacturerId;
	/** 硬件编号 **/
	@JSONField(name = "imeiNo")
	private String imeiNo;
	/** 设备编号 **/
	@JSONField(name = "terminalNo")
	private String terminalNo;
	/** 时间戳 **/
	@JSONField(name = "timeStamp")
	private String timeStamp;
	/** 终端交易流水号 **/
	@JSONField(name = "clientTransNo")
	private String clientTransNo;
	@JSONField(name = "trxTime")
	private String trxTime;
	/** 设备类型 **/
	@JSONField(name = "clientType")
	private String clientType;
	/** 档案类型 **/
	@JSONField(name = "docType")
	private String docType;
	/** 二级商户编号 **/
	@JSONField(name = "merNo")
	private String merNo;
	/** 商户订单编号 **/
	@JSONField(name = "merOrderNo")
	private String merOrderNo;
	/** 账户 **/
	@JSONField(name = "memAccno")
	private String memAccno;
	@JSONField(name = "featureType")
	private String featureType;
	/** 参数1 **/
	@JSONField(name = "feature")
	private String feature;
	@JSONField(name = "featureNo")
	private String featureNo;
	@JSONField(name = "featureCorgNo")
	private String featureCorgNo;

	/** 企业编号 **/
	@JSONField(name = "corpId")
	private String corpId;
	/** 合作机构编号 **/
	@JSONField(name = "corgNo")
	private String corgNo;

	/** 订单总额 **/
	@JSONField(name = "totalAmt")
	private BigDecimal totalAmt;
	/** 支付金额 **/
	@JSONField(name = "payAmt")
	private BigDecimal payAmt;
	/** 支付方式 **/
	@JSONField(name = "payType")
	private String payType;
	/** 交易来源 **/
	@JSONField(name = "orderSrc")
	private String orderSrc;
	/** 渠道来源 **/
	@JSONField(name = "dataSrc")
	private String dataSrc;
	/** 行业 **/
	@JSONField(name = "industry")
	private String industry;
	
	/** 交易方式 **/
	@JSONField(name = "tradeMode")
	private String tradeMode;
	/** 提示标志 **/
	@JSONField(name = "msgFlag")
	private String msgFlag;
	/** 确认选项类型 **/
	@JSONField(name = "optionType")
	private String optionType;
	/** 输入 **/
	@JSONField(name = "inputValue")
	private String inputValue;

	/** 操作人员编号 **/
	@JSONField(name = "operUser")
	private String operUser;
	/** 其他数据 **/
	@JSONField(name = "otherData")
	private String otherData;
	/**自定义金额标志**/
	@JSONField(name="definedAmtFlag")
	private String definedAmtFlag;
	/**人脸相似分标准**/
	@JSONField(name="faceSim")
	private String faceSim;

	/** 商品列表 **/
	@JSONField(name = "productList")
	private List<ProductInfoDto> productList;


	/** 优惠列表 **/
	@JSONField(name = "couponList")
	private List<CouponInfoDto> couponList;

	public String getManufacturerId() {
		return manufacturerId;
	}

	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public String getImeiNo() {
		return imeiNo;
	}

	public void setImeiNo(String imeiNo) {
		this.imeiNo = imeiNo;
	}

	public String getTerminalNo() {
		return terminalNo;
	}

	public void setTerminalNo(String terminalNo) {
		this.terminalNo = terminalNo;
	}

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

	public String getClientType() {
		return clientType;
	}

	public void setClientType(String clientType) {
		this.clientType = clientType;
	}

	

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getCorgNo() {
		return corgNo;
	}

	public void setCorgNo(String corgNo) {
		this.corgNo = corgNo;
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

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getOtherData() {
		return otherData;
	}

	public void setOtherData(String otherData) {
		this.otherData = otherData;
	}



	public String getOrderSrc() {
		return orderSrc;
	}

	public void setOrderSrc(String orderSrc) {
		this.orderSrc = orderSrc;
	}

	public String getTradeMode() {
		return tradeMode;
	}

	public void setTradeMode(String tradeMode) {
		this.tradeMode = tradeMode;
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

	
	public String getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(String msgFlag) {
		this.msgFlag = msgFlag;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public String getMerNo() {
		return merNo;
	}

	public void setMerNo(String merNo) {
		this.merNo = merNo;
	}


	public String getMemAccno() {
		return memAccno;
	}

	public void setMemAccno(String memAccno) {
		this.memAccno = memAccno;
	}

	public List<ProductInfoDto> getProductList() {
		return productList;
	}

	public void setProductList(List<ProductInfoDto> productList) {
		this.productList = productList;
	}
	
	
	
	public String getFeatureType() {
		return featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
	}

	public String getFeatureNo() {
		return featureNo;
	}

	public void setFeatureNo(String featureNo) {
		this.featureNo = featureNo;
	}

	public String getFeatureCorgNo() {
		return featureCorgNo;
	}

	public void setFeatureCorgNo(String featureCorgNo) {
		this.featureCorgNo = featureCorgNo;
	}

	public List<CouponInfoDto> getCouponList() {
		return couponList;
	}

	public void setCouponList(List<CouponInfoDto> couponList) {
		this.couponList = couponList;
	}

	public String getTrxTime() {
		return trxTime;
	}

	public void setTrxTime(String trxTime) {
		this.trxTime = trxTime;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}



	public String getMerOrderNo() {
		return merOrderNo;
	}

	public void setMerOrderNo(String merOrderNo) {
		this.merOrderNo = merOrderNo;
	}

	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}

	public String getDefinedAmtFlag() {
		return definedAmtFlag;
	}

	public void setDefinedAmtFlag(String definedAmtFlag) {
		this.definedAmtFlag = definedAmtFlag;
	}

	public String getFaceSim() {
		return faceSim;
	}

	public void setFaceSim(String faceSim) {
		this.faceSim = faceSim;
	}
}
