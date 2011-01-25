/*
 * Created on Jan 24, 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.fest.assertions.test.TestData.fivePixelBlueImage;
import static org.mockito.Mockito.*;

import java.awt.Dimension;

import org.fest.assertions.internal.Images;
import org.junit.*;

/**
 * Tests for <code>{@link ImageAssert#hasSize(Dimension)}</code>.
 *
 * @author Yvonne Wang
 */
public class ImageAssert_hasSize_Test {

  private static Dimension size;

  @BeforeClass public static void setUpOnce() {
    size = new Dimension(6, 8);
  }

  private Images images;
  private ImageAssert assertions;

  @Before public void setUp() {
    images = mock(Images.class);
    assertions = new ImageAssert(fivePixelBlueImage());
    assertions.images = images;
  }

  @Test public void should_verify_that_actual_has_expected_size() {
    assertions.hasSize(size);
    verify(images).assertHasSize(assertions.info, assertions.actual, size);
  }

  @Test public void should_return_this() {
    ImageAssert returned = assertions.hasSize(size);
    assertSame(assertions, returned);
  }
}
