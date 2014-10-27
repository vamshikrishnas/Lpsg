import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;
import java.util.Vector;

class proxy2 implements ActionListener,Runnable
{
	FileInputStream fin;
	JFrame jf;
	JButton jbexit,proxyinfo,stats;
	JTextArea jta;
	JList jlist;
	Vector<String> v=new Vector<String>();
	String trackeraddr="",proxy1addr="";
	int ch;
	int no_of_req=0;
	proxy2()
	{
		createproxy();
		readaddr();
		Thread t=new Thread(this);
		t.start();
	}
	
	void readaddr()
	{
		try
		{
			fin=new FileInputStream("tracker.txt");
			while((ch=fin.read())!=-1)
			trackeraddr+=(char)ch;
			trackeraddr.trim();
			System.out.println(trackeraddr);
			
			fin=new FileInputStream("proxy1.txt");
			while((ch=fin.read())!=-1)
			proxy1addr+=(char)ch;
			proxy1addr.trim();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	void createproxy()
	{
		jf=new JFrame("PROXY_SERVER");
		Container cp=jf.getContentPane();
		cp.setLayout(null);
		
		JLabel jl=new JLabel("PROXY SERVER ##2",JLabel.CENTER);
		jl.setFont(new Font("Times New Roman",Font.BOLD,28));
		jl.setForeground(Color.BLACK);
		
		JLabel jl1=new JLabel("Details of Requests given to Proxy1:");
		jta=new JTextArea("Waiting for request..\n");
		JScrollPane jsp=new JScrollPane(jta);
		jta.setBackground(Color.WHITE);
		jta.setForeground(Color.BLACK);
	
		
		JLabel jl2=new JLabel("List of available files in the proxy Server:");
		String dirname="C:\\Users\\vamshi krishna s\\Desktop\\proxyserverfiles1";
		File f=new File(dirname);
		String files[]=f.list();
		for (int i=0;i<files.length;i++)
		v.add(files[i]);
		jlist=new JList(files);
		JScrollPane jsp2=new JScrollPane(jlist) ;
		
		jbexit=new JButton("EXIT");
		proxyinfo=new JButton("System Info");


		stats=new JButton("Statistics");
		JLabel jla=new JLabel("CSE FINAL YEAR PROJECT 2012",JLabel.RIGHT);
		
		cp.add(jl);
		cp.add(jl1);
		cp.add(jsp);
		cp.add(jl2);
		cp.add(jsp2);
		cp.add(jbexit);
		cp.add(jla);
		cp.add(proxyinfo);
		cp.add(stats);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		

		jl.setBounds(0,0,d.width,30);
		jl1.setBounds(10,50,400,20);
		jsp.setBounds(10,80,600,600);
		jl2.setBounds(630,50,300,30);
		jsp2.setBounds(630,80,300,300);
		jbexit.setBounds(700,400,150,30);
		proxyinfo.setBounds(700,500,150,30);
		stats.setBounds(700,600,150,30);
		jf.setSize(d.width,d.height);
		jf.setVisible(true);
		jbexit.addActionListener(this);
		proxyinfo.addActionListener(this);
		stats.addActionListener(this);
	}
	
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jbexit)
		{
			System.exit(0);
		}
		else if(ae.getSource()==proxyinfo)
		{
			System.out.print("System Info");
			try {
				
				JOptionPane.showMessageDialog(null,"IP Address of the System: "+InetAddress.getLocalHost().getHostAddress()+"\nHost Name:"+InetAddress.getLocalHost().getHostName()+"\n");
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(ae.getSource()==stats)
		{
				JOptionPane.showMessageDialog(null,"Total No of Requests handled By Proxy Server In this Session\n "+no_of_req+"\n");
		}
	}
	
	
	public void run()
	{
		try
		{
			jta.append("Thread Started.");
			Socket soc=new Socket(trackeraddr,2000);
			DataOutputStream dout=new DataOutputStream(soc.getOutputStream());
			dout.writeUTF("PROXY2INFO");
			ObjectOutputStream oos=new ObjectOutputStream(soc.getOutputStream());
			oos.writeObject(v);
			oos.close();
			dout.close();
			soc.close();
			
			ServerSocket ss=new ServerSocket(1800);
			
			while(true)
			{
				Socket s=ss.accept();
				jta.append("\nNew Client Request Received..\n");
				new processreqq(this,s);
			}
		}
		catch(Exception e)
		{
			System.out.println(e+"Error in Server");
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		new proxy2();
	}
}