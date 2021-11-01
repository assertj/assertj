package org.assertj.core.api.recursive.assertion;

import static org.assertj.core.util.Lists.list;
import static org.assertj.core.util.Sets.newHashSet;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.assertj.core.api.recursive.FieldLocation;

public class RecursiveAssertionDriver {

  private final Set<String> markedBlackSet = newHashSet();
  private final List<FieldLocation> fieldsThatFailedTheAssertion = list();
  private final RecursiveAssertionConfiguration configuration;
  
  public RecursiveAssertionDriver(RecursiveAssertionConfiguration configuration) {
    this.configuration = configuration;
  }

  public List<FieldLocation> assertOverObjectGraph(Predicate<Object> predicate, Object graphNode) {
    assertRecursively(predicate, graphNode, FieldLocation.rootFieldLocation());
    return Collections.unmodifiableList(fieldsThatFailedTheAssertion); 
  }

  private void assertRecursively(Predicate<Object> predicate, Object node, FieldLocation fieldLocation) {
    // TODO 0: Add object id to black set
    // TODO 1: Check conditions that should cause us to ignore this field (ignore by name, type, null...)
    doTheActualAssertionAndRegisterInCaseOfFailure(predicate, node, fieldLocation);
    // TODO 3: Check recursive conditions
    // TODO 4: Check for map/collections/arrays/optionals
    // TODO 5: Make the recursive call for all applicable fields
  }

  private void doTheActualAssertionAndRegisterInCaseOfFailure(Predicate<Object> predicate, Object node, FieldLocation fieldLocation) {
    if (!predicate.test(node)) {
      fieldsThatFailedTheAssertion.add(fieldLocation);
    }
  }
  
}
