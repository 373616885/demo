package com.qin.mp;

import com.qin.mp.config.PageConfig;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class Base {

	@Autowired
	private UserMapper userMapper;

	@Test
	public void SelectAll() {
		//PageConfig.mytable.set("user");
		System.out.println(("----- selectAll method test ------"));
		List<User> userList = userMapper.selectList(null);
		userList.forEach(System.out::println);
	}


	@Test
	public void insert() {
		User user = User.builder()
				.name("向西")
				.age(25)
				.email("xxx@qq.com")
				.managerId(2L)
				.remark("这是一个备注信息")
				.createTime(LocalDateTime.now())
				.build();

		Integer rows =  userMapper.insert(user);
		System.out.println(rows);

	}


	@Test
	public void selectByid() {
		User user = userMapper.selectById(1L);
		System.out.println(user);

	}

}
