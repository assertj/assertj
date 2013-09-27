package org.assertj.core.groups;


import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Iterables;
import org.assertj.core.util.Lists;
import org.assertj.core.util.introspection.MethodSupport;

/**
 * Helper class for extracing method invocation result from lists.
 * 
 * @author Michał Piotrkowski
 *
 */
public class MethodInvocationResultExtractor {

  public static List<Object> extractResultOf(String methodName, Iterable<?> list){
    
    List<Object> extractedList = new ArrayList<Object>();
    
    for (Object item : Iterables.nonNullElementsIn(list)) {
    
      extractedList.add(MethodSupport.methodResultFor(item, methodName));
    }
    
    return extractedList;
  }
  
  public static Object[] extractResultOf(String methodName, Object[] array){
    
    List<Object> list = Lists.newArrayList(array);
    List<Object> extractedList = extractResultOf(methodName, list);
    return extractedList.toArray();
  }
  
}
