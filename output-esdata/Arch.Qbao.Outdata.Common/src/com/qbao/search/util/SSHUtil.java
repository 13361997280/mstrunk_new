package com.qbao.search.util;

 
import java.io.InputStream; 

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
 

public class SSHUtil {
	public static String executeSSHWithPublicKey( String ip,int port, String username, String id_rsa, String command ) throws Exception {
		if(  command==null||command.equals("") )
			return "";
		StringBuilder strBuffer=new StringBuilder();
		try{			
			JSch jsch=new JSch();
			jsch.addIdentity(id_rsa);
			Session session=jsch.getSession(username, ip, port);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
			Channel channel=session.openChannel("exec");
			ChannelExec ssh=(ChannelExec)channel;		     
		    InputStream InputStream = ssh.getInputStream();
		    InputStream ErrStream = ssh.getErrStream();		    
		    byte[] tmp = new byte[1024];		    
		    ssh.setCommand(command);
		    ssh.connect();		    
		    while(true){ 
		        while(ErrStream.available() > 0){
		            int i = ErrStream.read(tmp, 0, 1024);
		            if(i < 0){
		            	break;
		            }		                    
		            strBuffer.append(new String(tmp, 0, i));
		        }	            
		        while(InputStream.available() > 0){
		            int i = InputStream.read(tmp, 0, 1024);		                
		            if(i < 0){
		            	break;
		            }
		            strBuffer.append(new String(tmp, 0, i));
		        }
		        if(ssh.isClosed()){
		            break;
		        }
		        try{
		            Thread.sleep(100);
		        }
		        catch(Exception ee){
		        }
		    }
		    ssh.disconnect();
		    session.disconnect();
		    return strBuffer.toString();
		}
		catch(Exception e){
			strBuffer.append(e);
	    }
		return strBuffer.toString();
	}
	public static String executeSSHWithPassword( String ip,int port, String username, String password, String command ) throws Exception {
		if(  command==null||command.equals("") )
			return "";		
		StringBuilder strBuffer=new StringBuilder();
		try{			
			JSch jsch=new JSch();		     
			Session session=jsch.getSession(username, ip, port);
			session.setPassword(password); 
			session.setConfig("StrictHostKeyChecking", "no");
			 
			session.connect();
			Channel channel=session.openChannel("exec");
			ChannelExec ssh=(ChannelExec)channel;		     
		    InputStream InputStream = ssh.getInputStream();
		    InputStream ErrStream = ssh.getErrStream();   
		    
		    byte[] tmp = new byte[1024];		    
		    ssh.setCommand(command);
		    ssh.connect();		    
		    while(true){ 
		        while(ErrStream.available() > 0){
		            int i = ErrStream.read(tmp, 0, 1024);
		            if(i < 0){
		            	break;
		            }		                    
		            strBuffer.append(new String(tmp, 0, i));
		        }		            
		        while(InputStream.available() > 0)
		        {
		            int i = InputStream.read(tmp, 0, 1024);		                
		            if(i < 0){
		            	break;
		            }
		            strBuffer.append(new String(tmp, 0, i));
		        }		        
		        if(ssh.isClosed()){		                
		            break;
		        }
		        
		        try{
		            Thread.sleep(100);
		        }
		        catch(Exception ee){
		        }
		    }
		    ssh.disconnect();
		    return strBuffer.toString();
		}
		catch(Exception e){
			strBuffer.append(e);
	    }
		return strBuffer.toString();
	}
	public static void main(String[] args) throws Exception  {
		System.out.println(  executeSSHWithPassword("192.168.81.142",1022,"op1","csearch,142","cd /usr/arch/search/vacation_deploy_2013_01_15_15_13_15/bin ; ./deploy.sh -step deployengine -ip 192.168.81.143 -e dev "));
		 
	}
}
