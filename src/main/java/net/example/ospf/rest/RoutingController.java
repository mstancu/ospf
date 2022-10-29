package net.example.ospf.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.example.ospf.services.RoutingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RequiredArgsConstructor
@Slf4j
@Controller
public class RoutingController {
    private final RoutingService service;

    @GetMapping("/routing/{origin}/{destination}")

    public ResponseEntity<String> routing(@PathVariable String origin, @PathVariable String destination) {
        log.debug("Requested route from {} to {}", origin, destination);
        boolean routeFound = service.validateRequest(origin, destination);
        if (!routeFound) {
            return new ResponseEntity<>("Cannot fulfill request", HttpStatus.BAD_REQUEST);
        }
        String route = service.getRoute(origin, destination);
        return new ResponseEntity<>(route, HttpStatus.OK);
    }
}
