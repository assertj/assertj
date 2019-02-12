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
 * Copyright 2012-2019 the original author or authors.
 */
package org.assertj.core.error;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Access to constructors using Java reflection.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class ConstructorInvoker {

  public static final ConstructorInvoker CONSTRUCTOR_INVOKER = new ConstructorInvoker();

  public Object newInstance(String className, Class<?>[] parameterTypes, Object... parameterValues) throws Exception {
    Class<?> targetType = Class.forName(className);
    Constructor<?> constructor = targetType.getConstructor(parameterTypes);
    setAccessible(constructor, true);
    return constructor.newInstance(parameterValues);
  }

  private void setAccessible(AccessibleObject accessible, boolean value) {
    AccessController.doPrivileged(new SetAccessibleValueAction(accessible, value));
  }

  private static class SetAccessibleValueAction implements PrivilegedAction<Void> {
    private final AccessibleObject accessible;
    private final boolean value;

    private SetAccessibleValueAction(AccessibleObject accessible, boolean value) {
      this.accessible = accessible;
      this.value = value;
    }

    @Override
    public Void run() {
      accessible.setAccessible(value);
      return null;
    }
  }
}
