package querying;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class ManifestPuller extends Thread{
	public static File f4;
	public static File f3;
	public static File f5;
	static PrintWriter writer = null;
	static PrintWriter writer2 = null;
	public void run()
	{
		f4=new File("E:\\Manifests.txt");
		try {
			if(!f4.exists())
			{
				f4.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f5=new File("E:\\NoLatest.txt");
		try {
			if(!f5.exists())
			{
				f5.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		f3=new File("E:\\noDuplicatesFrom1000.txt");
		try {
			if(!f3.exists())
			{
				f3.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pullRegistry();
		
		
		
		
	}
	private static void pullRegistry() {
		// TODO Auto-generated method stub
		
		try {
			writer = new PrintWriter(f4, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer2 = new PrintWriter(f5, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			BufferedReader br = null;
			FileReader fr = null;
		try {

			fr = new FileReader(f3);
			br = new BufferedReader(fr);

			String sCurrentLine;

			br = new BufferedReader(new FileReader(f3));

			/*while ((sCurrentLine = br.readLine()) != null) {
				pullHttp(sCurrentLine);
			}*/
			int i=0;
			while (i<1000) {
				i++;
				sCurrentLine = br.readLine();
		}
			String s="";
			while (i<1050) {
				System.out.println(i);
				i++;
				sCurrentLine = br.readLine();
				System.out.println(sCurrentLine);
				s=s+pullHttp(sCurrentLine);
		}
			writer.write(s);
			writer.flush();
			writer.close();
			writer2.flush();
			writer2.close();
		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}

		}
	}
	private static String pullHttp(String nome) {	

		String url2 = "https://auth.docker.io/token?service=registry.docker.io&scope=repository:" + nome + ":pull";
		URL obj2 = null;

		try {
			obj2 = new URL(url2);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpURLConnection con2 = null;
		try {
			con2 = (HttpURLConnection) obj2.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// optional default is GET
		try {
			con2.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add request header
		// con.setRequestProperty("username", "simoneerba");
		//System.out.println("content       " + con2.getHeaderField("Www-Authenticate"));
		try {
			String s2 = con2.getResponseMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in2 = null;
		try {
			in2 = con2.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String encoding2 = con2.getContentEncoding();
		encoding2 = encoding2 == null ? "UTF-8" : encoding2;
		String body2 = null;
		try {
			body2 = IOUtils.toString(in2, encoding2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(body2);
		int responseCode2 = 0;
		try {
			responseCode2 = con2.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	System.out.println("\nSending 'GET' request to URL : " + url2);
	//	System.out.println("\nmessaggio : " + s2);
		//System.out.println("Response Code : " + responseCode2);
		JSONObject json = new JSONObject(body2);
		String token = json.getString("token");
		
		String url4 = "https://registry-1.docker.io/v2/"+nome+"/tags/list";
		URL obj4 = null;

		try {
			obj4 = new URL(url4);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpURLConnection con4 = null;
		try {
			con4 = (HttpURLConnection) obj4.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// optional default is GET
		try {
			con4.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add request header
		// con.setRequestProperty("username", "simoneerba");
		con4.setRequestProperty("Authorization", "Bearer " + token);
	//	System.out.println("content       " + con3.getHeaderField("Www-Authenticate"));

		try {
			String s4 = con4.getResponseMessage();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int responseCode4 = 0;
		try {
			responseCode4 = con4.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(responseCode4==200)
		{
		
		InputStream in4 = null;
		try {
			in4 = con4.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String encoding4 = con4.getContentEncoding();
		encoding4 = encoding4 == null ? "UTF-8" : encoding4;
		String body4 = null;
		try {
			body4= IOUtils.toString(in4, encoding4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject json4 = new JSONObject(body4);
		JSONArray tags=json4.getJSONArray("tags");
		String s="";
		for(int i=0;i<tags.length();i++)
		{
			String tag=tags.getString(i);
			System.out.println(i+"          "+tag);
			s+=pullTag(nome,tag,token);
		}
		return s;
		}
		return "";
		
}
	
	private static String pullTag(String nome, String tag, String token) {
		// TODO Auto-generated method stub
		//System.out.println(token);
		String url3 = "https://registry-1.docker.io/v2/" + nome + "/manifests/"+tag;
		URL obj3 = null;

		try {
			obj3 = new URL(url3);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpURLConnection con3 = null;
		try {
			con3 = (HttpURLConnection) obj3.openConnection();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// optional default is GET
		try {
			con3.setRequestMethod("GET");
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// add request header
		// con.setRequestProperty("username", "simoneerba");
		con3.setRequestProperty("Authorization", "Bearer " + token);
	//	System.out.println("content       " + con3.getHeaderField("Www-Authenticate"));

		try {
			String s3 = con3.getResponseMessage();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int responseCode3 = 0;
		try {
			responseCode3 = con3.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(responseCode3!=200)
		{
			writer2.println(nome);
		}
		//System.out.println("Response Code : " + responseCode3);
		if(responseCode3==200)
		{
		
		InputStream in3 = null;
		try {
			in3 = con3.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String encoding3 = con3.getContentEncoding();
		encoding3 = encoding3 == null ? "UTF-8" : encoding3;
		String body3 = null;
		try {
			body3 = IOUtils.toString(in3, encoding3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//body3=body3.substring(1,body3.length()-1);
		//System.out.println(body3);
		//body3="["+body3+"]";
		JSONObject json2 = new JSONObject(body3);
		//op.insert(json2);
	

		return pullHttpFile(json2,tag);
		
		}
		else
		{
			return "";
		}
	}
	private static String pullHttpFile(JSONObject json2, String tag)
	{
		String repo=json2.getString("name");
		int index=repo.lastIndexOf("/");
		if(index==-1)
		{
			repo="library/"+repo;
			repo.lastIndexOf("/");
		}
		String name=repo.substring(0,index);
		String surname=repo.substring(index+1,repo.length());	
		JSONArray layers2=json2.getJSONArray("fsLayers");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String s=	dateFormat.format(date); //2016/11/16 12:08:4

			return "{name:\""+name+"\",surname:\""+surname+"\",tag:\""+tag+"\",date:\""+s+"\",layers:"+layers2+"}\n";
	}	
}
