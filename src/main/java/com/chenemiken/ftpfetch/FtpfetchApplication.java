package com.chenemiken.ftpfetch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(SftpConfigProperties.class)
public class FtpfetchApplication{

	public static void main(String[] args) {
		SpringApplication.run(FtpfetchApplication.class, args);
	}

}
