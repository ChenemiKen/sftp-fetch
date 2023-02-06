package com.chenemiken.ftpfetch;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {
  @Autowired
  private SftpConfigProperties host;

  @Override
  public void run(String... args) throws Exception {
    System.out.println(host.address());
    connect();
  }

  public ChannelSftp connect() throws Exception{
    JSch jsch = new JSch();
    Session session =  jsch.getSession(host.username(), host.address(), host.port());
    session.setPassword(host.password());

    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);

    session.connect();
    ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
    channelSftp.connect();
    System.out.println("sftp connected");
    System.out.println(channelSftp.pwd());
    return channelSftp;
  }
}
