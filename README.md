# Object Oriented Programming - Ariel University
#### Assignment: ex1
#### Student: Alon Firestein

![Weighted-Graph](/Users/alon/Desktop/kJF5K.png)

For this assignment, I implemented a weighted graph using their respective interfaces that was provided to me and successfully tested them with tests that I built using JUnit 5.

------------------------------------

**WGraph_DS**

In order to implement the nodes in the graph, I created a nested class which built the nodes and all of its values (key, info, tag) . 

To sucessfuly keep track of every node and edge in the graph, I decided to use the HashMap data structure simply because using its "get" and "put" functions allow me to store and pull information in O(1) complexity.

Given the fact that this is a weighted graph and each edge has a weight, I used a multi dimensional HashMap with another HashMap in it as it's value, because that way I was able to quickly store each nodes neighbour (adjacent node) and the weight of their edge.

------------------------------------

**WGraph_Algo** 

This class was built in order to properly utilize a couple algorithms in the graph such as finding if the graph is connected, finding the shortest path in the weighted graph, and its distance.

Some of these algorithms I built using the BFS algorithm method in order for traversing and searching throughout the graph, and others I used the Dijkstra algorthim which I also implemented, and using his algorithm I was able to find a weighted graphs shortest path between two nodes.





### Run time for functions: Worst case scenarios (Asymptotic Upper Bounds):
| Function Name    | Time Complexity           | Extra Info                                                   |
| ---------------- | ------------------------- | ------------------------------------------------------------ |
| isConnected      | O(\|V\|+\|E\|)            | The function goes through every node in the graph to find out if it's connected. |
| shortestPath     | O(\|V\|+\|E\|)            | The function goes through almost every node in the graph to find the shortest path. |
| shortestPathDist | O(\|V\|+\|E\|)            | Using shortestPath to find the distance (# of edges it had to cross). |
| dijkstra_algo    | O(\|V\|*log\|V\| + \|E\|) | Dijkstra algorithm built to find the shortest path between two nodes. |
| save             | O(\|V\|+\|E\|)            | Saving a serializable map of the graph on to a file path.    |
| load             | O(\|V\|+\|E\|)            | Loading a graph from a file path and building it from scratch. |

V - vertices   ,     E - edges




------------------------------------





Sources used for help:

- [Serialization](https://www.tutorialspoint.com/java/java_serialization.htm)

- [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html#entrySet--)

- [Iterating through a hashmap](https://stackoverflow.com/questions/1066589/iterate-through-a-hashmap)

- [Dijkstra Algorithm video](https://www.youtube.com/watch?v=pVfj6mxhdMw)

  
