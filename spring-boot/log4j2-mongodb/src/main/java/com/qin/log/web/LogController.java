package com.qin.log.web;


import com.qin.log.bean.LogMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Slf4j
@RestController
@AllArgsConstructor
public class LogController {

    private final MongoTemplate mongoTemplate;

    @RequestMapping(value = "/log")
    public String save(int age, String name) {
        log.warn("mongo log ：age {} name {} ", age, name);
        return "success";
    }

    //模糊匹配
    static Pattern pattern = Pattern.compile("^.*mongo log.*$", Pattern.CASE_INSENSITIVE);

    String prefix = "^.*";
    String suffix = ".*$";

    @RequestMapping(value = "/query/log")
    public List<LogMessage> queryLog(String level,
                                     @RequestParam(required = false) String pattern) {
        int pageSize = 10;

        Sort sort = new Sort(Sort.Direction.ASC, "millis")
                .and(new Sort(Sort.Direction.ASC, "date"));

        Query query = new Query();

        Criteria criteria = Criteria.where("level").is(level);

        //criteria.and("message").regex(pattern);
        if (Objects.isNull(pattern)) {
            // 模糊查询
            criteria.and("message").regex(prefix + pattern + suffix);
        }


        query.addCriteria(criteria);

        //query.skip(skipNumber);
        query.limit(pageSize);
        query.with(sort);
        query.fields()
                .include("level")
                .include("message")
                .include("millis")
                .include("date");
        return mongoTemplate.find(query, com.qin.log.bean.LogMessage.class, "log");
    }


}
