package org.assertj.core.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaMember;
import com.tngtech.archunit.core.domain.properties.HasName.AndFullName;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchUnitRunner;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.runner.RunWith;

import java.net.URL;
import java.util.function.Predicate;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static org.assertj.core.architecture.ArchitectureTest.ROOT_PACKAGE;

@RunWith(ArchUnitRunner.class)
@AnalyzeClasses(packages = ROOT_PACKAGE)
public class ArchitectureTest {
  static final String ROOT_PACKAGE = "org.assertj.core.";

  private static final String ANNOTATIONS_LAYER = "annotations";
  private static final String API_LAYER = "api";
  private static final String CONDITION_LAYER = "condition";
  private static final String CONFIGURATION_LAYER = "configuration";
  private static final String DATA_LAYER = "data";
  private static final String DESCRIPTION_LAYER = "description";
  private static final String ERROR_LAYER = "error";
  private static final String EXTRACTOR_LAYER = "extractor";
  private static final String GROUPS_LAYER = "groups";
  private static final String INTERNAL_LAYER = "internal";
  private static final String MATCHER_LAYER = "matcher";
  private static final String PRESENTATION_LAYER = "presentation";
  private static final String UTIL_LAYER = "util";

  private static final String API_PACKAGE = ROOT_PACKAGE + API_LAYER;

  //@formatter:off
  @ArchTest
  private static final ArchRule layerRule = layeredArchitecture()
    .layer(ANNOTATIONS_LAYER).definedBy(ROOT_PACKAGE + ANNOTATIONS_LAYER)
    .layer(API_LAYER).definedBy(API_PACKAGE)
    .layer(CONDITION_LAYER).definedBy(ROOT_PACKAGE + CONDITION_LAYER)
    .layer(CONFIGURATION_LAYER).definedBy(ROOT_PACKAGE + CONFIGURATION_LAYER)
    .layer(DATA_LAYER).definedBy(ROOT_PACKAGE + DATA_LAYER)
    .layer(DESCRIPTION_LAYER).definedBy(ROOT_PACKAGE + DESCRIPTION_LAYER)
    .layer(ERROR_LAYER).definedBy(ROOT_PACKAGE + ERROR_LAYER)
    .layer(EXTRACTOR_LAYER).definedBy(ROOT_PACKAGE + EXTRACTOR_LAYER)
    .layer(GROUPS_LAYER).definedBy(ROOT_PACKAGE + GROUPS_LAYER)
    .layer(INTERNAL_LAYER).definedBy(ROOT_PACKAGE + INTERNAL_LAYER + ".(*)..")
    .layer(MATCHER_LAYER).definedBy(ROOT_PACKAGE + MATCHER_LAYER)
    .layer(PRESENTATION_LAYER).definedBy(ROOT_PACKAGE + PRESENTATION_LAYER)
    .layer(UTIL_LAYER).definedBy(ROOT_PACKAGE + UTIL_LAYER)
    .ignoreDependency(belongToTestClassDirectory(), alwaysTrue())

    .whereLayer(INTERNAL_LAYER).mayOnlyBeAccessedByLayers(
      ANNOTATIONS_LAYER,
      API_LAYER,
      CONDITION_LAYER,
      CONFIGURATION_LAYER,
      DATA_LAYER,
      DESCRIPTION_LAYER,
      ERROR_LAYER,
      EXTRACTOR_LAYER,
      GROUPS_LAYER,
      MATCHER_LAYER,
      PRESENTATION_LAYER,
      UTIL_LAYER);
  //@formatter:on

  private static DescribedPredicate<AndFullName> belongToTestClassDirectory() {
    return new DescribedPredicate<AndFullName>("belong to the test class directory") {

      @Override
      public boolean apply(AndFullName input) {
        return areInTestClassDirectoryPredicate().test(input);
      }
    };
  }

  private static Predicate<AndFullName> areInTestClassDirectoryPredicate() {
    return input -> {
      if (input instanceof JavaMember) {
        input = ((JavaMember) input).getOwner();
      }
      String urlStr = '/' + input.getFullName().replace(".", "/") + ".class";
      URL url = ArchitectureTest.class.getResource(urlStr);
      return (url != null) && (url.getFile() != null) && url.getFile().contains("target/test-classes");
    };
  }
}
