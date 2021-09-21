package com.icbc.bcss.trip.face.operationtools.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
@ComponentScan
public class MyTaskExecutorConfig implements AsyncConfigurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyTaskExecutorConfig.class);

    /*
    * 函数：给人脸注册和人脸删除使用的
    * */
    @Bean
    public Executor getAsyncExecutor(){
        ThreadPoolTaskExecutor taskExecutor =new ThreadPoolTaskExecutor();
        //配置核心线程数
        taskExecutor.setCorePoolSize(30);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setThreadNamePrefix("async-bcss-member-face-operationtools-servie-");

        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        taskExecutor.initialize();
        return taskExecutor;
    }

    /*
    * 通用线程池
    * */
    @Bean
    public Executor getDefaultAsyncExecutor(){
        ThreadPoolTaskExecutor taskExecutor =new ThreadPoolTaskExecutor();
        //配置核心线程数
        taskExecutor.setCorePoolSize(5);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setThreadNamePrefix("async-main-servie-");

        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        taskExecutor.initialize();
        return taskExecutor;
    }

    /*
     * 通用线程池
     * */
    @Bean
    public Executor getOrderAsyncExecutor(){
        ThreadPoolTaskExecutor taskExecutor =new ThreadPoolTaskExecutor();
        //配置核心线程数
        taskExecutor.setCorePoolSize(20);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(200);
        taskExecutor.setThreadNamePrefix("async-order-upload-servie-");

        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化
        taskExecutor.initialize();
        return taskExecutor;
    }



    //执行异常补充
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler(){
//        return  null;
        return new MyAsyncExceptionHandler();
    }

    //自定义异常捕捉
    class MyAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            LOGGER.info("Exception message for MyTaskExecutorConfig- " + throwable.getMessage());
            LOGGER.info("Method name - " + method.getName());
            for (Object param : objects) {
                LOGGER.info("Parameter value - " + param);
            }
            LOGGER.info("Exception message for MyTaskExecutorConfig- finshed all operation");
        }
    }

}
