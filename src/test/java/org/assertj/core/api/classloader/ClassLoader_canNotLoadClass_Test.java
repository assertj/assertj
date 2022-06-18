package org.assertj.core.api.classloader;

import static java.lang.reflect.Modifier.PUBLIC;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.classloader.ClassLoaderTestUtils.mockClassLoader;
import static org.assertj.core.error.ShouldNotBeNull.shouldNotBeNull;
import static org.assertj.core.error.classloader.ShouldNotLoadClassSuccessfully.shouldNotLoadClassSuccessfully;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.CharConversionException;
import java.io.IOException;
import java.lang.reflect.GenericSignatureFormatError;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.FileSystemException;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Stream;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import org.assertj.core.api.ClassLoaderAssert;
import org.assertj.core.api.classloader.ClassLoaderTestUtils.ByteClassLoader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link ClassLoaderAssert#canNotLoadClass(String)}.
 *
 * @author Ashley Scopes
 */
@DisplayName("ClassLoader canNotLoadClass tests")
class ClassLoader_canNotLoadClass_Test {

  static ByteBuddy byteBuddy;

  @BeforeAll
  static void setUpClass() {
    byteBuddy = new ByteBuddy(ClassFileVersion.JAVA_V8);
  }

  @Test
  void should_fail_if_actual_is_null() {
    // Then
    assertThatCode(() -> assertThat((ClassLoader) null).canNotLoadClass("foo"))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotBeNull().create());
  }

  @Test
  void should_fail_if_name_is_null() {
    // Given
    ByteClassLoader classLoader = new ByteClassLoader();

    // Then
    assertThatCode(() -> assertThat(classLoader).canNotLoadClass(null))
      .isInstanceOf(NullPointerException.class)
      .hasMessage("the binary name of the class to attempt to find should not be null");
  }

  @MethodSource("expectedExceptions")
  @ParameterizedTest
  void should_succeed_if_expected_exceptions_are_thrown(
    Throwable expectedCause
  ) throws ClassNotFoundException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    when(classLoader.loadClass(any()))
      .thenThrow(expectedCause);

    // Then
    assertThatNoException()
      .isThrownBy(() -> assertThat(classLoader)
        .canNotLoadClass("foo.bar.baz")
        .isSameAs(expectedCause));
  }

  @MethodSource("unexpectedExceptions")
  @ParameterizedTest
  void should_rethrow_if_unexpected_exceptions_are_thrown(
    Throwable unexpectedCause
  ) throws ClassNotFoundException {
    // Given
    ClassLoader classLoader = mockClassLoader();
    when(classLoader.loadClass(any()))
      .thenAnswer(ctx -> {
        // Have to use .thenAnswer rather than .thenThrow so Mockito doesn't snoop around the
        // method signature and try to fail because we are throwing an unexpected checked exception.
        throw unexpectedCause;
      });

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).canNotLoadClass("foo.bar.baz"))
      .isSameAs(unexpectedCause);
  }

  @Test
  void should_fail_for_valid_class() {
    // Given
    String binaryName = "org.assertj.core.api.tests.SomePublicClass";

    ByteClassLoader classLoader = new ByteClassLoader()
      .with(byteBuddy
        .subclass(Object.class)
        .name(binaryName)
        .modifiers(PUBLIC));

    // Then
    assertThatThrownBy(() -> assertThat(classLoader).canNotLoadClass(binaryName))
      .isInstanceOf(AssertionError.class)
      .hasMessage(shouldNotLoadClassSuccessfully(classLoader, binaryName).create());

  }

  static Stream<Throwable> expectedExceptions() {
    return Stream.of(
      new ClassNotFoundException("foo.bar.baz"),
      new ClassNotFoundException("lorem.ipsum.dolor.sit.amet"),
      new ClassNotFoundException(),
      new LinkageError("Linkage error!!"),
      new LinkageError(),
      new NoClassDefFoundError(),
      new NoClassDefFoundError("That class ain't gonna load!"),
      new ClassCircularityError(),
      new ClassCircularityError("Circular dependency"),
      new UnsupportedClassVersionError(),
      new UnsupportedClassVersionError("Java v99213213589238 does not exist yet"),
      new GenericSignatureFormatError(),
      new GenericSignatureFormatError("I can't read your signature"),
      new RuntimeException("That is bad"),
      new NullPointerException("That is also bad"),
      new ClassCastException("You cast something badly"),
      new IllegalArgumentException("That argument is illegal"),
      new IllegalStateException("That state is illegal"),
      new UnsupportedCharsetException("That charset does not exist")
    );
  }

  static Stream<Throwable> unexpectedExceptions() {
    return Stream.of(
      new OutOfMemoryError("you ran out of memory"),
      new ThreadDeath(),
      new IOException("an IO error occurred"),
      new FileSystemException("some bad stuff happened on the file system"),
      new NoSuchMethodException("No method found"),
      new NoSuchFieldException("No field found"),
      new NoSuchAlgorithmException("Algorithm not found"),
      new CharConversionException("Bad char")
    );
  }
}
