#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME};#end

import org.assertj.core.api.AbstractAssert;

#parse("File Header.java")
public class ${NAME} extends AbstractAssert<${NAME}, ${Class_being_asserted}> {

  private ${NAME}(${Class_being_asserted} actual) {
    super(actual, ${NAME}.class);
  }

  public static ${NAME} assertThat(${Class_being_asserted} actual) {
    return new ${NAME}(actual);
  }
}
