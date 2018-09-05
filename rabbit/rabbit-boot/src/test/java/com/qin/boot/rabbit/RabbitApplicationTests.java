package com.qin.boot.rabbit;

import com.qin.boot.rabbit.servie.Sender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitApplicationTests {


	@Autowired
	private Sender sender;

	@Test
	public void contextLoads() {
		sender.send();
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
