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
 * Copyright 2012-2020 the original author or authors.
 */
package org.assertj.core.error;

import java.util.Map;

/**
 * Generates a description for the type of a group of elements. The description is used in the error message.
 */
public class GroupTypeDescription {
  private static final int SPLITERATORS_CLASS_STACK_TRACE_NUM = 5;
  private String groupTypeName;
  private String elementTypeName;

  public GroupTypeDescription(String groupTypeName, String elementTypeName) {
    this.groupTypeName = groupTypeName;
    this.elementTypeName = elementTypeName;
  }

  public String getElementTypeName() {
    return elementTypeName;
  }

  public String getGroupTypeName() {
    return groupTypeName;
  }

  /**
   * Creates a new <code>{@link GroupTypeDescription}</code> for a group of elements.
   *
   * @param actual the group of elements.
   * @return the created GroupTypeDescription object
   */
  public static GroupTypeDescription getGroupTypeDescription(Object actual) {

    Class<?> clazz = actual.getClass();
    if (Thread.currentThread().getStackTrace()[SPLITERATORS_CLASS_STACK_TRACE_NUM].getClassName().contains("Spliterators"))
      return new GroupTypeDescription("spliterator characteristics", "characteristics");

    if (actual instanceof Map) return new GroupTypeDescription("map", "map entries");

    if (clazz.isArray())
      return new GroupTypeDescription(clazz.getSimpleName(), clazz.getComponentType().getSimpleName().toLowerCase() + "(s)");

    return new GroupTypeDescription(clazz.getSimpleName(), "element(s)");
  }
}
