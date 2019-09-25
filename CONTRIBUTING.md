Contributing
============

Thank you for your interest in contributing to AssertJ!

We appreciate your effort and to make sure that your pull request is easy to review, we ask you to note the following guidelines including legal contributor agreement:

* Use **[AssertJ code Eclipse formatting preferences](src/ide-support/assertj-eclipse-formatter.xml)** (for IntelliJ IDEA users, you can import it with the 'Eclipse Code Formatter' Plugin)
* Write complete Javadocs for each assertion method and include a code example (succeeding and failing assertion(s)).
* Write one JUnit test class for each assertion method with the following naming convention: `<AssertClass>_<assertion>_Test`.
* Write unit test assertions with AssertJ ! Let's eat our own dog food.
* Unit tests method naming convention is underscore-based (like python) and not camel-case, we find it is much readable for long test names!
* Put GIVEN WHEN THEN steps in each test (you can omit steps when not relevant)
* Use `@DisplayName` on the test class - see `OptionalAssert_containsInstanceOf_Test` as an example.
* Use `AssertionUtil.expectAssertionError` for tests expecting to get an `AssertionError`  - see `OptionalAssert_containsInstanceOf_Test` as an example..
* Successful assertion unit test method names should start with: `should_pass_...`.
* Failing assertion unit test method names should start with: `should_fail_...`.
* Use static import when it makes the code more readable.
* If possible, add a (fun) code example in [assertj-examples](https://github.com/joel-costigliola/assertj-examples) and use it in the javadoc.

A good unit test to use as a reference is `OptionalAssert_containsInstanceOf_Test`. Here's a sample below:

```java
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;
// other imports not shown for brevity

@DisplayName("OptionalAssert containsInstanceOf")
public class OptionalAssert_containsInstanceOf_Test extends BaseTest {

  @Test
  public void should_fail_if_optional_is_empty() {
    // GIVEN
    Optional<Object> actual = Optional.empty();
    // WHEN
    AssertionError assertionError = expectAssertionError(() -> assertThat(actual).containsInstanceOf(Object.class));
    // THEN
    assertThat(assertionError).hasMessage(shouldBePresent(actual).create());
  }

  @Test
  public void should_pass_if_optional_contains_required_type() {
    // GIVEN
    Optional<String> optional = Optional.of("something");
    // THEN
    assertThat(optional).containsInstanceOf(String.class)
                        .containsInstanceOf(Object.class);
  }
```

It's ok not to follow some of the rules described above if you have a good reason not to (use your best judgement)

[assertj-examples](https://github.com/joel-costigliola/assertj-examples) shows how to efficiently use AssertJ through fun unit test examples, it can be seen as AssertJs living documentation.

## Rebase your PR on master (no merge!)

We prefer integrating PR by squashing all the commits and rebase it to master, if you PR has diverged and needs to integrate with master, please rebase on master but do not merge as it will prevent rebasing later on.

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
 * assertThat(book).containsSequence("{", "author", "A Game of Thrones", "}");
 * </code></pre>
 *
 * @param values the Strings to look for, in order.
 * @return {@code this} assertion object.
 * @throws NullPointerException if the given values is {@code null}.
 * @throws IllegalArgumentException if the given values is empty.
 * @throws AssertionError if the actual {@code CharSequence} is {@code null}.
 * @throws AssertionError if the actual {@code CharSequence} does not contain all the given strings <b>in the given order</b>.
 * @since 2.1.0 / 3.1.0
 */
```

## Legal stuff:

Project license(s): Apache License Version 2.0

As a contributor:
* You will only submit contributions where you have authored 100% of the content.
* You will only submit contributions to which you have the necessary rights. This means that if you are employed, you have received the necessary permissions from your employer to make the contributions.
* Whatever content you contribute will be provided under the project license(s).
