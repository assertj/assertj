/*
 * Created on Feb 22, 2011
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
package org.assert4j.core.assertions.internal;

import static junit.framework.Assert.*;
import static org.assert4j.core.util.Introspection.getProperty;
import static org.assert4j.core.util.Lists.newArrayList;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.beans.PropertyDescriptor;
import java.util.*;


import org.assert4j.core.assertions.internal.JavaBeanDescriptor;
import org.assert4j.core.assertions.internal.PropertySupport;
import org.assert4j.core.test.Name;
import org.assert4j.core.util.IntrospectionError;
import org.junit.*;

/**
 * Tests for <code>{@link PropertySupport#propertyValues(String, Collection)}</code>.
 * 
 * @author Yvonne Wang
 * @author Mikhail Mazursky
 */
public class PropertySupport_propertyValues_with_mocks_Test {

  private static org.assert4j.core.test.Employee yoda;
  private static List<org.assert4j.core.test.Employee> employees;

  @BeforeClass
  public static void setUpOnce() {
    yoda = new org.assert4j.core.test.Employee(6000L, new Name("Yoda"), 800);
    employees = newArrayList(yoda);
  }

  private JavaBeanDescriptor descriptor;
  private PropertySupport propertySupport;

  @Before
  public void setUp() {
    descriptor = mock(JavaBeanDescriptor.class);
    propertySupport = new PropertySupport();
    propertySupport.javaBeanDescriptor = descriptor;
  }

  @Test
  public void should_throw_error_if_PropertyDescriptor_cannot_invoke_read_method() throws Exception {
    RuntimeException thrownOnPurpose = new RuntimeException("Thrown on purpose");
    PropertyDescriptor real = getProperty("id", yoda);
    when(descriptor.invokeReadMethod(real, yoda)).thenThrow(thrownOnPurpose);
    try {
      propertySupport.propertyValues("id", Long.class, employees);
      fail("expecting an IntrospectionError to be thrown");
    } catch (IntrospectionError expected) {
      assertSame(thrownOnPurpose, expected.getCause());
      String msg = String.format("Unable to obtain the value of the property <'id'> from <%s>", yoda.toString());
      assertEquals(msg, expected.getMessage());
    }
  }
}
