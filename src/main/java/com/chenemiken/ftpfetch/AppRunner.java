package com.chenemiken.ftpfetch;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements CommandLineRunner {
  @Autowired
  private SftpConfigProperties host;

  @Override
  public void run(String... args) throws Exception {
    try{
//      connect();
//      listFiles("Documents/fol1");
//      moveOneFile("Documents/fol1", "/courses.txt", "Documents/fol2");
//      moveAllFiles("Documents/fol1/", "Documents/fol2/");
//      downloadOneFile("Documents/fol2/", "courses.txt");
      downloadAllFiles("Documents/fol2/");
      System.exit(0);
    }catch (Exception e){
      System.out.println(e.getLocalizedMessage());
    }
  }

  private ChannelSftp connect() throws Exception{
    JSch jsch = new JSch();
    Session session =  jsch.getSession(host.username(), host.address(), host.port());
    session.setPassword(host.password());

    java.util.Properties config = new java.util.Properties();
    config.put("StrictHostKeyChecking", "no");
    session.setConfig(config);

    session.connect();
    ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
    channelSftp.connect();
    System.out.println("sftp connected!");
    System.out.println(">>>>>>>Currently in: " +channelSftp.pwd());
    return channelSftp;
  }

  public void listFiles(String sftpSourcePath) throws Exception {
    ChannelSftp channelSftp = connect();
    try {
      File folder = new File(channelSftp.realpath(sftpSourcePath));
      System.out.println(">>>>>>folder: "+folder);
      File[] listOfFiles = folder.listFiles();
//      System.out.println(">>>>>>>listOfFiles: "+listOfFiles);
      for (File file : listOfFiles) {
        String fileName = file.getName();
        System.out.println(">>>>>fileName: "+fileName);
      }
      channelSftp.disconnect();
      System.out.println("sftp disconnected");
//      session.disconnect();
    } finally {
			if (channelSftp != null && channelSftp.isConnected()) {
				channelSftp.disconnect();
			}
//			if (session != null && session.isConnected()) {
//				session.disconnect();
//			}
    }
  }

  public void moveOneFile(String sftpSourcePath, String fileName, String destinationPath) throws Exception{
    ChannelSftp channelSftp = connect();
    try {
      File file = new File(channelSftp.realpath(sftpSourcePath + fileName));
      System.out.println(">>>>>>file: "+file);
      if(file.exists()){
        boolean success = file.renameTo(new File(channelSftp.realpath(destinationPath + fileName)));
        System.out.println(success);
      }else{
        System.out.println(">>>>>>Error: file not found");
      }
    }finally {
      if (channelSftp != null && channelSftp.isConnected()) {
        channelSftp.disconnect();
      }
    }
  }

  public void moveAllFiles(String sftpSourcePath, String destinationPath) throws Exception{
    ChannelSftp channelSftp = connect();
    try{
      File folder = new File(channelSftp.realpath(sftpSourcePath));
      System.out.println(">>>>>>SourceFolder: "+folder);
      File[] listOfFiles = folder.listFiles();
      for (File file : listOfFiles) {
        String fileName = file.getName();
        File fileToMove = new File(channelSftp.realpath(sftpSourcePath + fileName));
        File newFile = new File(channelSftp.realpath((destinationPath + fileName)));
        boolean success = fileToMove.renameTo(newFile);
        System.out.println(newFile.getAbsoluteFile());
        System.out.println(success);
      }
    }catch (Exception e){
      System.out.println(e.getLocalizedMessage());
    }finally {
      if (channelSftp != null && channelSftp.isConnected()) {
        channelSftp.disconnect();
      }
    }
  }

  public void downloadOneFile(String sftpSourcePath, String fileName) throws Exception{
    ChannelSftp channelSftp = connect();
    try {
      File file = new File(channelSftp.realpath(sftpSourcePath + fileName));
      System.out.println(">>>>>>folder: "+file);
      if(file.exists()){
        File localDownloadDir = new File("./downloads/");
        if(!localDownloadDir.exists()){
          localDownloadDir.mkdir();
        }
        boolean success = file.renameTo(new File(localDownloadDir, fileName));
        System.out.println(success);
      }else{
        System.out.println(">>>>>>>Error: File not found" );
      }
    }catch (Exception e){
      System.out.println(e.getLocalizedMessage());
    }finally {
      if (channelSftp != null && channelSftp.isConnected()) {
        channelSftp.disconnect();
      }
    }
  }

  public void downloadAllFiles(String sftpSourcePath) throws Exception{
    ChannelSftp channelSftp = connect();
    try{
      File folder = new File(channelSftp.realpath(sftpSourcePath));
      System.out.println(">>>>>>SourceFolder: "+folder);
      File localDownloadDir = new File("./downloads/");
      if(!localDownloadDir.exists()){
        localDownloadDir.mkdir();
      }
      File[] listOfFiles = folder.listFiles();
      for (File file : listOfFiles) {
        String fileName = file.getName();
        File fileToDownload = new File(channelSftp.realpath(sftpSourcePath + fileName));
        File newFile = new File(localDownloadDir, fileName);
        boolean success = fileToDownload.renameTo(newFile);
        System.out.println(newFile.getAbsoluteFile());
        System.out.println(success);
      }
    }catch (Exception e){
      System.out.println(e.getLocalizedMessage());
    }finally {
      if (channelSftp != null && channelSftp.isConnected()) {
        channelSftp.disconnect();
      }
    }
  }
}