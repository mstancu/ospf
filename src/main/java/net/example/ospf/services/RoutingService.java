package net.example.ospf.services;

import org.springframework.stereotype.Service;

@Service
public class RoutingService {

    public boolean validateRequest(String origin, String destination) {
        return origin != null && destination != null;
    }

    public String getRoute(String origin, String destination) {
        return "Not implemeted !";
    }
}
