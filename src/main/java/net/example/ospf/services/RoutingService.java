package net.example.ospf.services;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.example.ospf.Country;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphWalk;
import org.jgrapht.graph.SimpleGraph;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoutingService {
    private final CountriesClient client;

    public Country[] validateRequest(String origin, String destination) {
        String countriesString = client.getCountries();
        Country[] countries = new Gson().fromJson(countriesString, Country[].class);
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
     * @param origin Start Country
     * @param destination Destination Country
     * @return route between - if exists
     */
    public String getRoute(String origin, String destination, Country[] countries) {

        Graph<String, DefaultEdge> worldCountriesMap = constructJGTGraph(countries);
        ShortestPathAlgorithm<String, DefaultEdge> pathFinder = new DijkstraShortestPath<>(worldCountriesMap);
        GraphWalk<String, DefaultEdge> path = (GraphWalk<String, DefaultEdge>) pathFinder
                .getPath(origin.toUpperCase(), destination.toUpperCase());
        if (path != null) {
            return path.getVertexList().toString();
        }
        return "";
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
