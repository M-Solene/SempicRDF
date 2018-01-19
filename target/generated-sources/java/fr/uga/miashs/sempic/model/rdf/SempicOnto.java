/* CVS $Id: $ */
package fr.uga.miashs.sempic.model.rdf; 
import org.apache.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from C:\Users\Sol�ne\Documents\Cours\M2\Projet\JavaEE\SempicRDF/src/main/resources/sempiconto.owl 
 * @author Auto-generated by schemagen on 19 janv. 2018 00:17 
 */
public class SempicOnto {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static final Model M_MODEL = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     * @return namespace as String
     * @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = M_MODEL.createResource( NS );
    
    public static final Property albumId = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#albumId" );
    
    public static final Property depicts = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#depicts" );
    
    public static final Property hasTitle = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#hasTitle" );
    
    public static final Property isChild = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#isChild" );
    
    public static final Property isFriend = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#isFriend" );
    
    public static final Property isParent = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#isParent" );
    
    public static final Property isSubject = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#isSubject" );
    
    public static final Property isType = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#isType" );
    
    public static final Property ownerId = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#ownerId" );
    
    public static final Property plays = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#plays" );
    
    public static final Property takeBy = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#takeBy" );
    
    public static final Property takenBy = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#takenBy" );
    
    public static final Property takenIn = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#takenIn" );
    
    public static final Property takenWhen = M_MODEL.createProperty( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#takenWhen" );
    
    public static final Resource Animal = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Animal" );
    
    public static final Resource Autumn = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Autumn" );
    
    public static final Resource Beach = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Beach" );
    
    public static final Resource Bird = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Bird" );
    
    public static final Resource Boardgame = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Boardgame" );
    
    public static final Resource Cat = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Cat" );
    
    public static final Resource Category = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Category" );
    
    public static final Resource Christmas = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Christmas" );
    
    public static final Resource City = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#City" );
    
    public static final Resource Cow = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Cow" );
    
    public static final Resource Date = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Date" );
    
    public static final Resource Daytime = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Daytime" );
    
    public static final Resource Depiction = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Depiction" );
    
    public static final Resource Dog = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Dog" );
    
    public static final Resource Farewell_party = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Farewell_party" );
    
    public static final Resource Female = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Female" );
    
    public static final Resource Fish = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Fish" );
    
    public static final Resource Forest = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Forest" );
    
    public static final Resource Game = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Game" );
    
    public static final Resource Hollidays = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Hollidays" );
    
    public static final Resource Horse = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Horse" );
    
    public static final Resource House_warming_party = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#House_warming_party" );
    
    public static final Resource Landscape = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Landscape" );
    
    public static final Resource Male = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Male" );
    
    public static final Resource Mammal = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Mammal" );
    
    public static final Resource Mountain = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Mountain" );
    
    public static final Resource Nigth = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Nigth" );
    
    public static final Resource Ostrich = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Ostrich" );
    
    public static final Resource Party = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Party" );
    
    public static final Resource Pegasus = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Pegasus" );
    
    public static final Resource Person = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Person" );
    
    public static final Resource Photo = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Photo" );
    
    public static final Resource Place = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Place" );
    
    public static final Resource Reptile = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Reptile" );
    
    public static final Resource Season = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Season" );
    
    public static final Resource Selfie = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Selfie" );
    
    public static final Resource Spring = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Spring" );
    
    public static final Resource Subject = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Subject" );
    
    public static final Resource Summer = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Summer" );
    
    public static final Resource Unicorn = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Unicorn" );
    
    public static final Resource Videogame = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Videogame" );
    
    public static final Resource Wedding = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Wedding" );
    
    public static final Resource Winter = M_MODEL.createResource( "http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#Winter" );
    
}
