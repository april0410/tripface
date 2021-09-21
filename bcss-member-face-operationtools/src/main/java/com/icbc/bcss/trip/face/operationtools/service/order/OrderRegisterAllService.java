package com.icbc.bcss.trip.face.operationtools.service.order;

import java.util.Map;

public interface OrderRegisterAllService {
    public Map<String,Object> registerOrder(Map<String,Object> parentParam);

    //共同进行1：n识别
    public Map<String,Object> registerOrderWithSearchN(Map<String,Object> parentParam);
}
