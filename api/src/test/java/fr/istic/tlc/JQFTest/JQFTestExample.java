package fr.istic.tlc.JQFTest;

import org.junit.runner.RunWith;

import edu.berkeley.cs.jqf.fuzz.Fuzz;
import edu.berkeley.cs.jqf.fuzz.JQF;

import static fr.istic.tlc.JavafuzzTestExample.testGet;
import static fr.istic.tlc.JavafuzzTestExample.testPost;
import static fr.istic.tlc.JavafuzzTestExample.testPut;

import com.pholser.junit.quickcheck.From;

import static fr.istic.tlc.JavafuzzTestExample.testDelete;

@RunWith(JQF.class)
public class JQFTestExample {
   

    @Fuzz
    public void fuzzTest(@From(JQFTestQueryGenerator.class) String input) {

        System.out.println("\n-- Query : "+input );
        String[] queries = input.split("\\s+");;

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
