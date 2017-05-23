package data;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;



public class DockerImage {
	List<String> layers;//first is the lowest layer
	String lettura;
	String user;
	String repo;
	public List<String> getLayers() {
		return layers;
	}
	public void setLayers(List<String> layers) {
		this.layers = layers;
	}
	public String getLettura() {
		return lettura;
	}
	public void setLettura(String lettura) {
		this.lettura = lettura;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getRepo() {
		return repo;
	}
	public void setRepo(String repo) {
		this.repo = repo;
	}
	public DockerImage(List<String> layers, String lettura, String user, String repo) {
		super();
		this.layers = layers;
		this.lettura = lettura;
		this.user = user;
		this.repo = repo;
	}
	public DockerImage(String info) {
		super();
		JSONObject json = new JSONObject(info);
		user = json.getString("user");
		repo= json.getString("repo");
		lettura=json.getString("date");
		List<String> temp=new ArrayList<String>();
		JSONArray arr = json.getJSONArray("layers");
		for(int i=0;i<arr.length();i++)
		{
			temp.add(arr.getString(i));
		}
		layers=temp;
	}
	public void insertDb()
	{

		// crea stanze (vertici)
	
		 

	}
	//chiamare sui parenti di un immagine
	public Relazione isRelated(DockerImage i)//positive number: i is a child (and have i layers in common)
								//negative number: this is a child (and have -i layers in common)
	{
		Relazione r;
		int layers=0;
		Iterator<String> it1=this.getLayers().iterator();
		Iterator<String> it2=i.getLayers().iterator();
		boolean diversi=false;
		while(diversi==false&&it1.hasNext()&&it2.hasNext())
		{
			if(it1.next().equals(it2.next()))
			{
				layers++;
			}
			else
			{
				diversi=true;
			}
		}
		if(layers==0)
		{
			return null;
		}
		//fratelli
		if(diversi==true)//sono fratelli
		{
			r=new Relazione(layers,false,false,true);
			return r;//chiamare sul padre di i
		}
		if(!it2.hasNext()&&!it1.hasNext())
		{
			r=new Relazione(layers,true,true,true);
			return r;
		}
		if(!it2.hasNext())
		{
			//this è il figlio
			r=new Relazione(layers,false,true,false);
			return r;//chiamare sul padre di i
		}
		else
		{
				//padre
				r=new Relazione(layers,true,false,false);
				return r;
			
		}
		
	}
	public String toString()
	{
		String layers = "[";
		Iterator<String> it1=this.getLayers().iterator();
		while(it1.hasNext())
		{
			String s=it1.next();
			layers=layers+"\""+s+"\",";
			
		}
		if(layers.length()>2)//se ce almeno un layer
		{
			layers=layers.substring(0, layers.length()-1);
		}
		layers=layers+"]";
		return "{user:"+"\""+user+"\",repo:\""+repo+"\",date:\""+lettura+"\",layers:"+layers	+"}";
	}
}
