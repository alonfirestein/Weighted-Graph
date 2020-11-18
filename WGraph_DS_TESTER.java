package ex1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * A JUnit 5 testing class built to test all of the functions built in the NodeInfo and WGraph_DS classes.
 */
public class WGraph_DS_TESTER {

    private int node;
    private double DefaultWeight = 10;
    private weighted_graph TestGraph;
    private int SEED = 6;
    private Random rand = new Random(SEED);


    void initGraph(weighted_graph g){
        TestGraph = g;
    }

    @BeforeEach
    void init(){
        TestGraph = new WGraph_DS();
        initGraph(TestGraph);
    }

    @Test
    @DisplayName("Number of Nodes Test")
    void Nodes_Test(){
        int NodeSize = 1000;
        for( node = 0 ; node < NodeSize; node++){
            TestGraph.addNode(node);
        }
        assertEquals(NodeSize, TestGraph.nodeSize());
        int removeCounter = 0;
        for (node = 5; node < NodeSize; node+=5) {
            TestGraph.removeNode(node);
            removeCounter++;

            assertNull(TestGraph.getNode(node));
        }
        assertEquals(NodeSize - removeCounter, TestGraph.nodeSize());

    }

    @Test
    @DisplayName("Number of Edges Test")
    void Edges_Test() {
        int NodeSize = 1000;
        int EdgeCounter = 0;
        for(node = 0; node < NodeSize; node += 3){
            TestGraph.addNode(node);
            TestGraph.addNode(node+1);
            TestGraph.connect(node,node+1, DefaultWeight);
            EdgeCounter++;
        }
        assertEquals(EdgeCounter, TestGraph.edgeSize());
        assertTrue(TestGraph.hasEdge(0,1));
        for (node = 0; node < NodeSize; node+= 3) {
            TestGraph.removeEdge(node, node+1);
            assertFalse(TestGraph.hasEdge(node, node+1));
        }
        assertEquals(0, TestGraph.edgeSize());
    }

    @Test
    void Removing_Nodes_Test(){
        int NodeSize = 10000;
        for(node = 0; node < NodeSize; node++) {
            TestGraph.addNode(node);
        }
        for(node = 0; node < NodeSize - 1 ; node++) {
            TestGraph.connect(node,node + 1, DefaultWeight);
        }
        int NumOfEdges = TestGraph.edgeSize();
        for(node = 2; node < NodeSize; node +=7) {
            TestGraph.removeNode(node);
            NodeSize--;
            NumOfEdges -= 2;
            assertNull(TestGraph.getNode(node));
        }
        //Checking number of edges and nodes in graph after removals.
        assertEquals(TestGraph.nodeSize(),NodeSize);
        assertEquals(TestGraph.edgeSize(),NumOfEdges);
    }

    @Test
    void Neighbours_Test(){
        int NodeSize = 10000;
        for( node = 0 ; node < NodeSize; node++){
            TestGraph.addNode(node);
        }
        int MainNode = rand.nextInt(NodeSize);
        HashMap<node_info, Double> NeighboursList = new HashMap<>();

        for(node = 0; node < 300; node++){
            int CurrentNode = rand.nextInt(NodeSize);
            double RandomWeight = rand.nextDouble();
            //To make sure we don't try to connect MainNode to himself
            while (CurrentNode == MainNode || NeighboursList.containsKey(TestGraph.getNode(CurrentNode))) {
                CurrentNode = rand.nextInt(NodeSize);
            }
            TestGraph.connect(MainNode, CurrentNode, RandomWeight);
            NeighboursList.put(TestGraph.getNode(CurrentNode), RandomWeight);
        }
        // Asserting true of an edge to all of the 300 neighbours of MainNode
        for (node_info node: TestGraph.getV(MainNode)) {
            assertTrue(NeighboursList.containsKey(node));
        }
    }

    @Test
    void Neighbours_Test_2(){
        int NodeSize = 50000;
        int EdgeCounter = 0;
        for( node = 0 ; node < NodeSize; node++){
            TestGraph.addNode(node);
        }
        assertEquals(TestGraph.nodeSize(), NodeSize);

        for(node = 0; node < 25000; node++){
            int FirstNode = rand.nextInt(TestGraph.nodeSize());
            int SecondNode = rand.nextInt(TestGraph.nodeSize());
            double RandomWeight = rand.nextDouble()*5;
            while (TestGraph.hasEdge(FirstNode, SecondNode) || FirstNode == SecondNode) {
                SecondNode = rand.nextInt(NodeSize);
            }
            TestGraph.connect(FirstNode, SecondNode, RandomWeight);
            EdgeCounter++;
        }
        assertNotNull(TestGraph.edgeSize());
        assertEquals(TestGraph.edgeSize(), EdgeCounter);
    }

    @Test
    @DisplayName("MC Test")
    void MCCounter_Test() {
        int NodeSize = 10000;
        int MCcounterCheck = 0;
        for (node = 0; node < NodeSize; node++) {
            TestGraph.addNode(node);
            MCcounterCheck++;
        }
        for(node = 0; node < 3000; node++){
            int CurrentNode = rand.nextInt(NodeSize);
            if (node != CurrentNode) {
                TestGraph.connect(node, CurrentNode, DefaultWeight);
                MCcounterCheck++;
            }
        }
        for (node = 2; node < NodeSize/2; node += 7) {
            if (!TestGraph.getV(node).isEmpty()) {
                for (node_info neighbour : TestGraph.getV(node)) {
                    MCcounterCheck++;
                }
            }
            TestGraph.removeNode(node);
            MCcounterCheck++;
            assertEquals(TestGraph.getMC(), MCcounterCheck);
        }
    }

    @Test
    void General_Test() {
        int MCcheck = 0, EdgeCheck = 0, NodeCheck = 0;
        int NodeSize = 100000;
        for (node = 0; node < NodeSize; node++) {
            TestGraph.addNode(node);
            MCcheck++;
            NodeCheck++;
        }
        for (node = 0; node < 150000; node++){
            int FirstNode = rand.nextInt(NodeSize);
            int SecondNode = rand.nextInt(NodeSize);
            double RandomWeight = rand.nextDouble()*10;
            while (FirstNode == SecondNode || TestGraph.hasEdge(FirstNode,SecondNode)) {
                SecondNode = rand.nextInt(NodeSize);
            }
            TestGraph.connect(FirstNode, SecondNode, RandomWeight);
            MCcheck++;
            EdgeCheck++;
    }
        for (node = 0; node < TestGraph.nodeSize(); node+=10) {
            if (!TestGraph.getV(node).isEmpty()) {
                EdgeCheck -= TestGraph.getV(node).size();
                MCcheck += TestGraph.getV(node).size();
            }
            TestGraph.removeNode(node);
            MCcheck++;
            NodeCheck--;
        }
        assertEquals(TestGraph.nodeSize(), NodeCheck);
        assertEquals(TestGraph.edgeSize(), EdgeCheck);
        assertEquals(TestGraph.getMC(), MCcheck);
        assertNotNull(TestGraph);
        assertNull(TestGraph.getNode(0));
    }


}
