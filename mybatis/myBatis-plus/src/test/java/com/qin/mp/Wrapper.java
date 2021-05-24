package com.qin.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

@SpringBootTest
public class Wrapper {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectList() {
        /**
         * where name like '%雨%' and age < 40
         */
        // 等价Wrappers.query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 数据库的列名
        queryWrapper.like("name", "雨")
                .lt("age", 40);
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectWrapper() {
        /**
         * where name like '%雨%' and age between 20 and 40 and email is not null;
         */
        // 等价Wrappers.query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 数据库的列名
        queryWrapper.like("name", "雨")
                .between("age", 20, 40)
                .isNotNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper2() {
        /**
         * where name like '王%' or age > 25 order by age desc , user_id asc
         */
        // 等价Wrappers.query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 数据库的列名
        queryWrapper.likeRight("name", "王")
                .or()
                .ge("age", 25)
                .orderByDesc("age")
                .orderByAsc("user_id");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper3() {
        /**
         * where date_format(create_time,'%Y-%m-%d') = '2020-09-13'
         * and manager_id in (select user_id from user where name like ='王%')
         */
        // 等价Wrappers.query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .apply("date_format(create_time,'%Y-%m-%d')={0}", "2020-09-13")
                // 这有sql注入的风险 所以用上面的
                //.apply("date_format(create_time,'%Y-%m-%d') = '2020-09-13' or true or true")
                //.apply("date_format(create_time,'%Y-%m-%d') = '2020-09-13'")
                .inSql("manager_id", "select user_id from t_user where name like '王%' ");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper4() {

        /**
         * 只获取指定字段
         * select user_id , name ,age from t_user
         * where name like '%雨%' and age between 20 and 40 and email is not null;
         */
        // 等价Wrappers.query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 数据库的列名
        queryWrapper.select("user_id", "name", "age")
                .like("name", "雨")
                .between("age", 20, 40)
                .isNotNull("email");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectWrapper5() {

        /**
         * 排除 manager_id和create_time字段
         * select user_id , name ,age from t_user
         * where name like '%雨%' and age between 20 and 40 and email is not null;
         */
        // 等价Wrappers.query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 数据库的列名
        queryWrapper
                .like("name", "雨")
                .between("age", 20, 40)
                .isNotNull("email")
                .select(User.class,
                        info -> !info.getColumn().equals("manager_id") &&
                                !info.getColumn().equals("create_time")
                );
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectWrapper6() {

        /**
         * 直接使用user对象进行查询
         * select user_id , name ,age from t_user
         * where name like '%雨%' and age between 20 and 40 and email is not null;
         */
        User user = User.builder()
                .name("向西")
                .age(25)
                .email("xxx@qq.com")
                .build();

        // 等价Wrappers.query();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper7() {
        /**
         * where name like '王%' and (age<40 or email is not null)
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .and(consumer -> consumer.lt("age", 40).or().isNotNull("email"));

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectWrapper8() {
        /**
         * where name like '王%' or (age<40 and age >20 and email is not null)
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.likeRight("name", "王")
                .or(consumer -> consumer.lt("age", 40).gt("age", 20)
                        .isNotNull("email"));

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectWrapper9() {
        /**
         * (age<40 or email is not null) and name like '王%'
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.nested(qw -> qw.lt("age", 40).or().isNotNull("email"))
                .likeRight("name", "王");

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper10() {
        /**
         * age in (20,31,32,40)
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.in("age", Arrays.asList(20, 31, 32, 40));
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper11() {
        /**
         * limit 1
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // last 无视一切直接拼接到 sql 的最后
        // 但 只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
        queryWrapper.last("limit 1");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper12() {
        /**
         * exists("select id from table where age = 40")
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // last 无视一切直接拼接到 sql 的最后
        // 但 只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
        queryWrapper.exists("select user_id from t_user where age = 1");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapper13() {
        /**
         * not exists("select id from table where age = 40")
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // last 无视一切直接拼接到 sql 的最后
        // 但 只能调用一次,多次调用以最后一次为准 有sql注入的风险,请谨慎使用
        queryWrapper.notExists("select user_id from t_user where age = 1");
        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }

    @Test
    public void selectWrapper14() {
        /**
         * name = '王天风' and age = 20
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Map<String, Object> params = new HashMap();
        params.put("name", "王天风");
        params.put("age", 20);
        params.put("email", null);
        // WHERE (name = '王天风' AND age = 20 AND email IS NULL);
        queryWrapper.allEq(params);
        // 过滤null
        queryWrapper.allEq(params,false);
        // 给条件判断是否执行
        Integer n = 1;
        queryWrapper.allEq(n > 0, params, false);
        // BiPredicate 条件过滤 k v
        queryWrapper.allEq(n > 0, (k, v) -> !Objects.equals(v,"王天风"), params, false);

        List<User> users = userMapper.selectList(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapperMaps() {
        /**
         * name = '王天风' and age = 20
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id","name")
                .likeRight("name", "王").lt("age",40);
        // 如果返回只返回两列，对象的很多属性就为 null 不优雅
        List<Map<String, Object>> users = userMapper.selectMaps(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapperMaps2() {
        /**
         * select avg(age) as age_avg ,min(age) as age_min , max(age) as age_max ,sum(age) as age_sum
         * from t_user group by manager_id having sum(age) > 50
         */
        // 很多别名不想创建对象的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("avg(age) as age_avg","min(age) as age_min","max(age) as age_max","sum(age) as age_sum")
                .groupBy("manager_id")
                .having("sum(age) > {0}", 50);
        // 如果返回只返回两列，对象的很多属性就为 null 不优雅
        List<Map<String, Object>> users = userMapper.selectMaps(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapperObjects() {
        /**
         * name = '王天风' and age = 20
         */
        // 很多别名不想创建对象的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id","name")
                .likeRight("name", "王").lt("age",40);
        // 如果返回只返回第一列
        List<Object> users = userMapper.selectObjs(queryWrapper);
        users.forEach(System.out::println);
    }


    @Test
    public void selectWrapperCount() {
        /**
         * count（1）
         * name = '王天风' and age = 20
         */
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .likeRight("name", "王").lt("age",40);
        Integer count = userMapper.selectCount(queryWrapper);
        System.out.println(count);
    }

    @Test
    public void selectWrapperOne() {
        /**
         * name = '王天风' and age = 20
         */
        // 很多别名不想创建对象的
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper
                .likeRight("name", "王").lt("age",40);
        // 如果返回只返回第一列
        User user = userMapper.selectOne(queryWrapper);
        System.out.println(user);
    }

}
