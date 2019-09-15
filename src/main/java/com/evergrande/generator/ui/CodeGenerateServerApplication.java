package com.evergrande.generator.ui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class CodeGenerateServerApplication {

	
	//https://github.com/LiuJiaBaiDing/spring-boot-learning.git  想要简单的模板 （就是差个swagger2）
	//http://localhost:8080/druid/login.html
	
	//应该访问：http://localhost:7777/index
	// http://localhost:7777/druid/index.html
	public static void main(String[] args) {
		SpringApplication.run(CodeGenerateServerApplication.class, args);
	}
}
