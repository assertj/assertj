package org.assertj.core.util.introspection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

public class ClassUtils {

  /**
   * <p>
   * Gets a {@code List} of all interfaces implemented by the given class and its superclasses.
   * </p>
   * 
   * <p>
   * The order is determined by looking through each interface in turn as declared in the source file and following its
   * hierarchy up. Then each superclass is considered in the same way. Later duplicates are ignored, so the order is
   * maintained.
   * </p>
   * 
   * @param cls the class to look up, may be {@code null}
   * @return the {@code List} of interfaces in order, {@code null} if null input
   */
  static List<Class<?>> getAllInterfaces(Class<?> cls) {
    if (cls == null) return null;
  
    LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<Class<?>>();
    getAllInterfaces(cls, interfacesFound);
  
    return new ArrayList<Class<?>>(interfacesFound);
  }

  /**
   * Get the interfaces for the specified class.
   * 
   * @param cls the class to look up, may be {@code null}
   * @param interfacesFound the {@code Set} of interfaces for the class
   */
  static void getAllInterfaces(Class<?> cls, HashSet<Class<?>> interfacesFound) {
    while (cls != null) {
      Class<?>[] interfaces = cls.getInterfaces();
  
      for (Class<?> i : interfaces) {
        if (interfacesFound.add(i)) {
          getAllInterfaces(i, interfacesFound);
        }
      }
  
      cls = cls.getSuperclass();
    }
  }

}
