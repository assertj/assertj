/*
 * Copyright © 2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.tests.core.testkit;

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

  public Jedi setStrangeNotReadablePrivateField(Object value) {
    strangeNotReadablePrivateField = value;
    return this;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (!super.equals(obj)) return false;
    if (getClass() != obj.getClass()) return false;
    Jedi other = (Jedi) obj;
    if (lightSaberColor == null) {
      if (other.lightSaberColor != null) return false;
    } else if (!lightSaberColor.equals(other.lightSaberColor)) return false;
    if (getName() == null) return other.getName() == null;
    return getName().equals(other.getName());
  }

}
