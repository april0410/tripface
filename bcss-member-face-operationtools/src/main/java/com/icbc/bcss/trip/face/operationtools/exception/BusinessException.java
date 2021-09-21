package com.icbc.bcss.trip.face.operationtools.exception;

/**
 * @author kfzx-ouhz
 * @date 2018/4/13
 * @description 业务异常
 * 服务提供方可直接抛出该异常，异常委托给调用方系统框架处理
 */
public class BusinessException extends RuntimeException {
  public BusinessException() {
    super();
  }

  public BusinessException(String message) {
    super(message);
  }
  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessException(Throwable cause) {
    super(cause);
  }


}

