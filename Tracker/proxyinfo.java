import java.net.*;
import java.io.*;
import java.util.*;

class proxyinfo implements Runnable
{
	Socket soc;
	tracker parent;
	int ch;
	String serveraddr="",proxy1addr="",proxy2addr="";
	FileInputStream fin;
	
	proxyinfo(tracker obj,Socket s)
	{
		parent=obj;
		soc=s;
		readaddr();
		Thread t=new Thread(this);
		t.start();
	}
	
	void readaddr()
	{
		try
		{
			fin=new FileInputStream("server.txt");
			while((ch=fin.read())!=-1)
			serveraddr+=(char)ch;
			serveraddr.trim();
			System.out.println(serveraddr);
			
			fin=new FileInputStream("proxy1.txt");
			while((ch=fin.read())!=-1)
			proxy1addr+=(char)ch;
			proxy1addr.trim();
			System.out.println(proxy1addr);
			fin=new FileInputStream("proxy2.txt");
			while((ch=fin.read())!=-1)
			proxy2addr+=(char)ch;
			proxy2addr.trim();
			System.out.println(proxy2addr);
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	public void run()
	{
		try
		{
			DataInputStream din=new DataInputStream(soc.getInputStream());
			String req=din.readUTF();
			
			if (req.equals("PROXY1INFO"))
			{
				parent.jta.append("File names received from proxy1.\n");
				ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
				Vector<String> v=(Vector<String>) oin.readObject();
				oin.close();
				parent.jlist1.setListData(v);
				
			}
			else
			if (req.equals("PROXY2INFO"))
			{
				parent.jta.append("File names received from proxy2.\n");
				ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
				Vector<String> v=(Vector<String>) oin.readObject();
				oin.close();
				parent.jlist2.setListData(v);
				
			}
			else
			if (req.equals("PROXY1REQ"))
			{
				
				parent.no_of_reqs++;
				parent.jta.append("Request for file from proxy1..\n");
				String cip=din.readUTF();
				int cport=din.readInt();
				String fname=din.readUTF();
				
				int size=parent.jlist2.getModel().getSize();
				boolean found=false;
				
				for (int i=0;i<size;i++)
				{
					if ((parent.jlist2.getModel().getElementAt(i).toString()).equalsIgnoreCase(fname))
					{
						found=true;
						break;
					}
				}
				
				if (found)
				{
					Socket s=new Socket(proxy2addr,1800);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("download1");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					dout.close();
					parent.jta.append("Request forwarded to Proxy2.\n");
				}
				else
				{
					parent.no_of_server++;
					Socket s=new Socket(serveraddr,1900);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("PROXY1REQ");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					dout.close();
					parent.jta.append("Request forwarded to server.\n");
				}
			}
			else
			if (req.equals("PROXY2REQ"))
			{
				parent.no_of_reqs++;
				parent.jta.append("Request for file from proxy2..\n");
				String cip=din.readUTF();
				int cport=din.readInt();
				String fname=din.readUTF();
				
				int size=parent.jlist1.getModel().getSize();
				boolean found=false;
				
				for (int i=0;i<size;i++)
				{
					if ((parent.jlist1.getModel().getElementAt(i).toString()).equalsIgnoreCase(fname))
					{
						found=true;
						break;
					}
				}
				
				if (found)
				{
					Socket s=new Socket(proxy1addr,1500);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("download1");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					dout.close();
					parent.jta.append("Request forwarded to Proxy1.\n");
				}
				else
				{
					parent.no_of_server++;
					Socket s=new Socket(serveraddr,1900);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("PROXY2REQ");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					dout.close();
					parent.jta.append("Request forwarded to server.\n");
				}
			}
			
			
			
			din.close();
				soc.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}