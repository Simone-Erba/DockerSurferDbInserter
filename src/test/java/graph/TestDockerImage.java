package graph;
import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.TraversalDescription;

import data.DockerImage;
import querying.GraphOperations;
import querying.Main;
import querying.NodeInfo;
enum MyRelationshipTypes implements RelationshipType
{
    DEPENDENCY
}
public class TestDockerImage {
	static GraphOperations op;
static Direction[] directions=Direction.values();//BOTH IN OUT
@Before public void initialize()
{
	op=new GraphOperations("E:/neo4jdatabaseTest");
	op.pulisci();
}
@Test public void testCreazione()
{
	
	JSONObject a=new JSONObject("{name:\"a\",surname:\"q\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"a\"}]}");
	 op.insert(a);
	JSONObject a2=new JSONObject("{name:\"a\",surname:\"qwe\",tag:\"2\",date:\"01:01:2000\",layers:[{blobSum:\"s\"},{blobSum:\"d\"},{blobSum:\"c\"},{blobSum:\"a\"}]}");
	op.insert(a2);
	JSONObject a3=new JSONObject("{name:\"a\",surname:\"qwewwe\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"o\"},{blobSum:\"i\"},{blobSum:\"f\"},{blobSum:\"c\"},{blobSum:\"a\"}]}");
	op.insert(a3);
	JSONObject a4=new JSONObject("{name:\"a\",surname:\"qw\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"t\"},{blobSum:\"i\"},{blobSum:\"f\"},{blobSum:\"c\"},{blobSum:\"a\"}]}");
	op.insert(a4);
	JSONObject a1=new JSONObject("{name:\"a\",surname:\"qw\",tag:\"2\",date:\"01:01:2000\",layers:[{blobSum:\"w\"},{blobSum:\"w\"},{blobSum:\"e\"},{blobSum:\"b\"},{blobSum:\"a\"}]}");
	op.insert(a1);
	JSONObject a5=new JSONObject("{name:\"a\",surname:\"qas\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"w\"},{blobSum:\"e\"},{blobSum:\"r\"},{blobSum:\"b\"},{blobSum:\"a\"}]}");
	op.insert(a5);
	JSONObject b=new JSONObject("{name:\"a\",surname:\"q\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"b\"},{blobSum:\"a\"}]}");
	op.insert(b);
	NodeInfo k=(NodeInfo) op.getMap().get("[a]");
	System.out.println(k.getN());
	/*JSONObject b2=new JSONObject("{name:\"abc\",surname:\"q\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"j\"},{blobSum:\"c\"},{blobSum:\"b\"},{blobSum:\"q\"},{blobSum:\"p\"}]}");
	op.insert(b2);
	JSONObject b3=new JSONObject("{name:\"abcd\",surname:\"ql\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"d\"},{blobSum:\"c\"},{blobSum:\"b\"},{blobSum:\"q\"},{blobSum:\"p\"}]}");
	op.insert(b3);
	JSONObject b4=new JSONObject("{name:\"abcde\",surname:\"q\",tag:\"17\",date:\"01:01:2000\",layers:[{blobSum:\"e\"},{blobSum:\"c\"},{blobSum:\"b\"},{blobSum:\"q\"},{blobSum:\"p\"}]}");
	op.insert(b4);
	JSONObject b1=new JSONObject("{name:\"ab\",surname:\"qu\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"f\"},{blobSum:\"c\"},{blobSum:\"b\"},{blobSum:\"q\"},{blobSum:\"p\"}]}");
	op.insert(b1);
	JSONObject b5=new JSONObject("{name:\"abc\",surname:\"qas\",tag:\"1\",date:\"01:01:2000\",layers:[{blobSum:\"j\"},{blobSum:\"c\"},{blobSum:\"b\"},{blobSum:\"q\"},{blobSum:\"p\"}]}");
	op.insert(b5);*/
	op.createRelationships();
	op.close();
	op.getGraph().shutdown();
	//JSONObject p=new JSONObject("{name:\"abc\",surname:\"qas\",date:\"01:01:2000\",layers:[{blobSum:\"p\"}]}");
	//op.insert(p);
	//long id=n.getId();
	/*System.out.println("nel test  "+id);
    List<String> l=new ArrayList<String>();
    l.add("p");
    l.add("a");
    System.out.println(l.toString());
	NodeInfo y=op.getMap().get(l.toString());
	System.out.println(y.toString());
	System.out.println("aaaaaaaaaa");
	TraversalDescription traversalDescription = op.getGraph().traversalDescription()
            .depthFirst()
            .relationships(MyRelationshipTypes.DEPENDENCY, Direction.OUTGOING);

   // Node a = ... // however you find your node A

    ResourceIterator<Node> nodes =traversalDescription.traverse(n).nodes().iterator();
    		
        while(nodes.hasNext()){
            Node n2 = nodes.next();
            //or whatever property name you use to get your names for nodes
           printNode(n2);
        }
	*/
	/*
	 * d -----> d1 ----> d3
	 * 
	 * 
	 * 
	 * */
}
private void printList(List<Node> n) {
	Iterator<Node> i=n.iterator();
	System.out.println("LISTA");
	while(i.hasNext())
	{
		printNode(i.next());
	}
	System.out.println("FINE LISTA");
}
private void printNode(Node n) {
	// TODO Auto-generated method stub
	System.out.println("inizio nodo"+n.getId());
	List<Node> l=op.getNodes(n, Direction.OUTGOING);
	List<Node> l2=op.getNodes(n, Direction.INCOMING);
	List<Node> l3=op.getNodes(n, Direction.BOTH);
	Iterator<Node> i=l.iterator();
	Iterator<Node> i2=l2.iterator();
	Iterator<Node> i3=l3.iterator();
	System.out.println("outgoing");
	while(i.hasNext())
	{
		System.out.println(i.next().getId());
	}
	System.out.println("incoming");
	while(i2.hasNext())
	{
		System.out.println(i2.next().getId());
	}
}
@After public void end()
{
		op.getT().success();
		op.getT().close();
		op.getGraph().shutdown();
		System.out.println("ciao");
}
}