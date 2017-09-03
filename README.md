# AssertJ - Fluent assertions for java

`master:` [![Build Status (master)](https://travis-ci.org/joel-costigliola/assertj-core.svg?branch=master)](https://travis-ci.org/joel-costigliola/assertj-core) 
`2.x:` [![Build Status (2.x)](https://travis-ci.org/joel-costigliola/assertj-core.svg?branch=2.x)](https://travis-ci.org/joel-costigliola/assertj-core/branches) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core)
[![Coverage Status](https://codecov.io/github/joel-costigliola/assertj-core/coverage.png?branch=master)](https://codecov.io/github/joel-costigliola/assertj-core?branch=master)

AssertJ provides a rich and intuitive set of strongly-typed assertions to use for unit testing (either with JUnit or TestNG).

* [AssertJ's goals](#goals)
* [Quick start](#quickstart)
* [Latest News](#news)
* [Features highlight](http://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html)
* [Assertions for custom types](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html)
* [Replacing JUnit assertions with AssertJ Assertions](#junit-to-assertj-assertions)
* [Migrating from FEST Assertions](#migrating-from-fest)
* [Contributing](#contributing)

The AssertJ web site contains all projects documentation and can be found **[here](http://joel-costigliola.github.io/assertj/index.html)**. It notably includes the [**AssertJ Core documentation**](http://joel-costigliola.github.io/assertj/assertj-core.html).

You can ask questions and make suggestions on [**AssertJ google group**](https://groups.google.com/forum/?fromgroups=#!forum/assertj).

## <a name="goals"/>AssertJ's goals

AssertJ's ambition is to provide a rich and intuitive set of strongly-typed assertions for unit testing.

The idea is that, when writing unit tests, we should have at our disposal assertions specific to the type of the objects
we are checking. If you're checking the value of a `String`, you use String-specific assertions. Checking the value of
a `Map`? Use Map-specific assertions, which make it easy to check the contents of the map.

AssertJ is composed of several modules:
* A core module (this one) to provide assertions for JDK types (`String`, `Iterable`, `Stream`, `Path`, `File`, `Map`...) - see [AssertJ Core documentation](http://joel-costigliola.github.io/assertj/assertj-core.html) and [javadoc](http://joel-costigliola.github.io/assertj/core/api/index.html). 
* A **[Guava module](https://github.com/joel-costigliola/assertj-guava#readme)** to provide assertions for Guava types (`Multimap`, `Optional`...) - see [AssertJ Guava documentation](http://joel-costigliola.github.io/assertj/assertj-guava.html) and  [javadoc](http://joel-costigliola.github.io/assertj/guava/api/index.html).
* A **[Joda Time module](https://github.com/joel-costigliola/assertj-joda-time#readme)** to provide assertions for Joda Time types (`DateTime`, `LocalDateTime`) - see [AssertJ Joda Time documentation](http://joel-costigliola.github.io/assertj/assertj-joda-time.html) and  [javadoc](http://joel-costigliola.github.io/assertj/jodatime/api/index.html).
* A **[Neo4J module](https://github.com/joel-costigliola/assertj-neo4j#readme)** to provide assertions for Neo4J types (`Path`, `Node`, `Relationship`...) - see [AssertJ Neo4J documentation](http://joel-costigliola.github.io/assertj/assertj-neo4j.html) and [javadoc](http://joel-costigliola.github.io/assertj/neo4j/api/index.html).
* A **[DB module](https://github.com/joel-costigliola/assertj-db#readme)** to provide assertions for relational database types (`Table`, `Row`, `Column`...) - see [AssertJ DB documentation](http://joel-costigliola.github.io/assertj/assertj-db.html) and [javadoc](http://joel-costigliola.github.io/assertj/db/current/api/index.html).
* A **[Swing module](https://github.com/joel-costigliola/assertj-swing#readme)** provides a simple and intuitive API for functional testing of Swing user interfaces - see [AssertJ Swing documentation](http://joel-costigliola.github.io/assertj/assertj-swing.html) and [javadoc](http://joel-costigliola.github.io/assertj/swing/api/index.html).

Assertion missing? Please [create an issue](https://github.com/joel-costigliola/assertj-core/issues)! 

AssertJ's assertions are super easy to use: just type **```assertThat```** followed by the actual value in parentheses and a dot,
then any Java IDE will show you all assertions available for the type of the object. No more confusion about the 
order of "expected" and "actual" value.

AssertJ's assertions read very close to plain English.

A lot of effort has been spent to provide intuitive failure messages showing clearly why the assertion failed.

Note that AssertJ 3.x requires at least Java 8 and AssertJ 2.x requires at least Java 7.

## <a name="quickstart"/>Quickstart

It is easy to start using AssertJ, just follow the [**one minute starting guide**](http://joel-costigliola.github.io/assertj/assertj-core-quick-start.html). 

## <a name="news"/>Latest News

To read details on the latest releases, please go to [**AssertJ Core latest news**](http://joel-costigliola.github.io/assertj/assertj-core-news.html). 

## <a name="custom types-assertions"/>Assertions for custom types

Having assertions for common types like `List` is great, but you might want some that are specific to your own types. This is possible with AssertJ because it is easily extensible so it's simple to [write assertions for your custom types](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html).  

Moreover, to ease your work, we provide assertions generator that can take a set of custom types and create specific assertions. The tools provided are: 
* A **[CLI assertions generator](http://joel-costigliola.github.io/assertj/assertj-assertions-generator.html)** 
* A **[Maven plugin assertions generator](http://joel-costigliola.github.io/assertj/assertj-assertions-generator-maven-plugin.html)**  

## <a name="junit-to-assertj-assertions"/>Replacing JUnit assertions with AssertJ Assertions

To help you [**replace JUnit assertions**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html) with AssertJ ones, you can use a [**script**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html#automatic-conversion) or do regexp search and replace manually as described [**here**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html#manual-conversion).

## <a name="migrating-from-fest"/>Migrating from FEST Assertions

Check our migration guide, it covers [migrating from Fest 1.4](http://joel-costigliola.github.io/assertj/assertj-core-migrating-from-fest.html#fest-1.4) and [migrating from Fest 2.x](http://joel-costigliola.github.io/assertj/assertj-core-migrating-from-fest.html).

## <a name="contributing"/>Want to contribute?

You are encouraged to contribute any missing, useful assertions. To do so, please read the [contributing section](https://github.com/joel-costigliola/assertj-core/blob/master/CONTRIBUTING.md).

