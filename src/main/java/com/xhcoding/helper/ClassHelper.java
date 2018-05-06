package com.xhcoding.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

/**
 * Created by Max on 17/3/10.
 */
public class ClassHelper {
    private static final String CGLIB_CLASS_SEPARATOR = "$$";

    private static Logger logger = LoggerFactory.getLogger(ClassHelper.class);

    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<Class<?>, Class<?>>(8);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, Boolean.TYPE);
        ClassHelper.primitiveWrapperTypeMap.put(Byte.class, Byte.TYPE);
        ClassHelper.primitiveWrapperTypeMap.put(Character.class, Character.TYPE);
        ClassHelper.primitiveWrapperTypeMap.put(Double.class, Double.TYPE);
        ClassHelper.primitiveWrapperTypeMap.put(Float.class, Float.TYPE);
        ClassHelper.primitiveWrapperTypeMap.put(Integer.class, Integer.TYPE);
        ClassHelper.primitiveWrapperTypeMap.put(Long.class, Long.TYPE);
        ClassHelper.primitiveWrapperTypeMap.put(Short.class, Short.TYPE);
    }

    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final String IS_PREFIX = "is";

    ////////// shortClassName 和 packageName//////////

    /**
     * 返回Class名, 不包含PackageName.
     * <p>
     * 内部类的话，返回"主类.内部类"
     */
    public static String getShortClassName(final Class<?> cls) {
        return ClassHelper.getShortClassName(cls);
    }

    /**
     * 返回Class名，不包含PackageName
     * <p>
     * 内部类的话，返回"主类.内部类"
     */
    public static String getShortClassName(final String className) {
        return ClassHelper.getShortClassName(className);
    }

    /**
     * 返回PackageName
     */
    public static String getPackageName(final Class<?> cls) {
        return ClassHelper.getPackageName(cls);
    }

    /**
     * 返回PackageName
     */
    public static String getPackageName(final String className) {
        return ClassHelper.getPackageName(className);
    }

    ////////// 获取全部父类，全部接口，以及全部Annotation//////////

    /**
     * 递归返回所有的SupperClasses，包含Object.class
     */
    public static List<Class<?>> getAllSuperclasses(final Class<?> cls) {
        return ClassHelper.getAllSuperclasses(cls);
    }

    /**
     * 递归返回本类及所有基类继承的接口，及接口继承的接口，比Spring中的相同实现完整
     */
    public static List<Class<?>> getAllInterfaces(final Class<?> cls) {
        return ClassHelper.getAllInterfaces(cls);
    }

    /**
     * 递归Class所有的Annotation，一个最彻底的实现.
     * <p>
     * 包括所有基类，所有接口的Annotation，同时支持Spring风格的Annotation继承的父Annotation，
     */
    public static Set<Annotation> getAllAnnotations(final Class<?> cls) {
        List<Class<?>> allTypes = getAllSuperclasses(cls);
        allTypes.addAll(getAllInterfaces(cls));
        allTypes.add(cls);

        Set<Annotation> anns = new HashSet<Annotation>();
        for (Class<?> type : allTypes) {
            anns.addAll(Arrays.asList(type.getDeclaredAnnotations()));
        }

        Set<Annotation> superAnnotations = new HashSet<Annotation>();
        for (Annotation ann : anns) {
            getSupperAnnotations(ann.annotationType(), superAnnotations);
        }

        anns.addAll(superAnnotations);

        return anns;
    }

    private static <A extends Annotation> void getSupperAnnotations(Class<A> annotationType, Set<Annotation> visited) {
        Annotation[] anns = annotationType.getDeclaredAnnotations();

        for (Annotation ann : anns) {
            if (!ann.annotationType().getName().startsWith("java.lang") && visited.add(ann)) {
                getSupperAnnotations(ann.annotationType(), visited);
            }
        }
    }

    //// 获取标注了annotation的所有属性和方法////////

    /**
     * 找出所有标注了该annotation的公共属性，循环遍历父类.
     * <p>
     * 暂未支持Spring风格Annotation继承Annotation
     * <p>
     * from org.unitils.util.AnnotationUtils
     */
    public static <T extends Annotation> Set<Field> getAnnotatedPublicFields(Class<? extends Object> clazz,
                                                                             Class<T> annotation) {

        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }

        Set<Field> annotatedFields = new HashSet<Field>();
        Field[] fields = clazz.getFields();

        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                annotatedFields.add(field);
            }
        }

        return annotatedFields;
    }

    /**
     * 找出所有标注了该annotation的属性，循环遍历父类，包含private属性.
     * <p>
     * 暂未支持Spring风格Annotation继承Annotation
     * <p>
     * from org.unitils.util.AnnotationUtils
     */
    public static <T extends Annotation> Set<Field> getAnnotatedFields(Class<? extends Object> clazz,
                                                                       Class<T> annotation) {
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }
        Set<Field> annotatedFields = new HashSet<Field>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.getAnnotation(annotation) != null) {
                annotatedFields.add(field);
            }
        }
        annotatedFields.addAll(getAnnotatedFields(clazz.getSuperclass(), annotation));
        return annotatedFields;
    }

    /**
     * 找出所有标注了该annotation的公共方法(含父类的公共函数)，循环其接口.
     * <p>
     * 暂未支持Spring风格Annotation继承Annotation
     * <p>
     * 另，如果子类重载父类的公共函数，父类函数上的annotation不会继承，只有接口上的annotation会被继承.
     */
    public static <T extends Annotation> Set<Method> getAnnotatedPublicMethods(Class<?> clazz, Class<T> annotation) {
        // 已递归到Objebt.class, 停止递归
        if (Object.class.equals(clazz)) {
            return Collections.emptySet();
        }

        List<Class<?>> ifcs = ClassHelper.getAllInterfaces(clazz);
        Set<Method> annotatedMethods = new HashSet<Method>();

        // 遍历当前类的所有公共方法
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            // 如果当前方法有标注，或定义了该方法的所有接口有标注
            if (method.getAnnotation(annotation) != null || searchOnInterfaces(method, annotation, ifcs)) {
                annotatedMethods.add(method);
            }
        }

        return annotatedMethods;
    }

    private static <T extends Annotation> boolean searchOnInterfaces(Method method, Class<T> annotationType,
                                                                     List<Class<?>> ifcs) {
        for (Class<?> iface : ifcs) {
            try {
                Method equivalentMethod = iface.getMethod(method.getName(), method.getParameterTypes());
                if (equivalentMethod.getAnnotation(annotationType) != null) {
                    return true;
                }
            } catch (Throwable ex) {
                // Skip this interface - it doesn't have the method...
            }
        }
        return false;
    }

    ///////// 获取方法////

    /**
     * 循环遍历，按属性名获取前缀为get或is的函数，并设为可访问
     */
    public static Method getSetterMethod(Class<?> clazz, String propertyName, boolean isWrap, Class<?> parameterType) {
        String setterMethodName = ClassHelper.SETTER_PREFIX + StringUtils.capitalize(propertyName);
        return ClassHelper.getAccessibleMethod(clazz, setterMethodName, isWrap, parameterType);
    }

    /**
     * @param isWrap 是否去除包装类型 例如 Integer.class --> int.class
     *               循环遍历，按属性名获取前缀为set的函数，并设为可访问
     */
    public static Method getGetterMethod(Class<?> clazz, boolean isWrap, String propertyName) {
        String getterMethodName = ClassHelper.GETTER_PREFIX + StringUtils.capitalize(propertyName);

        Method method = ClassHelper.getAccessibleMethod(clazz, getterMethodName, isWrap);

        // retry on another name
        if (method == null) {
            getterMethodName = ClassHelper.IS_PREFIX + StringUtils.capitalize(propertyName);
            method = ClassHelper.getAccessibleMethod(clazz, getterMethodName, isWrap);
        }
        return method;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredField, 并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     * <p>
     * 因为class.getFiled(); 不能获取父类的private函数, 因此采用循环向上的getDeclaredField();
     */
    public static Field getAccessibleField(final Class clazz, boolean isWrap, final String fieldName) {
        Validate.notNull(clazz, "clazz can't be null");
        Validate.notEmpty(fieldName, "fieldName can't be blank");
        for (Class<?> superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                ClassHelper.makeAccessible(field);
                return field;
            } catch (Throwable e) {// NOSONAR
                // Field不在当前类定义,继续向上转型
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod, 并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     *
     * @param isWrap 是否去除包装类型 例如 Integer.class --> int.class
     *               匹配函数名+参数类型.
     *               <p>
     *               因为class.getFiled() 不能获取父类的private函数, 因此采用循环向上的getDeclaredField();
     */
    public static Method getAccessibleMethod(final Class<?> clazz, final String methodName, boolean isWrap,
                                             Class<?>... parameterTypes) {
        Validate.notNull(clazz, "class can't be null");
        Validate.notEmpty(methodName, "methodName can't be blank");
        Class[] theParameterTypes = ArrayUtils.nullToEmpty(parameterTypes);

        if (isWrap) {// 处理原子类型与对象类型的兼容
            ClassHelper.wrapClassses(theParameterTypes);
        }

        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, theParameterTypes);
                ClassHelper.makeAccessible(method);
                return method;
            } catch (Throwable e) {
                // Method不在当前类定义,继续向上转型
                System.out.println(e);
            }
        }
        return null;
    }

    /**
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * <p>
     * 如向上转型到Object仍无法找到, 返回null.
     * <p>
     * 只匹配函数名, 如果有多个同名函数返回第一个
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     * <p>
     * 因为class.getMethods() 不能获取父类的private函数, 因此采用循环向上的getMethods();
     */
    public static Method getAccessibleMethodByName(final Class clazz, final String methodName) {
        Validate.notNull(clazz, "clazz can't be null");
        Validate.notEmpty(methodName, "methodName can't be blank");

        for (Class<?> searchType = clazz; searchType != Object.class; searchType = searchType.getSuperclass()) {
            Method[] methods = searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    ClassHelper.makeAccessible(method);
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    /**
     * 改变private/protected的成员变量为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())
                || Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * 兼容原子类型与非原子类型的转换，不考虑依赖两者不同来区分不同函数的场景
     */
    private static void wrapClassses(Class<?>[] source) {
        for (int i = 0; i < source.length; i++) {
            Class<?> wrapClass = primitiveWrapperTypeMap.get(source[i]);
            if (wrapClass != null) {
                source[i] = wrapClass;
            }
        }
    }

    /////////// 杂项 /////////

    /**
     * From Spring, 按顺序获取默认ClassLoader
     * <p>
     * 1. Thread.currentThread().getContextClassLoader()
     * <p>
     * 2. ReflectHelper的加载ClassLoader
     * <p>
     * 3. SystemClassLoader
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassHelper.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }

    /**
     * 获取CGLib处理过后的实体的原Class.
     */
    public static Class<?> unwrapCglib(Object instance) {
        Validate.notNull(instance, "Instance must not be null");
        Class<?> clazz = instance.getClass();
        if ((clazz != null) && clazz.getName().contains(CGLIB_CLASS_SEPARATOR)) {
            Class<?> superClass = clazz.getSuperclass();
            if ((superClass != null) && !Object.class.equals(superClass)) {
                return superClass;
            }
        }
        return clazz;
    }

    /**
     * 通过反射, 获得Class定义中声明的泛型参数的类型,
     * <p>
     * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
     * <p>
     * 如无法找到, 返回Object.class.
     * <p>
     * eg. public UserDao extends HibernateDao<User>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or Object.class if cannot be determined
     */
    public static <T> Class<T> getClassGenricType(final Class clazz) {
        return getClassGenricType(clazz, 0);
    }

    /**
     * 通过反射, 获得Class定义中声明的父类的泛型参数的类型.
     * <p>
     * 注意泛型必须定义在父类处. 这是唯一可以通过反射从泛型获得Class实例的地方.
     * <p>
     * 如无法找到, 返回Object.class.
     * <p>
     * 如public UserDao extends HibernateDao<User,Long>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration, start from 0.
     * @return the index generic declaration, or Object.class if cannot be determined
     */
    public static Class getClassGenricType(final Class clazz, final int index) {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if ((index >= params.length) || (index < 0)) {
            logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: "
                    + params.length);
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
            return Object.class;
        }

        return (Class) params[index];
    }

    /**
     * 探测类是否存在classpath中
     */
    public static boolean isPresent(String className, ClassLoader classLoader) {
        try {
            classLoader.loadClass(className);
            return true;
        } catch (Throwable ex) {
            // Class or one of its dependencies is not present...
            return false;
        }
    }



    public static Object getValueByRefler(Class clazz, Object target, String property) {
        if( !ObjectUtils.isEmpty(target) ){
            Field[] fields = clazz.getDeclaredFields() ;
            if(!ObjectUtils.isEmpty(fields)){
                String fieldName = null ;
                for(Field field : fields){
                    fieldName = field.getName() ;
                    if(!property.equals(fieldName)){
                        continue;
                    }

                    PropertyDescriptor pd = null;
                    try {
                        pd = new PropertyDescriptor(fieldName, clazz);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    if(ObjectUtils.isEmpty(pd)){
                        return null ;
                    }

                    field.setAccessible(true);
                    Method readMethod = pd.getReadMethod();
                    try {
                        return  readMethod.invoke(target) ;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    return null ;
                }
            }
        }

        return null;
    }

}
