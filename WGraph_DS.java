package ex1;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

public class WGraph_DS implements  weighted_graph, Serializable {

    private int EdgeCounter, MCcounter;
    private HashMap<Integer, node_info> NodesInGraph;
    private HashMap<node_info, HashMap<node_info, Double>> EdgesInGraph;

    /**
     * Creating an inner class (nested class) which executes the node_info interface in order to create nodes for
     * our graph. Each node has a personal key to identify it, a Tag, and it's info (string format).
     */
    public static class NodeInfo implements node_info, Serializable {
        private int key;
        private double Tag;
        private String NodeInfo;

        public NodeInfo(int key) {
            this.key = key;
            NodeInfo = "";
            Tag = -1.0;
        }
        public NodeInfo(node_info node) {
            this.key = node.getKey();
            NodeInfo = node.getInfo();
            Tag = node.getTag();
        }
        public String toString() {
            return "" + key;
        }

        @Override
        public int getKey() {
            return key;
        }

        @Override
        public String getInfo() {
            return NodeInfo;
        }

        @Override
        public void setInfo(String s) {
            NodeInfo = s;
        }

        @Override
        public double getTag() {
            return Tag;
        }

        @Override
        public void setTag(double t) {
            Tag = t;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return key == nodeInfo.key &&
                    Double.compare(nodeInfo.Tag, Tag) == 0 &&
                    NodeInfo.equals(nodeInfo.NodeInfo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, Tag, NodeInfo);
        }
    }
    @Override
    public String toString() {
        String result = "";
        for (int node = 0; node < NodesInGraph.size(); node ++) {
            result += "Node: " + node + ", Neighbours: " + EdgesInGraph.get(getNode(node)).keySet() +
                    //", Info: " + getNode(node).getInfo() +", Tag: "+getNode(node).getTag() +
                    "\n";
        }
        return result;
    }

    public WGraph_DS() {
        EdgeCounter = 0;
        MCcounter = 0;
        NodesInGraph = new HashMap<>();
        EdgesInGraph = new HashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return EdgeCounter == wGraph_ds.EdgeCounter &&
                NodesInGraph.equals(wGraph_ds.NodesInGraph) &&
                EdgesInGraph.equals(wGraph_ds.EdgesInGraph);
    }

    @Override
    public int hashCode() {
        return Objects.hash(EdgeCounter, NodesInGraph, EdgesInGraph);
    }

    /**
     * @param key - the node_id
     * @return - the node depending on the key entered.
     */
    @Override
    public node_info getNode(int key) {
        try {
            return NodesInGraph.get(key);
        } catch (Exception e) {
            throw new IllegalArgumentException("The Node " + key + " does not exist");
        }
    }

    /**
     * @param node1 - first node of the edge.
     * @param node2 - second node of the edge.
     * @return - Checking if two nodes have an edge between them.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        return EdgesInGraph.get(getNode(node1)).containsKey(getNode(node2));

    }

    /**
     * @param node1 - first node of the edge.
     * @param node2 - second node of the edge.
     * @return - The weight of an edge that connects between to different nodes.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if (!hasEdge(node1, node2)) {
            return -1;
        }
        if (EdgesInGraph.get(getNode(node1)).get(getNode(node2)) == null) {
            throw new IllegalArgumentException("The edge does not exist!");
        }
        return EdgesInGraph.get(getNode(node1)).get(getNode(node2));
    }

    /**
     * Adding a new node to the graph.
     * @param key - the ID of the new node to be added to the graph.
     */
    @Override
    public void addNode(int key) {
        if (!NodesInGraph.containsKey(key)) {
            node_info node = new NodeInfo(key);
            NodesInGraph.put(key, node);
            EdgesInGraph.put(node, new HashMap<>());
            MCcounter++;
        }
    }

    /**
     * Creating an edge and connecting it between two nodes and giving it a positive weight/value.
     * If an edge between those two nodes already exist, then we simply just update to the new weight entered.
     * If one of the entered nodes does not exist, or they are the same node, then we simply do nothing.
     * @param node1 - the first node of the edge we want to connect.
     * @param node2 - the second node of the edge we want to connect.
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if (w < 0) {
            throw new IllegalArgumentException("ERROR: The input weight is negative!");
        }
        if (node1 == node2) {
            return;
        }
        if (getNode(node1) == null || getNode(node2) == null) {
            System.err.println("ERROR: One of the Nodes does not exist - " + node1 +" or " + node2);
            return;
        }
        if (hasEdge(node1,node2)) {
            removeEdge(node1, node2); //MC increases by one here so no need to do it again in this function.
            EdgeCounter++; //removeEdge function decreases EdgeCounter by 1, so we normalize it.
            EdgesInGraph.get(getNode(node1)).put(getNode(node2), w);
            EdgesInGraph.get(getNode(node2)).put(getNode(node1), w);
            return;
        }
        EdgesInGraph.get(getNode(node1)).put(getNode(node2),w);
        EdgesInGraph.get(getNode(node2)).put(getNode(node1),w);
        EdgeCounter++;
        MCcounter++;
    }

    /**
     * @return - A list of all the nodes in the graph.
     */
    @Override
    public Collection<node_info> getV() {
        return NodesInGraph.values();
    }

    /**
     * @param node_id - the ID of the node we want to return a list of it's neighbours.
     * @return - A list of all the nodes connected to the node with the entered node_id received.
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        if (getNode(node_id) == null) {
            throw new IllegalArgumentException("ERROR: The node does not exist.");
        }
        return EdgesInGraph.get(getNode(node_id)).keySet();
    }

    /**
     * When removing a node from a graph, we must remove all the edges connected to that node.
     * @param key - the ID of the node we want to remove from the graph.
     * @return - the removed node.
     */
    @Override
    public node_info removeNode(int key) {
        if (NodesInGraph.get(key) == null || !NodesInGraph.containsKey(key)) {
            return null;
        }
        ArrayList<node_info> NeighboursToRemove = new ArrayList<>();
        node_info result = getNode(key);

        //If our node doesn't have neighbours
        if (EdgesInGraph.get(getNode(key)).isEmpty()) {
            NodesInGraph.remove(key);
            MCcounter++;
            return result;
        }
        NeighboursToRemove.addAll(EdgesInGraph.get(getNode(key)).keySet());

        //Removing all of the nodes edges
        for (node_info neighbour : NeighboursToRemove) {
            removeEdge(key, neighbour.getKey());
        }

        NodesInGraph.remove(key);
        MCcounter++;
        return result;
    }

    /**
     * Deleting an edge that connects between two nodes.
     * @param node1 - the first node of the edge we want to remove.
     * @param node2 - the second node of the edge we want to remove.
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if (!hasEdge(node1,node2)) { return; }
        if (getNode(node1) == null || getNode(node2) == null) { return; }
        EdgesInGraph.get(getNode(node1)).remove(getNode(node2));
        EdgesInGraph.get(getNode(node2)).remove(getNode(node1));
        EdgeCounter--;
        MCcounter++;

    }

    @Override
    public int nodeSize() {
        return NodesInGraph.size();
    }

    @Override
    public int edgeSize() {
        return EdgeCounter;
    }

    @Override
    public int getMC() {
        return MCcounter;
    }
}
