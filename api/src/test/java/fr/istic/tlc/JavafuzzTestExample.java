package fr.istic.tlc;

import com.gitlab.javafuzz.core.AbstractFuzzTarget;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;


public class JavafuzzTestExample extends AbstractFuzzTarget{

    

    public String[] parseData(byte[] data) {

        String dataString = new String(data);

        String [] outputs = new String[3];
        String [] inputs = dataString.split("\\s+");

        outputs[0] = (inputs.length > 1) ? inputs[1] : "";
        outputs[1] = (inputs.length > 1) ? inputs[1] : "";
        outputs[2] = (inputs.length > 2) ? inputs[2] : "";
        
        return outputs;
    }
    
    public void testGet(String[] queries) {


        String link = (queries.length > 1) ? queries[1] : "";
    
        // ValidatableResponse response = given().when().get(link)
        // .then().assertThat().statusCode(200);

        // System.out.println("Get Response : "+response+"\n\n");

        try {
            
            HttpRequest request = HttpRequest.newBuilder()
                                .uri(new URI(link))
                                .GET().build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                                            .send(request,HttpResponse.BodyHandlers.ofString());
                    
            assertEquals(response.statusCode(), 200);

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    public void testPost(String[] queries) {

            String link = (queries.length > 1) ? queries[1] : "";
            String inputData = (queries.length > 2) ? queries[2] : "";

            ValidatableResponse response = given().when().post(link+"?="+inputData)
            .then().assertThat().statusCode(200);

            System.out.println("Post Response : " + response + "\n\n");
        
    }

    public void testPut(String[] queries) {

            String link = (queries.length > 1) ? queries[1] : "";
            String inputData = (queries.length > 2) ? queries[2] : "";

            ValidatableResponse response = given().when().put(link + "?=" + inputData).then().assertThat().statusCode(200);
            System.out.println("Put Response : " + response + "\n\n");
        
    }

    public void testDelete(String[] queries) {

            String link = (queries.length > 1) ? queries[1] : "";

            ValidatableResponse response = given().when().delete(link).then().assertThat().statusCode(200);
            System.out.println("Delete Response : " + response + "\n\n");
        
    }

    public void fuzz(byte[] data) {

        String[] queries = parseData(data);

        if (queries.length > 0 && queries[0].equals("GET"))
            testGet(queries);
        else if(queries.length > 0 && queries[0].equals("POST"))
            testPost(queries);
        else if (queries.length > 0 && queries[0].equals("PUT"))
            testPut(queries);
        else if (queries.length > 0 && queries[0].equals("DELETE"))
            testDelete(queries);
    }

}
