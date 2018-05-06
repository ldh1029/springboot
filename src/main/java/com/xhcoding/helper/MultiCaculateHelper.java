package com.xhcoding.helper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Max on 17/6/2.
 */
@Slf4j
public class MultiCaculateHelper {

    public static <T> void caculate(Class<T> clazz, T org, T change) throws Exception{
            Field[] fields = clazz.getDeclaredFields();
            String fieldName = null ;
            Method readMethod = null ;
            Method writeMethod = null ;
            Long longDbValue = null ;
            Long longChangeValue = null ;
            Integer intDbValue = null ;
            Integer intChangeValue = null ;
            String typeName = null ;
            for(Field field: fields){
                fieldName = field.getName();
                PropertyDescriptor pd = new PropertyDescriptor(fieldName, clazz);
                field.setAccessible(true);
                readMethod = pd.getReadMethod();
                writeMethod =  pd.getWriteMethod();
                typeName = field.getGenericType().getTypeName();
                if(typeName.equals(Long.class.getTypeName())){
                    longChangeValue = (Long) ClassHelper.getValueByRefler(clazz, change, fieldName);
                    if(ObjectUtils.isEmpty(longChangeValue)){
                        continue;
                    }
                    longDbValue = (Long) readMethod.invoke(org);
                    longDbValue = longDbValue == null ? 0 : longDbValue ;
                    writeMethod.invoke(org, longDbValue + longChangeValue) ;
                }else if(typeName.equals(Integer.class.getTypeName())){
                    intChangeValue = (Integer) ClassHelper.getValueByRefler(clazz, change, fieldName);
                    if(ObjectUtils.isEmpty(intChangeValue)){
                        continue;
                    }
                    intDbValue = (Integer) readMethod.invoke(org);
                    intDbValue = intDbValue == null ? 0 : intDbValue ;
                    writeMethod.invoke(org, intDbValue + intChangeValue) ;
                }
            }
    }
}
