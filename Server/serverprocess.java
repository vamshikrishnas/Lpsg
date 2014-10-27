import java.io.*;
import java.net.*;

class serverprocess implements Runnable
{
	server parent;
	Socket soc;
	int ch;
	String proxy1addr="",proxy2addr="";
	FileInputStream fin;
	String dirname="C:\\Users\\vamshi krishna s\\Desktop\\serverfiles";
	
	serverprocess(server obj,Socket s)
	{
		parent=obj;
		soc=s;
		readaddr();
		
		Thread t=new Thread(this);
		parent.jta.append("Server Thread Started");
		
		t.start();
		
	}
	
	void readaddr()
	{
		try
		{
			fin=new FileInputStream("proxy1.txt");
			while((ch=fin.read())!=-1)
			proxy1addr+=(char)ch;
			proxy1addr.trim();
			
			fin=new FileInputStream("proxy2.txt");
			while((ch=fin.read())!=-1)
			proxy2addr+=(char)ch;
			proxy2addr.trim();
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
			parent.no_of_req++;
			DataInputStream din=new DataInputStream(soc.getInputStream());
			String req=din.readUTF();
			String cip=din.readUTF();
			int cport=din.readInt();
			String fname=din.readUTF();
			parent.jta.append("File Name is "+fname+"\n");
			
			boolean found=false;
			int size=parent.jlist.getModel().getSize();
			
			int i=0;
			for (i=0;i<size;i++)
			{
				if ((parent.jlist.getModel().getElementAt(i).toString()).equalsIgnoreCase(fname))
				{
					found=true;
					break;
				}
			}
			
			if (req.equals("PROXY1REQ"))
			{
				if (found)
				{
					FileInputStream fin=new FileInputStream(dirname+"//"+parent.jlist.getModel().getElementAt(i).toString());
					byte b[]=new byte[fin.available()];
					fin.read(b);
					fin.close();
					
					Socket s=new Socket(proxy1addr,1500);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("SERVERRESPONSE1");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(b);
					oos.close();
					dout.close();
					s.close();
					
					parent.jta.append("File found and forwarded to Proxy1.\n ");
				}
				else
				{
					Socket s=new Socket(proxy1addr,1500);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("SERVERRESPONSE2");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					dout.close();
					s.close();
					parent.jta.append("File not found. Response sent to proxy1.\n");
				}
			}
			else
			if (req.equals("PROXY2REQ"))
			{
				if (found)
				{
					FileInputStream fin=new FileInputStream(dirname+"//"+parent.jlist.getModel().getElementAt(i).toString());
					byte b[]=new byte[fin.available()];
					fin.read(b);
					fin.close();
					
					Socket s=new Socket(proxy2addr,1800);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("SERVERRESPONSE1");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
					oos.writeObject(b);
					oos.close();
					dout.close();
					s.close();
					parent.jta.append("File found and frowarded to Proxy2.\n");
				}
				else
				{
					Socket s=new Socket(proxy2addr,1800);
					DataOutputStream dout=new DataOutputStream(s.getOutputStream());
					dout.writeUTF("SERVERRESPONSE2");
					dout.writeUTF(cip);
					dout.writeInt(cport);
					dout.writeUTF(fname);
					dout.close();
					s.close();
					parent.jta.append("File not found. Response sent to proxy2.\n");
				}
			}
			
			
			
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
	}
}