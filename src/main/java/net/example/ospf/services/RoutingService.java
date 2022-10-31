package net.example.ospf.services;

import com.google.common.graph.MutableValueGraph;
import com.google.common.graph.ValueGraph;
import com.google.common.graph.ValueGraphBuilder;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.example.ospf.model.Country;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.BFSShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class RoutingService {
    private final Country[] countries;

    public RoutingService(CountriesClient client) {
        String countriesString = client.getCountries();
        countries = new Gson().fromJson(countriesString, Country[].class);
        log.debug("Loaded {} countries", countries.length);
    }

    public Country[] validateRequest(String origin, String destination) {
        boolean isValidOrigin = Arrays.stream(countries).anyMatch(country -> country.getCca3().equals(origin));
        boolean isValidDestination = Arrays.stream(countries).anyMatch(country -> country.getCca3().equals(destination));
        if (isValidOrigin && isValidDestination) {
            return countries;
        }
        log.error("Either origin: {}, or destination: {} doesn't belong to countries", origin, destination);
        return new Country[0];
    }

    /**
     * Implemented with RouteJGraphT
     *
     * @param origin      Start Country
     * @param destination Destination Country
     * @return route between - if exists
     */
    public String getRoute(String origin, String destination, Country[] countries) {

        Graph<String, DefaultEdge> worldCountriesMap = constructJGTGraph(countries);
        GraphPath<String, DefaultEdge> path;

        long start = System.currentTimeMillis();
        path = ((ShortestPathAlgorithm<String, DefaultEdge>) new DijkstraShortestPath<>(worldCountriesMap))
                .getPath(origin.toUpperCase(), destination.toUpperCase());
        log.debug("JGraphT-DijkstraShortestPath (time {}): {}", System.currentTimeMillis() - start,
                path != null ? path.getVertexList().toString() : "path not found");

        start = System.currentTimeMillis();
        path = ((ShortestPathAlgorithm<String, DefaultEdge>) new BFSShortestPath<>(worldCountriesMap))
                .getPath(origin.toUpperCase(), destination.toUpperCase());
        log.debug("JGraphT-BFSShortestPath (time {}): {}", System.currentTimeMillis() - start,
                path != null ? path.getVertexList().toString() : "path not found");

        start = System.currentTimeMillis();
        ValueGraph<String, Integer> graph = constructGraphGuava(countries);
        List<String> path1 = DijkstraAlgorithm.findShortestPath(graph, origin, destination);
        log.debug("Dijkstra custom (time {}): {}", System.currentTimeMillis() - start,
                path1.isEmpty() ? "path not found" : path1.toString());

        if (path != null) {
            return path.getVertexList().toString();
        }
        return "";
    }

    private ValueGraph<String, Integer> constructGraphGuava(Country[] countries) {
        MutableValueGraph<String, Integer> graph = ValueGraphBuilder.undirected().build();
        for (Country country : countries) {
            for (String borderTo : country.getBorders()) {
                graph.putEdgeValue(country.getCca3(), borderTo, 1);
            }
        }
        return graph;
    }

    private Graph<String, DefaultEdge> constructJGTGraph(Country[] countries) {
        Graph<String, DefaultEdge> graph = new SimpleGraph<>(DefaultEdge.class);
        Arrays.stream(countries).forEach(country -> graph.addVertex(country.getCca3()));
        for (Country country : countries) {
            String countryName = country.getCca3();
            for (String borderTo : country.getBorders()) {
                graph.addEdge(countryName, borderTo);
            }
        }
        return graph;
    }
}
