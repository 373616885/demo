package com.redis.mq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author qinjp
 * @date 2019-06-04
 **/
@Service
public class OpsForZSet {

    @Autowired
    private StringRedisTemplate template;


    public void opsZSet() {
        System.out.println(template.opsForZSet().add("zset1", "zset-1", 1.0));

        ZSetOperations.TypedTuple<String> objectTypedTuple2 = new DefaultTypedTuple<String>("zset-2", 9.6);
        ZSetOperations.TypedTuple<String> objectTypedTuple3 = new DefaultTypedTuple<String>("zset-3", 9.9);
        ZSetOperations.TypedTuple<String> objectTypedTuple4 = new DefaultTypedTuple<String>("zset-4", 9.9);
        ZSetOperations.TypedTuple<String> objectTypedTuple5 = new DefaultTypedTuple<String>("zset-5", 9.9);
        ZSetOperations.TypedTuple<String> objectTypedTuple6 = new DefaultTypedTuple<String>("zset-6", 9.9);

        Set<ZSetOperations.TypedTuple<String>> tuples = new HashSet<ZSetOperations.TypedTuple<String>>();
        tuples.add(objectTypedTuple2);
        tuples.add(objectTypedTuple3);
        tuples.add(objectTypedTuple4);
        tuples.add(objectTypedTuple5);
        tuples.add(objectTypedTuple6);

        System.out.println(template.opsForZSet().add("zset1", tuples));
        System.out.println(template.opsForZSet().range("zset1", 0, -1));


        System.out.println(template.opsForZSet().range("zset1", 0, -1));
        // 从有序集合中移除一个或者多个元素
        System.out.println(template.opsForZSet().remove("zset1", "zset-4"));
        System.out.println(template.opsForZSet().range("zset1", 0, -1));

        System.out.println(template.opsForZSet().range("zset1", 0, -1));
        // 返回有序集中指定成员的排名，其中有序集成员按分数值递增(从小到大)顺序排列 -- 0 开始
        System.out.println(template.opsForZSet().rank("zset1", "zset-2"));
        // 返回有序集中指定成员的排名，其中有序集成员按分数值递减(从大到小)顺序排列 -- 0 开始
        System.out.println(template.opsForZSet().range("zset1", 0, -1));
        System.out.println(template.opsForZSet().reverseRank("zset1", "zset-2"));


        Set<ZSetOperations.TypedTuple<String>> rangeScores = template.opsForZSet().rangeByScoreWithScores("zset1", 0, 5, 0, 2);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = rangeScores.iterator();
        while (iterator.hasNext()) {
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            System.out.println("value:" + typedTuple.getValue() + "score:" + typedTuple.getScore());
        }

        System.out.println(template.opsForZSet().reverseRange("zset1", 0, -1));
        // 通过分数返回有序集合指定区间内的成员对象，其中有序集成员按分数值递增(从小到大)顺序排列
        Set<ZSetOperations.TypedTuple<String>> reverseScores =
                template.opsForZSet().reverseRangeWithScores("zset1", 0, -1);
        Iterator<ZSetOperations.TypedTuple<String>> reverseScoresIterator = reverseScores.iterator();
        while (reverseScoresIterator.hasNext()) {
            ZSetOperations.TypedTuple<String> typedTuple = reverseScoresIterator.next();
            System.out.println("value:" + typedTuple.getValue() + "  score:" + typedTuple.getScore());
        }

        System.out.println(template.opsForZSet().rangeByScore("zset1",0,5));
        System.out.println(template.opsForZSet().rangeByScore("zset1",9,10,1,2));


        System.out.println(template.opsForZSet().rangeByScore("zset1",0,5));
        System.out.println(template.opsForZSet().count("zset1",0,5));

    }


}
