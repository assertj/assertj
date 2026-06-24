/*
 * Copyright 2012-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.assertj.protobuf.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;

/**
 * Internal utility class for comparing Protobuf messages.
 * This class is not part of the public API and may change without notice.
 *
 * @author JongJun Kim
 */
public class MessageDifferencer {

  public enum RepeatedFieldComparison {
    AS_LIST, AS_SET
  }

  public enum Scope {
    FULL, PARTIAL
  }

  private RepeatedFieldComparison repeatedFieldComparison = RepeatedFieldComparison.AS_LIST;
  private Scope scope = Scope.FULL;
  private final Set<String> ignoredFields = new HashSet<>();
  private String lastDifferences = "";

  public void setRepeatedFieldComparison(RepeatedFieldComparison comparison) {
    this.repeatedFieldComparison = comparison;
  }

  public void setScope(Scope scope) {
    this.scope = scope;
  }

  public void ignoreField(String fieldPath) {
    ignoredFields.add(fieldPath);
  }

  public String getLastDifferences() {
    return lastDifferences;
  }

  /**
   * Compares two Protobuf messages and returns true if they are equal according to the
   * configured comparison settings.
   */
  public boolean compare(Message actual, Message expected) {
    if (actual == expected) return true;
    if (actual == null || expected == null) return false;

    if (!actual.getDescriptorForType().equals(expected.getDescriptorForType())) {
      lastDifferences = String.format("Message types differ: <%s> vs <%s>",
                                      actual.getDescriptorForType().getFullName(),
                                      expected.getDescriptorForType().getFullName());
      return false;
    }

    StringBuilder differences = new StringBuilder();
    boolean isEqual = compareMessages(actual, expected, "", differences);

    if (!isEqual) {
      lastDifferences = differences.toString();
    }

    return isEqual;
  }

  private boolean compareMessages(Message actual, Message expected, String path, StringBuilder differences) {
    boolean isEqual = true;

    // Get all fields to compare
    Set<FieldDescriptor> fieldsToCompare = new HashSet<>();

    if (scope == Scope.FULL) {
      // Compare all fields in both messages
      fieldsToCompare.addAll(actual.getAllFields().keySet());
      fieldsToCompare.addAll(expected.getAllFields().keySet());
    } else {
      // Only compare fields set in expected message
      fieldsToCompare.addAll(expected.getAllFields().keySet());
    }

    for (FieldDescriptor field : fieldsToCompare) {
      String fieldPath = path.isEmpty() ? field.getName() : path + "." + field.getName();

      if (ignoredFields.contains(fieldPath)) {
        continue;
      }

      // For repeated fields, we need to check if the field is set differently
      Object actualValue;
      Object expectedValue;

      if (field.isRepeated()) {
        actualValue = actual.getField(field);
        expectedValue = expected.getField(field);
        // Empty repeated fields are considered as null
        @SuppressWarnings("unchecked")
        List<Object> actualList = (List<Object>) actualValue;
        @SuppressWarnings("unchecked")
        List<Object> expectedList = (List<Object>) expectedValue;
        if (actualList.isEmpty()) actualValue = null;
        if (expectedList.isEmpty()) expectedValue = null;
      } else {
        actualValue = actual.hasField(field) ? actual.getField(field) : null;
        expectedValue = expected.hasField(field) ? expected.getField(field) : null;
      }

      if (actualValue == null && expectedValue == null) {
        continue;
      }

      if (actualValue == null || expectedValue == null) {
        differences.append(String.format("Field <%s>: expected <%s> but was <%s>%n",
                                         fieldPath, expectedValue, actualValue));
        isEqual = false;
        continue;
      }

      if (field.isRepeated()) {
        if (!compareRepeatedField(actualValue, expectedValue, field, fieldPath, differences)) {
          isEqual = false;
        }
      } else if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
        if (!compareMessages((Message) actualValue, (Message) expectedValue, fieldPath, differences)) {
          isEqual = false;
        }
      } else {
        if (!actualValue.equals(expectedValue)) {
          differences.append(String.format("Field <%s>: expected <%s> but was <%s>%n",
                                           fieldPath, expectedValue, actualValue));
          isEqual = false;
        }
      }
    }

    return isEqual;
  }

  private boolean compareRepeatedField(Object actualValue, Object expectedValue,
                                       FieldDescriptor field, String fieldPath,
                                       StringBuilder differences) {
    @SuppressWarnings("unchecked")
    List<Object> actualList = (List<Object>) actualValue;
    @SuppressWarnings("unchecked")
    List<Object> expectedList = (List<Object>) expectedValue;

    if (repeatedFieldComparison == RepeatedFieldComparison.AS_SET) {
      return compareRepeatedFieldAsSet(actualList, expectedList, field, fieldPath, differences);
    } else {
      return compareRepeatedFieldAsList(actualList, expectedList, field, fieldPath, differences);
    }
  }

  private boolean compareRepeatedFieldAsList(List<Object> actualList, List<Object> expectedList,
                                             FieldDescriptor field, String fieldPath,
                                             StringBuilder differences) {
    if (actualList.size() != expectedList.size()) {
      differences.append(String.format("Repeated field <%s>: size differs, expected %d but was %d%n",
                                       fieldPath, expectedList.size(), actualList.size()));
      return false;
    }

    boolean isEqual = true;
    for (int i = 0; i < actualList.size(); i++) {
      Object actualItem = actualList.get(i);
      Object expectedItem = expectedList.get(i);

      if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
        if (!compareMessages((Message) actualItem, (Message) expectedItem,
                             fieldPath + "[" + i + "]", differences)) {
          isEqual = false;
        }
      } else {
        if (!actualItem.equals(expectedItem)) {
          differences.append(String.format("Repeated field <%s>[%d]: expected <%s> but was <%s>%n",
                                           fieldPath, i, expectedItem, actualItem));
          isEqual = false;
        }
      }
    }

    return isEqual;
  }

  private boolean compareRepeatedFieldAsSet(List<Object> actualList, List<Object> expectedList,
                                            FieldDescriptor field, String fieldPath,
                                            StringBuilder differences) {
    if (actualList.size() != expectedList.size()) {
      differences.append(String.format("Repeated field <%s>: size differs, expected %d but was %d%n",
                                       fieldPath, expectedList.size(), actualList.size()));
      return false;
    }

    List<Object> actualCopy = new ArrayList<>(actualList);
    List<Object> expectedCopy = new ArrayList<>(expectedList);

    for (Object expectedItem : expectedCopy) {
      boolean found = false;
      for (int i = 0; i < actualCopy.size(); i++) {
        Object actualItem = actualCopy.get(i);

        boolean matches;
        if (field.getJavaType() == FieldDescriptor.JavaType.MESSAGE) {
          StringBuilder tempDiff = new StringBuilder();
          matches = compareMessages((Message) actualItem, (Message) expectedItem, fieldPath, tempDiff);
        } else {
          matches = actualItem.equals(expectedItem);
        }

        if (matches) {
          actualCopy.remove(i);
          found = true;
          break;
        }
      }

      if (!found) {
        differences.append(String.format("Repeated field <%s>: expected to contain <%s> but was not found%n",
                                         fieldPath, expectedItem));
        return false;
      }
    }

    return true;
  }
}
