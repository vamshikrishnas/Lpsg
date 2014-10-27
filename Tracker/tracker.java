import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.net.*;

class tracker implements ActionListener,Runnable
{
	
	JFrame jf;
	JButton exit,trackerinfo,stats;
	JList jlist1,jlist2;
	JTextArea jta;
	int no_of_reqs=0,no_of_server=0;
	
	tracker()
	{
		create_tracker();
		Thread t=new Thread(this);
		t.start();
	}
	
	void create_tracker()
	{
		jf=new JFrame("Tracker");
		Container cp=jf.getContentPane();
		cp.setLayout(null);
		
		JLabel jl=new JLabel("TRACKER",JLabel.CENTER);
		jl.setFont(new Font("Times new Roman",Font.BOLD,28));
		jl.setForeground(Color.BLACK);
		
		JLabel jl1=new JLabel("Details of Requests given to Tracker:");
		jta=new JTextArea("Waiting for request..\n");
		JScrollPane jsp=new JScrollPane(jta);
		jta.setBackground(Color.WHITE);
		jta.setForeground(Color.RED); 
		
		JLabel jl2=new JLabel("List of Files Available in Proxy Server 1:");
		jlist1=new JList();
		JScrollPane jsp2=new JScrollPane(jlist1) ;
		
		JLabel jl3=new JLabel("List of available files in Proxy Server 2:");
		jlist2=new JList();
		JScrollPane jsp3=new JScrollPane(jlist2) ;
		
		exit=new JButton("EXIT");
		trackerinfo=new JButton("Tracker Info");
		stats=new JButton("Statistics");
		cp.add(jl);
		cp.add(jl1);
		cp.add(jsp);
		cp.add(jl2);
		cp.add(jsp2);
		cp.add(jl3);
		cp.add(jsp3);
		cp.add(exit);
		cp.add(trackerinfo);
		cp.add(stats);
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		
		jl.setBounds(0,0,d.width,30);
		jl2.setBounds(10,50,300,20);
		jsp2.setBounds(10,80,300,300);
		jl3.setBounds(380,50,300,20);
		jsp3.setBounds(380,80,300,300);
		jl1.setBounds(10,420,400,20);
		jsp.setBounds(10,450,600,200);
		exit.setBounds(700,400,150,30);
		trackerinfo.setBounds(700,500,150,30);
		stats.setBounds(700,600,150,30);
		
		jf.setSize(d.width,d.height);
		jf.setVisible(true);
		exit.addActionListener(this);
		trackerinfo.addActionListener(this);
		stats.addActionListener(this);
		
	}
	
	public void run()
	{
		try
		{
			jta.append("Thread Started in Tracker\n");
			ServerSocket ss=new ServerSocket(2000);//opening server port in tracker
			
			while (true)
			{
				Socket soc=ss.accept();
				jta.append("Request from proxy.\n");
				//no_of_reqs++;
				new proxyinfo(this,soc);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==exit)
			System.exit(0);
		else if(ae.getSource()==trackerinfo)
		{
			try {
				
					JOptionPane.showMessageDialog(null,"IP Address of the System: "+InetAddress.getLocalHost().getHostAddress()+"\nHost Name:"+InetAddress.getLocalHost().getHostName()+"\n");
				
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else if(ae.getSource()==stats)
		{
			//System.out.println("No of requests handled by tracker in this session::\n"+no_of_reqs+"\n No of reqs Forwarded 2 Server in this session"+no_of_server);
			JOptionPane.showMessageDialog(null,"No of requests handled by tracker in this session::\n"+no_of_reqs+"\n No of reqs Forwarded 2 Server in this session::\n"+no_of_server);
		}
	}
	
	
	public static void main(String args[])
	{
		new tracker();
	}
}