/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2021 the original author or authors.
 */
package org.assertj.core.test;

import java.util.Objects;

public abstract class Xml {
  protected final String value;

  public Xml(String value) {
    this.value = value;
  }

  public static Xml newDosXml(String value) {
    return new DosXml(value);
  }

  public static Xml newUnixXml(String value) {
    return new UnixXml(value);
  }

  public static Xml newMacXml(String value) {
    return new MacXml(value);
  }

  protected abstract String getLineSeparator();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Xml xml = (Xml) o;
    return Objects.equals(value, xml.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return
      "<xml>" + getLineSeparator() +
      "  <value>" + value + "</value>" + getLineSeparator() +
      "</xml>";
  }

  public static class UnixXml extends Xml {
    public UnixXml(String value) {
      super(value);
    }

    @Override protected String getLineSeparator() {
      return "\n";
    }
  }

  public static class MacXml extends Xml {
    public MacXml(String value) {
      super(value);
    }

    @Override protected String getLineSeparator() {
      return "\r";
    }
  }

  public static class DosXml extends Xml {
    public DosXml(String value) {
      super(value);
    }

    @Override protected String getLineSeparator() {
      return "\r\n";
    }
  }
}


