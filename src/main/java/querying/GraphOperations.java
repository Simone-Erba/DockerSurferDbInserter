package querying;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import javax.imageio.event.IIOReadProgressListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import data.DockerImage;
import data.IdManager;

@SuppressWarnings("deprecation")
public class GraphOperations {
	static GraphDatabaseService graph;
	Transaction t;
	IdManager id;
	DB db;
	@SuppressWarnings("deprecation")
	Label myLabel = DynamicLabel.label("Image");
	ConcurrentMap map;
	static Direction[] directions=Direction.values();//BOTH IN OUT
	public GraphOperations(String path) {
		super();
		id=new IdManager();
		db = DBMaker.memoryDB().make();
		map= db.hashMap("map").make();
		
		File f=new File(path);
		graph = new GraphDatabaseFactory().newEmbeddedDatabase( f );
		t=graph.beginTx();
	}
	public void newT()
	{
		t.success();
		t.close();
		t=graph.beginTx();
	}


	public ConcurrentMap getMap() {
		return map;
	}

	public void setMap(ConcurrentMap l) {
		this.map = l;
	}

	public Transaction getT() {
		return t;
	}

	public GraphDatabaseService getGraph() {
		return graph;
	}
	
	public Node cercaFigli(Node v, Node figlio)
	{
		//System.out.println("                 CERCA FIGLI");
		//System.out.println(v.getProperty("value").toString());
		//System.out.println(figlio.getProperty("value").toString());
		DockerImage image=new DockerImage(v.getProperty("value").toString());
		Iterable<Node> possibiliFigli=getNodes(figlio,directions[1]);

		//figlio.getVertices(directions[1], "dependency");
		Iterator<Node> possibiliPadriIterator=possibiliFigli.iterator();
		while(possibiliPadriIterator.hasNext())
		{

			Node pp=possibiliPadriIterator.next();
			//System.out.println(pp.getProperty("value").toString());
			DockerImage p2=new DockerImage(pp.getProperty("value").toString());
			if(v.getProperty("value").toString().equals(pp.getProperty("value").toString()))//se sono arrivato alla stessa immagine
			{
				return figlio;
			}
			if(p2.isRelated(image)!=null&&p2.isRelated(image).isFiglio())//i è figlio
			{
				return cercaFigli(v,pp);	
			}	
		}
		return figlio;
	}
	public List<Node> getNodes(Node n,Direction d)
	{
		List<Node> r=new ArrayList<Node>();
		Iterable<Relationship> l=n.getRelationships(d);
		Iterator<Relationship> i=l.iterator();
		while(i.hasNext())
		{
			Relationship e=i.next();
			Node node=e.getOtherNode(n);
			r.add(node);
		}
		return r;
	}
	public Node cercaPadre(Node v, Node padre)
	{
		//System.out.println("                 CERCA padre");
		//System.out.println(v.getProperty("value").toString());
		//System.out.println(padre.getProperty("value").toString());
		DockerImage image=new DockerImage(v.getProperty("value").toString());
		List<Node> possibiliPadri=getNodes(padre,directions[2]);
		//padre.getVertices(directions[2], "dependency");
		Iterator<Node> possibiliPadriIterator=possibiliPadri.iterator();
		while(possibiliPadriIterator.hasNext())
		{
			Node pp=possibiliPadriIterator.next();
			//System.out.println(pp.getProperty("value").toString());
			DockerImage p2=new DockerImage(pp.getProperty("value").toString());
			if(v.getProperty("value").toString().equals(pp.getProperty("value").toString()))//se sono arrivato alla stessa immagine
			{
				return padre;
			}
			if(p2.isRelated(image)!=null&&p2.isRelated(image).isPadre())//i è figlio
			{
				return cercaPadre(v,pp);	
			}	
		}
		return padre;
	}
	public void pulisci()
	{
		Iterable<Relationship> l2= graph.getAllRelationships();
		Iterator<Relationship> i=l2.iterator();
		while(i.hasNext())
		{
			Relationship v=i.next();
			v.delete();
		}
				Iterable<Node> l= graph.getAllNodes();
				Iterator<Node> i2=l.iterator();
				while(i2.hasNext())
				{
					Node v=i2.next();
					v.delete();
				}
	}
	public void stampa()
	{
		//STAMPA ARCHI	
				Iterable<Node> l= graph.getAllNodes();
				Iterator<Node> i2=l.iterator();
				//System.out.println("VERTICI");
				while(i2.hasNext())
				{
					Node v=i2.next();
				//	System.out.println(v.getProperty("value"));
				}
				//STAMPA VERTICI	
				Iterable<Relationship> l2= graph.getAllRelationships();
				Iterator<Relationship> i=l2.iterator();
				//System.out.println("ARCHI");
				while(i.hasNext())
				{
					Relationship v=i.next();
					//System.out.println(v.getProperty("value"));
				}
	}
	public void close()
	{
		System.out.println("s1");
		t.success();
		System.out.println("s2");
		t.close();
		System.out.println("s3");
		db.close();
	}
	public void insert(JSONObject json2)
	{
		String name=json2.getString("name");
		String tag=json2.getString("tag");
		String surname=json2.getString("surname");
		JSONArray layers2=json2.getJSONArray("layers");
		if(layers2.length()>0)
		{
		List<String> layers=new ArrayList<String>();
		for(int i=layers2.length()-1;i>=0;i--)
		{
			String a=layers2.getJSONObject(i).getString("blobSum");
			layers.add(a);
			//System.out.println(a);
		}
		String s=json2.getString("date"); //2016/11/16 12:08:43
		//DockerImage i=new DockerImage(layers, s, name, surname);
			Node nodo =graph.createNode();
			// nodo.setProperty("value", i.getUser()+"/"+i.getRepo());
	      	nodo.setProperty("tag", tag);
			nodo.setProperty("date", s);
			nodo.setProperty("name", surname);
			nodo.setProperty("user", name);
			nodo.setProperty("fullname", name+"/"+surname);
			nodo.setProperty("fulltag", name+"/"+surname+":"+tag);
			nodo.setProperty("layers", layers.toString());
			//nodo.setProperty("layers2", layerArray);
			//nodo.setProperty("value", i.toString());
			nodo.addLabel(myLabel);
			//System.out.println(layers2.toString());
			//List<String> list = Arrays.asList(nodo.getProperty("layers").toString().split("\\s*,\\s*"));
			//se ce nella mappa con tutti i layers ok, altrimenru tolgo un layers e cosi via trovo il padre. i figli si mettoni da soli
			//System.out.println(layers.toString());
			this.getMap().put(layers.toString(), new NodeInfo(null,nodo.getId()));
		}
			//return nodo;
	}
	public void createRelationships()
	{
		int ind=0;
		
		ResourceIterable<Node> nodes=this.getGraph().getAllNodes();
		Iterator<Node> i=nodes.iterator();
		while(i.hasNext())
		{
			System.out.println(ind);
			if(ind!=0&&ind%1000==0)
			{
				this.newT();
			}
			Node nodo=i.next();
			//System.out.println(nodo.getProperty("layers"));
			Node padre=null;
			/*ResourceIterator<Node> padri=null;
			
			int index2=0;
			String lay=(String) nodo.getProperty("layers");
			while(padre==null&&index2!=-1)
			{
				index2=lay.lastIndexOf(",");
				if(index2!=-1)
				{
					lay=lay.substring(0,index2)+"]";
					padri=graph.findNodes(myLabel, "layers", lay);
					if(padri.hasNext())
					{
						padre=padri.next();
					}
				}
			}*/
			String s=(String) nodo.getProperty("layers");
			//System.out.println(s);
			List<String> list = Arrays.asList(s.toString().split("\\s*,\\s*"));
			//List<Integer> l=new ArrayList<Integer>();
			Iterator<String> it=list.iterator();
			int count=0;
			while(it.hasNext())
			{
				String a=it.next();
				if(count==0)
				{
					int index=a.length();
					a=a.substring(1, index);
					list.set(0, a);
				}
				if(count==list.size()-1)
				{
					int index=a.length();
					a= a.substring(0, index-1);
					list.set(count,a);
				}
				count++;
			}
			//Iterator<String> it2=list.iterator();
			/*while(it2.hasNext())
			{
				try{
				l.add(Integer.valueOf(it2.next()));
				}
				catch(java.lang.NumberFormatException e)
				{
					System.out.println("skipping..");
				}
			}*/
			//System.out.println(list.toString().replaceAll("\\s+",""));
			long id=findFather(list);
			//System.out.println(id);
			if(id!=-1)
			{
				//System.out.println(id);
				padre=this.getGraph().getNodeById(id);
			}
			if(padre!=null)
			{
				//System.out.println("found father");
				Relationship r=padre.createRelationshipTo(nodo, MyRelationshipTypes.DEPENDENCY);
				r.setProperty("value", "DEPENDENCY");
			}
			ind++;
		}
			//boolean finito=false;
		/*	while(iter4.hasNext()&&finito==false)
			{
				Node n=iter4.next();
				Relazione r=Relazione(layers,(List<String>) n.getProperty("layers"));
				/*DockerImage d=new DockerImage(n.getProperty("value").toString());
				Relazione r=i.isRelated(d);*/
				/*if(r!=null&&r.isFiglio()&&r.isFratello()&&r.isPadre())
				{//stessa immagine, nomi diversi 
					System.out.println(nodo.getId()+"  and   "+n.getId()+"same layers");
					finito=true;
					List<Node> l=getNodes(n,directions[0]);
					Iterator<Node> i2=l.iterator();
					while(i2.hasNext())
					{
						Node temp=i2.next();
						nodo.createRelationshipTo(temp,  MyRelationshipTypes.DEPENDENCY);
					}
					Iterable<Relationship> a=n.getRelationships(directions[0]);
					Iterator<Relationship> i4=a.iterator();
					while(i4.hasNext())
					{
						i4.next().delete();
					}
					n.createRelationshipTo(nodo,   MyRelationshipTypes.DEPENDENCY);
				}
				
			}*/
		/*if(finito!=true)
		{
			//iter vecchio
			findFather();
		while(itnuovo.hasNext()&&trovato==false)
		{
			Node temp=itnuovo.next();
			Relazione r=Relazione(layers,(List<String>) temp.getProperty("layers"));
			if(r!=null&&r.isPadre())
			{
				//padre=cercaPadre(nodo,temp);
				padre=temp;
				trovato=true;
			}
			if(imm.isRelated(i)!=null&&imm.isRelated(i).isFratello())//new insert
			{
				padre=findFatherFromBrother(temp,i);
				if(padre!=null)
				{
					trovato=true;
				}
			}
		}*/
		/*if(padre!=null)//se so il padre so i figli
		{
			//aggiorna i figli
			//new insert
			List<Node> figli=getNodes(padre,directions[2]);
			Iterator<Node> i4=figli.iterator();
			Iterator<Node> i5=figli.iterator();
			Node figlio=null;
			while(i4.hasNext())
			{
				Node n=i4.next();
				Relazione r=Relazione(layers,(List<String>) n.getProperty("layers"));
				if(r!=null&&r.isFiglio())
				{
					figlio=cercaFigli(nodo,n);
					if(figlio.hasRelationship(directions[1]))
					{
						figlio.getSingleRelationship(MyRelationshipTypes.DEPENDENCY, directions[1]).delete();
					}
					Relationship arco =nodo.createRelationshipTo(figlio,  MyRelationshipTypes.DEPENDENCY);
					arco.setProperty("value", "dependency");
				}
				
			}
			Relationship arco2 =padre.createRelationshipTo(nodo, MyRelationshipTypes.DEPENDENCY);
			arco2.setProperty("value", "dependency");

		}
		else//new insert
		{
			
			//cercafigli

				Node figlio=null; 
				//iter3 su tutti i nodi
				Iterator<Node> iternuovo=this.getPossibiliPadri().iterator();
				List<Node> figli=new ArrayList<Node>();
				while(iternuovo.hasNext())
				{
					Node temp=iternuovo.next();
					Relazione r=Relazione(layers,(List<String>) temp.getProperty("layers"));
					if(r!=null&&r.isFiglio())
					{
						//figlio=cercaFigli(nodo,temp);
						figlio=temp;
						if(figlio.hasRelationship(directions[1]))
						{
							figlio.getSingleRelationship(MyRelationshipTypes.DEPENDENCY, directions[1]).delete();
						}
						Relationship arco =nodo.createRelationshipTo(figlio,  MyRelationshipTypes.DEPENDENCY);
						arco.setProperty("value", "dependency");
						figli.add(figlio);
					}
					
				}
				this.getPossibiliPadri().add(nodo);
				if(!figli.isEmpty())
				{
					this.getPossibiliPadri().removeAll(figli);
				}
				
		}	
		}*/
	//	stampa();
		System.out.println("finito");
		//this.getMap().clear();
		id.close();
		}

