package fr.istic.tlc.resources;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.istic.tlc.domain.Weather;

@RestController
@RequestMapping("/api")
public class WeatherRessourceEx {

    @GetMapping("/weather")
    public ResponseEntity<Weather> retrieveWeather() {
        Weather weather = new Weather(new java.util.Date());

        return new ResponseEntity<>(weather, HttpStatus.OK);
    }

}
