"# sematic-app-demo" 

## App Pizza 
### UTPL - Sistemas Basados en Conocimientos
Nombre: Jorge Alcivar Hurtado Duarte

- Permite realizar consultas y recuperar información de la ontología de Pizzas

- Permite inferir ingredientes que sean similares para preparar las cubiertas

### Características del demo

1. Nos permirte leer una ontologia local, o recuperar infoacion de una ontologia desde la web
```java
Model model = RDFDataMgr.loadModel("PizzaTutorial.owl");
``` 
2. Realizar consultas a la ontología.
```java
    public static void findIndividuals(String typeClass, Model model) {
        String queryString = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
                + "PREFIX owl: <http://www.w3.org/2002/07/owl#>"
                + "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
                + "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>"
                + "PREFIX pizza: <http://www.semanticweb.org/jorge/ontologies/2021/5/PizzaTutorial#>"
                + "SELECT DISTINCT * WHERE { ?s rdf:type pizza:" + typeClass + " }";
        printQuery(queryString, model);

    }
``` 
3. Cuenta con la implementación de un razonador que nos permitirá encontrar las relaciones de subclase entre todos los conceptos declarados explícitamente a fin de construir la jerarquía de clases.
```java
  public static void reasonerClass(String classModel, Model model) {
        Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
        reasoner = reasoner.bindSchema(model);
        InfModel infmodel = ModelFactory.createInfModel(reasoner, model);
        Resource nForce = infmodel.getResource("http://www.semanticweb.org/jorge/ontologies/2021/5/PizzaTutorial#"
                + classModel);
        System.out.println("nForce *:");
        printStatements(infmodel, nForce, null, null);
    }
``` 
