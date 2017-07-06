# extra-core
This repository contains the core implementation of the IPTC EXTRA project.

## Getting Started

Extra-core is a Java maven project and consists of four main packages:

* org.iptc.extra.core.types: contains the classes represent the core classes used in EXTRA, like Rules, Schemas, Documents, etc.
* org.iptc.extra.core.daos: contains a set of classes to support CRUD operations on mongodb for the objects of EXTRA.
* org.iptc.extra.core.es: contains a client for access to Elastic Search, to support indexing of documents and rules, retrieval, etc
* org.iptc.extra.core.eql: is the main package that contains a set of classes for the processing and transoformation of EQL rules.

## Extra Query Language
EQL, the EXTRA Query Language, is a formal language for representing rules, used for retrieval and tagging of documents. EQL queries are intended to be human readable and writable, intuitive, and expressive. This implementation can parse EQL syntax, and translates it to ElasticSearch query language.

### Search Clauses

The core building block of EQL is a search clause, which consists of an **Index** a **Relation** and a **SearchTerm**. SearchTerm MUST be enclosed in double quotes if they contain any of the following characters: < > = / ( ) and whitespace. Index and relation are optional. In that case any field is implied:

*searchClause = (index relation)? searchTerm*

For example:
  - title adj "civil liberties"
  -	body any "cannabis cocaine crack drug drugs heroin marijuana meth pot narcotic narcotics"
  - "religious freedom"

Modifiers can be applied on relations to adapt their meaning.

**relation = relationName (/modifierName comparator value)* **

For example:
  - title adj/stemming "civil liberties"

### Boolean Operators
Search clauses can be combined with n-ary boolean operators in a prefix way. The set of supported operators is or, and, not and prox. Both lower and upper case is valid.

The basic way to way combine search clauses is:

**booleanClause = (booleanOperator searchClause+)**

Also boolean operators can be used to combine other boolean clauses, or boolean with searchClauses. 

Boolean operators can be modified in a similar way as relations:  

**booleanOperator = booleanName (/modifierName comparator value)* **

For example:

	- prox/unit=word/distance>4
	- prox/unit=sentence/distance<2
	- prox/unit=paragraph/distance<=1
	- or/count>4



## Usage

See the other repositories of the IPTC EXTRA project:
* https://github.com/iptc/extra-ext
* https://github.com/iptc/extra-rules


## Authors
* **[Manos Schinas](https://github.com/manosetro)** - manosetro@iti.gr
* **[Akis Papadopoulos](https://github.com/kleinmind)** - papadop@iti.gr
