package com.icbc.bcss.trip.face.operationtools.service.composite;

import java.util.Map;

public interface BcssMemFaceAndOrderCompositeService {
    public Map<String,Object> execute(Map<String,Object> parentParam);

    //二维码入口
    public Map<String, Object> executeQrCodeRecognizeService(Map<String, Object> parentParam);
}
