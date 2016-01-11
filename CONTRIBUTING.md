Contributing
============

Thank you for your interest in contributing to AssertJ!

We appreciate your effort and to make sure that your pull request is easy to review, we ask you to make note of the following guidelines including legal contributor agreement:

* If you want to contribute a feature which requires Java 8, please create a pull request for the master branch. Otherwise please create a pull request for the 2.x branch.
* Use **[AssertJ code Eclipse formatting preferences](src/ide-support/assertj-eclipse-formatter.xml)** (for IntelliJ IDEA users, you can import it)
* Write complete Javadocs for each assertion method and include a code example (succeeding and failing assertion(s)).
* Write one JUnit test class for each assertion method with the following naming convention: `<AssertClass>_<assertion>_Test`.
* Write unit test assertions with AssertJ ! Lets eat our own dog food.
* Unit tests method naming convention is underscore-based (like python) and not camel-case, we find it is much readable for long test names!
* Successful assertion unit test method name should start with: `should_pass_...`.
* Failing assertion unit test method name should start with: `should_fail_...`.
* If possible, add a (fun) code example in [assertj-examples](https://github.com/joel-costigliola/assertj-examples) and use it in the javadoc.

[assertj-examples](https://github.com/joel-costigliola/assertj-examples) shows how to use efficiently AssertJ through fun unit test examples, it can be seen as AssertJ living documentation.

## Naming conventions with some examples:

Here some of `ThrowableAssert` assertions: `hasMessage`, `hasNoCause`, `hasMessageContaining`, for each of them we have a test class, note the naming convention:
* `ThrowableAssert_hasMessage_Test`
* `ThrowableAssert_hasNoCause_Test`
* `ThrowableAssert_hasMessageContaining_Test`

Let's look at `Throwables_assertHasNoCause_Test` tests method names (underscore based only):
* `should_pass_if_actual_has_no_cause`
* `should_fail_if_actual_is_null`
* `should_fail_if_actual_has_a_cause`

A good javadoc example taken from [`AbstractCharSequenceAssert.containsSequence`](src/main/java/org/assertj/core/api/AbstractCharSequenceAssert.java) including:
* assertion description
* a code example showing how to use the assertion (succeeding and failing assertion)
* parameters description (if any)
* exceptions description

```java
/**
 * Verifies that the actual {@code CharSequence} contains all the given strings <b>in the given order</b>.
 * <p>
 * Example:
 *
 * <pre><code class='java'>
 * String book = "{ 'title':'A Game of Thrones', 'author':'George Martin'}";
 *
 * // this assertion succeeds ...
 * assertThat(book).containsSequence("{", "title", "A Game of Thrones", "}");
 *
 * // ... but this one fails as "author" must come after "A Game of Thrones"
 * assertThat(book).containsSequence("{", "author", "A Game of Thrones", "}");
 * </code></pre>
 *
 * @param values the Strings to look for, in order.
 * @return {@code this} assertion object.
 * @throws NullPointerException if the given values is {@code null}.
 * @throws IllegalArgumentException if the given values is empty.
 * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
 * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings <b>in the given order</b>.
 */
```

Not all AssertJ javadoc meets this standard but this is something we are working on.

## Legal stuff:

Project license(s): Apache License Version 2.0

As a contributor:
* You will only submit contributions where you have authored 100% of the content.
* You will only submit contributions to which you have the necessary rights. This means that if you are employed, you have received the necessary permissions from your employer to make the contributions.
* Whatever content you contribute will be provided under the project license(s). 
