/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.uga.miashs.sempic.rdf;

import fr.uga.miashs.sempic.model.rdf.SempicOnto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfconnection.RDFConnection;
import org.apache.jena.rdfconnection.RDFConnectionFactory;
import org.apache.jena.sparql.core.Var;
import org.apache.jena.sparql.modify.request.QuadAcc;
import org.apache.jena.sparql.modify.request.UpdateDeleteWhere;
import org.apache.jena.update.Update;
import org.apache.jena.vocabulary.RDFS;

/**
 *
 * @author martin
 */
@Stateless
@LocalBean
public class RDFStore {

    public final static String ENDPOINT_QUERY = "http://localhost:3030/sempic/sparql"; // SPARQL endpoint
    public final static String ENDPOINT_UPDATE = "http://localhost:3030/sempic/update"; // SPARQL UPDATE endpoint
    public final static String ENDPOINT_GSP = "http://localhost:3030/sempic/data"; // Graph Store Protocol

    protected final RDFConnection cnx;

    public RDFStore() {
        cnx = RDFConnectionFactory.connect(ENDPOINT_QUERY, ENDPOINT_UPDATE, ENDPOINT_GSP);
    }

    /**
     * Save the given model into the triple store.
     * @param m THe Jena model to be persisted
     */
    public void saveModel(Model m) {
        cnx.begin(ReadWrite.WRITE);
        cnx.load(m);
        cnx.commit();
    }

    /**
     * Delete the given model from the triple store. Be carreful: Blank nodes
     * are replaced by variables
     * @param m the model to be deleted
     */
    public void deleteModel(Model m) {
        HashMap<Resource, Var> map = new HashMap<>();
        QuadAcc acc = new QuadAcc();
        m.listStatements().forEachRemaining(st -> {
            if (st.getSubject().isAnon() || st.getObject().isAnon()) {
                Node s = blankNodeAsVariable(st.getSubject(), map);
                Node p = st.getPredicate().asNode();
                Node o = blankNodeAsVariable(st.getObject(), map);

                acc.addTriple(new Triple(s, p, o));
            } else {

                acc.addTriple(st.asTriple());
            }
        });

        Update u = new UpdateDeleteWhere(acc);
        //System.out.println(u);
        cnx.begin(ReadWrite.WRITE);
        cnx.update(u);
        cnx.commit();
    }

    private Node blankNodeAsVariable(Resource r, Map<Resource, Var> bnMap) {
        if (r.isAnon()) {
            Var v = bnMap.get(r);
            if (v == null) {
                bnMap.put(r, v = Var.alloc("A" + bnMap.size()));
            }
            return v;
        }
        return r.asNode();
    }

    private Node blankNodeAsVariable(RDFNode r, Map<Resource, Var> bnMap) {
        if (r.isAnon()) {
            return blankNodeAsVariable(r.asResource(), bnMap);
        }
        return r.asNode();
    }

    /**
     * Delete all the statements where the resource appears as subject or object
     * @param r The named resource to be deleted (the resource cannot be annonymous)
     */
    public void deleteResource(Resource r) {
        if (r.isURIResource()) {
            cnx.begin(ReadWrite.WRITE);
            cnx.update("DELETE WHERE { <" + r.getURI() + "> ?p ?o }");
            cnx.update("DELETE WHERE { ?s ?p <" + r.getURI() + "> }");
            cnx.commit();
        }
    }

    /**
     * Retieves all the resources that are subclasses of resource c. To be
     * selected classes must have the property rdfs:label instanciated
     *
     * @param c A named class (the resource cannot be annonymous)
     * @return
     */
    public List<Resource> listSubClassesOf(Resource c) {
        Model m = cnx.queryConstruct("CONSTRUCT { "
                + "?s <" + RDFS.label + "> ?o "
                + "} WHERE {"
                + "?s <" + RDFS.subClassOf + "> <" + c.getURI() + "> ."
                + "?s <" + RDFS.label + "> ?o ."
                + "}");
        return m.listSubjects().toList();
    }


