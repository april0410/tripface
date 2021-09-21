/**
 * 
 */
package com.icbc.bcss.trip.face.operationtools.requestmodel;

import com.icbc.api.AbstractIcbcRequest;
import com.icbc.api.BizContent;
import com.icbc.api.internal.util.fastjson.annotation.JSONField;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberOfflineTradeinfoRegisterResponseV1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author kfzx-liul03
 *
 */
public class BcssMemberOfflineTradeinfoRegisterRequestV1 extends AbstractIcbcRequest<BcssMemberOfflineTradeinfoRegisterResponseV1> {
	@Override
	public Class<BcssMemberOfflineTradeinfoRegisterResponseV1> getResponseClass() {
		return BcssMemberOfflineTradeinfoRegisterResponseV1.class;
	}

	public BcssMemberOfflineTradeinfoRegisterRequestV1() {
		setServiceUrl("https://**.**.**.**/api/bcss/member/groupservice/tradeReg/V1");
	}

	@Override
	public boolean isNeedEncrypt() {
		return false;
	}

	@Override
	public String getMethod() {
		return "POST";
	}

	@Override
	public Class<? extends BizContent> getBizContentClass() {
		return BcssMemberOfflineTradeInfoRegisterRequestBizV1.class;
	}

	public static class BcssMemberOfflineTradeInfoRegisterRequestBizV1 implements BizContent {
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
	    /** 交易时间 **/
	    @JSONField(name = "trxTime")
	    private String trxTime;
	    /** 设备类型 **/
	    @JSONField(name = "clientType")
	    private String clientType;
	    /** 二级商户编号 **/
	    @JSONField(name = "merNo")
	    private String merNo;
	    /** 会员账户 **/
        @JSONField(name = "memAccno")
        private String memAccno;
	    /** 参数1 **/
	    @JSONField(name = "feature")
	    private String feature;
	    /** 参数1 **/
	    @JSONField(name = "featureNo")
	    private String featureNo;
	    /** 参数1 **/
	    @JSONField(name = "featureType")
	    private String featureType;
	    
	    /** 参数1 **/
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
     
        /** 其他数据 **/
        @JSONField(name = "otherData")
        private String otherData;
        /** 交易来源 **/
        @JSONField(name = "orderSrc")
        private String ordersrc;
        /** 渠道来源 **/
        @JSONField(name = "dataSrc")
        private String dataSrc;
        /** 行业 **/
        @JSONField(name = "industry")
        private String industry;
        /** 档案类型 **/
    	@JSONField(name = "docType")
    	private String docType;
    	
    	
    	/** 商户订单编号 **/
    	@JSONField(name = "merOrderNo")
    	private String merOrderNo;
    	/** 操作人员编号 **/
    	@JSONField(name = "operUser")
    	private String operUser;
        
       
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
		public List<CouponInfoDto> getCouponList() {
			return couponList;
		}
		public void setCouponList(List<CouponInfoDto> couponList) {
			this.couponList = couponList;
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
    
        public String getTradeMode() {
            return tradeMode;
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
		public String getFeatureNo() {
			return featureNo;
		}
		public void setFeatureNo(String featureNo) {
			this.featureNo = featureNo;
		}
		public String getFeatureType() {
			return featureType;
		}
		public void setFeatureType(String featureType) {
			this.featureType = featureType;
		}
		public String getFeatureCorgNo() {
			return featureCorgNo;
		}
		public void setFeatureCorgNo(String featureCorgNo) {
			this.featureCorgNo = featureCorgNo;
		}
		public String getTrxTime() {
			return trxTime;
		}
		public void setTrxTime(String trxTime) {
			this.trxTime = trxTime;
		}
		public void setTradeMode(String tradeMode) {
			this.tradeMode = tradeMode;
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

		@Override
		public String toString() {
			return "BcssMemberOfflineTradeInfoRegisterRequestBizV1{" + "manufacturerId='" + manufacturerId + '\'' + ", imeiNo='" + imeiNo + '\'' + ", terminalNo='" + terminalNo + '\'' + ", timeStamp" +
					"='" + timeStamp + '\'' + ", clientTransNo='" + clientTransNo + '\'' + ", trxTime='" + trxTime + '\'' + ", clientType='" + clientType + '\'' + ", merNo='" + merNo + '\'' + ", " +
					"memAccno='" + memAccno + '\'' + ", feature='" +"********" + '\'' + ", featureNo='" + featureNo + '\'' + ", featureType='" + featureType + '\'' + ", featureCorgNo='" + featureCorgNo + '\''
					+ ", corpId='" + corpId + '\'' + ", corgNo='" + corgNo + '\'' + ", totalAmt=" + totalAmt + ", payAmt=" + payAmt + ", payType='" + payType + '\'' + ", otherData='" + otherData + '\''
					+ ", ordersrc='" + ordersrc + '\'' + ", dataSrc='" + dataSrc + '\'' + ", industry='" + industry + '\'' + ", docType='" + docType + '\'' + ", merOrderNo='" + merOrderNo + '\'' + ", operUser='"
					+ operUser + '\'' + ", tradeMode='" + tradeMode + '\'' + ", msgFlag='" + msgFlag + '\'' + ", optionType='" + optionType + '\'' + ", inputValue='" + inputValue + '\'' + ", productList=" + productList + ", couponList=" + couponList + '}';
		}
	}
	
	public static class ProductInfoDto implements Serializable{
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

		@Override
		public String toString() {
			return "ProductInfoDto{" + "productId='" + productId + '\'' + ", productName='" + productName + '\'' + ", productNum=" + productNum + ", productAmt=" + productAmt + ", productDetail='" + productDetail + '\'' + ", productType='" + productType + '\'' + ", groupNo='" + groupNo + '\'' + ", distributeDate='" + distributeDate + '\'' + ", productRemark='" + productRemark + '\'' + ", periodid='" + periodid + '\'' + '}';
		}
	}
	 
	 
	 public static class CouponInfoDto{
		 private String couponNo;
		 private BigDecimal  couponAmt;
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
//		public String getCouponAmt() {
//			return couponAmt;
//		}
//		public void setCouponAmt(String couponAmt) {
//			this.couponAmt = couponAmt;
//		}
//		public BigDecimal getCouponType() {
//			return couponType;
//		}
//		public void setCouponType(BigDecimal couponType) {
//			this.couponType = couponType;
//		}

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

}
