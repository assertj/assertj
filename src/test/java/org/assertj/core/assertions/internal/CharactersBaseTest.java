package org.assertj.core.assertions.internal;

import static org.assertj.core.assertions.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.assertions.internal.Characters;
import org.assertj.core.assertions.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.assertions.internal.Failures;
import org.assertj.core.assertions.test.ExpectedException;
import org.assertj.core.assertions.util.CaseInsensitiveCharacterComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for Characters unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Characters#failures} appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class CharactersBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected Failures failures;
  protected Characters characters;

  protected ComparatorBasedComparisonStrategy caseInsensitiveComparisonStrategy;
  protected Characters charactersWithCaseInsensitiveComparisonStrategy;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    characters = new Characters();
    characters.failures = failures;
    caseInsensitiveComparisonStrategy = new ComparatorBasedComparisonStrategy(CaseInsensitiveCharacterComparator.instance);
    charactersWithCaseInsensitiveComparisonStrategy = new Characters(caseInsensitiveComparisonStrategy);
    charactersWithCaseInsensitiveComparisonStrategy.failures = failures;
  }

}