    public List<Resource> createAnonInstances(List<Resource> classes) {
        Model m = ModelFactory.createDefaultModel();
        List<Resource> res = new ArrayList<>();
        for (Resource c : classes) {
            Resource instance = m.createResource(c);
            instance.addLiteral(RDFS.label, "a " + c.getProperty(RDFS.label).getLiteral());
            res.add(instance);
        }
        return res;
    }

    public Resource createPhoto(long photoId, long albumId, long ownerId) {
        // create an empty RDF graph
        Model m = ModelFactory.createDefaultModel();
        // create an instance of Photo in Model m
        Resource pRes = m.createResource(Namespaces.getPhotoUri(photoId), SempicOnto.Photo);

        pRes.addLiteral(SempicOnto.albumId, albumId);
        pRes.addLiteral(SempicOnto.ownerId, ownerId);

        saveModel(m);

        return pRes;
    }

    /**
     * Query a Photo and retrieve all the direct properties of the photo and if
     * the property are depic, takenIn or takenBy, it also retrieve the labels
     * of the object of these properties
     *
     * @param id
     * @return
     */
    public Resource readPhoto(long id) {
        String pUri = Namespaces.getPhotoUri(id);

        String s = "CONSTRUCT {"
                + "<" + pUri + "> ?p ?o . "
                + "<" + pUri + "> ?p1 ?o1 . "
                + "?o1 <" + RDFS.label + "> ?o2 . "
                + "} "
                + "WHERE { "
                + "<" + pUri + "> ?p ?o . "
                + "OPTIONAL {"
                + "<" + pUri + "> ?p1 ?o1 ."
                + "?o1 <" + RDFS.label + "> ?o2 ."
                + "FILTER (?p1 IN (<" + SempicOnto.depicts + ">,<" + SempicOnto.takenIn + ">,<" + SempicOnto.takenBy + ">)) "
                + "}"
                + "}";
        Model m = cnx.queryConstruct(s);
        return m.getResource(pUri);
    }

