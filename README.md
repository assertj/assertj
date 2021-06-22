# AssertJ - Fluent assertions for java

[![Github CI status](https://github.com/assertj/assertj-core/actions/workflows/main.yml/badge.svg?branch=main)](https://github.com/assertj/assertj-core/actions/workflows/main.yml?query=branch%3Amain)
[![Github Cross-Version status](https://github.com/assertj/assertj-core/actions/workflows/cross-version.yml/badge.svg?branch=main)](https://github.com/assertj/assertj-core/actions/workflows/cross-version.yml?query=branch%3Amain)
[![Binary Compatibility](https://github.com/assertj/assertj-core/actions/workflows/binary-compatibility.yml/badge.svg?branch=main)](https://github.com/assertj/assertj-core/actions/workflows/binary-compatibility.yml?query=branch%3Amain)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.assertj/assertj-core)
[![Javadocs](http://www.javadoc.io/badge/org.assertj/assertj-core.svg)](http://www.javadoc.io/doc/org.assertj/assertj-core)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=joel-costigliola_assertj-core&metric=alert_status)](https://sonarcloud.io/dashboard?id=joel-costigliola_assertj-core)

AssertJ provides a rich and intuitive set of strongly-typed assertions to use for unit testing (with JUnit, TestNG or any other test framework).

* [AssertJ's goals](#goals)
* [Quick start](https://assertj.github.io/doc/#assertj-core-quick-start)
* [Latest releases](https://assertj.github.io/doc/#assertj-core-release-notes)
* [Documentation](https://assertj.github.io/doc/#assertj-core)
* [Contributing](#contributing)

You can ask questions in [**stackoverflow (assertj tag)**](https://stackoverflow.com/questions/tagged/assertj?mixed=1) and make suggestions by simply creating an issue.

## <a name="goals"/>AssertJ's goals</a>

AssertJ's ambition is to provide a rich and intuitive set of strongly-typed assertions for unit testing.

The idea is that disposal assertions should be specific to the type of the objects we are checking when writing unit tests. If you're checking the value of a `String`, you use String-specific assertions. Checking the value of a `Map`? Use Map-specific assertions to easily check the contents of the map.

AssertJ's assertions are super easy to use: just type **`assertThat(underTest).`** and use code completion to show you all assertions available.

Assertion missing? Please [create an issue](https://github.com/assertj/assertj-core/issues) to discuss it and even better [contribute to the project](https://github.com/assertj/assertj-core/blob/main/CONTRIBUTING.md)!


AssertJ is composed of several modules:
* A core module (this one) to provide assertions for JDK types (`String`, `Iterable`, `Stream`, `Path`, `File`, `Map`...) - see [AssertJ Core documentation](https://assertj.github.io/doc/#assertj-core-assertions-guide) and [javadoc](https://www.javadoc.io/doc/org.assertj/assertj-core/latest/index.html).
* A **[Guava module](https://github.com/assertj/assertj-guava#readme)** to provide assertions for Guava types (`Multimap`, `Optional`...) - see [AssertJ Guava documentation](https://assertj.github.io/doc/#assertj-guava) and [javadoc](https://www.javadoc.io/doc/org.assertj/assertj-guava/latest/index.html).
* A **[Joda Time module](https://github.com/assertj/assertj-joda-time#readme)** to provide assertions for Joda Time types (`DateTime`, `LocalDateTime`) - see [AssertJ Joda Time documentation](http://joel-costigliola.github.io/assertj/assertj-joda-time.html) and  [javadoc](https://www.javadoc.io/doc/org.assertj/assertj-joda-time/latest/index.html).
* A **[Neo4J module](https://github.com/assertj/assertj-neo4j#readme)** to provide assertions for Neo4J types (`Path`, `Node`, `Relationship`...) - see [AssertJ Neo4J documentation](http://joel-costigliola.github.io/assertj/assertj-neo4j.html) and [javadoc](https://www.javadoc.io/doc/org.assertj/assertj-neo4j/latest/index.html).
* A **[DB module](https://github.com/assertj/assertj-db#readme)** to provide assertions for relational database types (`Table`, `Row`, `Column`...) - see [AssertJ DB documentation](https://assertj.github.io/doc/#assertj-db) and [javadoc](https://www.javadoc.io/doc/org.assertj/assertj-db/latest/index.html).
* A **[Swing module](https://github.com/assertj/assertj-swing#readme)** provides a simple and intuitive API for functional testing of Swing user interfaces - see [AssertJ Swing documentation](http://joel-costigliola.github.io/assertj/assertj-swing.html) and [javadoc](https://www.javadoc.io/doc/org.assertj/assertj-swing/latest/index.html).



## <a name="contributing"/>Want to contribute?</a>

You are encouraged to contribute any missing useful assertions. 

Please read the [contributing section](https://github.com/assertj/assertj-core/blob/main/CONTRIBUTING.md) and raise a PR!
