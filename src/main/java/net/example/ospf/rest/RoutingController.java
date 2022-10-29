package net.example.ospf.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.example.ospf.Country;
import net.example.ospf.services.RoutingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Slf4j
@Controller
public class RoutingController {
    private final RoutingService service;

    @GetMapping(value = "/routing/{origin}/{destination}", produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<String> routing(@PathVariable String origin, @PathVariable String destination) {
        log.debug("Requested route from {} to {}", origin, destination);
        Country[] countries = service.validateRequest(origin, destination);
        if (countries.length == 0) {
            return new ResponseEntity<>("Invalid parameters", HttpStatus.BAD_REQUEST);
        }
        String route = service.getRoute(origin, destination, countries);
        if (StringUtils.hasLength(route)) {
            return new ResponseEntity<>(route, HttpStatus.OK);
        }
        return new ResponseEntity<>("No route found between " + origin + " and " + destination, HttpStatus.BAD_REQUEST);
    }
}
