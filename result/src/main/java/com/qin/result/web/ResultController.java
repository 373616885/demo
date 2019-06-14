package com.qin.result.web;

import com.google.common.collect.Maps;
import com.qin.result.base.BizException;
import com.qin.result.common.ResponseResult;
import com.qin.result.model.Dog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * @author qinjp
 * @date 2019-05-30
 **/
@RestController
public class ResultController {

    private Map<Integer, Dog> dogs = Maps.newConcurrentMap();

    @ResponseResult
    @RequestMapping("/dog/{id}")
    public Dog getDog(@PathVariable("id") Integer id) {
        if (dogs.get(id) == null) {
            throw new BizException("that dog non existent");
        }
        return dogs.get(id);
    }

    @ResponseResult
    @RequestMapping("/dog/{name}")
    public Dog getDog(@PathVariable("name") String name) {
        for (Map.Entry<Integer, Dog> entry : dogs.entrySet()) {
            Dog dog = entry.getValue();
//            if (StringUtils.equals(dog.getName(), name)) {
//                return dog;
//            }
        }
        throw new BizException("that dog non existent");
    }

    /**
     * @RequestBody 只支持 post 和 json
     */
    @ResponseResult
    @RequestMapping(value = "insert/dog")
    public void insert(@Valid Dog dog) {
        if (dogs.containsKey(dog.getId())) {
            throw new BizException("已存在不可添加");
        }
        dogs.put(dog.getId(), dog);
    }

    /**
     * @RequestBody 只支持 post 和 json
     */
    @ResponseResult
    @PostMapping(value = "add/dog", produces = MediaType.APPLICATION_JSON_VALUE)
    public void addDog(@Valid @RequestBody Dog dog, @RequestParam("token") String token) {
        if (dogs.containsKey(dog.getId())) {
            throw new BizException("已存在不可添加");
        }
        dogs.put(dog.getId(), dog);
    }

    @ResponseResult
    @RequestMapping("/dogs")
    public Map<Integer, Dog> dogs() {
        return dogs;
    }

}
