/*
 * Created on Jan 25, 2011
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
 * Copyright @2011 the original author or authors.
 */
package org.fest.assertions.util;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

/**
 * Reads an image from a file.
 *
 * @author Yvonne Wang
 */
public final class ImageReader {

  /**
   * Decodes the image in the file at the specified path.
   * @param path the path of the image to read.
   * @return the read image.
   * @throws NullPointerException if the given path is {@code null}.
   * @throws IllegalArgumentException if the given path does not belong to a file.
   * @throws IOException if an error occurs during reading.
   */
  public static BufferedImage readImageFrom(String path) throws IOException {
    if (path == null) throw new NullPointerException("The path of the image to read should not be null");
    File file = new File(path);
    if (!file.isFile())
      throw new IllegalArgumentException(String.format("The path '%s' does not belong to a file", path));
    return ImageIO.read(file);
  }

  private ImageReader() {}
}
