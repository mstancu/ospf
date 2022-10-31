package net.example.ospf.model;

import java.util.*;

public class Graph {
    List<String> adjacentNodes1 = new LinkedList<>();
    public List<String> adjacentNodes(String node){
        return new LinkedList<>();
    }

    Map<String, String> edges = new HashMap<>();

    public void putEdge(String vertex, String vertexNeighbour){
        edges.put(vertex, vertexNeighbour);
    }
}
