/**
 * 
 */
package com.icbc.bcss.trip.face.operationtools.responsemodel;

import com.icbc.api.IcbcResponse;
import com.icbc.api.internal.util.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 
 * @author kfzx-liul03
 *
 */
public class BcssMemberOfflineTradeinfoRegisterResponseV1 extends IcbcResponse {
	/** 终端交易流水号 **/
	@JSONField(name = "clientTransNo")
	private String clientTransNo;

	@JSONField(name="memCount")
	private BigDecimal memCount;

	@JSONField(name="nextPage")
	private int nextPage;

	@JSONField(name="dataList")
	private List<DataInfo> dataList;

	@JSONField(name="corgNo")
	private String corgNo;


	public String getClientTransNo() {
		return clientTransNo;
	}

	public void setClientTransNo(String clientTransNo) {
		this.clientTransNo = clientTransNo;
	}
	
	
	public BigDecimal getMemCount() {
		return memCount;
	}

	public void setMemCount(BigDecimal memCount) {
		this.memCount = memCount;
	}

	public int getNextPage() {
		return nextPage;
	}

	public void setNextPage(int nextPage) {
		this.nextPage = nextPage;
	}

	public List<DataInfo> getDataList() {
		return dataList;
	}

	public void setDataList(List<DataInfo> dataList) {
		this.dataList = dataList;
	}

	public String getCorgNo() {
		return corgNo;
	}

	public void setCorgNo(String corgNo) {
		this.corgNo = corgNo;
	}



	public static class DataInfo implements Serializable {

		private String memNo;
		private String memName;
		private String memParam;
		private String memDetail1;
		private String memDetail2;
		private String memDetail3;

		public String getMemNo() {
			return memNo;
		}

		public void setMemNo(String memNo) {
			this.memNo = memNo;
		}

		public String getMemName() {
			return memName;
		}

		public void setMemName(String memName) {
			this.memName = memName;
		}

		public String getMemParam() {
			return memParam;
		}

		public void setMemParam(String memParam) {
			this.memParam = memParam;
		}

		public String getMemDetail1() {
			return memDetail1;
		}

		public void setMemDetail1(String memDetail1) {
			this.memDetail1 = memDetail1;
		}

		public String getMemDetail2() {
			return memDetail2;
		}

		public void setMemDetail2(String memDetail2) {
			this.memDetail2 = memDetail2;
		}

		public String getMemDetail3() {
			return memDetail3;
		}

		public void setMemDetail3(String memDetail3) {
			this.memDetail3 = memDetail3;
		}
	}

}
