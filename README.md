# AssertJ - Fluent assertions for java

[![Github CI status](https://github.com/joel-costigliola/assertj-core/workflows/CI/badge.svg)](https://github.com/joel-costigliola/assertj-core/actions?query=workflow%3ACI) 
[![Github Cross-Version status](https://github.com/joel-costigliola/assertj-core/workflows/Cross-Version/badge.svg)](https://github.com/joel-costigliola/assertj-core/actions?query=workflow%3ACross-Version) 
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core)
[![Javadocs](http://www.javadoc.io/badge/org.assertj/assertj-core.svg)](http://www.javadoc.io/doc/org.assertj/assertj-core)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=joel-costigliola_assertj-core&metric=alert_status)](https://sonarcloud.io/dashboard?id=joel-costigliola_assertj-core)

AssertJ provides a rich and intuitive set of strongly-typed assertions to use for unit testing (with JUnit, TestNG or any other test framework).

* [AssertJ's goals](#goals)
* [Quick start](#quickstart)
* [Latest News](#news)
* [Features highlight](http://joel-costigliola.github.io/assertj/assertj-core-features-highlight.html) (still in the old site but will soon be available in the [new site](https://assertj.github.io/doc/#overview))
* [Assertions for custom types](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html) (still in the old site but will soon be available in the [new site](https://assertj.github.io/doc/#overview))
* [Replacing JUnit assertions with AssertJ Assertions](#junit-to-assertj-assertions)
* [Contributing](#contributing)

The new AssertJ web site contains the [**AssertJ Core documentation**](https://assertj.github.io/doc/#assertj-core-assertions-guide).

You can ask questions in [**stackoverflow (assertj tag)**](https://stackoverflow.com/questions/tagged/assertj?mixed=1) and make suggestions by simply creating an issue.

## <a name="goals"/>AssertJ's goals

AssertJ's ambition is to provide a rich and intuitive set of strongly-typed assertions for unit testing.

The idea is that disposal assertions should be specific to the type of the objects we are checking when writing unit tests. If you're checking the value of a `String`, you use String-specific assertions. Checking the value of
a `Map`? Use Map-specific assertions to easily check the contents of the map.

AssertJ is composed of several modules:
* A core module (this one) to provide assertions for JDK types (`String`, `Iterable`, `Stream`, `Path`, `File`, `Map`...) - see [AssertJ Core documentation](https://assertj.github.io/doc/#assertj-core-assertions-guide) and [javadoc](https://www.javadoc.io/doc/org.assertj/assertj-core/latest/index.html).
* A **[Guava module](https://github.com/joel-costigliola/assertj-guava#readme)** to provide assertions for Guava types (`Multimap`, `Optional`...) - see [AssertJ Guava documentation](http://joel-costigliola.github.io/assertj/assertj-guava.html) and  [javadoc](http://joel-costigliola.github.io/assertj/guava/api/index.html).
* A **[Joda Time module](https://github.com/joel-costigliola/assertj-joda-time#readme)** to provide assertions for Joda Time types (`DateTime`, `LocalDateTime`) - see [AssertJ Joda Time documentation](http://joel-costigliola.github.io/assertj/assertj-joda-time.html) and  [javadoc](http://joel-costigliola.github.io/assertj/jodatime/api/index.html).
* A **[Neo4J module](https://github.com/joel-costigliola/assertj-neo4j#readme)** to provide assertions for Neo4J types (`Path`, `Node`, `Relationship`...) - see [AssertJ Neo4J documentation](http://joel-costigliola.github.io/assertj/assertj-neo4j.html) and [javadoc](http://joel-costigliola.github.io/assertj/neo4j/api/index.html).
* A **[DB module](https://github.com/joel-costigliola/assertj-db#readme)** to provide assertions for relational database types (`Table`, `Row`, `Column`...) - see [AssertJ DB documentation](http://joel-costigliola.github.io/assertj/assertj-db.html) and [javadoc](http://joel-costigliola.github.io/assertj/db/current/api/index.html).
* A **[Swing module](https://github.com/joel-costigliola/assertj-swing#readme)** provides a simple and intuitive API for functional testing of Swing user interfaces - see [AssertJ Swing documentation](http://joel-costigliola.github.io/assertj/assertj-swing.html) and [javadoc](http://joel-costigliola.github.io/assertj/swing/api/index.html).

Assertion missing? Please [create an issue](https://github.com/joel-costigliola/assertj-core/issues)!

AssertJ's assertions are super easy to use: just type **```assertThat```** followed by the actual value in parentheses and a dot,
then any Java IDE will show you all assertions available for the type of the object. No more confusion about the
order of "expected" and "actual" value.

AssertJ's assertions are very close to plain English.

A lot of effort has been spent to provide intuitive failure messages showing clearly why the assertion failed.

Note that AssertJ 3.x requires at least Java 8 and AssertJ 2.x requires at least Java 7.

## <a name="quickstart"/>Quickstart

It is easy to start using AssertJ, just follow the [**one minute starting guide**](https://assertj.github.io/doc/#assertj-core-quick-start).

## <a name="news"/>Latest News

To read details on the latest releases, please go to [**AssertJ Core latest news**](https://assertj.github.io/doc/#assertj-core).

## <a name="custom types-assertions"/>Assertions for custom types

Having assertions for common types like `List` is great, but you might want some assertions specific to your own types. It is simple to [write assertions for your custom types](http://joel-costigliola.github.io/assertj/assertj-core-custom-assertions.html) with AssertJ because it is easily extensible.  

Moreover, to ease your work, we provide assertions generator that can take a set of custom types and create specific assertions. The tools provided are:
* A **[CLI assertions generator](http://joel-costigliola.github.io/assertj/assertj-assertions-generator.html)**
* A **[Maven plugin assertions generator](http://joel-costigliola.github.io/assertj/assertj-assertions-generator-maven-plugin.html)**  

## <a name="junit-to-assertj-assertions"/>Replacing JUnit assertions with AssertJ Assertions

To help you [**replace JUnit assertions**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html) with AssertJ ones, you can use a [**script**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html#automatic-conversion) or do regexp search and replace manually as described [**here**](http://joel-costigliola.github.io/assertj/assertj-core-converting-junit-assertions-to-assertj.html#manual-conversion).

## <a name="contributing"/>Want to contribute?

You are encouraged to contribute any missing, useful assertions. To do so, please read the [contributing section](https://github.com/joel-costigliola/assertj-core/blob/main/CONTRIBUTING.md).
