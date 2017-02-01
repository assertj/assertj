/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2017 the original author or authors.
 */
package org.assertj.core.test;

import java.util.Random;

/**
 * Object for test.
 *
 * @author Nicolas François
 * @author Joel Costigliola
 */
public class Jedi extends Person {

  @SuppressWarnings("unused")
  private static final String TEST = "TEst" + new Random().nextInt();

  public String lightSaberColor;
  private Object strangeNotReadablePrivateField;

  public Jedi(String name, String lightSaberColor) {
    super(name);
    this.lightSaberColor = lightSaberColor;
  }

  @Override
  public String toString() {
    return getName() + " the Jedi";
  }

  @SuppressWarnings("unused")
  private Object getStrangeNotReadablePrivateField() {
    return strangeNotReadablePrivateField;
  }

}
