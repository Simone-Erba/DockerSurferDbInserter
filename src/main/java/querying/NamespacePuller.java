package querying;

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

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class NamespacePuller extends Thread{
	public static File english;//repo2
	public static File repositories;//repo3
	public static File words;//words
	public static File lines;//words
	public static File few;//words
	public void run()
	{
		few=new File("E:\\userNames.txt");
		try {
			if(!few.exists())
			{
				few.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		english=new File("E:\\words.txt");
		try {
			if(!english.exists())
			{
				english.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			lines=new File("E:\\line.txt");
			try {
				if(!lines.exists())
				{
					lines.createNewFile();
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		repositories=new File("E:\\repo2.txt");
		try {
			if(!repositories.exists())
			{
				repositories.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		words=new File("E:\\repo3.txt");
		try {
			if(!words.exists())
			{
				words.createNewFile();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldSearchv1();
		//System.out.println(existInFile("phyton",words));
		//append("aaa",f2);

	}
	private static void oldSearchv1() {


		 PrintWriter writer = null;
			try {
				writer = new PrintWriter(english, "UTF-8");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			PrintWriter writer2 = null;
			try {
				writer2 = new PrintWriter(repositories, "UTF-8");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		int i=8500;
		int n=0;
		int count=0;
		do
		{
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
				System.out.println(body2);
				
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
				count=0;
				i++;
			}
			else
			{
				count++;
				//writer2.println("500 aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa "+i);

				if(count>8)
				{
					writer2.println("non fatta "+i);
					i++;
					count=0;
				}
				
			}

		}
		while(i<9000);
		writer.flush();
		writer.close();
		writer2.flush();
		writer2.close();
	}
	private static void searchv1() {

		 PrintWriter writer = null;
			try {
				writer = new PrintWriter(repositories, "UTF-8");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			//per ogni parola
			FileReader fr = null;
			BufferedReader br = null;
			try{
			fr = new FileReader(few);
			br = new BufferedReader(fr);
			//int line=0;
			String sCurrentLine;
			int letti=0;
			String sc;
			/*while (letti<800&&(sCurrentLine=br.readLine()) != null) {
				letti++;
			}*/
			while (letti<1000&&(sCurrentLine=br.readLine()) != null) {
				//line++;
				letti++;
					int i=0;
					int n=0;
					String name="";
					do
					{
						i++;
					String url2 = "https://index.docker.io/v1/search?q="+sCurrentLine+"&n=100&page="+i;
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
						//System.out.println("\nmessaggio : " + s2);
						//System.out.println("Response Code : " + responseCode2);
						JSONObject json = new JSONObject(body2);
						JSONArray array = json.getJSONArray("results");
						n = json.getInt("num_pages");
						System.out.println(array.length());
						
						for(int j=0;j<array.length();j++)
						{
							name=name+array.getJSONObject(j).getString("name");
							int a=name.indexOf("/");
							if(a==-1)
							{
								name="library/"+name;
							}
							name=name+"\n";
							/*int a=name.indexOf("/");
							if(a!=-1)
							{
								String user=name.substring(0,a);
								if(!existInFile(user,english))
								{
									append(user,english);
								}
							}*/
							/*String desc=array.getJSONObject(j).getString("description");

							 String[] arr = desc.split(" ");    

							 for ( String ss : arr) {
								 	System.out.println("se esiste");
							       if(!existInFile(ss,english))
							       {
							    	   System.out.println("controllato");
							    	   append(ss,english);
							       }
							  }*/
						}
						
					}
					}
					while(i<n);
					writer.println(name);
					//append(String.valueOf(line),lines);
			}

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
			writer.flush();
			writer.close();
	}
	public static boolean existInFile(String s,File f)
	{
		FileReader fr = null;
		BufferedReader br = null;
		try{
		fr = new FileReader(f);
		br = new BufferedReader(fr);
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			if(sCurrentLine.equals(s))
			{
				return true;
			}
		}

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
		return false;
}
	public static void append(String s,File f)
	{
		 BufferedWriter bw = null;

	      try {
	         // APPEND MODE SET HERE
	         bw = new BufferedWriter(new FileWriter(f, true));
		 bw.write(s);
		 bw.newLine();
		 bw.flush();
	      } catch (IOException ioe) {
		 ioe.printStackTrace();
	      } finally {                       // always close the file
		 if (bw != null) try {
		    bw.close();
		 } catch (IOException ioe2) {
		    // just ignore it
		 }
	      } // end try/catch/finally

	   } // end test()
	
}
