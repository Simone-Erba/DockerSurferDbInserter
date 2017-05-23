import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import data.DockerImage;
import querying.GraphOperations;
import querying.Main;
enum MyRelationshipTypes implements RelationshipType
{
    DEPENDENCY
}
public class TestDockerImage {
 
static Direction[] directions=Direction.values();//BOTH IN OUT

@Test
public void testCreazione()
{
	GraphOperations op=new GraphOperations("E:\\a");
	op.pulisci();
	String a="{user:\"ciao\",repo:\"si\",date:\"01:01:2000\",layers:[\"asdad\"]}";
	DockerImage d=new DockerImage(a);
	List<String> l=new ArrayList<String>();
	l.add("asdad");
	String user="ciao";
	String repo="si";
	String date="01:01:2000";
	DockerImage d2=new DockerImage(l,date,user,repo);
	assertEquals(a,d2.toString());
	assertEquals(a,d.toString());
	DockerImage d1=new DockerImage("{user:\"ciao\",repo:\"si\",date:\"01:01:2000\",layers:[\"asdad\",\"qqqq\"]}");
	DockerImage d3=new DockerImage("{user:\"ciao\",repo:\"si\",date:\"01:01:2000\",layers:[\"asdad\",\"qqqq\",\"ttt\"]}");
	assertEquals(true,d1.isRelated(d).isFiglio());
	Node nodo3 = op.getGraph().createNode();
	nodo3.setProperty("value", d3.toString());
	Node nodo = op.getGraph().createNode();
	   // nodo.setProperty("value", i.getUser()+"/"+i.getRepo());
		nodo.setProperty("value", d.toString());
	
		Node nodo2 = op.getGraph().createNode();
		// nodo.setProperty("value", i.getUser()+"/"+i.getRepo());
		nodo2.setProperty("value", d1.toString());
	
		//Relationship e=nodo.createRelationshipTo(nodo2, MyRelationshipTypes.DEPENDENCY);
		Relationship e2=nodo2.createRelationshipTo(nodo3, MyRelationshipTypes.DEPENDENCY);
	//assertEquals(nodo2,op.cercaFigli(nodo,nodo3));
	//assertEquals(nodo2,op.cercaPadre(nodo3,nodo));
	//assertEquals(nodo3,op.cercaFigli(nodo2,nodo3));
	//assertEquals(nodo,op.cercaPadre(nodo,nodo));
	
		op.getT().success();
		op.getT().close();
	op.getGraph().shutdown();
	/*
	 * d -----> d1 ----> d3
	 * 
	 * 
	 * 
	 * */
}
}