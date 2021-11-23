package fr.istic.tlc;

import com.gitlab.javafuzz.core.AbstractFuzzTarget;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class JavafuzzTestExample extends AbstractFuzzTarget{

    public String parseData(byte[] data) {

        return "";
    }
    
    public boolean testGet(byte[] data) {


        // given()
        // .when().
        // get("/api/polls").
        // then().
        // statusCode(200).
        // body(is("hello"));

        return true;
    }

    public void fuzz(byte[] data) {

        testGet(data);
    }

}
