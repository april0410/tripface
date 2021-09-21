package com.icbc.bcss.trip.face.operationtools.service.cloudwalk;

import java.util.Map;

public interface FaceRecognizeService {
    public Map<String,Object> recognizeServiceInOne(Map<String,Object> param) ;

    public Map<String,Object> recognizeServiceInN(Map<String,Object> param) ;
}
