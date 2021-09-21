package com.icbc.bcss.trip.face.operationtools.utils;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.ConfigurablePropertyResolver;
import org.springframework.stereotype.Component;


@Component
public class PropertyPlaceholder extends PropertySourcesPlaceholderConfigurer {

//    private static Map<String, String> propertyMap;
//
//	@Override
//    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
//        super.processProperties(beanFactoryToProcess, props);
//        propertyMap = new HashMap<String, String>();
//        for (Object key : props.keySet()) {
//            String keyStr = key.toString();
//            String value = props.getProperty(keyStr);
//            propertyMap.put(keyStr, value);
//        }
//    }
//
//    public static String getProperty(String name) {
//        return propertyMap.get(name);
//    }

    private static ConfigurablePropertyResolver stringValueResolver;

    @Override
    public void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
                                  final ConfigurablePropertyResolver propertyResolver) throws BeansException {
        super.processProperties(beanFactoryToProcess, propertyResolver);
        stringValueResolver = propertyResolver;
    }

    public static String getProperty(String key) {
        return stringValueResolver.getProperty(key);
    }

    public static void setProperties(String key,String value) {stringValueResolver.setRequiredProperties(key,value);}



}
