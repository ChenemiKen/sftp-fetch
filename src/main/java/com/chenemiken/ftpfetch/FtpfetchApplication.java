package com.chenemiken.ftpfetch;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableConfigurationProperties(SftpConfigProperties.class)
public class FtpfetchApplication{

	public static void main(String[] args) {
		SpringApplication.run(FtpfetchApplication.class, args);
	}



	public static void listFiles(String address, Integer serverPort, String username,
			String destinationPath, String password) throws Exception {

		try {
			JSch jsch = new JSch();
			Session session =  jsch.getSession(username, address, serverPort);
			session.setPassword(password);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);

			session.connect();
			ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
			System.out.println("sftp connected");
			System.out.println(channelSftp.pwd());
//			ChannelSftp channelSftp = connect();
//			channelSftp.cd(destinationPath);
//			log.info("this destination path " + destinationPath + " files are " + channelSftp
//					.ls(destinationPath));
//			System.out.println("this destination path " + destinationPath + " files are " + channelSftp
//					.ls(destinationPath));
			File folder = new File(channelSftp.realpath(destinationPath));
			System.out.println(">>>>>>folder: "+folder);
			File[] listOfFiles = folder.listFiles();
			System.out.println(">>>>>>>listOfFiles: "+listOfFiles);
			for (File file : listOfFiles) {
				String fileName = file.getName();
				System.out.println(">>>>>fileName: "+fileName);
			}
			channelSftp.disconnect();
//			log.info("sftp disconnected");
			System.out.println("sftp disconnected");
			session.disconnect();
		} finally {
//			if (channelSftp != null && channelSftp.isConnected()) {
//				channelSftp.disconnect();
//			}
//			if (session != null && session.isConnected()) {
//				session.disconnect();
//			}
		}
	}

	public static void moveAFile(String serverAddress, Integer serverPort, String username,
			String destinationPath, String password) throws Exception{
		Session session = null;
		ChannelSftp channelSftp = new ChannelSftp();
		try {
			JSch jsch = new JSch();
			session = jsch.getSession(username, serverAddress, serverPort);
			session.setPassword(password);

			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");

			session.setConfig(config);
			session.connect();
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect();
//			log.info("sftp connected");
			System.out.println("sftp connected");
			System.out.println(channelSftp.pwd());
			File folder = new File(channelSftp.realpath(destinationPath+"/courses.txt"));
			System.out.println(">>>>>>folder: "+folder);
//			boolean success = folder.renameTo(new File(channelSftp.realpath("Documents/fol2/courses.txt")));
			File localDownloadDir = new File("./downloads");
			System.out.println(localDownloadDir.exists());
			if(!localDownloadDir.exists()){
				localDownloadDir.mkdir();
			}
			System.out.println(localDownloadDir.exists());
			boolean success = folder.renameTo(new File(localDownloadDir.toString(), "/courses.txt"));
			System.out.println(success);
//			File folder =
//			File[] listOfFiles = folder.listFiles();


		}finally {

		}
	}
}
