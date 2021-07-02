import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFLanguages;
import org.apache.jena.riot.RDFParser;
import org.apache.jena.riot.system.ErrorHandlerFactory;
import org.apache.jena.riot.system.stream.LocatorFile;
import org.apache.jena.riot.system.stream.StreamManager;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.PrintUtil;
import org.apache.jena.vocabulary.RDF;

import java.io.*;
import java.util.Iterator;

import static org.apache.jena.ontology.OntModelSpec.OWL_MEM;
import static org.apache.jena.ontology.OntModelSpec.OWL_MEM_MICRO_RULE_INF;

public class JENADemoApplication {
    public static void main(String[] args) {

        Model model = RDFDataMgr.loadModel("PizzaTutorial.owl");

        // AmericanaHotPizza - forIndividuals
        // CheeseTopping - forSubClass
        String typeClass = "AmericanaHotPizza";

        //findSubClass(typeClass, model);
        //findIndividuals(typeClass, model);

        // It's pizza ? typeIndividuo - MargheritaPizza1 typeClass - Pizza
        String typeIndividual = "MargheritaPizza1";
        String typeClassReasoner = "Pizza";
        reasonerSubClass(typeIndividual, typeClassReasoner, model);

    }

    public static void printStatements(Model m, Resource s, Property p, Resource o) {
        for (StmtIterator i = m.listStatements(s, p, o); i.hasNext(); ) {
            Statement stmt = i.nextStatement();
            System.out.println(" - " + PrintUtil.print(stmt));
        }
    }

    public static void printQuery(String queryString, Model model) {
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();

            for (; results.hasNext(); ) {
                QuerySolution soln = results.nextSolution();
                System.out.print("\n" + soln);

            }

        }
    }

    public static void findSubClass(String typeClass, Model model) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                + "PREFIX pizza: <http://www.semanticweb.org/jorge/ontologies/2021/5/PizzaTutorial#>"
                + "SELECT DISTINCT * WHERE { ?s rdfs:subClassOf pizza:" + typeClass + " }";
        printQuery(queryString, model);

    }

    public static void findIndividuals(String typeClass, Model model) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                + "PREFIX pizza: <http://www.semanticweb.org/jorge/ontologies/2021/5/PizzaTutorial#>"
                + "SELECT DISTINCT * WHERE { ?s rdf:type pizza:" + typeClass + " }";
        printQuery(queryString, model);

    }

    public static void reasonerSubClass(String subClass, String classModel, Model model) {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(model);
        InfModel infmodel = ModelFactory.createInfModel(reasoner, model);
        //Resource nForce = infmodel.getResource("http://www.semanticweb.org/jorge/ontologies/2021/5/PizzaTutorial#Pizza");
        //System.out.println("nForce *:");
        //printStatements(infmodel, nForce, null, null);

        Resource pizza = infmodel.getResource("http://www.semanticweb.org/jorge/ontologies/2021/5/PizzaTutorial#" + classModel);
        Resource typePizza = infmodel.getResource("http://www.semanticweb.org/jorge/ontologies/2021/5/PizzaTutorial#" + subClass);
        if (infmodel.contains(typePizza, RDF.type, pizza)) {
            System.out.println(subClass + " es " + classModel);
        } else {
            System.out.println(subClass + " no es " + classModel);
        }
    }


}
