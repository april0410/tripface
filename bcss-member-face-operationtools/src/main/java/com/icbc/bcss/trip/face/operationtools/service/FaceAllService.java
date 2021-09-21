package com.icbc.bcss.trip.face.operationtools.service;

import java.util.Map;

public interface FaceAllService {
    public Map<String,Object> regANDdelFaceAllService(Map<String,Object> param);

    public Map<String,Object>  queryProgressService(Map<String,Object> param);
    
    public Map<String,Object> pullFaceService(Map<String,Object> param);

//    public Map<String,Object>  recognizeFaceAllService(Map<String,Object> param);

}
