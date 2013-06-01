package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;
import static org.mockito.Mockito.spy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;

/**
 * Base class for {@link org.assertj.core.internal.Classes} unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set
 * {@link org.assertj.core.internal.Classes#failures} appropriately.
 * 
 * @author William Delanoue
 */
public abstract class ClassesBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Failures failures;
  protected Classes classes;
  protected Class<?> actual;

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  protected static @interface MyAnnotation {

  }

  @MyAnnotation
  protected static class AnnotatedClass {
    public String publicField;
    protected String protectedField;
    private String privateField;
  }

  protected static class Jedi {

  }

  protected static class HumanJedi extends Jedi {

  }

  @Before
  public void setUp() {
    failures = spy(new Failures());
    classes = new Classes();
    classes.failures = failures;
  }

}
