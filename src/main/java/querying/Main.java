package querying;
import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpRequest;
import groovy.ui.SystemOutputInterceptor;

import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.json.JSONArray;
import org.json.JSONObject;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.Info;
import com.github.dockerjava.api.model.SearchItem;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;


enum MyRelationshipTypes implements RelationshipType
{
    DEPENDENCY
}

public class Main {

public static File f;
public static File f2;
public static File f4;
public static File f5;
static Direction[] directions=Direction.values();//OUT IN BOTH
static GraphOperations op;
//static Graph graph = new Neo4jGraph("E:\\DbDocker");

	public static void main(String[] args) {
		f4=new File("E:\\c.txt");
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
		op=new GraphOperations("E:\\neo4jdatabase");
		//op.pulisci();
		System.out.println("tot "+Runtime.getRuntime().totalMemory()); 

		// Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
		System.out.println("max "+Runtime.getRuntime().maxMemory());

		 // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
		System.out.println("free "+Runtime.getRuntime().freeMemory());
		readManifests();
		op.newT();
		op.createRelationships();
		//getUserNames();


		//removeDuplicates();
		//getUserNames();
		//NamespacePuller m1=new NamespacePuller();
		//m1.start();
		//ManifestPuller m=new ManifestPuller();
		//m.start();
		//op.pulisci();
		//pullSinglev1Page(6509);
		//myreg();
		//searchApi("ryccoo");
		//searchHttp("ngix");
	//pullHttp("eliotk/elasticsearch-beta");
		// readManifests();
		//quayCatalog();
		//searchv121();
	//	pullQuay("gsmiro/registry");
		//getUserNames();
		//stampa();
		//addFiles();
		System.out.println("fin qui");
		op.close();
		op.getGraph().shutdown();
		
	}
	
