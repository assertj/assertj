/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.core;

// @formatter:off

// tag::java_7_imports[]
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
// ... code omitted for brevity

// end::java_7_imports[]
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIOException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.catchIOException;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenCode;
import static org.assertj.core.api.BDDAssertions.thenNoException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

// not run by the build (class name does not match *Test), some examples fail on purpose
class ExceptionAssertionsExample {

  private final List<String> fellowshipOfTheRing = Arrays.asList("Frodo", "Sam", "Merry", "Pippin", "Gandalf", "Legolas",
                                                                 "Gimli", "Aragorn", "Boromir");

  @Test
  void message() {
    // tag::message[]
    Throwable throwable = new IllegalArgumentException("wrong amount 123");

    assertThat(throwable).hasMessage("wrong amount 123")
                         .hasMessage("%s amount %d", "wrong", 123)
                         // check start
                         .hasMessageStartingWith("wrong")
                         .hasMessageStartingWith("%s a", "wrong")
                         // check content
                         .hasMessageContaining("wrong amount")
                         .hasMessageContaining("wrong %s", "amount")
                         .hasMessageContainingAll("wrong", "amount")
                         // check end
                         .hasMessageEndingWith("123")
                         .hasMessageEndingWith("amount %s", "123")
                         // check with regex
                         .hasMessageMatching("wrong amount .*")
                         // check does not contain
                         .hasMessageNotContaining("right")
                         .hasMessageNotContainingAny("right", "price");
    // end::message[]
  }

  @Test
  void cause() {
    // tag::cause_direct[]
    NullPointerException cause = new NullPointerException("boom!");
    Throwable throwable = new Throwable(cause);

    assertThat(throwable).hasCause(cause)
                         // hasCauseInstanceOf will match inheritance.
                         .hasCauseInstanceOf(NullPointerException.class)
                         .hasCauseInstanceOf(RuntimeException.class)
                         // hasCauseExactlyInstanceOf will match only exact same type
                         .hasCauseExactlyInstanceOf(NullPointerException.class);
    // end::cause_direct[]

    // tag::cause_navigation[]
    // navigate before checking
    assertThat(throwable).cause()
                         .hasMessage("boom!")
                         .hasMessage("%s!", "boom")
                         .hasMessageStartingWith("bo")
                         .hasMessageEndingWith("!")
                         .hasMessageContaining("boo")
                         .hasMessageContainingAll("bo", "oom", "!")
                         .hasMessageMatching("b...!")
                         .hasMessageNotContaining("bam")
                         .hasMessageNotContainingAny("bam", "bim")
                         // isInstanceOf will match inheritance.
                         .isInstanceOf(NullPointerException.class)
                         .isInstanceOf(RuntimeException.class)
                         // isExactlyInstanceOf will match only exact same type
                         .isExactlyInstanceOf(NullPointerException.class);
    // end::cause_navigation[]
  }

  @Test
  void having_cause() {
    // tag::having_cause[]
    assertThatExceptionOfType(RuntimeException.class)
             .isThrownBy(() -> { throw new RuntimeException(new IllegalArgumentException("boom!")); })
             .havingCause()
             .withMessage("boom!");
    // end::having_cause[]
  }

  @Test
  void root_cause() {
    // tag::root_cause[]
    NullPointerException rootCause = new NullPointerException("null!");
    Throwable throwable = new Throwable(new IllegalStateException(rootCause));

    // direct root cause check
    assertThat(throwable).hasRootCause(rootCause)
                         .hasRootCauseMessage("null!")
                         .hasRootCauseMessage("%s!", "null")
                         // hasRootCauseInstanceOf will match inheritance
                         .hasRootCauseInstanceOf(NullPointerException.class)
                         .hasRootCauseInstanceOf(RuntimeException.class)
                         // hasRootCauseExactlyInstanceOf will match only exact same type
                         .hasRootCauseExactlyInstanceOf(NullPointerException.class);

    // navigate to root cause and check
    assertThat(throwable).rootCause()
                         .hasMessage("null!")
                         .hasMessage("%s!", "null")
                         .hasMessageStartingWith("nu")
                         .hasMessageEndingWith("!")
                         .hasMessageContaining("ul")
                         .hasMessageContainingAll("nu", "ull", "l!")
                         .hasMessageMatching("n...!")
                         .hasMessageNotContaining("NULL")
                         .hasMessageNotContainingAny("Null", "NULL")
                         // isInstanceOf will match inheritance.
                         .isInstanceOf(NullPointerException.class)
                         .isInstanceOf(RuntimeException.class)
                         // isExactlyInstanceOf will match only exact same type
                         .isExactlyInstanceOf(NullPointerException.class);
    // end::root_cause[]
  }

