# extra-core

This repository contains the core implementation of the IPTC EXTRA project.

## Getting Started

Extra-core is a Java project, configured as a Maven project to handle dependencies, and consists of four main packages:

* **org.iptc.extra.core.types:** contains the classes represent the core classes used in EXTRA, like Rules, Schemas, Documents, etc.
* **org.iptc.extra.core.daos:** contains a set of classes to support CRUD operations on mongodb for the objects of EXTRA.
* **org.iptc.extra.core.es:** contains a client for access to Elastic Search, to support indexing of documents and rules, retrieval, etc
* **org.iptc.extra.core.eql:** is the main package that contains a set of classes for the parsing, processing and transformation of EQL rules.

## Extra Query Language
EQL, the EXTRA Query Language, is a formal language for representing rules, used for retrieval and tagging of documents. EQL queries are intended to be human readable and writable, intuitive, and expressive. This implementation can parse EQL syntax, and translates it to ElasticSearch query language.

### Search Clauses

The core building block of EQL is a search clause, which consists of an **Index** a **Relation** and a **SearchTerm**. SearchTerm MUST be enclosed in double quotes if they contain any of the following characters: < > = / ( ) and whitespace. Index and relation are optional. In that case any field is implied:

**searchClause = (index relation)? searchTerm**

For example:
	
	- title adj "civil liberties"
	- body any "cannabis cocaine crack drug drugs heroin marijuana meth pot narcotic narcotics"
	- "religious freedom"

Modifiers can be applied on relations to adapt their meaning.

**relation = relationName (/modifierName comparator value)* **

For example:
	
	- *title adj/stemming "civil liberties"*

### Boolean Operators
Search clauses can be combined with n-ary boolean operators in a prefix way. The set of supported operators is or, and, not and prox. Both lower and upper case is valid.

The basic way to way combine search clauses is:

**booleanClause = (booleanOperator searchClause+)**

Also boolean operators can be used to combine other boolean clauses, or boolean with searchClauses. 

**booleanClause = (booleanOperator (searchClause | booleanClause)+)**

Boolean operators can be modified in a similar way as relations:  

**booleanOperator = booleanName (/modifierName comparator value) **

For example:

	- prox/unit=word/distance>4
	- prox/unit=sentence/distance<2
	- prox/unit=paragraph/distance<=1
	- or/count>4

## Usage

### Get the artifacts

To use extra-core project add the following dependency to your `pom.xml`:

```xml
<dependency>
	<groupId>org.iptc</groupId>
  	<artifactId>extra-core</artifactId>
	<version>0.1.1-SNAPSHOT</version>
</dependency>
```

### Retrieval and Creation of Rules, Schemas, Taxonomies and Topics

To support serialization/deserialization from/to MongoDB:
 
```java
MongoClient mongoClient = new MongoClient("localhost", 27017);
Morphia morphia = new Morphia();
Datastore datastore = morphia.createDatastore(mongoClient, database);

RulesDAO rulesDAO = new RulesDAO(datastore);	// initialize a rules dao

Rule rule = rulesDAO.get("595a913da7b11b0001cae336");	// get rule with id 595a913da7b11b0001cae336
String eqlQuery = rule.getQuery();						// get the EQL query of the rule

SchemasDAO schemasDAO = new SchemasDAO(datastore);	// initialize a schemas dao

Schema schema = new Schema();	// create a new schema
schema.setName("Test schema");	// set name of the new schema

schema.addField("title", true, true, false);	// add title field textual=true, hasSentences=true, hasParagraphs=false
schema.addField("body", true, true, true);		// add body field textual=true, hasSentences=true, hasParagraphs=true

schemasDAO.save(schema);	// save the new schema into mongodb

```

### Parse, process and transformation of EQL queries

To parse a string containing an EQL query use parse method of EQLParser:

```java

String eqlQuery = "....";

SyntaxTree tree = EQLParser.parse(eqlQuery);
Node root = tree.getRootNode();

```

To transform a syntax tree generated from an EQL query to an Elastic Search query, use EQLMapper:

```java
QueryBuilder esQuery = EQLMapper.toElasticSearchQuery(node, schema);
```

To perform a depth-first traversal of the syntax tree:
```java
SyntaxTreeVisitor visitor = new SyntaxTreeVisitor();		
visitor.visit(root);
```

The change that functionality, e.g. to perform an operation in each visited node of the syntax tree, extend `java org.iptc.extra.core.eql.tree.visitor.SyntaxTreeVisitor<T>`.

For example the following class extends the default behavior of SyntaxTreeVisitor by aggregating the Index names across all Index nodes in the syntax tree:

```java
public class MyVisitor extends SyntaxTreeVisitor<Set<String>> {

	// override visit method for Index nodes
	@Override
	public Set<String> visitIndex(Index index) {
		Set<String> set = new HashSet<String>();
		return set.add(index.getIndex());
	}
	
	protected Set<String> aggregateResult(Set<String> aggregate, Set<String> nextResult) {
		return aggregate.addAll(nextResult);
	}
	
	protected Set<String> defaultResult() {
		return new HashSet<String>;
	}
}
```

Although extra-core can be used as a dependency in any Java project, it's recommended to use the [integrated framework](https://github.com/iptc/extra-ext) developed on top of extra-core. This framework includes a REST API for the management of rules, schemas, etc but also a web user interface for the development, testing and usage of rules. 

See the other repositories of the IPTC EXTRA project:
* [extra-ext](https://github.com/iptc/extra-ext) User Interface, API and other extensions of the Core
* [extra-examples](https://github.com/iptc/extra-examples) Examples developed for the EXTRA Project


## Authors
* **[Manos Schinas](https://github.com/manosetro)** - manosetro@iti.gr
* **[Akis Papadopoulos](https://github.com/kleinmind)** - papadop@iti.gr
