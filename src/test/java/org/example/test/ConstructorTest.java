package org.example.test;

/**
 * @author phx
 */

import org.assertj.core.api.ClassAssert;
import org.assertj.core.api.ObjectArrayAssert;
import org.junit.jupiter.api.Test;


import java.lang.reflect.Constructor;

import static org.assertj.core.api.Assertions.*;


public class ConstructorTest {
  class person{
    String name;
    int age;
    public person(String n,int a){
      this.age=a;
      this.name=n;
    }

    protected person(String n,Integer a){
      this.age=a;
      this.name=n;
    }

    private person(String s,char age){
    }
    private person(String s,String  age){
    }
  }
  @Test
  public void tets1(){
    ClassAssert classAssert = assertThat(person.class).hasProtectedConstructor(String.class,
      Integer.class );
  }

  @Test
  public void tets2(){
    ClassAssert classAssert = assertThat(person.class).hasPublicConstructor(String.class,
      int.class );
  }

  @Test
  public void tets3(){
    ClassAssert classAssert = assertThat(person.class).hasPrivateConstructor(String.class,
      char.class );
  }

  @Test
  public void tets4(){
    ClassAssert classAssert = assertThat(person.class).hasPrivateConstructor(String.class,
      String.class );
  }
}
