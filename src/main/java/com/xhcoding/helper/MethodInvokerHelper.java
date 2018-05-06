package com.xhcoding.helper;


import java.lang.reflect.Method;

/**
 * Created by Max on 17/3/10.
 */
public class MethodInvokerHelper {
    private final Method method;

    /**
     * @param isWrap         是否去除包装类型 例如 Integer.class --> int.class
     * @param clz
     * @param isWrap
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static MethodInvokerHelper createMethod(final Class<?> clz, boolean isWrap, final String methodName, Class<?>... parameterTypes) {
        Method method = ClassHelper.getAccessibleMethod(clz, methodName, isWrap, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + clz + ']');
        }
        return new MethodInvokerHelper(method);
    }

    /**
     * @param isWrap       是否去除包装类型 例如 Integer.class --> int.class
     * @param clz
     * @param propertyName
     * @return
     */
    public static MethodInvokerHelper createGetter(final Class<?> clz, boolean isWrap, final String propertyName) {
        Method method = ClassHelper.getGetterMethod(clz, isWrap, propertyName);
        if (method == null) {
            throw new IllegalArgumentException(
                    "Could not find getter method [" + propertyName + "] on target [" + clz + ']');
        }
        return new MethodInvokerHelper(method);
    }

    /**
     * @param isWrap        是否去除包装类型 例如 Integer.class --> int.class
     * @param clz
     * @param propertyName
     * @param parameterType
     * @return
     */
    public static MethodInvokerHelper createSetter(final Class<?> clz, final String propertyName, boolean isWrap, Class<?> parameterType) {
        Method method = ClassHelper.getSetterMethod(clz, propertyName, isWrap, parameterType);
        if (method == null) {
            throw new IllegalArgumentException(
                    "Could not find getter method [" + propertyName + "] on target [" + clz + ']');
        }
        return new MethodInvokerHelper(method);
    }

    protected MethodInvokerHelper(Method method) {
        this.method = method;
    }

    /**
     * 调用已准备好的Method
     */
    public <T> T invoke(final Object obj, Object... args) {
        try {
            return (T) method.invoke(obj, args);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }
}
