PREFIX ws:<http://miashs.univ-grenoble-alpes.fr/ontologies/sempic.owl#>
PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX owl:<http://www.w3.org/2002/07/owl#>
PREFIX xml:<http://www.w3.org/XML/1998/namespace>
PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>
PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>
PREFIX foaf:<http://xmlns.com/foaf/0.1/>
PREFIX dc:<http://purl.org/dc/elements/1.1/>
PREFIX dcterm:<http://purl.org/dc/terms/>
PREFIX db:<http://fr.dbpedia.org/resource/>

# Select all pictures
SELECT ?pictures
WHERE  {?pictures a ws:Photo .}

# Select pictures with an animal
SELECT DISTINCT ?pictures
WHERE {
	?pictures a ws:Photo .
	?pictures ws:isSubject ?animal .
	?animal a ws:Animal .
}

# Select pictures with an animal, taken in mountain
SELECT DISTINCT ?pictures
WHERE {
	?pictures a ws:Photo .
	?pictures ws:isSubject ?animal .
	?animal a ws:Animal .
	?pictures ws:takenIn ?place .
	?place a ws:Mountain
}

# Select landscape pictures and pictures with some people
SELECT DISTINCT ?pictures
WHERE {
	?pictures a ws:Photo .
	?pictures ws:isSubject ?subject .
	?subject a ws:landscape .
	UNION {
		?pictures a ws:Photo ;
			ws:isSubject  ?subject .
		?subject a ws:Person .
	}
}

# Select all pictures of hollidays 2017
SELECT DISTINCT ?pictures  
WHERE {
	?pictures a ws:Photo ;
		dc:date ?d .
	bind(strdt(?d, xsd:date) as ?date)
	FILTER (year(?date) = 2017 )
	?pictures ws:depicts ?category .
	?category a ws:Hollidays .
}