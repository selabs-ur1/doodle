package fr.istic.tlc;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.gitlab.javafuzz.core.AbstractFuzzTarget;


public class JavafuzzTestExample extends AbstractFuzzTarget{

    
    public String[] parseData(byte[] data) {

        String dataString = new String(data);

        String[] outputs = new String[3];
        String[] inputs = dataString.split("\\s+");

        outputs[0] = (inputs.length > 1) ? inputs[1] : "";
        outputs[1] = (inputs.length > 1) ? inputs[1] : "";
        outputs[2] = (inputs.length > 2) ? inputs[2] : "";

        return outputs;
    }

    public void testGet(String[] queries) {

        String link = (queries.length > 1) ? queries[1] : "";


        try {

            HttpRequest request = HttpRequest.newBuilder().uri(new URI(link)).GET().build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            assertEquals(response.statusCode(), 200);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void testPost(String[] queries) {

        String link = (queries.length > 1) ? queries[1] : "";
        String inputData = (queries.length > 2) ? queries[2] : "";

        try {
            
            HttpRequest request = HttpRequest.newBuilder().uri(new URI(link))
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(inputData)).build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            assertEquals(response.statusCode(), 201);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void testPut(String[] queries) {

        String link = (queries.length > 1) ? queries[1] : "";
        String inputData = (queries.length > 2) ? queries[2] : "";

        try {

            HttpRequest request = HttpRequest.newBuilder().uri(new URI(link))
                    .headers("Content-Type", "application/json;charset=UTF-8")
                    .PUT(HttpRequest.BodyPublishers.ofString(inputData)).build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            assertEquals(response.statusCode(), 200);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void testDelete(String[] queries) {

        String link = (queries.length > 1) ? queries[1] : "";

        try {

            HttpRequest request = HttpRequest.newBuilder().uri(new URI(link)).DELETE().build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request,
                    HttpResponse.BodyHandlers.ofString());

            assertEquals(response.statusCode(), 200);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void fuzz(byte[] data) {

        String[] queries = parseData(data);

        if (queries.length > 0 && queries[0].equals("GET"))
            testGet(queries);
        else if (queries.length > 0 && queries[0].equals("POST"))
            testPost(queries);
        else if (queries.length > 0 && queries[0].equals("PUT"))
            testPut(queries);
        else if (queries.length > 0 && queries[0].equals("DELETE"))
            testDelete(queries);
    }

    

}
