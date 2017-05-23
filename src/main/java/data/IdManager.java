package data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class IdManager {
	PrintWriter writer;
	
	Map<String, Integer> m;
	int num;
	public IdManager() {
		super();
		File f=new File("E://id.txt");
		try {
			writer=new PrintWriter(f);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m =new HashMap<String, Integer>();
		num=1;
	}
	public void close()
	{
		writer.flush();
		writer.close();
	}
	public Integer getNumero(String s)
	{
		if(m.get(s) != null)
		{
			return m.get(s);
		}
		else
		{
			num++;
			m.put(s, num);
			writer.println("{\"number\":\""+m.get(s)+"\";\"string\":\""+s+"\"}");
			return m.get(s);
		}
	}
}
