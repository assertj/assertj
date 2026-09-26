# Project Context and Agent Guidelines

This file defines the technical stack, development conventions, and documentation standards for this repository. All AI agents, code generators, and automated review tools must strictly adhere to these rules. If a more specific `AGENTS.md` exists deeper in the tree, follow that file for the narrower scope.

## Technical Stack
* **Build Toolchain**: Use JDK 25 or newer to build the project and generate documentation.
* **Production Code Compatibility**: Target the language version declared in the `java.version` property of the root POM.
  * Do *not* use preview features or APIs introduced in later Java versions.
* **Dependency Management**: Maven (always use the wrapper via `./mvnw`).
* **Testing Ecosystem**: JUnit, Mockito, and AssertJ.

## Code and Testing Conventions

### Visibility and Structure
* Prefer `package-private` (no modifier) visibility for test classes and methods.
  * `@Nested` test classes, test helper methods, and test constants should also be package-private (or `private` where appropriate).
* Write exactly one JUnit test class for each assertion method under test in the public API (e.g., `OptionalAssert_containsInstanceOf_Test` tests `OptionalAssert#containsInstanceOf`).
* **Naming Convention**: Use `<AssertClass>_<assertion>_Test` for the class name.
* **Method Names**: Use underscore-based (snake_case) naming rather than camelCase for unit test methods.
* **Variable Declarations**: `var` is permitted for local variables in test methods when the right-hand side type is explicit.

### Test Architecture (GIVEN/WHEN/THEN)
* Use explicit `GIVEN`, `WHEN`, and `THEN` comments in every test.
* **Assertions**: Prefer `BDDAssertions.then` over `Assertions.assertThat` for assertions in the `THEN` step, except for `WHEN/THEN` steps meant to test the `assertThat` entry point directly.
* **Exception Testing**: Use `AssertionsUtil.expectAssertionError` for tests expecting an `AssertionError`.
* **Imports**: Use static imports when it improves code readability.

### Reference Unit Test Example
```java
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.error.OptionalShouldBePresent.shouldBePresent;
import static org.assertj.core.util.AssertionsUtil.expectAssertionError;

import java.util.Optional;

import org.junit.jupiter.api.Test;

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

## Javadoc Rules (JDK Markdown)

Newly introduced documentation comments must use Markdown, specifically the [CommonMark](https://spec.commonmark.org/) variant supported by the standard `javadoc` doclet, alongside extensions for Javadoc tags and links to program elements.
Existing comments should also be converted to Markdown whenever they are updated.

### Syntax and Formatting

* **Prefix**: Always use the `///` (three forward slashes) prefix for documentation comments instead of the traditional `/** ... */` block.
* **Styling**: Use standard Markdown syntax (e.g., `**bold**`, `_italic_`, `[link](url)`). **Never use HTML tags** (such as `<p>`, `<ul>`, `<code>`).
* **Line Breaks**: Use plain newlines to separate consecutive sentences or paragraphs in documentation comments. Do not use `<br>`; if you need a new paragraph, insert a blank line instead.
* **Code Blocks**: Enclose code examples in fenced `java` code blocks. Do not use `<pre><code>` or inline `{@code ...}` for multi-line snippets.
* **Lists**: Create lists using standard Markdown lists (`-` or `1.`).
* **Tags**: Place standard Javadoc tags at the end of the comment block, formatting their accompanying descriptions in Markdown. Tags should appear following Oracle's standard order:
  1. `@author` (classes and interfaces only)
  2. `@param` (methods and constructors only)
  3. `@return` (methods only)
  4. `@throws`
  5. `@see`
  6. `@since`
  7. `@deprecated`

### Referencing Program Elements (Links)

Use the extended Markdown reference link syntax instead of traditional `{@link ...}` or `{@linkplain ...}` inline tags:

* **Within the Same Class**: Reference local methods or fields directly in square brackets: `[#localMethod()]` or `[#localField]`.
* **Other Classes and Packages**: Use simple names if imported `[String]`, fully qualified names if not `[java.util.List]`, or reference entire packages via `[java.util]`. Never unnecessarily qualify a link to an already imported type. If an import exists solely for Javadoc references, delete the import and use the fully qualified name instead.
* **Members of Other Classes**: Join class and member using the `#` symbol: `[String#chars()]` or `[String#CASE_INSENSITIVE_ORDER]`.
* **Methods with Varargs**: Use standard ellipsis notation inside the signature: `[String#format(String, Object...)]`.
* **Custom Link Text**: Use the `[alternative text][Element]` syntax.
* **Escaping Brackets**: Escape array parameter brackets with backslashes: `[String#copyValueOf(char\[\])]`.

## Build and Run Commands

Use the Maven wrapper for the following verification and formatting commands:

* **License Headers**: `./mvnw license:format` to add or update license headers.
* **Code Formatting**: `./mvnw spotless:apply` to format code and optimize imports.
* **Verification**:
  * **Single Test Class**: `./mvnw clean test -Dtest=<test-class-name>` to ensure all tests in a class pass (e.g., `./mvnw clean test -Dtest=OptionalAssert_containsInstanceOf_Test`).
  * **Module Test Suite**: `./mvnw -pl <module-name> -am clean test` to ensure all tests in a module pass (e.g., `./mvnw -pl assertj-core-tests -am clean test`).
  * **Full Test Suite**: `./mvnw clean verify` to ensure all tests and verifications pass.
* **Documentation**: `./mvnw clean javadoc:javadoc` to generate Javadoc documentation.

## Strict Restrictions (Do Not)

* **No Unapproved Dependencies in `assertj-core`**: Do not add third-party runtime dependencies to `assertj-core` production code beyond standard JDK library APIs (with `byte-buddy` as the sole exception for runtime proxying).
* **No Unapproved Dependencies in `assertj-guava`**: Do not add dependencies to `assertj-guava` other than `assertj-core` with `compile` scope (default) and `guava` with `provided` scope.
* **No Kotlin**: Do not suggest Kotlin alternatives or mix Kotlin into the codebase, except for test code in the `assertj-tests/assertj-integration-tests/assertj-core-kotlin` module.
