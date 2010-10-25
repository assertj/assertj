/*
 * Created on Oct 17, 2010
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
 * Copyright @2010 the original author or authors.
 */
package org.fest.assertions.test;

import static java.awt.Color.*;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import java.awt.*;
import java.awt.image.BufferedImage;

import org.fest.assertions.description.*;

/**
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public final class TestData {

  private static final TextDescription DESCRIPTION = new TextDescription("who's the more foolish: the fool, or the fool who follows him?");

  public static Description someDescription() {
    return DESCRIPTION;
  }

  public static String someTextDescription() {
    return "there's always a bigger fish";
  }

  public static BufferedImage fivePixelBlueImage() {
    return newImage(5, 5, BLUE);
  }

 public  static BufferedImage fivePixelYellowImage() {
    return newImage(5, 5, YELLOW);
  }

  public static BufferedImage newImage(int width, int height, Color color) {
    BufferedImage image = new BufferedImage(width, height, TYPE_INT_ARGB);
    Graphics graphics = image.createGraphics();
    graphics.setColor(color);
    graphics.fillRect(0, 0, width, height);
    return image;
  }

  private TestData() {}
}