	private long findFather(List<String> l) {
		// TODO Auto-generated method stub
		List<String> lnew=l;

		while(!lnew.isEmpty())
		{
			lnew=removeLastFromList(lnew);
			NodeInfo a=(NodeInfo) this.getMap().get(lnew.toString());
			if(a!=null)//accerdere alla mappa
				//&&Relazione(lnew,a.getL())!=null&&Relazione(lnew,a.getL()).isFiglio()
				//non controllo nemmeno se sia il padre
			{
				ConcurrentMap map=this.getMap();
				String s=l.toString();
				//System.out.println(s);
				NodeInfo t=(NodeInfo) map.get(s);
				long n=t.getN();
				NodeInfo nd=new NodeInfo(lnew,n);
				this.getMap().put(l.toString(),nd);
				return ((NodeInfo) this.getMap().get(lnew.toString())).getN();
			}
		}
		return -1;
	}



	public List<String> removeLastFromList(List<String> l) {
		List<String> l2=new ArrayList<String>();
		Iterator<String> i=l.iterator();
		for(int j=0;j<l.size()-1;j++)
		{
			l2.add(i.next());
		}
		return l2;
	}



	private Node findFatherFromBrother(Node n,DockerImage i) {
		// TODO Auto-generated method stub
		if(n.hasRelationship(directions[1]))
		{
				Relationship p=n.getSingleRelationship(MyRelationshipTypes.DEPENDENCY, directions[1]);
				Node nodo=p.getOtherNode(n);
				DockerImage imm2=new DockerImage(nodo.getProperty("value").toString());
				System.out.println(imm2.getRepo());
				if(imm2.isRelated(i)!=null&&imm2.isRelated(i).isPadre())
				{
					return nodo;
				}
				if(imm2.isRelated(i)!=null&&imm2.isRelated(i).isFratello())
				{
					return findFatherFromBrother(nodo,i);
				}
				return null;
		}
		else
		{
			return null;
		}
	}
	public static Relazione Relazione(List<String> a,List<String> b) 
	{
		Relazione r;
		int layers=0;
		Iterator<String> it1=a.iterator();
		Iterator<String> it2=b.iterator();
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
}
