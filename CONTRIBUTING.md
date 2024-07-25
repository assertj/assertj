Contributing
============

Thank you for your interest in contributing to AssertJ!

We appreciate your effort and to make sure that your pull request is easy to review, we ask you to note the following guidelines including legal contributor agreement:

* Use JDK 21 or newer to build the project.
* Use **[AssertJ code Eclipse formatting preferences](eclipse/assertj-eclipse-formatter.xml)** (for IntelliJ IDEA users, you can import it with the [Adapter for Eclipse Code Formatter](https://plugins.jetbrains.com/plugin/6546-adapter-for-eclipse-code-formatter) plugin)
* Write complete Javadocs for each assertion method and include a code example (succeeding and failing assertion(s)).
* As we use JUnit 5, favor `package-private` visibility for both test classes and test methods.
* Write one JUnit test class for each assertion method with the following naming convention: `<AssertClass>_<assertion>_Test`.
* Write unit test assertions with AssertJ! Let's eat our own dog food.
* The unit test method naming convention is underscore-based (like Python) rather than camel-case; we find it more readable for long test names!
* Successful assertion unit test method names should start with: `should_pass_xxx` (if you find a better test name, use your best judgment and go for it!)
* Failing assertion unit test method names should start with: `should_fail_xxx`. (if you find a better test name, use your best judgment and go for it!)
* Put `GIVEN` `WHEN` `THEN` steps in each test, prefer `BDDAssertions.then` over `Assertions.assertThat` for assertions in the `THEN` step. Steps can be combined or omitted if a separate step does not provide much benefit to test readability, just ensure that the WHEN step (either single or combined) contains the test target.
* Use `AssertionUtil.expectAssertionError` for tests expecting to get an `AssertionError` - see `OptionalAssert_containsInstanceOf_Test` below for an example.
* Use static import when it makes the code more readable.
* If possible, add a (fun) code example in [assertj-examples](https://github.com/assertj/assertj-examples) and use it in the Javadoc.

A good unit test to use as a reference is `OptionalAssert_containsInstanceOf_Test`, here's a sample below:

```java
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
// other imports not shown for brevity

class OptionalAssert_containsInstanceOf_Test extends BaseTest {

  @Test
  void should_fail_if_optional_is_empty() {
    // GIVEN
    Optional<Object> actual = Optional.empty();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsInstanceOf(Object.class));
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

It's ok not to follow some rules described above if you have a good reason not to (use your best judgment).

[assertj-examples](https://github.com/assertj/assertj-examples) shows how to efficiently use AssertJ through fun unit test examples, it is a kind of living documentation.

## Rebase your PR on main (no merge!)

We prefer integrating PR by squashing all the commits and rebasing it to `main`; if your PR has diverged and needs to get the newer `main` commits, please rebase on `main` but **do not merge `main` in your PR branch** as it will prevent rebasing later on.

## Naming conventions with some examples:

Here are some of the `ThrowableAssert` assertions: `hasMessage`, `hasNoCause`, `hasMessageContaining`; for each of them we have a test class, note the naming convention:
* `ThrowableAssert_hasMessage_Test`
* `ThrowableAssert_hasNoCause_Test`
* `ThrowableAssert_hasMessageContaining_Test`

Let's look at the `Throwables_assertHasNoCause_Test` test method names (underscore based only):
* `should_pass_if_actual_has_no_cause`
* `should_fail_if_actual_is_null`
* `should_fail_if_actual_has_a_cause`

A good Javadoc example taken from [`AbstractCharSequenceAssert.containsSequence`](src/main/java/org/assertj/core/api/AbstractCharSequenceAssert.java) including:
* assertion description
* a code example showing how to use the assertion (succeeding and failing assertion)
* parameters description (if any)
* exceptions description
* since tag (e.g. `@since 3.9.0`)

```java
/**
 * Verifies that the actual {@code CharSequence} contains all the given strings <b>in the given order</b>.
 * <p>
 * Example:
 *
 * <pre><code class='java'> String book = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
 *
 * // this assertion succeeds ...
 * assertThat(book).containsSequence("{", "title", "A Game of Thrones", "}");
 *
 * // ... but this one fails as "author" must come after "A Game of Thrones"
 * assertThat(book).containsSequence("{", "author", "A Game of Thrones", "}"); </code></pre>
 *
 * @param values the Strings to look for, in order.
 * @return {@code this} assertion object.
 * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
 * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings <b>in the given order</b>
 * @throws IllegalArgumentException if the given values is empty..
 * @throws NullPointerException if the given values is {@code null}.
 * @since 2.1.0 / 3.1.0
 */
```

Note that to get a good HTML rendering for the code examples, the code should start at the same line and one space after `<pre><code class='java'>`.

Good:
```text
 * <pre><code class='java'> String book = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
```

BAD! (missing space)
```text
 * <pre><code class='java'>String book = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
```

BAD! (not in the same line)
```text
 * <pre><code class='java'>
 * String book = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
```

To see what the Javadoc actually looks like, simply generate it and read it in your browser.

## Binary compatibility

Try to keep [binary compatibility](https://docs.oracle.com/javase/specs/jls/se21/html/jls-13.html) whenever possible. It means that you can safely:
* Rewrite the body of methods, constructors, and initializers (like static blocks).
* Rewrite code in the above that previously threw exceptions to no longer do so.
* Add fields, methods, and constructors.
* Delete elements declared private.
* Reorder fields, methods, and constructors.
* Move a method higher in a class hierarchy.
* Reorder the list of direct super-interfaces in a class or interface.
* Insert new class or interface types in a type hierarchy.
* Add generics (since the compiler erases them).
* Update package-private elements.

Other changes could compromise binary compatibility.
These are not automatically rejected, but we will carefully evaluate each one to weigh its pros and cons.

## Using Gitpod

To avoid setting up your local development environment, you can use [Gitpod](https://www.gitpod.io/) and develop directly in browser-based Visual Studio Code, or [JetBrains Client via JetBrains Gateway](https://www.gitpod.io/docs/ides-and-editors/jetbrains-gateway).

[![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/from-referrer/)

## Legal stuff:

Project license(s): Apache License Version 2.0

As a contributor:
* You will only submit contributions where you have authored 100% of the content.
* You will only submit contributions to which you have the necessary rights. This means that if you are employed, you have received the necessary permissions from your employer to make the contributions.
* Whatever content you contribute will be provided under the project license(s).
