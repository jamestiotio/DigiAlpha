package dfs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.ArrayDeque;

public class Graph {
    private HashMap<Vertex, HashSet<Vertex>> adjacencyLists;
    private HashMap<Integer, Vertex> vertices;
    private int index; // This index will be used by Tarjan's Algorithm
    private ArrayDeque<Vertex> stack = new ArrayDeque<>();
    private ArrayList<ArrayList<Vertex>> sccList = new ArrayList<>();

    Graph(int numOfVertices, ArrayList<ArrayList<Integer>> edges) {
        this.adjacencyLists = new HashMap<>();
        this.vertices = new HashMap<>();

        // Add pair of vertices into the graph's adjacency list
        for (int id = 1; id <= numOfVertices; id++) {
            Vertex positiveVertex = new Vertex();
            positiveVertex.setID(id);
            this.adjacencyLists.put(positiveVertex, new HashSet<>());
            this.vertices.put(id, positiveVertex);

            Vertex negativeVertex = new Vertex();
            negativeVertex.setID(-id);
            this.adjacencyLists.put(negativeVertex, new HashSet<>());
            this.vertices.put(-id, negativeVertex);
        }

        // Add all edges
        for (ArrayList<Integer> edge : edges) {
            Vertex from = vertices.get(edge.get(0));
            Vertex to = vertices.get(edge.get(1));

            this.adjacencyLists.get(from).add(to);
        }
    }

    public HashSet<Vertex> getVertexFromAdjacencyLists(Vertex vertex) {
        return this.adjacencyLists.get(vertex);
    }

    public Set<Vertex> getAdjacencyListsKeySet() {
        return this.adjacencyLists.keySet();
    }

    public int getIndex() {
        return this.index;
    }

    public ArrayList<ArrayList<Vertex>> getSCCList() {
        return this.sccList;
    }

    public void setIndex(int newIndex) {
        this.index = newIndex;
    }

    public void pushToStack(Vertex vertex) {
        this.stack.push(vertex);
    }

    public Vertex popFromStack() {
        return this.stack.pop();
    }

    public void addToSCCList(ArrayList<Vertex> newSCC) {
        this.sccList.add(newSCC);
    }
}
