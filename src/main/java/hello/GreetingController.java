package hello;

import java.util.concurrent.atomic.AtomicLong;
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
