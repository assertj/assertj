/*
 * Created on Oct 17, 2010
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
 * Copyright @2010-2011 the original author or authors.
 */
package org.fest.assertions.test;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

import static org.fest.assertions.data.Index.atIndex;
import static org.fest.assertions.data.RgbColor.color;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.regex.Pattern;

import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.core.WritableAssertionInfo;
import org.fest.assertions.data.Index;
import org.fest.assertions.data.RgbColor;
import org.fest.assertions.description.Description;
import org.fest.assertions.description.TextDescription;

/**
 * @author Alex Ruiz
 * @author Yvonne Wang
 */
public final class TestData {

  private static final RgbColor BLUE = color(0x0000FF);
  private static final RgbColor YELLOW = color(0xFFFF00);
  private static final AssertionInfo ASSERTION_INFO = new WritableAssertionInfo();
  private static final TextDescription DESCRIPTION = new TextDescription(
      "who's the more foolish: the fool, or the fool who follows him?");
  private static final Index INDEX = atIndex(0);
  private static final Pattern MATCH_ANYTHING = Pattern.compile(".*");

  public static RgbColor blue() {
    return BLUE;
  }

  public static BufferedImage fivePixelBlueImage() {
    return fivePixelImage(Color.BLUE);
  }

  public static BufferedImage fivePixelYellowImage() {
    return fivePixelImage(Color.YELLOW);
  }

  private static BufferedImage fivePixelImage(Color color) {
    return newImage(5, 5, color);
  }

  public static BufferedImage newImage(int width, int height, Color color) {
    BufferedImage image = new BufferedImage(width, height, TYPE_INT_ARGB);
    Graphics graphics = image.createGraphics();
    graphics.setColor(color);
    graphics.fillRect(0, 0, width, height);
    return image;
  }

  public static Pattern matchAnything() {
    return MATCH_ANYTHING;
  }

  public static Index someIndex() {
    return INDEX;
  }

  public static AssertionInfo someInfo() {
    return ASSERTION_INFO;
  }

  public static Description someDescription() {
    return DESCRIPTION;
  }

  public static String someTextDescription() {
    return "there's always a bigger fish";
  }

  public static RgbColor yellow() {
    return YELLOW;
  }

  private TestData() {}
}
