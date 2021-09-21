package com.icbc.bcss.trip.face.operationtools.http;

import java.util.Map;

public interface HttpBaseService {
    public Map<String,Object> execute(Map<String,Object> param,String httpurl);
    public String executeAll(Map<String,Object> param,String httpurl);
}
