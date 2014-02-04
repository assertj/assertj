package org.assertj.core.internal;

import static org.assertj.core.data.MapEntry.entry;
import static org.assertj.core.test.ExpectedException.none;
import static org.assertj.core.test.Maps.mapOf;


import static org.mockito.Mockito.spy;

import java.util.Map;

import org.assertj.core.data.MapEntry;
import org.assertj.core.internal.Failures;
import org.assertj.core.internal.Maps;
import org.assertj.core.test.ExpectedException;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for {@link Maps} unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Maps} attributes appropriately.
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

  protected static MapEntry[] emptyEntries() {
    return new MapEntry[0];
  }
}