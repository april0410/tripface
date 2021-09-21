package com.icbc.bcss.trip.face.operationtools.service;

import java.util.Map;

public interface FaceRecognizeAllService {

    //
    public Map<String, Object> recognizeFaceWithOneByOne(Map<String, Object> param) ;

    //
    public Map<String, Object> recognizeFaceWithOneByN(Map<String, Object> param) ;

}
