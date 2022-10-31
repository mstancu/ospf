package net.example.ospf.services;

import com.google.common.graph.ValueGraph;
import net.example.ospf.model.VertexWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class DijkstraAlgorithm {

    public static <N> List<N> findShortestPath(ValueGraph<N, Integer> graph, N source, N target) {
        Map<N, VertexWrapper<N>> explored = new HashMap<>();
        PriorityQueue<VertexWrapper<N>> unexplored = new PriorityQueue<>();
        Set<N> shortestPath = new HashSet<>();

        VertexWrapper<N> sourceVertex = new VertexWrapper<>(source, 0, null);
        explored.put(source, sourceVertex);
        unexplored.add(sourceVertex);

        while (!unexplored.isEmpty()) {
            VertexWrapper<N> vertexWrapper = unexplored.poll();
            N node = vertexWrapper.getNode();
            shortestPath.add(node);

            if (node.equals(target)) {
                return buildPath(vertexWrapper);
            }

            Set<N> neighbors = graph.adjacentNodes(node);
            for (N neighbor : neighbors) {
                if (shortestPath.contains(neighbor)) {
                    continue;
                }

                int distance = graph.edgeValue(node, neighbor).orElseThrow(IllegalStateException::new);
                int totalDistance = vertexWrapper.getTotalDistance() + distance;

                VertexWrapper<N> neighborWrapper = explored.get(neighbor);
                if (neighborWrapper == null) {
                    neighborWrapper = new VertexWrapper<>(neighbor, totalDistance, vertexWrapper);
                    explored.put(neighbor, neighborWrapper);
                    unexplored.add(neighborWrapper);
                }
                else {
                    if (totalDistance < neighborWrapper.getTotalDistance()) {
                        neighborWrapper.setTotalDistance(totalDistance);
                        neighborWrapper.setPredecessor(vertexWrapper);

                        unexplored.remove(neighborWrapper);
                        unexplored.add(neighborWrapper);
                    }
                }
            }
        }
        return Collections.emptyList();
    }

    private static <N> List<N> buildPath(VertexWrapper<N> dest) {
        VertexWrapper<N> destination = dest;
        List<N> path = new ArrayList<>();
        while (destination != null) {
            path.add(destination.getNode());
            destination = destination.getPredecessor();
        }
        Collections.reverse(path);
        return path;
    }
}