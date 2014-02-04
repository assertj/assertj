package org.assertj.core.internal;

import static org.assertj.core.test.ExpectedException.none;

import static org.mockito.Mockito.spy;

import org.assertj.core.internal.Characters;
import org.assertj.core.internal.ComparatorBasedComparisonStrategy;
import org.assertj.core.internal.Failures;
import org.assertj.core.test.ExpectedException;
import org.assertj.core.util.CaseInsensitiveCharacterComparator;
import org.junit.Before;
import org.junit.Rule;


/**
 * Base class for Characters unit tests
 * <p>
 * Is in <code>org.assertj.core.internal</code> package to be able to set {@link Characters#failures} appropriately.
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