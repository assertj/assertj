/*
 * Created on Sep 17, 2010
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
package org.assert4j.core.assertions.internal.objects;

import static org.assert4j.core.assertions.error.ShouldBeSame.shouldBeSame;
import static org.assert4j.core.assertions.test.TestData.someInfo;


import static org.junit.Assert.fail;
import static org.mockito.Mockito.verify;

import org.assert4j.core.assertions.core.AssertionInfo;
import org.assert4j.core.assertions.internal.Objects;
import org.assert4j.core.assertions.internal.ObjectsBaseTest;
import org.assert4j.core.test.Person;
import org.junit.Test;


/**
 * Tests for <code>{@link Objects#assertSame(AssertionInfo, Object, Object)}</code>.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public class Objects_assertSame_Test extends ObjectsBaseTest {

  @Test
  public void should_pass_if_objects_are_same() {
    Object actual = new Object();
    objects.assertSame(someInfo(), actual, actual);
  }

  @Test
  public void should_fail_if_objects_are_not_same() {
    AssertionInfo info = someInfo();
    Object a = new Person("Yoda");
    Object e = new Person("Yoda");
    try {
      objects.assertSame(info, a, e);
      fail();
    } catch (AssertionError err) {}
    verify(failures).failure(info, shouldBeSame(a, e));
  }
}
