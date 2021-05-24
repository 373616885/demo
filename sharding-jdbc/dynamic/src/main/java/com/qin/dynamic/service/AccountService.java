package com.qin.dynamic.service;

import com.qin.dynamic.config.DataSource;
import com.qin.dynamic.dao.AccountMapper;
import com.qin.dynamic.entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author qinjp
 * @date 2020/12/4
 */
@Service
@AllArgsConstructor
public class AccountService {

    private final AccountMapper accountMapper;

    @Transactional
    @DataSource
    public List<Account> account() {
        return accountMapper.selectList(null);
    }
}
