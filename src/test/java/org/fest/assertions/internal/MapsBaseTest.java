package org.fest.assertions.internal;

import static org.fest.assertions.data.MapEntry.entry;
import static org.fest.assertions.test.ExpectedException.none;
import static org.fest.assertions.test.Maps.mapOf;

import static org.mockito.Mockito.spy;

import java.util.Map;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.test.ExpectedException;

/**
 * Base class for {@link Maps} unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Maps} attributes appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class MapsBaseTest {

  @Rule
  public ExpectedException thrown = none();
  protected Map<String, String> actual;
  protected Failures failures;
  protected Maps maps;

  @SuppressWarnings("unchecked")
  @Before
  public void setUp() {
    actual = (Map<String, String>) mapOf(entry("name", "Yoda"), entry("color", "green"));
    failures = spy(new Failures());
    maps = new Maps();
    maps.failures = failures;
  }

}