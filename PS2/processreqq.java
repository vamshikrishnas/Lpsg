import java.io.*;
import java.net.*;
import java.util.*;

class processreqq implements Runnable
{
	proxy2 parent;
	Socket soc;
	//System.out.println(parent.tra)
	
	int ch;
	FileInputStream fin;
	String dirname="C:\\Users\\vamshi krishna s\\Desktop\\proxyserverfiles1";
	
	processreqq(proxy2 obj,Socket s)
	{
		parent=obj;
		soc=s;
		//readaddr();
		Thread t=new Thread(this);
		t.start();
	}
	
	
	
		
	
	public void run()
	{
		try
		{
			String trackeraddr=parent.trackeraddr,proxy1addr=parent.proxy1addr;
			DataInputStream din=new DataInputStream(soc.getInputStream());
			String req=din.readUTF();
			System.out.println(req);
			
			if (req.equals("download"))
			{
				parent.no_of_req++;
				parent.jta.append("Request is download..\n");
				String ip=din.readUTF();
				int port=din.readInt();
				String fname=din.readUTF();
				
				
				parent.jta.append("check for file in list\n");
				File f=new File(dirname);
				File files[]=f.listFiles();
				
				int i=0;
				for (i=0;i<files.length;i++)
				{
					String name=files[i].getName();
					if (fname.equalsIgnoreCase(name))
					{
						FileInputStream fin=new FileInputStream(dirname+"//"+name);
						byte b[]=new byte[fin.available()];
						fin.read(b);
						fin.close();
						
						Socket s=new Socket(ip,port);
						DataOutputStream dout=new DataOutputStream(s.getOutputStream());
						dout.writeUTF("SUCCESS");
						dout.writeUTF(name);
						
						ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
						oos.writeObject(b);
						
						parent.jta.append("File found and forwarded to client..\n") ;
						
						oos.close();
						dout.close();
						s.close();
						break;
					}
					
					
					
					
					
				}
				
				if (i==files.length)
				{
					parent.jta.append("File not found\n");
					Socket s=new Socket(trackeraddr,2000);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("PROXY2REQ");
					dout.writeUTF(ip);
					dout.writeInt(port);
					dout.writeUTF(fname);
					dout.close();
					s.close();
					parent.jta.append("Request forwarded to tracker.\n");
				}
				
			//	din.close();
			//	soc.close();
				
				
			}
			else
			if (req.equals("SERVERRESPONSE1"))
			{
				parent.jta.append("File response from server\n");
				String cip=din.readUTF();
				int cport=din.readInt();
				String fname=din.readUTF();
				ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
				byte b[]=(byte[]) oin.readObject();
				FileOutputStream fout=new FileOutputStream(dirname+"//"+fname);
				fout.write(b);
				fout.close();
				parent.jta.append("File saved in proxy.\n");
				
				Socket s=new Socket(cip,cport);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF("SUCCESS");
				dout.writeUTF(fname);
					
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(b);
				
				parent.jta.append("File forwardedt to client\n");
						
				oos.close();
				dout.close();
				s.close();
				
				oin.close();
				
				File f=new File(dirname);
				String files[]=f.list();
				Vector<String> v=new Vector<String>();
				for (int i=0;i<files.length;i++)
				v.add(files[i]);
				parent.jlist.setListData(v);
				
				Socket soc1=new Socket(trackeraddr,2000);
				DataOutputStream dout1=new DataOutputStream(soc1.getOutputStream());
				dout1.writeUTF("PROXY2INFO");
				ObjectOutputStream oos1=new ObjectOutputStream(soc1.getOutputStream());
				oos1.writeObject(v);
				oos1.close();
				dout1.close();
				soc1.close();
				
			}
			else
			if (req.equals("SERVERRESPONSE2"))
			{
				parent.jta.append("File response from server\n");
				String cip=din.readUTF();
				int cport=din.readInt();
				String fname=din.readUTF();
				Socket s=new Socket(cip,cport);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF("FAIL");
				dout.writeUTF(fname);
				
				parent.jta.append("File not found, response forwarded to client.\n");
				
				dout.close();
				s.close();
			}
			else
			if (req.equals("download1"))
			{
				String cip=din.readUTF();
				int cport=din.readInt();
				String fname=din.readUTF();
				
				parent.jta.append("check for file in list\n");
				File f=new File(dirname);
				File files[]=f.listFiles();
				int i=0;
				for (i=0;i<files.length;i++)
				if (fname.equalsIgnoreCase(files[i].getName()))
				break;
				
				FileInputStream fin=new FileInputStream(dirname+"//"+files[i].getName());
				byte b[]=new byte[fin.available()];
				fin.read(b);
				fin.close();
						
				Socket s=new Socket(proxy1addr,1500);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF("FORWARD");
				dout.writeUTF(cip);
				dout.writeInt(cport);
				dout.writeUTF(files[i].getName());
						
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(b);
				
				parent.jta.append("File found and forwarded to Proxy1..\n") ;
						
				oos.close();
				dout.close();
				s.close();
			}
			else
			if (req.equals("FORWARD"))
			{
				parent.jta.append("File response from Proxy1\n");
				String cip=din.readUTF();
				int cport=din.readInt();
				String fname=din.readUTF();
				ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
				byte b[]=(byte[]) oin.readObject();
				FileOutputStream fout=new FileOutputStream(dirname+"//"+fname);
				fout.write(b);
				fout.close();
				parent.jta.append("File saved in proxy.\n");
				
				Socket s=new Socket(cip,cport);
				DataOutputStream dout=new DataOutputStream(s.getOutputStream());
				dout.writeUTF("SUCCESS");
				dout.writeUTF(fname);
					
				ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
				oos.writeObject(b);
				
				parent.jta.append("File forwarded to client\n");
						
				oos.close();
				dout.close();
				s.close();
				
				oin.close();
				
				File f=new File(dirname);
				String files[]=f.list();
				Vector<String> v=new Vector<String>();
				for (int i=0;i<files.length;i++)
				v.add(files[i]);
				parent.jlist.setListData(v);
				
				Socket soc1=new Socket(trackeraddr,2000);
				DataOutputStream dout1=new DataOutputStream(soc1.getOutputStream());
				dout1.writeUTF("PROXY2INFO");
				ObjectOutputStream oos1=new ObjectOutputStream(soc1.getOutputStream());
				oos1.writeObject(v);
				oos1.close();
				dout1.close();
				soc1.close();
				
				
			}
			
			
			din.close();
			soc.close();
		}
		catch(Exception e)
		{
			System.out.println("in process req:"+e);
		}
	}
}