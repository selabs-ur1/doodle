package fr.istic.tlc.JQFTest;

import com.mifmif.common.regex.Generex;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;


public class JQFTestQueryGenerator extends Generator<String> {

    public JQFTestQueryGenerator() {
        super(String.class);
    }

    @Override
    public String generate(SourceOfRandomness r, GenerationStatus s) {

        Generex regex = new Generex("(GET|POST|PUT|DELETE) https?://localhost://[0-9]{4}/([a-zA-Z0-9]{1,10}/){1,5}[1-9]*");
            
        return regex.random(0, s.size());
    }

}

