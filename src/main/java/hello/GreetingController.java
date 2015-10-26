package hello;

import java.util.concurrent.atomic.AtomicLong;

import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;


@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting
    (
        @RequestParam(value="name", defaultValue="World") String name
    ) 
    {
        return new Greeting(counter.incrementAndGet(),
                            String.format(template, name));
    }

    // some definitions
    static String personURI    = "http://somewhere/JohnSmith";
    static String fullName     = "John Smith";

    @RequestMapping("/")
    public String greeting()
    {
        Map<String, String[]> map = new HashMap<>();

        String[] keys = {"new", "world", "order"};
        String[][] values = {{"york", "delhi"}, 
                           {"health", "war"}, 
                           {"form", "former"}};

        for (int i = 0; i < keys.length; i++)
        {
            map.put(keys[i], values[i]);
        }
        
        StringBuilder mapString = new StringBuilder();
        mapString.append("<ol>");

        map.forEach( 

            (String a, String[] b) -> 
            {
                mapString.append("<li>" + a + ":");
                mapString.append("<ol>");

                Arrays.stream(b)
                      .forEach(
                        b_el -> 
                            mapString.append("<li>" + b_el + "</li>")
                );

                mapString.append("</ol>");
                mapString.append("</li>");
            }
        );

        mapString.append("</ol>");
        
        return "<h2>Hello, World!</h2><br>" + mapString.toString();
    }

    @RequestMapping("/rdf_test")
    public RDFTest rdfTest()
    {
        // TODO let full name, personURI, first and last names be arguments
        // that can get passed. Do the example iterating over all the statements
        // in the person model, as shown just under "Statements" on
        // https://jena.apache.org/tutorials/rdf_api.html#ch-Statements
        
        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource person = model.createResource(personURI);

        // add the property
        person.addProperty(VCARD.FN, fullName);

        System.out.print(model.listStatements().nextStatement().getSubject().toString());

        return new RDFTest();
    }
}
