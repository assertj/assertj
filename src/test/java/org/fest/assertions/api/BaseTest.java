/*
 * Created on Jul 29, 2012
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
package org.fest.assertions.api;

import static junit.framework.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.fest.assertions.core.Assert;
import org.fest.assertions.core.AssertionInfo;
import org.fest.assertions.internal.Conditions;
import org.fest.assertions.internal.Objects;
import org.junit.Before;
import org.junit.Test;

/**
 * Root class for API tests.
 * 
 * @param <S> the "self" type of the assertion under test.
 * @param <A> the type of the "actual" value.
 * 
 * @author Olivier Michallat
 */
public abstract class BaseTest<S extends AbstractAssert<S, A>, A> {
  protected S assertions;
  protected Objects objects;
  protected Conditions conditions;

  @Before
  public final void setUp() {
    assertions = create_assertions();
    inject_internal_objects();
  }

  /**
   * Builds an instance of the {@link Assert} implementation under test.
   * 
   * This object will be accessible through the {@link #assertions} field.
   */
  protected abstract S create_assertions();

  /**
   * Injects any additional internal objects (typically mocks) into {@link #assertions}.
   * 
   * Subclasses that override this method must call the superclass implementation.
   */
  protected void inject_internal_objects() {
    objects = mock(Objects.class);
    assertions.objects = objects;
    conditions = mock(Conditions.class);
    assertions.conditions = conditions;
  }

  @Test
  public void should_delegate_to_internal_object() {
    invoke_api_method();
    verify_internal_object_was_invoked();
  }

  @Test
  public void should_return_this() {
    S returned = invoke_api_method();
    assertSame(assertions, returned);
  }

  /**
   * Provides access to the package private {@link AbstractAssert#info} field, for subclasses that reside in a different package.
   * @return the field
   */
  protected AssertionInfo assertionsInfo() {
    return assertions.info;
  }

  /**
   * Provides access to the package private {@link AbstractAssert#actual} field, for subclasses that reside in a different
   * package.
   * @return the field
   */
  protected A assertionsActual() {
    return assertions.actual;
  }

  protected abstract S invoke_api_method();

  protected abstract void verify_internal_object_was_invoked();
}
