package com.icbc.bcss.trip.face.operationtools.api;

//import cn.cloudwalk.libproject.livedetect.util.LogUtils;

import com.icbc.api.DefaultIcbcClient;
import com.icbc.api.IcbcApiException;
import com.icbc.api.internal.util.internal.util.fastjson.JSONObject;
import com.icbc.bcss.trip.face.operationtools.requestmodel.BcssMemberFeatureQuerymemberinfoRequestV1;
import com.icbc.bcss.trip.face.operationtools.responsemodel.BcssMemberFeatureQuerymemberinfoResponseV1;
import com.icbc.bcss.trip.face.operationtools.utils.PropertyPlaceholder;

import org.springframework.beans.factory.annotation.Autowired;

//import com.icbc.api.response.BcssMemberFeatureQuerymemberinfoResponseV1;
@Deprecated
public class BcssMemberFeatureQuerymemberinfoTestV1 {
	// MY_PRIVATE_KEY
//	protected static final String MY_PRIVATE_KEY = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDME5evAleNhLF2To1o/aqeZKKQADOAdpsZ4A+6G1uFrtF0MD6OJ8Y2wAvdqaZD0yrIp8xVjw25VdyoAO4m/+Va792GQWQbv9+pjfGr+gohzT9zVdoVatlFamXu/BtMjPTtJw5eMLkBRTptatJ0oWa3uBuDRH2pV/w8ku3Y0DOTBoFas2x537fcvzcRtO7YFBGr4PwjEACnJ/oEK/KljvFlVgpmEz2e678uMK3vM5QkcGv7e91emkTcnzlgk1XaGc+a71W4q3ISmdN8v3Ad4hk7rbxQ9gqFUbx47ctWl8fYnrArSvXj6RGrV70F+B5DaXA4Kp4lLMRl5LR9M+WUldTbAgMBAAECggEBAJggitQlNMsD/Z4plHSNlCCa+CvMWpgK0jgJtO+Q8TrIRM8OH7Ospq+tnarRimXJBn2pyEKHbz2XafO7LczYuUp+wG4SVOPytnIYo+0FaYQlEoWRnA6XJfv/ApmSSW9ZLEOU97rzKxZtWs6x8MyMfJu7nJbP+bTBKACnrrIPEkMoI4LK/Llgh7zhUmEE4f3MbVpedl4a9zsLfCz+LZxYwvCqYXrGPAqtEsk+kvvlF9pEjCeBeJ+0oDEc9a3jQVxEetCJabcOXcal4dCfTYMvdrmqWun9599Ybo0N0BDS65YTI5HULV+0oMiDpgi8SDZisG4LOSW0gNp7Wu4wrIWsbUECgYEA5TCwsWVNDjbTzD0XWb2inSnMvqCiZ0NANkRIUp5WYoarcNwmnjpBJ7deSRdbz23cZTD5D1cShCzCCuTJrKoH3NPy4gCgj/kh4BQnvA1t76Pakef7cwkuDoiABull9+3dp6OZ71v+oF+V2aw+7Fpq5vPMkEMVtIPlcn8P41V/LzECgYEA4/LZyYJ7lFWgDCvouGBH9Tt5kcx+x5uf9Vbi2K5znicmrJXyVH809vKYsE8DS0WcKC5DdjWoWL7i9mZHB3BTOVBxfl/0uUNm0uAXr9vxXAdEBiLiCY4vSBWxyddqslN/+VwwgsubgMpIW1NsVAJctQHyF2TyoUJR0D4yn/txucsCgYAWfIZ5gTTkNiDlowDNfzubK+S4t033z7NXH6/w2zQiio/7jL4FPDX0TZtklHQfj+nVy85D4QkiCronE3duB2iqE+l6nxEhzxGxyIh1fOPSZUMmmZokGTx5lI7rHq8wehgLQJPz1hTPa1Fqf+nUirJ+YmEprktM4Lv8VdcbOOI3oQKBgQCPRAUJl47gLejHfZlouGrBgL3pmuNbxT4FzrQIh6x3DBhMhHmoDcNWKic5OI/TcU6yGlGgLB1AmcjAIIwn6rsD3MFYMir3fZtUki+RAeVyd8aTLL1wUAL7hVcWiuOubLiUGxMKQbncekZ7z7TYIBvwT+a+OV4daDdPcKgU0M4rfQKBgBHaqffhJvq5nhab8YIBve8GKCtVRO+0AYIXYwO6Ooe4Epmf+rqyqMTg7LJhaNcPbt/KGVvbGI2G896NP4NHBJWd3F+ChYehlqyCuwuZIr1X6q/JcORZ5sax6BERMT5BgSLvOqEd33+jpF1HUZEwlDqAYrC9WieG+kEVNpNvfQez";
	protected static final String MY_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbFZ4tsIgTBbroQsMZJunv+ZRwoQxNL/8ZyQqWk7qn1g1Tb635KsUOEQNrFT+b0gWL91W3+rGF7ShSCNaS0Vb5wTva8a0xK0E70sLIhujxdIw9021EbS9BwCm7202LzXyd2jVrwf8rIfptcoaHcP344ZMM78r52rzZVCxWw5z0tZYsUuOMB9PLsm1J8RkDpbcAm2olbMtPpZhsAuDHKPH6EVaRrxC0rReZy2eh+Q6jdba3zxCRc8QO+rq1nJO6q330cscZ00+Y2e1ywF5pAXf81+Kg+QggGwn0pkAhn9KApqYNZ2Jwe7dPxRW1Skz6w8/x3AtfZNt4ezqOp3gQcaExAgMBAAECggEANJEBSq9ZkEkS74MhqjbceLD6NasBBnDMYSsZ4aw1SoptfeiO6bQrkvcFV5ieNOzdYHH3piLdZW3biuLgCGfYuVNcPHxKni3xMJvh1iKUdrNwjcxKbzUrHXhLLRfKkyaVpNO/48Sf/zjHL63wF5yfGWsscugcvs/7zxaO6OHpI7CYloyJ+Y3AC2QnzZLOVfjtcR19qq2jtU/rHwwGBBzthYeisicvwsQZA/Oj5jZylSAh83dKc5HW21AVmc3whe95Vz4k/IMyz0o/OU9UuGYiiH0VaHXKPiSNicEtAgP431t89PE9ErNusLvimeNeg0b/VdloC+0glrmqwhDLWWUpgQKBgQDnsl8rO81yKHin+AlAvqzC0NXQjj9LJXxnVeraTKxfStN9RFqxtHv8B6RiIpyANFDnd9mTtIR6N3tb2HJ8Km3S6BDOkZqF2UIwpzkrpZdFQN7p6atakNxm6Pb0tS2IVDyGjQctT9xO4FTsOgtTj9waVbNNB9a4vSxizUVNtx7JWQKBgQCrWgRPEp9ck6plKiPRcCaepyzwx/q+SuTk2ova3nu5cmc35Uo1KxBP6d4Y/42vylDGFMZU10LtODLFD/f7E6/KLb1m1bV3IwjyKFCqRefe/eVP9cBKSlIGPjLqfacDuMyJYaZd+Fngp43+lzos8lgKdwT5MU3e1RipYoVYHwFDmQKBgGkA8JqCVsBm0Q+mnGLoRxlfVZdX8B1ZVsDqMi5O00u4eJJr2QJyPkJhIEGNWAnOK+BK86M6C1PsMw7T0EavX+hWXc+QM0x3wsST9JfwStcK6DtwN8Uqo4hMCiequIDxVCDSZy9E4x4oErSgNaPgLasNrd26MLi2mxgH2WG4HM65AoGAHjYh2ls7M9RpT6rtY8j1VjW9i7qGsDR+RQdvbyiZAep03nsT6WntV1mxqhCsx5jRQwt4qI7HoxGsieg13dPrw6bq5Q27EAViV2faSRtINZ3oZ3+55p9R9P3UdlmvL83Oak6ISbs3BZAlTgUV4cTc1wODIfiadTZ3Qa44OoBE9ckCgYEAmOZzycNjDPS59lYIs+yK4GLmmruAwfUBf1sTfN6YWz8K41j8BQSTMvMjm20F5V1HN8SgLWdy61KdaSn6LQZKNp2BXS2AHTL1D/GXP4FrCo7ZZpnSBDJjQ18wctYdjS+4fMZdSPZ0A+0x4lPKjKEr5JDrkUgYnwp58w+ekYPJs40=";

