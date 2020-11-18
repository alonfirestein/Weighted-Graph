package ex1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

/**
 * A JUnit 5 testing class built to test all of the functions built in the WGraph_Algo class.
 */
public class WGraph_Algo_TESTER {

    int node;
    private double DefaultWeight = 10;
    private static Random random = new Random();
    private weighted_graph TestGraph;
    private weighted_graph_algorithms TestGraphAlgo;

    void initGraph(weighted_graph g){
        TestGraph = g;
    }

    public weighted_graph GraphCreator(int NodeSize, int EdgeSize) {
        int EdgeCounter = 0;
        int NodeA , NodeB;
        HashSet<String> EdgeList = new HashSet<>();

        for(node = 0; node < NodeSize; node++) {
            TestGraph.addNode(node);
        }
        for (int edges = 0; edges < EdgeSize; edges++) {
        //while (EdgeCounter < EdgeSize){
            NodeA = random.nextInt(NodeSize);
            NodeB = random.nextInt(NodeSize);
            while (NodeA == NodeB || EdgeList.contains(NodeA + "" + NodeB) || EdgeList.contains(NodeB + "" + NodeA)) {
                NodeB = random.nextInt(NodeSize);
            }
            TestGraph.connect(NodeA, NodeB, DefaultWeight);
            EdgeList.add(NodeA + "" + NodeB);
            EdgeCounter++;
            }
        return TestGraph;
    }

    @BeforeEach
    void init(){
        TestGraph = new WGraph_DS();
        TestGraphAlgo = new WGraph_Algo();
        TestGraphAlgo.init(TestGraph);
        initGraph(TestGraph);
    }
    @Test
    void Copy_Test(){
        int NodeSize= 10000;
        for(node=0; node < NodeSize; node++) {
            TestGraph.addNode(node);
        }
        for(node = 0; node < NodeSize - 1 ; node++) {
            TestGraph.connect(node,node+1, DefaultWeight);
        }
        weighted_graph NewCopiedGraph = TestGraphAlgo.copy();
        int a = random.nextInt(NodeSize);
        TestGraph.removeNode(5);
        TestGraph.removeEdge(0,1);
        assertEquals(NewCopiedGraph.nodeSize() , TestGraph.nodeSize() + 1); //removed one node
        assertEquals(NewCopiedGraph.edgeSize(), TestGraph.edgeSize() + 3); //removed one edge and a node with 2 edges
        assertFalse(TestGraph.hasEdge(0,1));
        assertTrue(NewCopiedGraph.hasEdge(0,1));
    }

    @Test
    void Connected_Graph_Test_1() {
        int NodeSize = 10000;
        for(node = 0; node < NodeSize; node++) {
            TestGraph.addNode(node);
        }
        for(node=0; node < NodeSize - 2; node++){
            TestGraph.connect(node,node+1, DefaultWeight);
            TestGraph.connect(node, node+2, DefaultWeight);
        }
        assertTrue(TestGraphAlgo.isConnected());
    }
    @Test
    void Connected_Graph_Test_2() {
        GraphCreator(10000,9000);
        assertFalse(TestGraphAlgo.isConnected());
        //Any graph with (NodeSize-1) edges is not connected.
    }
    @Test
    void Shortest_Path_Dist_Test() {
        int NodeSize = 10000;
        for(node = 0; node < NodeSize; node++) {
            TestGraph.addNode(node);
        }
        for(node = 0; node < NodeSize - 1; node++) {
            TestGraph.connect(node,node + 1,DefaultWeight);
        }
        int NodeA = random.nextInt(NodeSize);
        int NodeB = random.nextInt(NodeSize);
        int PathDistance = Math.abs(NodeA-NodeB);
        assertEquals(TestGraphAlgo.shortestPathDist(NodeA, NodeB) , (PathDistance * DefaultWeight));
    }
    @Test
    void Shortest_Path_Test() {
        int NodeSize = 10000;
        for(node = 0; node < NodeSize; node++) {
            TestGraph.addNode(node);
        }
        for(node = 0; node < NodeSize - 1; node++) {
            TestGraph.connect(node,node + 1,DefaultWeight);
        }
        int removedNode = (NodeSize / 2);
        TestGraph.removeNode(removedNode);
        assertEquals(-1 , TestGraphAlgo.shortestPathDist(removedNode - 1,removedNode + 1));
        assertEquals(0 , TestGraphAlgo.shortestPathDist(0,0));
        assertNull(TestGraphAlgo.shortestPath(removedNode - 1,removedNode + 1));
        assertNotNull(TestGraphAlgo.shortestPath(0,5));
    }
    @Test
    void Shortest_Path_Test_2() {
        int NodeSize = 15;
        for( node = 0 ; node < NodeSize; node++){
            TestGraph.addNode(node);
        }
        TestGraph.connect(0,3,17);
        TestGraph.connect(10,7,9);
        TestGraph.connect(5,9,11);
        TestGraph.connect(4,6,14);
        TestGraph.connect(4,12,7);
        TestGraph.connect(1,8,20);
        TestGraph.connect(8,11,4);
        TestGraph.connect(2,13,15);
        TestGraph.connect(6,5,2);
        TestGraph.connect(12,1,3);
        TestGraph.connect(11,2,19);
        TestGraph.connect(13,7,8);
        TestGraph.connect(13,3,18);
        TestGraph.connect(14,2,6);
        TestGraph.connect(9,14,10);
        assertTrue(TestGraphAlgo.isConnected());
        assertNotNull(TestGraphAlgo.shortestPath(5,13));
        assertEquals(39.0, TestGraphAlgo.shortestPathDist(9,8));
        TestGraph.connect(1,8,1); // updating edge weight to check if it changes paths
        assertEquals(38.0, TestGraphAlgo.shortestPathDist(9,8));
        TestGraph.removeNode(2);
        assertFalse(TestGraphAlgo.isConnected());
        assertEquals(-1, TestGraphAlgo.shortestPathDist(9,0));
        TestGraph.connect(8,0,3); //Connecting edge to check path
        assertEquals(41.0, TestGraphAlgo.shortestPathDist(9,0));

    }
    @Test
    void save_and_load() throws IOException {
        String file = "MyGraph.txt";
        GraphCreator(10000,15000);
        assertTrue(TestGraphAlgo.save(file));
        TestGraph.removeNode(100);
        TestGraph = new WGraph_DS();
        TestGraphAlgo.init(TestGraph);
        TestGraphAlgo.load(file);
        assertEquals(10000 ,TestGraph.nodeSize());
        assertEquals(15000 ,TestGraph.edgeSize());
        // After loading the graph, node 100 still exists because it was removed after the graph was saved.
        assertNotNull(TestGraph.getNode(100));


    }
    @Test
    void General_Test() {
        GraphCreator(10000, 40000);
        TestGraph.removeNode(404);
        assertEquals(null, TestGraph.getNode(404)); //Should be null, 404 not found :)
        assertEquals(9999, TestGraph.nodeSize());
        assertFalse(TestGraphAlgo.isConnected());
        for (node_info node : TestGraph.getV()) {
            if (TestGraph.getV(node.getKey()).isEmpty()) {
                TestGraph.connect(node.getKey(), 100, DefaultWeight);
            }
        }
        assertTrue(TestGraphAlgo.isConnected());
    }


}

