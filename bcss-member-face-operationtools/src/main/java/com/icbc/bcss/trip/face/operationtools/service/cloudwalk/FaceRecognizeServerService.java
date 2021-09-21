package com.icbc.bcss.trip.face.operationtools.service.cloudwalk;

import java.util.Map;

public interface FaceRecognizeServerService {
    public Map<String,Object> recognizeInServerInOne(Map<String,Object> param) ;

    public Map<String,Object> recognizeInServerInN(Map<String,Object> param) ;
}
