package com.qin.mybatis.demo;

import com.qin.mybatis.demo.mybatis.client.PlayerMapper;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author qinjp
 * @date 2019-06-05
 **/
public class ParamNameTests {

    public static void main(String[] args) throws NoSuchMethodException {
        Class<PlayerMapper> mapper = PlayerMapper.class;

        Method method = mapper.getMethod("selectByUidAndName",String.class,String.class);

        Parameter[] parameters = method.getParameters();

        System.out.println(Arrays.stream(parameters).map(Parameter::getName).collect(Collectors.toList()));

    }
}
