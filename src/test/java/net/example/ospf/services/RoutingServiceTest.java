package net.example.ospf.services;

import net.example.ospf.model.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RoutingServiceTest {

    public static final String ORIGIN_1 = "CZE";
    public static final String DESTINATION_1 = "LSO";
    public static final String DESTINATION_2 = "USA";
    public static final String EXPECTED_ROUTE_1 = "[CZE, DEU, FRA, ESP, MAR, DZA, LBY, TCD, CAF, COD, AGO, NAM, ZAF, LSO]";

    class MyFeign implements CountriesClient {

        @Override
        public String getCountries() {
            return countriesString;
        }
    }

    MyFeign countriesClient = new MyFeign();

    String countriesString;

    @BeforeEach
    void setUp() throws Exception {
        File file = new File(getClass().getResource("/countries.json").toURI());
        countriesString = new String(Files.readAllBytes(file.toPath()));
    }

    @Test
    void getReachable() {
        RoutingService routingService = new RoutingService(countriesClient);
        Country[] countries = routingService.validateRequest(ORIGIN_1, DESTINATION_1);
        String route = routingService.getRoute(ORIGIN_1, DESTINATION_1, countries);
        assertEquals(EXPECTED_ROUTE_1, route);
    }

    @Test
    void getUnreachable() {
        RoutingService routingService = new RoutingService(countriesClient);
        Country[] countries = routingService.validateRequest(ORIGIN_1, DESTINATION_1);
        String route = routingService.getRoute(ORIGIN_1, DESTINATION_2, countries);
        assertEquals("", route);
    }
}