    public static void main(String[] args) {
        RDFStore s = new RDFStore();
        Model m = ModelFactory.createDefaultModel();
        
        // Album 1, de l'owner 1
        // Photo 1 : Description -> Photo de Patrick et Medor, prise par Pierre à Grenoble le 05/01/2017 
        Resource pRes = s.createPhoto(1, 1, 1);

        Resource newMedor = m.createResource(SempicOnto.Dog);
        newMedor.addLiteral(RDFS.label, "Medor");
        m.add(pRes, SempicOnto.depicts, newMedor);
        Resource newPatrick = m.createResource(SempicOnto.Male);
        newPatrick.addLiteral(RDFS.label, "Patrick");
        m.add(pRes, SempicOnto.depicts, newPatrick);
        Resource newPierre = m.createResource(SempicOnto.Male);
        newPierre.addLiteral(RDFS.label, "Pierre");
        m.add(pRes, SempicOnto.takenBy, newPierre);
        m.add(pRes, SempicOnto.takenWhen, "2017-01-05");
        Resource newGrenoble = m.createResource(SempicOnto.City);
        newGrenoble.addLiteral(RDFS.label, "Grenoble");
        m.add(pRes, SempicOnto.takenIn, newGrenoble);
        
        // Photo 2 : Description -> 2 hommes + 2 chevaux à la plage, prise par Pierre à l'île Maurice, en été
        Resource pRes2 = s.createPhoto(2, 1, 1);
        
        Resource newHorse1 = m.createResource(SempicOnto.Horse);
        Resource newHorse2 = m.createResource(SempicOnto.Horse);
        Resource newAnonymous1 = m.createResource(SempicOnto.Person);
        Resource newAnonymous2 = m.createResource(SempicOnto.Person);
        Resource newPlaceBeach = m.createResource(SempicOnto.Beach);
        Resource newSummer = m.createResource(SempicOnto.Summer);
        
        m.add(pRes2, SempicOnto.depicts, newHorse1);
        m.add(pRes2, SempicOnto.depicts, newHorse2);
        m.add(pRes2, SempicOnto.depicts, newAnonymous1);
        m.add(pRes2, SempicOnto.depicts, newAnonymous2);
            m.add(newAnonymous1, SempicOnto.isFriend, newAnonymous2);
        m.add(pRes2, SempicOnto.takenWhen, newSummer);
        m.add(pRes2, SempicOnto.takenIn, newPlaceBeach);
        m.add(pRes2, SempicOnto.takenBy, newPierre);
       
        // Photo 3 : Description -> Photo de Félix le chat, prise par Pierre à Grenoble, le 27/08/2017
        Resource pRes3 = s.createPhoto(3, 1, 1);
        
        Resource newFelix = m.createResource(SempicOnto.Cat);
        
        m.add(pRes3, SempicOnto.depicts, newFelix);
        m.add(pRes3, SempicOnto.takenWhen, "2017-08-27");
        m.add(pRes3, SempicOnto.takenIn, newGrenoble);
        m.add(pRes3, SempicOnto.takenBy, newPierre);
        
        
        
        // Album 2, de l'owner 1
        // Photo 1 : Descrption -> Une autruche dans la savanne
        Resource pRes4 = s.createPhoto(4, 2, 1);
        
        Resource newOstrich = m.createResource(SempicOnto.Ostrich);
        Resource newSavanna = m.createResource(SempicOnto.Savanna);
        
        m.add(pRes4, SempicOnto.depicts, newOstrich);
        m.add(pRes4, SempicOnto.takenWhen, "2014-10-10");
        m.add(pRes4, SempicOnto.takenIn, newSavanna);
        m.add(pRes4, SempicOnto.takenBy, newPierre);
        
        // Photo 2 : Description ->
        Resource pRes5 = s.createPhoto(5, 2, 1);
        Resource newNight = m.createResource (SempicOnto.Nigth);
        Resource newMountain = m.createResource (SempicOnto.Mountain);
       
        m.add(pRes5, SempicOnto.takenWhen, "2014-10-10");
        m.add(pRes5, SempicOnto.takenWhen, newNight);
        m.add(pRes5, SempicOnto.takenIn, newMountain);
        m.add(pRes5, SempicOnto.takenBy, newPierre);
        
        // Photo 3 : Description ->
        Resource pRes6 = s.createPhoto(6, 2, 1);
        Resource newBeach = m.createResource (SempicOnto.Beach);
        Resource newHollidays = m.createResource (SempicOnto.Hollidays);
       
        m.add(pRes5, SempicOnto.takenWhen, "2014-05-07");
        m.add(pRes5, SempicOnto.takenWhen, newHollidays);
        m.add(pRes5, SempicOnto.takenIn, newBeach);
        m.add(pRes5, SempicOnto.takenBy, newPierre);
        
        // Album 3, de l'owner 2
        // Photo 1 : Description -> Pendaison de crémaillère avec Elisa, Marcella, Sophia et Lucas
        Resource pRes7 = s.createPhoto(7, 3, 2);
        
        Resource newElisa = m.createResource(SempicOnto.Female);
        Resource newMarcella = m.createResource(SempicOnto.Female);
        Resource newSophia = m.createResource(SempicOnto.Female);
        Resource newLucas = m.createResource(SempicOnto.Male);
        newElisa.addLiteral(RDFS.label, "Elisa");
        newMarcella.addLiteral(RDFS.label, "Marcella");
        newSophia.addLiteral(RDFS.label, "Sophia");
        newLucas.addLiteral(RDFS.label, "Lucas");
            m.add(newElisa, SempicOnto.isFriend, newMarcella);
            m.add(newElisa, SempicOnto.isFriend, newSophia);
            m.add(newElisa, SempicOnto.isFriend, newLucas);
            m.add(newMarcella, SempicOnto.isFriend, newSophia);
            m.add(newMarcella, SempicOnto.isFriend, newLucas);
            m.add(newLucas, SempicOnto.isFriend, newSophia);
        Resource newParty = m.createResource(SempicOnto.House_warming_party);
        
        m.add(pRes7, SempicOnto.depicts, newElisa);
        m.add(pRes7, SempicOnto.depicts, newMarcella);
        m.add(pRes7, SempicOnto.depicts, newSophia);
        m.add(pRes7, SempicOnto.depicts, newLucas);
        m.add(pRes7, SempicOnto.takenWhen, "2016-08-02");
        m.add(pRes7, SempicOnto.takenBy, newElisa);
        
        // Photo 2 -> Description : Lucie, Emilie et Suzie jouent aux jeux vidéos
        Resource pRes8 = s.createPhoto(8, 3, 2);
        
        Resource newLucie = m.createResource(SempicOnto.Female);
        newLucie.addLiteral(RDFS.label, "Lucie");
        Resource newEmilie = m.createResource(SempicOnto.Female);
        newEmilie.addLiteral(RDFS.label, "Emilie");
        Resource newSuzie = m.createResource(SempicOnto.Female);
        newSuzie.addLiteral(RDFS.label, "Suzie");
        
        m.add(pRes8, SempicOnto.depicts, newLucie);
        m.add(pRes8, SempicOnto.depicts, newEmilie);
        m.add(pRes8, SempicOnto.depicts, newSuzie);
        
        Resource newVideoGame = m.createResource(SempicOnto.Videogame);
        
        m.add(newLucie, SempicOnto.plays, newVideoGame);
        m.add(newEmilie, SempicOnto.plays, newVideoGame);
        m.add(newSuzie, SempicOnto.plays, newVideoGame);
        
        m.add(pRes8, SempicOnto.takenWhen, "2016-15-02");
        m.add(pRes8, SempicOnto.takenBy, newElisa);
        
        // Photo 3 -> Description : Elisa et sa maman Madeleine
        Resource pRes9 = s.createPhoto(9, 3, 2);
        
        Resource newMadeleine = m.createResource(SempicOnto.Female);
        newMadeleine.addLiteral(RDFS.label, "Madeleine");
        
        m.add(pRes8, SempicOnto.depicts, newMadeleine);
        m.add(pRes8, SempicOnto.depicts, newElisa);
        m.add(newMadeleine, SempicOnto.isParent, newElisa);
        
        m.add(pRes7, SempicOnto.takenWhen, "2001-08-03");
        m.add(pRes7, SempicOnto.takenBy, newElisa);
        
        
        // Photo 4 : Description -> Deux amis d'Elis jouent à un jeu de plateau
        Resource pRes10 = s.createPhoto(10, 3, 2);
        
        Resource newSam = m.createResource(SempicOnto.Male);
        newSam.addLiteral(RDFS.label, "Sam");
        Resource newElisabeth = m.createResource (SempicOnto.Female);
        newElisabeth.addLiteral(RDFS.label, "Elisabeth");
        Resource newBoardGame = m.createResource (SempicOnto.Boardgame);
        
        m.add(pRes9, SempicOnto.depicts, newSam);
        m.add(pRes8, SempicOnto.depicts, newElisabeth);
            m.add(newSam, SempicOnto.plays, newBoardGame);
            m.add(newElisabeth, SempicOnto.plays, newBoardGame);
            m.add(newElisa, SempicOnto.isFriend, newSam);
            m.add(newElisa, SempicOnto.isFriend, newElisabeth);
            m.add(newSam, SempicOnto.isFriend, newElisabeth);
        m.add(pRes7, SempicOnto.takenWhen, "2017-09-11");
        m.add(pRes7, SempicOnto.takenBy, newElisa);
        
        
        
        m.write(System.out, "turtle");

        /* List<Resource> classes = s.listSubClassesOf(SempicOnto.Animal);
        classes.forEach(c -> {System.out.println(c);});

        List<Resource> instances = s.createAnonInstances(classes);
        instances.forEach(i -> {
            System.out.println(i.getProperty(RDFS.label));
        }); */
    }
}
