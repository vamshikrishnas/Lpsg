import java.net.*;
import java.io.*;
import javax.swing.*;

class respserver implements Runnable
{
	cl parenobjt;
	int myport=0;
	
	respserver(cl obj,int port)
	{
		parenobjt=obj;
		myport=port;
		
		Thread t=new Thread(this)	;
		t.start();
	}
	
	public void run()
	{
		try
		{
			ServerSocket ss=new ServerSocket(myport);
			
			while (true)
			{
				Socket soc=ss.accept();
				DataInputStream din=new DataInputStream(soc.getInputStream());
				String status=din.readUTF();
				String fname=din.readUTF();
				
				
				if (status.equals("SUCCESS"))
				{
					ObjectInputStream oin=new ObjectInputStream(soc.getInputStream());
					byte b[]=(byte[])oin.readObject();
					
					FileOutputStream fout=new FileOutputStream(fname);
					fout.write(b);
					fout.close();
					oin.close();
					
					long end=System.currentTimeMillis();
					long ttkn=end-parenobjt.start;
					
					parenobjt.jta.append("File Name::"+fname+".Time Taken::"+ttkn+" ms\n");
					JOptionPane.showMessageDialog(null,"File "+fname+" downloaded Successfully. Total Time Taken 2 Dwnld file = "+ttkn+" ms");
					Runtime.getRuntime().exec("explorer "+fname.trim());
				}
				else
				{
					long end=System.currentTimeMillis();
					long ttkn=end-parenobjt.start;
					parenobjt.jta.append("File Name::"+fname+".Time Taken::"+ttkn+" ms\n");
					JOptionPane.showMessageDialog(null,"File "+fname+" not found.Total Time Taken= "+ttkn+" ms");
				}	
				
				din.close();
				soc.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}