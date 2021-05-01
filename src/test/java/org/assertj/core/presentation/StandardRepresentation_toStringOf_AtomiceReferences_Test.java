package org.assertj.core.presentation;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class StandardRepresentation_toStringOf_AtomiceReferences_Test {
  static Representation standardRepresentation = new StandardRepresentation();

  @Test
  public void should_use_overridden_toString_AtomicReference(){
    class MyData extends AtomicReference<String> {
      private String description;

      MyData(String description) {
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    assertThat(standardRepresentation.toStringOf(new MyData("Description"))).isEqualTo("MyData[Description]");
  }

  @Test
  public void should_use_predefined_toString_AtomicReference(){
    class MyData extends AtomicReference<String> {
      private String description;

      MyData(String description) {
        this.description = description;
      }

      // has no overridden toString
    }
    assertThat(standardRepresentation.toStringOf(new MyData("Description"))).isEqualTo("MyData[null]");
  }

  @Test
  public void should_use_overridden_toString_AtomicMarkableReference(){
    class MyData extends AtomicMarkableReference<String> {
      private String description;

      MyData(String description) {
        super(description, true);
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    assertThat(standardRepresentation.toStringOf(new MyData("Description"))).isEqualTo("MyData[Description]");
  }

  @Test
  public void should_use_predefined_toString_AtomicMarkableReference(){
    class MyData extends AtomicMarkableReference<String> {
      private String description;

      MyData(String description) {
        super(description, true);
        this.description = description;
      }

      // has no overridden toString
    }
    assertThat(standardRepresentation.toStringOf(new MyData("Description"))).isEqualTo("MyData[marked=true, reference=\"Description\"]");
  }

  @Test
  public void should_use_overridden_toString_AtomicStampedReference(){
    class MyData extends AtomicStampedReference<String> {
      private String description;

      MyData(String description) {
        super(description, 0);
        this.description = description;
      }

      @Override
      public String toString() {
        return "MyData[" + description + "]";
      }
    }
    assertThat(standardRepresentation.toStringOf(new MyData("Description"))).isEqualTo("MyData[Description]");
  }

  @Test
  public void should_use_predefined_toString_AtomicStampedReference(){
    class MyData extends AtomicStampedReference<String> {
      private String description;

      MyData(String description) {
        super(description, 0);
        this.description = description;
      }

      // has no overridden toString
    }
    assertThat(standardRepresentation.toStringOf(new MyData("Description"))).isEqualTo("MyData[stamp=0, reference=\"Description\"]");
  }

  @Test
  public void should_use_smartFormat(){
    class MyIterable implements Iterable<String> {
      ArrayList<String> arrayList;
      public MyIterable(String[] strings) {
        arrayList = new ArrayList<>(Arrays.asList(strings));
      }

      @Override
      public Iterator<String> iterator() {
        return arrayList.iterator();
      }

      // has no overridden toString
    }
    String[] strings = {"A", "B", "C"};
    assertThat(standardRepresentation.toStringOf(new MyIterable(strings)))
      .isEqualTo("[\"A\", \"B\", \"C\"]");
  }

  @Test
  public void should_use_overridden_toString(){
    class MyIterable implements Iterable<String> {
      ArrayList<String> arrayList;
      public MyIterable(String[] strings) {
        arrayList = new ArrayList<>(Arrays.asList(strings));
      }

      @Override
      public Iterator<String> iterator() {
        return arrayList.iterator();
      }

      @Override
      public String toString(){
        return "MyIterable: " + arrayList;
      }
    }
    String[] strings = {"A", "B", "C"};
    assertThat(standardRepresentation.toStringOf(new MyIterable(strings)))
      .isEqualTo("MyIterable: [A, B, C]");
  }
}
