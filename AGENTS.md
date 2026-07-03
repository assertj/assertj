# Project Context & Agent Guidelines

This file defines the technical stack, development conventions, and documentation standards for this repository. All AI agents, code generators, and automated review tools must strictly adhere to these rules. If a more specific `AGENTS.md` exists deeper in the tree, follow that file for the narrower scope.

## Technical Stack
* **Build Toolchain**: Use JDK 25 or newer to build the project and generate documentation.
* **Production Code Compatibility**: Target the language version declared in the `java.version` property of the root POM.
  * Do *not* use preview features or APIs introduced in later Java versions.
* **Dependency Management**: Maven 3.9.x (always use the wrapper via `./mvnw`).
* **Testing Ecosystem**: JUnit, Mockito, and AssertJ.

## Code & Testing Conventions

### Visibility & Structure
* Prefer `package-private` (no modifier) visibility for test classes and methods.
* Write exactly one JUnit test class for each assertion method.
* **Naming Convention**: Use `<AssertClass>_<assertion>_Test` for the class name.
* **Method Names**: Use underscore-based (snake_case) naming rather than camelCase for unit test methods.

### Test Architecture (GIVEN/WHEN/THEN)
* Use explicit `GIVEN`, `WHEN`, and `THEN` comments in every test.
* **Assertions**: Prefer `BDDAssertions.then` over `Assertions.assertThat` for assertions in the `THEN` step.
* **Exception Testing**: Use `AssertionUtil.expectAssertionError` for tests expecting an `AssertionError`.
* **Imports**: Use static imports when it improves code readability.

### Reference Unit Test Example
```java
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

class OptionalAssert_containsInstanceOf_Test {

  @Test
  void should_fail_if_actual_is_empty() {
    // GIVEN
    Optional<Object> actual = Optional.empty();
    // WHEN
    var assertionError = expectAssertionError(() -> assertThat(actual).containsInstanceOf(Object.class));
    // THEN
    then(assertionError).hasMessage(shouldBePresent(actual).create());
  }

  @Test
  void should_pass_if_actual_contains_required_type() {
    // GIVEN
    Optional<String> actual = Optional.of("something");
    // WHEN/THEN
    assertThat(actual).containsInstanceOf(String.class);
  }

}
```

## Javadoc Rules (JDK 25 Markdown)

Newly introduced or updated documentation comments must use Markdown and be accepted by the JDK 25 `javadoc` tool.

### Syntax & Formatting

* **Prefix**: Always use the `///` (three forward slashes) prefix for documentation comments instead of the traditional `/ ... */` block.
* **Styling**: Use standard Markdown syntax (e.g., `bold`, `*italic*`, `[link](url)`). **Never use HTML tags** (such as `<p>`, `<ul>`, `<code>`).
* **Line Breaks**: Use plain newlines to separate consecutive sentences or paragraphs in documentation comments. Do not use `<br>`; if you need a new paragraph, insert a blank line instead.
* **Code Blocks**: Enclose code examples in fenced `java` code blocks. Do not use `<pre><code>` or inline `{@code ...}` for multi-line snippets.
* **Lists**: Create lists using standard Markdown lists (`-` or `1.`).
* **Block Tags**: Place standard Javadoc block tags (`@param`, `@return`, `@throws`) at the end of the comment block, formatting their accompanying descriptions in Markdown.

### Referencing Program Elements (Links)

Use the extended Markdown reference link syntax instead of traditional `{@link ...}` or `{@linkplain ...}` inline tags:

* **Within the Same Class**: Reference local methods or fields directly in square brackets: `[#localMethod()]` or `[#localField]`.
* **Other Classes and Packages**: Use simple names if imported `[String]`, fully qualified names if not `[java.util.List]`, or reference entire packages via `[java.util]`. If an import exists solely for Javadoc references, delete the import and use the fully qualified name instead.
* **Members of Other Classes**: Join class and member using the `#` symbol: `[String#chars()]` or `[String#CASE_INSENSITIVE_ORDER]`.
* **Methods with Varargs**: Use standard ellipsis notation inside the signature: `[String#format(String, Object...)]`.
* **Custom Link Text**: Use the `[alternative text][Element]` syntax.
* **Escaping Brackets**: Escape array parameter brackets with backslashes: `[String#copyValueOf(char\[\])]`.

## Build & Run Commands

Use the Maven wrapper for the following verification and formatting commands:

* **License Headers**: `./mvnw license:format` to add or update license headers.
* **Code Formatting**: `./mvnw spotless:apply` to format code and optimize imports.
* **Verification**: `./mvnw clean verify` to ensure all tests pass.
* **Documentation**: `./mvnw clean  javadoc:javadoc` to generate Javadoc documentation.

## Strict Restrictions (Do Not)

* **No Kotlin**: Do not suggest Kotlin alternatives or mix Kotlin into the codebase, except for test code in the `assertj-tests/assertj-integration-tests/assertj-core-kotlin` module.
