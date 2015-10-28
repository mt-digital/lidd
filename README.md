# [LIDD](https://mt.northwestknowledge.net/lidd)

## Linked Interdisciplinary Data Discovery

This project started as [rda-lod](http://github.com/mtpain/rda-lod)
when I didn't know exactly where my RDA project was taking me. Now
that the prototype is finished, I've begun more serious test-driven
work here. Instead of using Python for building the intermediate
"normalized metadata" stored in MongoDB, I'm using Java.  This is
because after the normalized metadata has been created from parsing
through the metadata repositories, we want to establish semantic
relationships between metadata values.  After a bit of reviewing, 
[Apache Jena](https://jena.apache.org) seems to be the standard, and
Java seems popular in the semantic web community.  I'm also interested
in learning more about the Java ecosystem and what it has to offer for
parallelism.

I'm using both the Spring Data framework for working with MongoDB and
the Spring 

## Installation & Quickstart

1. [Install gradle](https://goo.gl/fU3qjr) (more on this soon)
1. `$ gradle test`
1. `$ gradle build && java -jar build/libs/mongo-test-0.1.0.jar ddi path/to/metadata`
    * `ddi` is an example of one of the supported metadata standards.
    * The only other current standard is `eml`
    * Not sure yet if I'll further encapsulate this method for running the parser over multiple directories and/or for multiple standards

If you want to add a new metadata standard you simply need to write a
new parser. Currently this is not well done, you have to add a new
function to the Parsers class.  In the near future we should probably
have something like `Inteface Parser` that must be implemented for
each new standard.