	// APIGW_PUBLIC_KEY
	protected static final String APIGW_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwFgHD4kzEVPdOj03ctKM7KV+16bWZ5BMNgvEeuEQwfQYkRVwI9HFOGkwNTMn5hiJXHnlXYCX+zp5r6R52MY0O7BsTCLT7aHaxsANsvI9ABGx3OaTVlPB59M6GPbJh0uXvio0m1r/lTW3Z60RU6Q3oid/rNhP3CiNgg0W6O3AGqwIDAQAB";


	// APP_ID
//	protected static final String APP_ID = "10000000000004097348";
//	protected static final String APP_ID = "10000000000001827500";

	protected static final String APP_ID = "10000000000004097413";

	protected static final String RSA = "RSA";
	protected static final String RSA2 = "RSA2";

	@Autowired
	private PropertyPlaceholder environment;

/*???????????????????????????*/
	//?????????????????????BcssMemberConstant??????????????????????????????
	public  void RequestFaceFromMember() throws IcbcApiException {
		BcssMemberFeatureQuerymemberinfoRequestV1 request = new BcssMemberFeatureQuerymemberinfoRequestV1();
//		request.setServiceUrl("http://114.255.225.36:8081/api/bcss/member/feature/querymemberinfo/V1");
		request.setServiceUrl(PropertyPlaceholder.getProperty("bcss.member.querymemberinfoURL"));
		BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1 bizContent = new BcssMemberFeatureQuerymemberinfoRequestV1.BcssMemberFeatureQuerymemberinfoRequestBizV1();
		bizContent.setClientTransNo("12345678912345678920");
		bizContent.setTimeStamp("2020-10-22 10:10:12");
		bizContent.setCorpId("2000000067");
		bizContent.setOperType("1");
		bizContent.setFeatureType("1");
		bizContent.setChannel("");
		bizContent.setStartNum("1");
		bizContent.setEndNum("10");
		bizContent.setStartDate("2020-01-27");
		bizContent.setEndDate("2020-10-27");
		request.setBizContent(bizContent);

		DefaultIcbcClient client = new DefaultIcbcClient(APP_ID, RSA2, environment.getProperty("bcss.member.api.my_private_key"), environment.getProperty("bcss.member.api.apigw_public_key"));
		BcssMemberFeatureQuerymemberinfoResponseV1 response = client.execute(request);

		System.out.println(JSONObject.toJSONString(response));
//		System.out.println(response.getDataList().get(0).getSeqNo());
	}



}