package com.qin.sharding.jdbc.web;

import com.qin.sharding.jdbc.dao.AccountMapper;
import com.qin.sharding.jdbc.entity.Account;
import com.qin.sharding.jdbc.vo.MenuVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author qinjp
 * @date 2020/12/3
 */
@Slf4j
@RestController
@AllArgsConstructor
public class AccountController {

    private final AccountMapper accountMapper;

    @GetMapping(value = "account")
    public List<Account> account() {
        return accountMapper.selectList(null);
    }

}