  @Test
  void having_root_cause() {
    // tag::having_root_cause[]
    assertThatExceptionOfType(RuntimeException.class)
             .isThrownBy(() -> { throw new RuntimeException(new IllegalArgumentException(new NullPointerException("root error"))); })
             .havingRootCause()
             .withMessage("root error");
    // end::having_root_cause[]
  }

  @Test
  void catch_throwable() {
    // tag::catch_throwable[]
    // GIVEN
    String[] names = { "Pier ", "Pol", "Jak" };
    // WHEN
    Throwable thrown = catchThrowable(() -> System.out.println(names[9]));
    // THEN
    then(thrown).isInstanceOf(ArrayIndexOutOfBoundsException.class)
                .hasMessageContaining("9");

    // assertThat is also available but is less "BDD style"
    assertThat(thrown).isInstanceOf(ArrayIndexOutOfBoundsException.class)
                      .hasMessageContaining("9");
    // end::catch_throwable[]
  }

  @Test
  void catch_throwable_of_type() {
    // tag::catch_throwable_of_type[]
    class TextException extends Exception {
      int line;
      int column;

      public TextException(String msg, int line, int column) {
        super(msg);
        this.line = line;
        this.column = column;
      }
    }

    TextException textException = catchThrowableOfType(TextException.class,
                                                       () -> { throw new TextException("boom!", 1, 5); });
    // assertions succeed
    assertThat(textException).hasMessageContaining("boom");
    assertThat(textException.line).isEqualTo(1);
    assertThat(textException.column).isEqualTo(5);

    // fails as TextException is not a RuntimeException
    catchThrowableOfType(RuntimeException.class, () -> { throw new TextException("boom!", 1, 5); });
    // end::catch_throwable_of_type[]
  }

  @Test
  void catch_io_exception() {
    // tag::catch_io_exception[]
    IOException iOException = catchIOException(() -> {throw new IOException("boom!");});
    // assertions succeed
    assertThat(iOException).hasMessage("boom!");

    // succeeds as catchIOException returns null when the code does not throw any exceptions
    assertThat(catchIOException(() -> {})).isNull();

    // fails as the thrown instance is not an IOException
    catchIOException(() -> {throw new Exception("boom!");});
    // end::catch_io_exception[]
  }

  @Test
  void assert_that_thrown_by() {
    // tag::assert_that_thrown_by[]
    assertThatThrownBy(() -> { throw new Exception("boom!"); }).isInstanceOf(Exception.class)
                                                               .hasMessageContaining("boom");
    // end::assert_that_thrown_by[]
  }

  @Test
  void assert_that_exception_of_type() {
    // tag::assert_that_exception_of_type[]
    assertThatExceptionOfType(IOException.class).isThrownBy(() -> { throw new IOException("boom!"); })
                                                .withMessage("%s!", "boom")
                                                .withMessageContaining("boom")
                                                .withNoCause();
    // end::assert_that_exception_of_type[]
  }

  @Test
  void assert_that_io_exception() {
    // tag::assert_that_io_exception[]
    assertThatIOException().isThrownBy(() -> { throw new IOException("boom!"); })
                           .withMessage("%s!", "boom")
                           .withMessageContaining("boom")
                           .withNoCause();
    // end::assert_that_io_exception[]
  }

  @Test
  void no_exception() {
    // tag::assert_that_no_exception[]
    // standard style
    assertThatNoException().isThrownBy(() -> System.out.println("OK"));
    // BDD style
    thenNoException().isThrownBy(() -> System.out.println("OK"));
    // end::assert_that_no_exception[]

    // tag::does_not_throw_any_exception[]
    // standard style
    assertThatCode(() -> System.out.println("OK")).doesNotThrowAnyException();
    // BDD style
    thenCode(() -> System.out.println("OK")).doesNotThrowAnyException();
    // end::does_not_throw_any_exception[]
  }

  @Test
  void java_7() {
    // tag::java_7[]
    assertThat(fellowshipOfTheRing).hasSize(9);

    // here's the typical pattern to use Fail :
    try {
      fellowshipOfTheRing.get(9); // argggl !
      // we should not arrive here => use fail to expresses that
      // if IndexOutOfBoundsException was not thrown, test would fail the specified message
      fail("IndexOutOfBoundsException expected because fellowshipOfTheRing has only 9 elements");
    } catch (IndexOutOfBoundsException e) {
      assertThat(e).hasMessage("Index: 9, Size: 9");
    }

    // Warning: don't catch Throwable as it would also catch the AssertionError thrown by fail method

    // another way to do the same thing
    try {
      fellowshipOfTheRing.get(9); // argggl !
      // if IndexOutOfBoundsException was not thrown, test would fail with message :
      // "Expected IndexOutOfBoundsException to be thrown"
      failBecauseExceptionWasNotThrown(IndexOutOfBoundsException.class);
    } catch (IndexOutOfBoundsException e) {
      assertThat(e).hasMessage("Index: 9, Size: 9");
    }
    // end::java_7[]
  }
}
// @formatter:on
