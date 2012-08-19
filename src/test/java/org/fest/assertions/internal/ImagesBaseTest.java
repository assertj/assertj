package org.fest.assertions.internal;

import static org.fest.assertions.data.Offset.offset;
import static org.fest.test.ExpectedException.none;
import static org.fest.assertions.test.TestData.fivePixelBlueImage;

import static org.mockito.Mockito.spy;

import java.awt.image.BufferedImage;

import org.junit.Before;
import org.junit.Rule;

import org.fest.assertions.data.Offset;
import org.fest.test.ExpectedException;

/**
 * Base class for {@link Images} unit tests
 * <p>
 * Is in <code>org.fest.assertions.internal</code> package to be able to set {@link Images} attributes appropriately.
 * 
 * @author Joel Costigliola
 * 
 */
public class ImagesBaseTest {

  @Rule
  public ExpectedException thrown = none();

  protected BufferedImage actual;
  protected Offset<Integer> offset;
  protected Failures failures;
  protected Images images;

  @Before
  public void setUp() {
    failures = spy(new Failures());
    images = new Images();
    images.failures = failures;
    actual = fivePixelBlueImage();
    offset = offset(0);
  }

  protected Object sizeOf(BufferedImage actual) {
    return Images.sizeOf(actual);
  }

}