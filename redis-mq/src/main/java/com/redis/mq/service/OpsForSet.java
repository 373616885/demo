package com.redis.mq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author qinjp
 * @date 2019-06-04
 **/
@Service
public class OpsForSet {

    @Autowired
    private StringRedisTemplate template;

    public void opsSet(){
        String[] strarrays = new String[]{"strarr1","sgtarr2"};
        System.out.println(template.opsForSet().add("setTest", strarrays));

        // 删除元素--返回成功删除个数
        strarrays = new String[]{"strarr1"};
        System.out.println(template.opsForSet().remove("setTest",strarrays));

        Set<String> set =template.opsForSet().members("setTest");
        System.out.println(set);
        // 添加元素--返回成功添加个数
        Long c = template.opsForSet().add("setTest","aaa","bbb","ccc");
        System.out.println(c);

        set =template.opsForSet().members("setTest");
        System.out.println(set);

        // 将 value 元素从 key 集合移动到 destKey 集合
        template.opsForSet().move("setTest","aaa","setTest2");
        System.out.println(template.opsForSet().members("setTest"));
        System.out.println(template.opsForSet().members("setTest2"));

        // 返回set集合大小
        System.out.println(template.opsForSet().size("setTest"));
        // 判断 member 元素是否是集合 key 的成员
        System.out.println(template.opsForSet().isMember("setTest","ccc"));
        System.out.println(template.opsForSet().isMember("setTest","asd"));


        template.opsForSet().add("members","aaa","bbb","ccc","ddd");
        template.opsForSet().add("members2","aaa","bbb","fff");
        template.opsForSet().add("members3","aaa","bbb","eee");

        System.out.println(template.opsForSet().members("members"));
        System.out.println(template.opsForSet().members("members2"));
        System.out.println(template.opsForSet().members("members3"));

        // key对应的无序集合与otherKey对应的无序集合求交集
        System.out.println(template.opsForSet().intersect("members","members2"));


        List<String> strlist = new ArrayList<String>();
        strlist.add("members2");
        strlist.add("members3");
        // key对应的无序集合与多个otherKey对应的无序集合求交集
        System.out.println(template.opsForSet().intersect("members",strlist));

        // key无序集合与otherkey无序集合的交集存储到destKey无序集合中
        System.out.println(template.opsForSet().intersectAndStore("members","members2","members4"));
        System.out.println(template.opsForSet().members("members4"));

        // key对应的无序集合与多个otherKey对应的无序集合求交集存储到destKey无序集合中
        System.out.println(template.opsForSet().intersectAndStore("members",strlist,"members5"));
        System.out.println(template.opsForSet().members("members5"));

    }
}
