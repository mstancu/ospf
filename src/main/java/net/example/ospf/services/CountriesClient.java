package net.example.ospf.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

@FeignClient(name = "countries", url = "https://raw.githubusercontent.com/mledoze/countries/master/countries.json")
public interface CountriesClient {
    @RequestMapping(method = RequestMethod.GET, value = "", produces = "application/json", consumes = TEXT_PLAIN_VALUE)
    String getCountries();
}
