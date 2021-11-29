package fr.istic.tlc.resources;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.eclipse.microprofile.config.inject.ConfigProperty;

@RestController
@RequestMapping("/api")
public class WeatherRessourceEx {

    @ConfigProperty(name = "doodle.weatherServiceUrl", defaultValue = "http://localhost:3000/")
    String weatherServiceUrl = "";

    @GetMapping("/weather")
    public ResponseEntity<String> retrieveWeather() throws InterruptedException, ExecutionException, IOException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        HttpGet request = new HttpGet(weatherServiceUrl + "api/weather");

        Future<HttpResponse> future = client.execute(request, null);
        HttpResponse response = future.get();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        response.getEntity().writeTo(out);
        String responseString = out.toString();
        out.close();
        client.close();

        System.out.println(responseString);

        return new ResponseEntity<>(responseString, HttpStatus.OK);
    }

}