	private static void searchv121() {
		String url = "https://registry-1.docker.io/v1.26/images/json";

		URL obj = null;
		try {
			obj = new URL(url);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpURLConnection con = null;
		try {
			con = (HttpURLConnection) obj.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// add request header
		con.setRequestProperty("content-type", "application/json");
		String s = null;
		try {
			s = con.getResponseMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int responseCode = 0;
		try {
			responseCode = con.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in2 = null;
		try {
			in2 = con.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String encoding2 = con.getContentEncoding();
		encoding2 = encoding2 == null ? "UTF-8" : encoding2;
		String body2 = null;
		try {
			body2 = IOUtils.toString(in2, encoding2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("\nbody: " + body2);
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("\nmessaggio : " + s);
		System.out.println("Response Code : " + responseCode);
		System.out.println("content       " + con.getHeaderField("Www-Authenticate"));
	}

	private static void pullQuay(String repo) {
		// TODO Auto-generated method stub
		try {
			String url = "https://quay.io/api/v1/repository/" + repo+"/image/";
			//String url = "https://quay.io/api/v1/repository/" + repo+"/manifest/latest:a:manifestref/labels";
			//String url = "https://quay.io/api/v1/repository/" + repo+"/logs";
			URL obj;

			obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			// add request header
		//	con.setRequestProperty("content-type", "application/json");
		//	con.setRequestProperty("Accept", "application/json");
			// con.setRequestProperty("username", "simoneerba");
			String s = con.getResponseMessage();
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("\nmessaggio : " + s);
			System.out.println("Response Code : " + responseCode);
			System.out.println("content       " + con.getHeaderField("Www-Authenticate"));
			
			/*InputStream in = con.getInputStream();
			String encoding = con.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);
			System.out.println(body);*/
			
			
			
		//String url2 = "https://auth.docker.io/token?service=quay.io&scope=repository:" + repo + ":pull";
		String url2 = "https://auth.docker.io/token?service=registry.docker.io&scope="+repo+":read";
		URL obj2;

		obj2 = new URL(url2);

		HttpURLConnection con2 = (HttpURLConnection) obj2.openConnection();

		// optional default is GET
		con2.setRequestMethod("GET");
		// add request header
		// con.setRequestProperty("username", "simoneerba");
		String s2 = con2.getResponseMessage();
		int responseCode2 = con2.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url2);
		System.out.println("\nmessaggio : " + s2);
		System.out.println("Response Code : " + responseCode2);
		System.out.println("content       " + con2.getHeaderField("Www-Authenticate"));
		
		InputStream in2 = con2.getInputStream();
		String encoding2 = con2.getContentEncoding();
		encoding2 = encoding2 == null ? "UTF-8" : encoding2;
		String body2 = IOUtils.toString(in2, encoding2);
		System.out.println(body2);

		JSONObject json = new JSONObject(body2);
		String token = json.getString("token");
		System.out.println(token);
		String url3 ="https://quay.io/api/v1/repository/" + repo+"/image/";
		URL obj3;

		
			obj3 = new URL(url3);

		HttpURLConnection con3 = (HttpURLConnection) obj3.openConnection();

		// optional default is GET
		con3.setRequestMethod("GET");

		con3.setRequestProperty("Authorization", "Bearer " + token);
		//con3.setRequestProperty("Authorization", "Basic $oauthtoken:"+token);  
		//con3.setRequestProperty("Accept", "application/json");
		//con3.setRequestProperty("Content-Type", "application/json");
		
		System.out.println("content       " + con3.getHeaderField("Www-Authenticate"));
		String s3 = con3.getResponseMessage();
		InputStream in3 = con3.getInputStream();
		String encoding3 = con3.getContentEncoding();
		encoding3 = encoding3 == null ? "UTF-8" : encoding3;
		String body3 = IOUtils.toString(in3, encoding3);
		int responseCode3 = con3.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url3);
		System.out.println("\nmessaggio : " + s3);
		System.out.println("Response Code : " + responseCode3);
		System.out.println(body3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private static void quayCatalog() {
		 PrintWriter writer = null;
		try {
			writer = new PrintWriter(f, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String url = "https://quay.io/v2/_catalog";
		while(url!=null)
		{
		URL obj;

		try {
			obj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");
		// add request header
		// con.setRequestProperty("username", "simoneerba");
		//con3.setRequestProperty("Authorization", "Bearer " + token);
		if(con.getHeaderField("Link")!=null)
		{
			String next=con.getHeaderField("Link");
			String partialUrl=next.substring(next.indexOf('<')+1, next.indexOf('>'));
			url="https://quay.io"+partialUrl;
		}
		else
		{
			url=null;
		}
		int responseCode = con.getResponseCode();
		String s3 = con.getResponseMessage();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("\nmessaggio : " + s3);
		System.out.println("Response Code : " + responseCode);
		InputStream in = con.getInputStream();
		String encoding = con.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		System.out.println(body);
		JSONObject json = new JSONObject(body);
		JSONArray array = json.getJSONArray("repositories");
		String repository=null;
		for(int i=0;i<array.length();i++)
		{
			repository=array.getString(i);
			writer.println(repository);
		}

		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}

	}
	
	private static void searchHttp(String string) {
		try {
			String url = "https://index.docker.io/v1/search?q=library/*&page=1&n=100";
			URL obj;

			obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// optional default is GET
			con.setRequestMethod("GET");
			// add request header
			// con.setRequestProperty("username", "simoneerba");
			System.out.println("content       " + con.getHeaderField("Www-Authenticate"));
			String s = con.getResponseMessage();
			InputStream in1 = con.getInputStream();
			String encoding1 = con.getContentEncoding();
			encoding1 = encoding1 == null ? "UTF-8" : encoding1;
			String body1 = IOUtils.toString(in1, encoding1);
			System.out.println(body1);
			JSONObject json1 = new JSONObject(body1);
			String token1 = json1.getJSONArray("results").toString();
			System.out.println(token1);
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("\nmessaggio : " + s);
			System.out.println("Response Code : " + responseCode);
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	private static void searchApi(String query) {
		DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
				.withDockerHost("tcp://localhost:2375").withDockerTlsVerify(false)
				// .withDockerCertPath("/Users/leonardo/.docker/machine/machines/docker-1.11.2")
				// .withDockerConfig("/home/user/.docker")
				.withApiVersion("1.23").withRegistryUrl("http://registry-1.docker.io/v1/")
				.withRegistryUsername("simoneerba").withRegistryPassword("").withRegistryEmail("dockeruser@github.com")
				.build();
		DockerClient docker = DockerClientBuilder.getInstance(config).build();
		Info info = docker.infoCmd().exec();
		System.out.print(info);
		List<Image> dockerSearch = docker.listImagesCmd().exec();

		System.out.println("Search returned" + dockerSearch.toString());
		Iterator<Image> i = dockerSearch.iterator();
		while (i.hasNext()) {
			Image s = i.next();
			System.out.println(s.getId());
		}

	}
	private static void myreg()
	{
		
			String url = "http://localhost:5001/v2/_catalog";
			URL obj = null;

			try {
				obj = new URL(url);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HttpURLConnection con = null;
			try {
				con = (HttpURLConnection) obj.openConnection();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// add request header
			// con.setRequestProperty("username", "simoneerba");
			System.out.println("content       " + con.getHeaderField("Www-Authenticate"));
			String s = null;
			try {
				s = con.getResponseMessage();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			int responseCode = 0;
			try {
				responseCode = con.getResponseCode();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("\nmessaggio : " + s);
			System.out.println("Response Code : " + responseCode);
		
	}
	public static void pullSinglev1Page(int i)
	{
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(f, "UTF-8");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	int n=0;

		
		String url2 = "https://index.docker.io/v1/search?q=*&n=100&page="+i;

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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		int responseCode2 = 0;
		try {
			responseCode2 = con2.getResponseCode();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(responseCode2+"           "+i);
		if(responseCode2==200)
		{
			String s2 = null;
			try {
				s2 = con2.getResponseMessage();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
			
			System.out.println("\nSending 'GET' request to URL : " + url2);
			System.out.println("\nmessaggio : " + s2);
			System.out.println("Response Code : " + responseCode2);
			JSONObject json = new JSONObject(body2);
			JSONArray array = json.getJSONArray("results");
			n = json.getInt("num_pages");
			System.out.println(array.length());
			System.out.println("scritte  "+i+"  pagine su  "+n);
			
			for(int j=0;j<array.length();j++)
			{
				writer.println(array.getJSONObject(j).getString("name"));
			}
		}
		
	writer.flush();
	writer.close();
	
	}
	public static void readManifests()
	{
		BufferedReader br = null;
		FileReader fr = null;
	try {

		fr = new FileReader(f4);
		br = new BufferedReader(fr);

		String sCurrentLine;

		br = new BufferedReader(new FileReader(f4));
		int i=0;
		JSONObject o = null;
		while ((sCurrentLine = br.readLine())!=null) {
			System.out.println(i);
			if(i%1000==0)
			{
				//op.getT().success();
				//op.getT().close();
				//System.out.println("tot "+Runtime.getRuntime().totalMemory()); 

				// Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
				//System.out.println("max "+Runtime.getRuntime().maxMemory());

				 // Get amount of free memory within the heap in bytes. This size will increase // after garbage collection and decrease as new objects are created.
				//System.out.println("free "+Runtime.getRuntime().freeMemory());
				op.newT();
				
			}
			if(!sCurrentLine.equals(""))
			{
				try{
				o=new JSONObject(sCurrentLine);
				op.insert(o);
				}
				catch(org.json.JSONException e)
				{
					System.out.println("skipping..");
				}
				
			i++;
			}
		}
		/*while ((sCurrentLine = br.readLine())!=null) {
			System.out.println(i);
			
			if(!sCurrentLine.equals(""))
			{
				o=new JSONObject(sCurrentLine);
				//op.insert(o);
			i++;
			}
		}*/
	//	System.out.println("ciao");
		//op.newT();
		
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
	System.out.println("finito2");
	}
	public static void removeDuplicates()
	{
		File inputFile = new File("E://c.txt");
		File tempFile = new File("E://NoDuplicatesTotal.txt");

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(tempFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String currentLine;
		Map<String,Integer> m=new HashMap<String,Integer>();
		try {
			while((currentLine = reader.readLine()) != null) {
			    // trim newline when comparing with lineToRemove
			    if(m.get(currentLine) == null)
			    {
			    	m.put(currentLine, 1);
			    	writer.write(currentLine + System.getProperty("line.separator"));
			    }

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public static void getUserNames()
	{
		File inputFile = new File("E://noDuplicatesFromTotal.txt");
		File tempFile = new File("E://userNames2.txt");

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(inputFile));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(tempFile));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String currentLine;
		Map<String,Integer> m=new HashMap<String,Integer>();
		try {
			while((currentLine = reader.readLine()) != null) {
			    // trim newline when comparing with lineToRemove
				int index=currentLine.indexOf("/");
				String name="";
				if(index!=-1)
				{
				 name=currentLine.substring(0, index);
				}
				else
				{
					name="library";
				}
			    if(m.get(name) == null)
			    {
			    	m.put(name, 1);
			    	writer.write(name + System.getProperty("line.separator"));
			    }

			}
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public static void addFiles()
	{
		System.out.println("ciao");
		File f1=new File("E:/ManifestsProva.txt");
		File f2=new File("E:/ManifestsProva2.txt");
		File f3=new File("E:/c.txt");
		String content1="";
		String content2="";
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(f1));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedReader reader2 = null;
		try {
			reader2 = new BufferedReader(new FileReader(f2));
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(f3));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int count=0;
		String currentLine="";
		try {
			while((currentLine = reader.readLine()) != null) {
				count++;
				//if(count%100000==0)
				//{
					System.out.println(count);
				//}
				if(currentLine.length()>0)
				{
					content1+=currentLine+System.lineSeparator();
				}
				
			}

		while((currentLine = reader2.readLine()) != null) {
			count++;
			if(count%100000==0)
			{
				System.out.println(count);
			}
			if(currentLine.length()>0)
			{
				content2+=currentLine+System.lineSeparator();
			}
		}
		System.out.println("before writing");
		writer.write(content1+content2);
		
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			reader2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		}

	
}