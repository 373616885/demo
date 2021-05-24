package com.qin.mp.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



}
