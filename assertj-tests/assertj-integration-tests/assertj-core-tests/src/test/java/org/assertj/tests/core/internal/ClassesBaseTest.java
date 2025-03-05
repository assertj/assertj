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
 * Copyright 2012-2025 the original author or authors.
 */
package org.assertj.tests.core.internal;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.assertj.core.internal.Classes;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author William Delanoue
 */
public abstract class ClassesBaseTest {

  protected Classes classes;
  protected Class<?> actual;

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  protected @interface MyAnnotation {

  }

  @MyAnnotation
  protected static class AnnotatedClass {
    public String publicField;
    public String publicField2;
    protected String protectedField;
    @SuppressWarnings("unused")
    private String privateField;
  }

  protected static class Jedi {

  }

  protected static class HumanJedi extends Jedi {

  }

  protected static class MethodsClass {
    public void publicMethod() {}

    protected void protectedMethod() {}

    @SuppressWarnings("unused")
    private void privateMethod() {}

  }

  @BeforeEach
  public void setUp() {
    classes = new Classes();
  }

  public class NoField {

  }

}
