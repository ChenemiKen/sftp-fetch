package com.chenemiken.ftpfetch;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//@Configuration
@ConfigurationProperties("host")
public record SftpConfigProperties(String address, int port, String username, String password){
}
