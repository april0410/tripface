package com.icbc.bcss.trip.face.operationtools.controller;


import com.icbc.api.IcbcApiException;
import com.icbc.bcss.trip.face.operationtools.api.BcssMemberFeatureQuerymemberinfoTestV1;

import org.jasypt.encryption.StringEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
//@RequestMapping(value = "/live")
public class LiveController {
    private static final Logger looger= LoggerFactory.getLogger(LiveController.class);

    @Autowired
    private StringEncryptor stringEncryptor;

    @RequestMapping(value="/detect")
    public Map livedetct(){
        HashMap<String,String> map=new HashMap<>(2);
        map.put("return_code","0");
        map.put("return_msg","探活成功");
        looger.info("探活成功");
        System.out.println("探活成功");
//        BcssMemberFeatureQuerymemberinfoTestV1 apine=new BcssMemberFeatureQuerymemberinfoTestV1();
//        try {
//            apine.RequestFaceFromMember();
//        } catch (IcbcApiException e) {
//            e.printStackTrace();
//        }
//        System.out.println(stringEncryptor.encrypt("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCbFZ4tsIgTBbroQsMZJunv+ZRwoQxNL/8ZyQqWk7qn1g1Tb635KsUOEQNrFT+b0gWL91W3+rGF7ShSCNaS0Vb5wTva8a0xK0E70sLIhujxdIw9021EbS9BwCm7202LzXyd2jVrwf8rIfptcoaHcP344ZMM78r52rzZVCxWw5z0tZYsUuOMB9PLsm1J8RkDpbcAm2olbMtPpZhsAuDHKPH6EVaRrxC0rReZy2eh+Q6jdba3zxCRc8QO+rq1nJO6q330cscZ00+Y2e1ywF5pAXf81+Kg+QggGwn0pkAhn9KApqYNZ2Jwe7dPxRW1Skz6w8/x3AtfZNt4ezqOp3gQcaExAgMBAAECggEANJEBSq9ZkEkS74MhqjbceLD6NasBBnDMYSsZ4aw1SoptfeiO6bQrkvcFV5ieNOzdYHH3piLdZW3biuLgCGfYuVNcPHxKni3xMJvh1iKUdrNwjcxKbzUrHXhLLRfKkyaVpNO/48Sf/zjHL63wF5yfGWsscugcvs/7zxaO6OHpI7CYloyJ+Y3AC2QnzZLOVfjtcR19qq2jtU/rHwwGBBzthYeisicvwsQZA/Oj5jZylSAh83dKc5HW21AVmc3whe95Vz4k/IMyz0o/OU9UuGYiiH0VaHXKPiSNicEtAgP431t89PE9ErNusLvimeNeg0b/VdloC+0glrmqwhDLWWUpgQKBgQDnsl8rO81yKHin+AlAvqzC0NXQjj9LJXxnVeraTKxfStN9RFqxtHv8B6RiIpyANFDnd9mTtIR6N3tb2HJ8Km3S6BDOkZqF2UIwpzkrpZdFQN7p6atakNxm6Pb0tS2IVDyGjQctT9xO4FTsOgtTj9waVbNNB9a4vSxizUVNtx7JWQKBgQCrWgRPEp9ck6plKiPRcCaepyzwx/q+SuTk2ova3nu5cmc35Uo1KxBP6d4Y/42vylDGFMZU10LtODLFD/f7E6/KLb1m1bV3IwjyKFCqRefe/eVP9cBKSlIGPjLqfacDuMyJYaZd+Fngp43+lzos8lgKdwT5MU3e1RipYoVYHwFDmQKBgGkA8JqCVsBm0Q+mnGLoRxlfVZdX8B1ZVsDqMi5O00u4eJJr2QJyPkJhIEGNWAnOK+BK86M6C1PsMw7T0EavX+hWXc+QM0x3wsST9JfwStcK6DtwN8Uqo4hMCiequIDxVCDSZy9E4x4oErSgNaPgLasNrd26MLi2mxgH2WG4HM65AoGAHjYh2ls7M9RpT6rtY8j1VjW9i7qGsDR+RQdvbyiZAep03nsT6WntV1mxqhCsx5jRQwt4qI7HoxGsieg13dPrw6bq5Q27EAViV2faSRtINZ3oZ3+55p9R9P3UdlmvL83Oak6ISbs3BZAlTgUV4cTc1wODIfiadTZ3Qa44OoBE9ckCgYEAmOZzycNjDPS59lYIs+yK4GLmmruAwfUBf1sTfN6YWz8K41j8BQSTMvMjm20F5V1HN8SgLWdy61KdaSn6LQZKNp2BXS2AHTL1D/GXP4FrCo7ZZpnSBDJjQ18wctYdjS+4fMZdSPZ0A+0x4lPKjKEr5JDrkUgYnwp58w+ekYPJs40="));
//        System.out.println(stringEncryptor.encrypt("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmxWeLbCIEwW66ELDGSbp7/mUcKEMTS//GckKlpO6p9YNU2+t+SrFDhEDaxU/m9IFi/dVt/qxhe0oUgjWktFW+cE72vGtMStBO9LCyIbo8XSMPdNtRG0vQcApu9tNi818ndo1a8H/KyH6bXKGh3D9+OGTDO/K+dq82VQsVsOc9LWWLFLjjAfTy7JtSfEZA6W3AJtqJWzLT6WYbALgxyjx+hFWka8QtK0XmctnofkOo3W2t88QkXPEDvq6tZyTuqt99HLHGdNPmNntcsBeaQF3/NfioPkIIBsJ9KZAIZ/SgKamDWdicHu3T8UVtUpM+sPP8dwLX2TbeHs6jqd4EHGhMQIDAQAB"));
//        System.out.println(stringEncryptor.encrypt("645r6z1azEItiR+ghwF4+g=="));
        return map;
    }

}
