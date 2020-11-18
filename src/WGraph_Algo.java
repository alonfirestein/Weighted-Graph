package ex1;


import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms, Serializable {
    private weighted_graph graph_algo;

    public WGraph_Algo() {
        graph_algo = new WGraph_DS();
    }


    @Override
    public void init(weighted_graph g) { graph_algo = g; }

    @Override
    public weighted_graph getGraph() {
        return graph_algo;
    }

    /**
     * Creating a deep copy of the weighted graph, including creating new nodes and edges with the exact same
     * values and info of the graph it copies.
     * @return - The new copied graph.
     */
    @Override
    public weighted_graph copy() {

        weighted_graph NewGraphCopy = new WGraph_DS();
        HashMap<Integer, node_info> NewNodeMap = new HashMap<>();

        for (node_info OriginalNode : graph_algo.getV()) {
            if (!NewGraphCopy.getV().contains(OriginalNode)) {
                node_info CopiedNode = new WGraph_DS.NodeInfo(OriginalNode);
                NewGraphCopy.addNode(CopiedNode.getKey());
                NewNodeMap.put(OriginalNode.getKey(), CopiedNode);

                if (!graph_algo.getV(OriginalNode.getKey()).isEmpty()) {
                    for (node_info neighbour : graph_algo.getV(OriginalNode.getKey())) {
                        if (NewNodeMap.get(neighbour.getKey()) == null) {
                            node_info CopiedNeighbour = new WGraph_DS.NodeInfo(neighbour);
                            NewGraphCopy.addNode(CopiedNeighbour.getKey());
                            NewNodeMap.put(OriginalNode.getKey(), CopiedNeighbour);
                            NewGraphCopy.connect(CopiedNode.getKey(), CopiedNeighbour.getKey(),
                                    graph_algo.getEdge(CopiedNode.getKey(), CopiedNeighbour.getKey()));
                        }
                    }
                }
            }
        }
        return NewGraphCopy;
    }

    /**
     * Checking if a graph is connected or not using the BFS algorithm method.
     * @return - If graph is connected
     */
    @Override
    public boolean isConnected() {
        if (graph_algo.getV().isEmpty() || (graph_algo.nodeSize() == 1)) {
            return true;
        }
        if (graph_algo.edgeSize() < graph_algo.nodeSize() - 1) {
            return false;
        }
        Queue<node_info> NodeQueue = new LinkedList<>();
        HashSet<node_info> VisitedNodesList = new HashSet<>();
        node_info CurrentNode;
        for (node_info node : graph_algo.getV()) {
            NodeQueue.add(node);
            break;
        }
        while (!NodeQueue.isEmpty()) {
            CurrentNode = NodeQueue.poll();
            if (!VisitedNodesList.contains(CurrentNode)) {
                VisitedNodesList.add(CurrentNode);
                for (node_info neighbour : graph_algo.getV(CurrentNode.getKey())) {
                    if (!VisitedNodesList.contains(neighbour)) {
                        NodeQueue.add(neighbour);
                    }
                }
            }
        }
        return graph_algo.nodeSize() == VisitedNodesList.size();
    }

    /**
     * Using the Dijkstra algorithm, we find the shortest path between two nodes of a Weighted Graph and taking
     * into account each edges weight.
     * @param src - start node
     * @param dest - end (target) node
     * @return - the sum weight of all the edges of the path
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if (src == dest) {
            return 0;
        }
        if (graph_algo.getNode(src) == null || graph_algo.getNode(dest) == null) {
            return -1;
        }
        HashMap<node_info, node_info> ParentsList = new HashMap<>();
        HashMap<node_info, Double> DistanceList = new HashMap<>();
        dijkstra_algo(src, ParentsList, DistanceList);
        if (DistanceList.get(graph_algo.getNode(dest)) == Double.MAX_VALUE) {
            return -1;
        }
        return DistanceList.get(graph_algo.getNode(dest));
    }

    /**
     * Using the Dijkstra algorithm, we find the shortest path between two nodes of a Weighted Graph and taking
     * into account each edges weight.
     * * @param src - start node
     * @param dest - end (target) node
     * @return - A list of the path between the two nodes.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        LinkedList<node_info> path = new LinkedList<>();
        if (src == dest) {
            return path;
        }
        if (graph_algo.getNode(src) == null || graph_algo.getNode(dest) == null) {
            return null;
        }
        HashMap<node_info, node_info> ParentsList = new HashMap<>();
        HashMap<node_info, Double> DistanceList = new HashMap<>();
        for (node_info node : graph_algo.getV()) {
            DistanceList.put(node, Double.MAX_VALUE);
        }
        dijkstra_algo(src, ParentsList, DistanceList);
        node_info BackTrackPath = graph_algo.getNode(dest);
        while (DistanceList.get(BackTrackPath) != 0.0) {
            path.add(BackTrackPath);
            if (ParentsList.get(BackTrackPath) != null)
                BackTrackPath = ParentsList.get(BackTrackPath);
            else {
                return null;
            }
        }
        path.add(BackTrackPath);
        Collections.reverse(path);
        return path;
    }

    /**
     * @param file - the file name (may include a relative path).
     * @return - If the save function was successful or not.
     */
    @Override
    public boolean save(String file) {
        if (graph_algo == null) {
            return false;
        }
        //Recreating the HashMap with all the nodes and it's edges to write to the file.
        HashMap <node_info, HashMap<node_info, Double>> GraphToWrite = new HashMap<>();
        for (node_info OriginalNode : graph_algo.getV()) {
            GraphToWrite.put(OriginalNode, new HashMap<node_info, Double>());

            if (!graph_algo.getV(OriginalNode.getKey()).isEmpty()) {
                for (node_info neighbour : graph_algo.getV(OriginalNode.getKey())) {
                    GraphToWrite.get(OriginalNode).put(neighbour, (graph_algo.getEdge(OriginalNode.getKey(), neighbour.getKey())));
                }
            }
        }
        try {
            FileOutputStream File = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(File);
            out.writeObject(GraphToWrite);
            out.close();
            File.close();
            return true;
        }
        catch (IOException ex) {
            System.out.println("IOException is caught");
            return false;
        }
    }

    /**
     * Using the EntrySet feature in Hashmaps, the function iterates over every node and it's neighbours in order
     * to connect them and add them to the new loaded graph.
     * @param file - file name
     * @return - If the load function was successful or not.
     */
    @Override
    public boolean load(String file) {
        if (file.equals(null)) {
            return false;
        }
        if (file.equals("")) {
            return false;
        }

        HashMap<node_info, HashMap<node_info, Double>> LoadGraph = new HashMap<>();
        try {
            FileInputStream File = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(File);
            LoadGraph = (HashMap<node_info, HashMap<node_info, Double>>) input.readObject();
            File.close();
            input.close();

        } catch (Exception ex) {
            System.out.println("IOException is caught");
            return false;
        }
        if (graph_algo == null) {
            graph_algo = new WGraph_DS();
        }
        for (Map.Entry<node_info,HashMap<node_info,Double>> EntryNode : LoadGraph.entrySet()) {
            if(graph_algo.getNode(EntryNode.getKey().getKey()) == null) {
                graph_algo.addNode(EntryNode.getKey().getKey());
            }
            for(Map.Entry<node_info,Double> Neigbour: EntryNode.getValue().entrySet()){
                if(graph_algo.getNode(Neigbour.getKey().getKey())==null) {
                    graph_algo.addNode(Neigbour.getKey().getKey());
                }
                graph_algo.connect(EntryNode.getKey().getKey(),Neigbour.getKey().getKey(),Neigbour.getValue());
            }
        }
        return graph_algo.nodeSize() == LoadGraph.size();
    }

    /**
     * To build and use the dijkstra algorithm, we use a number of different data structures in order to maximize
     * efficiency and find the shortest path and its distance between two nodes, using and prioritizing the weight
     * of each edge that the path passes through. Marking each visited node, and saving the distance of each different
     * path from the original src node where the path started. If we were able to find a shorter path to a certain
//     * node, then we know to use the new shorter path and disregard the older and longer one.
     * @param src - Source node, the node where all the distances will be measured from.
     * @param ParentsList - The list that saves all the parent/previous nodes in the path.
     * @param DistanceList - The list that saves all the distances in edge weight from the src node.
     */
    public void dijkstra_algo(int src, HashMap<node_info, node_info> ParentsList, HashMap<node_info, Double> DistanceList) {
        Queue<node_info> NodeQueue = new LinkedList<>();
        HashSet<node_info> VisitedNodes = new HashSet<>();
        for (node_info node : graph_algo.getV()) {
            DistanceList.put(node, Double.MAX_VALUE);
        }
        DistanceList.replace(graph_algo.getNode(src), 0.0);
        NodeQueue.add(graph_algo.getNode(src));
        VisitedNodes.add(graph_algo.getNode(src));
        while (!NodeQueue.isEmpty()) {
            node_info CurrentNode = NodeQueue.poll();

            for (node_info neighbour : graph_algo.getV(CurrentNode.getKey())) {
                VisitedNodes.add(neighbour);
                double distance = DistanceList.get(CurrentNode) +
                        graph_algo.getEdge(CurrentNode.getKey(), neighbour.getKey());

                if (distance < DistanceList.get(neighbour)) {
                    DistanceList.replace(neighbour, distance);
                    ParentsList.put(neighbour, CurrentNode);
                    NodeQueue.add(neighbour);
                    VisitedNodes.add(neighbour);
                }
            }
        }
    }
}
