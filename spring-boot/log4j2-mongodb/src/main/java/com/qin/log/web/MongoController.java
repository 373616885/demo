package com.qin.log.web;

import com.qin.log.bean.Person;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class MongoController {

    private final MongoTemplate mongoTemplate;

    @RequestMapping(value = "/query/people")
    public Person save(int age, String name) {
        Query query = new Query();
        //查询条件 age指MongoDB里面的key
        Criteria criteria = Criteria.where("age").is(age);
        //and条件 name
        criteria.and("name").is(name);
        query.addCriteria(criteria);
        Person people = mongoTemplate.findOne(query, Person.class, "people");
        return people;
    }

}
