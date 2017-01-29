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
package org.assertj.core.util.introspection;

import static org.assertj.core.util.Preconditions.checkArgument;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * Shameless copy from Apache commons lang and then modified to keep only the interesting stuff for AssertJ.
 * 
 * Utilities for working with fields by reflection. Adapted and refactored from the dormant [reflect] Commons sandbox
 * component.
 * <p>
 * The ability is provided to break the scoping restrictions coded by the programmer. This can allow fields to be
 * changed that shouldn't be. This facility should be used with care.
 */
public class FieldUtils {

  /**
   * Gets an accessible <code>Field</code> by name breaking scope if requested. Superclasses/interfaces will be
   * considered.
   * 
   * @param cls the class to reflect, must not be null
   * @param fieldName the field name to obtain
   * @param forceAccess whether to break scope restrictions using the <code>setAccessible</code> method.
   *          <code>False</code> will only match public fields.
   * @return the Field object
   * @throws IllegalArgumentException if the class or field name is null
   * @throws IllegalAccessException if field exists but is not public
   */
  static Field getField(final Class<?> cls, String fieldName, boolean forceAccess) throws IllegalAccessException {
    checkArgument(cls != null, "The class must not be null");
    checkArgument(fieldName != null, "The field name must not be null");
    // Sun Java 1.3 has a bugged implementation of getField hence we write the
    // code ourselves

    // getField() will return the Field object with the declaring class
    // set correctly to the class that declares the field. Thus requesting the
    // field on a subclass will return the field from the superclass.
    //
    // priority order for lookup:
    // searchclass private/protected/package/public
    // superclass protected/package/public
    // private/different package blocks access to further superclasses
    // implementedinterface public

    // check up the superclass hierarchy
    for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
      try {
        Field field = acls.getDeclaredField(fieldName);
        // getDeclaredField checks for non-public scopes as well and it returns accurate results
        if (!Modifier.isPublic(field.getModifiers())) {
          if (forceAccess) {
            field.setAccessible(true);
          } else {
            throw new IllegalAccessException("can not access" + fieldName + " because it is not public");
          }
        }
        return field;
      } catch (NoSuchFieldException ex) { // NOPMD
        // ignore
      }
    }
    // check the public interface case. This must be manually searched for
    // incase there is a public supersuperclass field hidden by a private/package
    // superclass field.
    Field match = null;
    for (Class<?> class1 : ClassUtils.getAllInterfaces(cls)) {
      try {
        Field test = class1.getField(fieldName);
        checkArgument(match == null, "Reference to field " + fieldName + " is ambiguous relative to " + cls
              + "; a matching field exists on two or more implemented interfaces.");
        match = test;
      } catch (NoSuchFieldException ex) { // NOPMD
        // ignore
      }
    }
    return match;
  }

  /**
   * Reads an accessible Field.
   * 
   * @param field the field to use
   * @param target the object to call on, may be null for static fields
   * @return the field value
   * @throws IllegalArgumentException if the field is null
   * @throws IllegalAccessException if the field is not accessible
   */
  private static Object readField(Field field, Object target) throws IllegalAccessException {
    return readField(field, target, false);
  }

  /**
   * Reads a Field.
   * 
   * @param field the field to use
   * @param target the object to call on, may be null for static fields
   * @param forceAccess whether to break scope restrictions using the <code>setAccessible</code> method.
   * @return the field value
   * @throws IllegalArgumentException if the field is null
   * @throws IllegalAccessException if the field is not made accessible
   */
  private static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {
    checkArgument(field != null, "The field must not be null");
    if (forceAccess && !field.isAccessible()) {
      field.setAccessible(true);
    } else {
      MemberUtils.setAccessibleWorkaround(field);
    }
    return field.get(target);
  }

  /**
   * Reads the named field. Superclasses will be considered.
   * 
   * @param target the object to reflect, must not be null
   * @param fieldName the field name to obtain
   * @param forceAccess whether to break scope restrictions using the <code>setAccessible</code> method.
   *          <code>False</code> will only match public fields.
   * @return the field value
   * @throws IllegalArgumentException if the class or field name is null
   * @throws IllegalAccessException if the named field is not made accessible
   */
  static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
    checkArgument(target != null, "target object must not be null");
    Class<?> cls = target.getClass();
    Field field = getField(cls, fieldName, forceAccess);
    checkArgument(field != null, "Cannot locate field " + fieldName + " on " + cls);
    // already forced access above, don't repeat it here:
    return readField(field, target);
  }

}
