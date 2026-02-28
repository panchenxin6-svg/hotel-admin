package com.pcx.hotel_admin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.pcx.hotel_admin.mapper")
public class HotelAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelAdminApplication.class, args);
	}

}
