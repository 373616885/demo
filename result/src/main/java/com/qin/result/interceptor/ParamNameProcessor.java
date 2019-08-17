package com.qin.result.interceptor;

import com.qin.result.common.ParamName;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletModelAttributeMethodProcessor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
public class ParamNameProcessor  extends ServletModelAttributeMethodProcessor {

    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    //Rename cache
    private final Map<Class<?>, Map<String, String>> replaceMap = new ConcurrentHashMap<>();

    public ParamNameProcessor(boolean annotationNotRequired) {
        super(annotationNotRequired);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestParam.class)
                && !BeanUtils.isSimpleProperty(parameter.getParameterType())
                && Arrays.stream(parameter.getParameterType().getDeclaredFields())
                .anyMatch(field -> field.getAnnotation(ParamName.class) != null);
    }

    @Override
    protected void bindRequestParameters(WebDataBinder binder, NativeWebRequest nativeWebRequest) {
        Object target = binder.getTarget();
        Class<?> targetClass = Objects.requireNonNull(target).getClass();
        if (!replaceMap.containsKey(targetClass)) {
            replaceMap.put(targetClass, analyzeClass(targetClass));
        }
        Map<String, String> mapping = replaceMap.get(targetClass);
        ParamNameDataBinder paramNameDataBinder = new ParamNameDataBinder(target, binder.getObjectName(), mapping);
        Objects.requireNonNull(requestMappingHandlerAdapter.getWebBindingInitializer())
                .initBinder(paramNameDataBinder);
        super.bindRequestParameters(paramNameDataBinder, nativeWebRequest);
    }

    private Map<String, String> analyzeClass(Class<?> targetClass) {
        Map<String, String> renameMap = new HashMap<>();
        for (Field field : targetClass.getDeclaredFields()) {
            ParamName paramNameAnnotation = field.getAnnotation(ParamName.class);
            if (paramNameAnnotation != null && !paramNameAnnotation.value().isEmpty()) {
                renameMap.put(paramNameAnnotation.value(), field.getName());
            }
        }
        if (targetClass.getSuperclass() != Object.class) {
            renameMap.putAll(analyzeClass(targetClass.getSuperclass()));
        }
        return renameMap;
    }
}
