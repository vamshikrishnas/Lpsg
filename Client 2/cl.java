import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.net.*;
import java.io.*;


class cl implements ActionListener
{
	JFrame jf;
	JTextField jtext,jport,jip;
	JButton download,clear,exit,systeminfo;
	int portno=0;
	respserver r=null;
	long start;
	JTextArea jta;
	
	cl()
	{
		create_client_window();
		Thread t=new Thread();
		t.start();
	}
	
	public void run()
	{
		
	}
	void create_client_window()
	{
		jf=new JFrame("Client") ;
		Container cp=jf.getContentPane();
		cp.setLayout(null);
		
		JLabel jl=new JLabel("Client side of the architecture ",JLabel.CENTER);
		jl.setFont(new Font("Dialog",Font.BOLD,24));
		JLabel portno=new JLabel("Port Number:");
		jport=new JTextField();
		
		JLabel filename=new JLabel("Enter The File Name:");
		jtext=new JTextField();
		JLabel ipaddr=new JLabel("IP Addr of PS:");
		jip=new JTextField();
		download=new JButton("DOWNLOAD");
		clear=new JButton("CLEAR");
		exit=new JButton("EXIT");
		systeminfo=new JButton("SYSTEM INFO");
		jta=new JTextArea("CLIENT STATISTICS::\n");
		JScrollPane jsp=new JScrollPane(jta);
		jta.setBackground(Color.BLACK);
		jta.setForeground(Color.RED);
		JLabel jla=new JLabel("CSE FINAL YEAR PROJECT 2012");
		jl.setBounds(0,0,400,30);
		portno.setBounds(20,100,180,25);
		jport.setBounds(190,100,180,25);
		filename.setBounds(20,140,180,25);
		jtext.setBounds(190,140,180,25);
		ipaddr.setBounds(20,180,180,25);
		jip.setBounds(190,180,180,25);
		download.setBounds(20,300,120,30);
		clear.setBounds(150,300,100,30);
		exit.setBounds(270,300,100,30);
		systeminfo.setBounds(100,400, 150, 30);
		jla.setBounds(100,350,500,20);
		jsp.setBounds(400,10,400,400);
		
		cp.add(jl);
		cp.add(portno);
		cp.add(jport);
		cp.add(filename);
		cp.add(jtext);
		cp.add(download);
		cp.add(clear);
		cp.add(exit);
		cp.add(jla);
		cp.add(systeminfo);
		cp.add(jsp);
		cp.add(ipaddr);
		cp.add(jip);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		
		jf.setSize(d.width,d.height);
		jf.setVisible(true);
		
		download.addActionListener(this);
		clear.addActionListener(this);
		exit.addActionListener(this);
		systeminfo.addActionListener(this);
	}
	
	void sendreq() 
	{
		try
		{
			String fname=jtext.getText().trim();
			String port=jport.getText();
			String proxyaddr=jip.getText().trim();
			
			if (!fname.equals("") && !port.equals(""))
			{
				String ip=InetAddress.getLocalHost().getHostAddress();
				portno=Integer.parseInt(port);
				
				if (r==null)
					r=new respserver(this,portno);
				
				//start=System.currentTimeMillis();
				Socket s=new Socket(proxyaddr,1500);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF("download");
				start=System.currentTimeMillis();
				dout.writeUTF(ip);
				dout.writeInt(portno);
				dout.writeUTF(fname);
			
				dout.close();
				s.close();
			}
			else
			{
				JOptionPane.showMessageDialog(null,"Some required info missing..");
			}	
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource()==download)
		{
			sendreq();
		}
		
		if (ae.getSource()==clear)
		{
			jtext.setText("");
		}
		
		if (ae.getSource()==exit)
		{
			System.exit(0);
		}
		if(ae.getSource()==systeminfo)
		{
			System.out.print("System Info");
			try {
				//System.out.print("IP Address of the System:"+InetAddress.getLocalHost());
				JOptionPane.showMessageDialog(null,"IP Address of the System: "+InetAddress.getLocalHost().getHostAddress()+"\nHost Name:"+InetAddress.getLocalHost().getHostName()+"\n");
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	
	public static void main(String args[])
	{
		new cl();
	}
}