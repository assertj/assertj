package org.assertj.core.extractor;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.test.Employee;
import org.assertj.core.test.Name;
import org.junit.Test;


public class ToStringExtractorTest {

  private ToStringExtractor toStringExtractor = new ToStringExtractor();
  
  @Test
  public void should_extract_toString() throws Exception {
    Employee someEmloyee = new Employee(1, new Name("John Doe"), 28);
    
    String result = toStringExtractor.extract(someEmloyee);
    
    assertThat(result).isEqualTo("Employee[id=1, name=Name[first='John Doe', last='null'], age=28]");
  }
}
