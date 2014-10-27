import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;
import java.net.*;

class server implements ActionListener,Runnable
{
	JFrame jf;
	JButton jbexit,info,stats;
	JList jlist;
	JTextArea jta;
	int no_of_req=0;
	server()
	{
		createwin();
		Thread t=new Thread(this);
		t.start();
	}
	
	void createwin()
	{
		jf=new JFrame("SERVER");
		Container cp=jf.getContentPane();
		cp.setLayout(null);
		
		JLabel jl=new JLabel("Server Side of the Architecture ",JLabel.CENTER);
		jl.setFont(new Font("Dialog",Font.BOLD,20));
		jl.setForeground(Color.BLACK);
		
		JLabel jl1=new JLabel("Server Activity:");
		jta=new JTextArea("Waiting for request..\n");
		JScrollPane jsp=new JScrollPane(jta);
		jta.setBackground(Color.WHITE);
		jta.setForeground(Color.BLUE);
		
		JLabel jl2=new JLabel("List of available files:");
		String dirname="C:\\Users\\vamshi krishna s\\Desktop\\serverfiles";
		File f=new File(dirname);
		
		String files[]=f.list();
		jlist=new JList(files);
		JScrollPane jsp2=new JScrollPane(jlist) ;
		
		jbexit=new JButton("EXIT");
		info=new JButton("System Info");
		stats=new JButton("Statistics");
		cp.add(jl);
		cp.add(jl1);
		cp.add(jsp);
		cp.add(jl2);
		cp.add(jsp2);
		cp.add(jbexit);
		cp.add(info);
		cp.add(stats);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		
		jl.setBounds(0,0,d.width,30);
		jl1.setBounds(10,50,400,20);
		jsp.setBounds(10,80,600,600);
		jl2.setBounds(630,50,150,20);
		jsp2.setBounds(630,80,300,300);
		jbexit.setBounds(700,400,100,30);
		info.setBounds(850,400,150,30);
		stats.setBounds(700,550,150,30);
		jf.setSize(d.width,d.height);
		jf.setVisible(true);
		jbexit.addActionListener(this);
		info.addActionListener(this);
		stats.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==jbexit)
		{
			System.exit(0);
		}
		else if(ae.getSource()==info)
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
			JOptionPane.showMessageDialog(null,"Total No of Requests handled By Server In this Session\n "+no_of_req+"\n");
		}
	}
	
	
	public void run()
	{
		try
		{
			ServerSocket ss=new ServerSocket(1900);
			while(true)
			{
				Socket s=ss.accept();
				jta.append("\nRequest recedived from tracker\n");
	//			no_of_req++;
				new serverprocess(this,s);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[])
	{
		new server();
	}
}