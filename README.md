# AssertJ - Fluent assertions for java

[![Build Status (2.x)](https://travis-ci.org/joel-costigliola/assertj-core.svg?branch=2.x)](https://travis-ci.org/joel-costigliola/assertj-core/branches)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core)

[![Coverage Status](https://coveralls.io/repos/joel-costigliola/assertj-core/badge.svg?branch=2.x&service=github)](https://coveralls.io/github/joel-costigliola/assertj-core?branch=2.x)

AssertJ provides a rich and intuitive set of strongly-typed assertions to use for unit testing (either with JUnit or TestNG).

* [AssertJ's goals](#goals)
* [Latest News](#news)
* [Quick start](#quickstart)
* [Features highlight](http://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html)
* [Assertions for your own custom types](#custom types-assertions)
* [Replacing JUnit assertions by AssertJ Assertions](#junit-to-assertj-assertions)
* [Migrating from FEST Assertions](#migrating-from-fest)
* [Contributing](#contributing)

Documentation of all AssertJ projects is maintained on **[assertj.org](http://assertj.org)**, AssertJ Core documentation is [**here**](http://joel-costigliola.github.io/assertj/assertj-core.html).

You can ask questions and make suggestions on [**AssertJ google group**](https://groups.google.com/forum/?fromgroups=#!forum/assertj).

## <a name="goals"/>AssertJ's goals

AssertJ's ambition is to provide a rich and intuitive set of strongly-typed assertions to use for unit testing.  
The idea is that, when writing unit tests, we should have at our disposal assertions specific to the type of the objects
we are checking. If you're checking the value of a String, you use String-specific assertions. Checking the value of
a Map? Use the Map-specific assertions, which make it easy to check on the contents of the map.

AssertJ is composed of several modules:
* A core module (this one) to provide assertions for JDK types (String, Collections, File, Map ...) - see [AssertJ Core documentation](http://joel-costigliola.github.io/assertj/assertj-core.html) and  [javadoc](http://joel-costigliola.github.io/assertj/core/api/index.html). 
* A **[Guava module](https://github.com/joel-costigliola/assertj-guava#readme)** to provide assertions for Guava types (Multimap,Optional, ...) - see [AssertJ Guava documentation](http://joel-costigliola.github.io/assertj/assertj-guava.html) and  [javadoc](http://joel-costigliola.github.io/assertj/guava/api/index.html).
* A **[Joda Time module](https://github.com/joel-costigliola/assertj-joda-time#readme)** to provide assertions for Joda Time types (DateTime, LocalDateTime) - see [AssertJ Joda Time documentation](http://joel-costigliola.github.io/assertj/assertj-joda-time.html) and  [javadoc](http://joel-costigliola.github.io/assertj/jodatime/api/index.html).
* A **[Neo4J module](https://github.com/joel-costigliola/assertj-neo4j#readme)** to provide assertions for Neo4J types (Path, Node, Relationship ...) - see [AssertJ Neo4J documentation](http://joel-costigliola.github.io/assertj/assertj-neo4j.html) and [javadoc](http://joel-costigliola.github.io/assertj/neo4j/api/index.html).
* A **[DB module](https://github.com/joel-costigliola/assertj-db#readme)** to provide assertions for relational database types (Table, Row, Column ...) - see [AssertJ DB documentation](http://joel-costigliola.github.io/assertj/assertj-db.html) and [javadoc](http://joel-costigliola.github.io/assertj/db/current/api/index.html).
* A **[Swing module](https://github.com/joel-costigliola/assertj-swing#readme)** provides a simple and intuitive API for functional testing of Swing user interfaces - see [AssertJ Swing documentation](http://joel-costigliola.github.io/assertj/assertj-swing.html) and [javadoc](http://joel-costigliola.github.io/assertj/swing/api/index.html).

Assertion missing? Please [create an issue](https://github.com/joel-costigliola/assertj-core/issues)! 

AssertJ's assertions are super easy to write: just type **```assertThat```** followed by the actual value in parentheses and then a dot,
and any Java IDE will show you all the assertions available for the type of the object to verify. No more confusion about the 
order of the "expected" and "actual" values. Our assertions are very readable as well: they read very close to plain 
English, making it easier for non-technical people to read test code.
A lot of effort have been done to provide intuitive error messages showing as clearly as possible what the problem is.

Note that AssertJ 2.x requires at least Java 7 and AssertJ 3.x requires at least Java 8.

AssertJ core javadoc is published [here](http://joel-costigliola.github.io/assertj/core/api/index.html).

## <a name="news"/>Latest News

To read details on the latest releases, please go to [**AssertJ Core latest news**](http://joel-costigliola.github.io/assertj/assertj-core-news.html). 

## <a name="quickstart"/>Quickstart

It is easy to start using AssertJ, follow the [**One minute starting guide**](http://joel-costigliola.github.io/assertj/assertj-core-quick-start.html). 

## <a name="custom types-assertions"/>Assertions for your own custom types 

Having assertions for common types like `List` is great, but you might want some that are specific to your own types. This is possible with AssertJ because it is easily extensible so it's simple to [write assertions for your custom types](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html).  

Moreover, to ease your work, we provide assertions generator that can take a set of custom types and create specific assertions. The tools provided are: 
* A **[CLI assertions generator](http://joel-costigliola.github.io/assertj/assertj-assertions-generator.html)** 
* A **[Maven plugin assertions generator](http://joel-costigliola.github.io/assertj/assertj-assertions-generator-maven-plugin.html)**  

## <a name="junit-to-assertj-assertions"/>Replacing JUnit assertions by AssertJ Assertions

To help you [**replace JUnit assertions**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html) by AssertJ ones, you can use a [**script**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html#automatic-conversion) or do regexp search and replace manually as indicated [**here**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html#manual-conversion).

## <a name="migrating-from-fest"/>Migrating from FEST Assertions

Check our migration guide, it covers [migrating from Fest 1.4](http://joel-costigliola.github.io/assertj/assertj-core-migrating-from-fest.html#fest-1.4) and [migrating from Fest 2.x](http://joel-costigliola.github.io/assertj/assertj-core-migrating-from-fest.html).

## <a name="contributing"/>Want to contribute?

You are encouraged to contribute any missing, useful assertions. To do so, please read the [contributors section](http://joel-costigliola.github.io/assertj/assertj-core.html#contributing).

