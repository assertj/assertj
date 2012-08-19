/*
 * Created on Jan 25, 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.util;

import static java.awt.Color.RED;
import static org.fest.test.ExpectedException.none;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.fest.test.ExpectedException;
import org.junit.*;

/**
 * Tests for <code>{@link ImageReader#readImageFrom(String)}</code>.
 * 
 * @author Yvonne Wang
 */
public class ImageReader_readImageFrom_Test {

  @Rule
  public ExpectedException thrown = none();

  @Test
  public void should_throw_error_if_path_is_null() throws IOException {
    thrown.expectNullPointerException("The path of the image to read should not be null");
    ImageReader.readImageFrom(null);
  }

  @Test
  public void should_throw_error_if_path_does_not_belong_to_file() throws IOException {
    thrown.expectIllegalArgumentException("The path 'xyz' does not belong to a file");
    ImageReader.readImageFrom("xyz");
  }

  @Test
  public void should_read_image_file() throws IOException {
    BufferedImage image = ImageReader.readImageFrom("src/test/resources/red.png");
    assertNotNull(image);
    int w = image.getWidth();
    assertEquals(6, w);
    int h = image.getHeight();
    assertEquals(6, h);
    for (int x = 0; x < w; x++)
      for (int y = 0; y < h; y++)
        assertEquals(RED.getRGB(), image.getRGB(x, y));
  }
}
