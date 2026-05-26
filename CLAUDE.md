# Project Context

## Stack
* Use JDK 25 or newer to build the project.
- Java 17 (LTS). Do not use preview features or APIs introduced after Java 17.
- Maven 3.9.x (wrapper via `./mvnw`)
- JUnit 6, Mockito, AssertJ for testing

## Conventions
* As we use JUnit 6, favor `package-private` visibility for test classes and methods.
* Write one JUnit test class for each assertion method with the naming convention: `<AssertClass>_<assertion>_Test`
* The unit test method naming convention is underscore-based (like Python) rather than camel-case
* Put `GIVEN` `WHEN` `THEN` steps in each test; prefer `BDDAssertions.then` over `Assertions.assertThat` for assertions in the `THEN` step. 
* Use `AssertionUtil.expectAssertionError` for tests expecting an `AssertionError`
* Use static import when it makes the code more readable.
* Execute `./mvnw license:format` to add or update license headers
* Execute `./mvnw spotless:apply` to format the code
* Execute `./mvnw clean verify` to make sure all tests are passing

A good unit test to use as a reference is `OptionalAssert_containsInstanceOf_Test`, here's a sample below:

```java
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class OptionalAssert_containsInstanceOf_Test {

  @Test
  void should_fail_if_optional_is_empty() {
    // GIVEN
    Optional<Object> actual = Optional.empty();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsInstanceOf(Object.class));
    // THEN
    then(assertionError).hasMessage(shouldBePresent(actual).create());
  }

  @Test
  void should_pass_if_optional_contains_required_type() {
    // GIVEN
    Optional<String> optional = Optional.of("something");
    // WHEN/THEN
    then(optional).containsInstanceOf(String.class);
  }

}
```

## Build & Run
- Use JDK 25 or newer to build the project.
- `./mvnw license:format` to add or update license headers
- `./mvnw spotless:apply` to format the code
- `./mvnw clean verify` to run all tests
- `./mvnw clean javadoc:javadoc` to generate javadoc

## Do Not
- Do not suggest Kotlin alternatives
- Do not use Java EE / Jakarta EE APIs that are not part of Spring Boot's managed dependencies
- Do not generate code that requires Java > 17
