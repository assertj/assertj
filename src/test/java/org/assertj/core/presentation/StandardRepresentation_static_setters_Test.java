package org.assertj.core.presentation;

import org.assertj.core.test.ExpectedException;
import org.junit.Rule;
import org.junit.Test;

/**
 * @author Filip Hrisafov
 */
public class StandardRepresentation_static_setters_Test extends AbstractBaseRepresentationTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_fail_on_invalid_maxElementsForPrinting() throws Exception {
    thrown.expectIllegalArgumentException("maxElementsForPrinting must be >= 1, but was 0");
    StandardRepresentation.setMaxElementsForPrinting(0);
  }

  @Test
  public void should_fail_on_invalid_maxLengthForSingleLineDescription() throws Exception {
    thrown.expectIllegalArgumentException("maxLengthForSingleLineDescription must be > 0 but was 0");
    StandardRepresentation.setMaxLengthForSingleLineDescription(0);
  }
}
