package com.qin.dynamic.web;

import com.qin.dynamic.entity.Account;
import com.qin.dynamic.service.AccountService;
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

    private final AccountService accountService;

    @GetMapping(value = "account")
    public List<Account> account() {
        return accountService.account();
    }

}
