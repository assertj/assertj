package org.assertj.core.generator;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toUnmodifiableMap;
import static org.apache.commons.lang3.reflect.TypeUtils.wildcardType;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.annotation.Beta;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AbstractOptionalAssert;
import org.assertj.core.api.Assert;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.InstanceOfAssertFactory;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.OptionalAssert;
import org.assertj.core.api.soft.DefaultSoftAssertFactory;
import org.assertj.core.api.soft.SoftAssert;
import org.assertj.core.api.soft.SoftListAssert;
import org.assertj.core.api.soft.SoftObjectAssert;
import org.assertj.core.api.soft.SoftOptionalAssert;
import org.assertj.core.api.soft.SoftStringAssert;
import org.jspecify.annotations.NonNull;
import org.springframework.javapoet.AnnotationSpec;
import org.springframework.javapoet.ArrayTypeName;
import org.springframework.javapoet.ClassName;
import org.springframework.javapoet.FieldSpec;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.MethodSpec;
import org.springframework.javapoet.ParameterSpec;
import org.springframework.javapoet.ParameterizedTypeName;
import org.springframework.javapoet.TypeName;
import org.springframework.javapoet.TypeSpec;
import org.springframework.javapoet.TypeVariableName;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.javadoc.Javadoc;

@SuppressWarnings("rawtypes")
public class SoftAssertionsGenerator {

  private static final String SOFT_ASSERT_CLASSES_PACKAGE_NAME = "org.assertj.core.api.soft";

  private static final List<String> NAVIGATION_METHODS = List.of("asBase64Decoded",
                                                                 "asBase64Encoded",
                                                                 "asBoolean",
                                                                 "asByte",
                                                                 "asDouble",
                                                                 "asFloat",
                                                                 "asInstanceOf",
                                                                 "asInt",
                                                                 "asLong",
                                                                 "asShort",
                                                                 "asString",
                                                                 "asHexString",
                                                                 "binaryContent",
                                                                 "cause",
                                                                 "content",
                                                                 "extracting",
                                                                 "extractingByKey",
                                                                 "extractingByKeys",
                                                                 "extractingFromEntries",
                                                                 "extractingResultOf",
                                                                 "filteredOn",
                                                                 "filteredOnAssertions",
                                                                 "filteredOnNull",
                                                                 "flatExtracting",
                                                                 "flatMap",
                                                                 "get",
                                                                 "map",
                                                                 "message",
                                                                 "newAbstractIterableAssert",
                                                                 "rootCause",
                                                                 "scale",
                                                                 "size",
                                                                 "succeedsWithin",
                                                                 "toAssert",
                                                                 "usingRecursiveComparison");

  static final List<String> METHODS_TO_DELEGATE_TO = List.of("equals",
                                                             "hashCode",
                                                             "getWritableAssertionInfo",
                                                             "toString");
  static final List<String> METHODS_NOT_THROWING_ASSERTION_ERRORS = List.of("as",
                                                                            "describedAs",
                                                                            "descriptionText",
                                                                            "inBinary",
                                                                            "inHexadecimal",
                                                                            "newAbstractIterableAssert",
                                                                            "newObjectArrayAssert",
                                                                            "overridingErrorMessage",
                                                                            "succeedsWithin",
                                                                            "failsWithin",
                                                                            "usingComparator",
                                                                            "usingDefaultComparator",
                                                                            "usingElementComparator",
                                                                            "usingEquals",
                                                                            "usingValueComparator",
                                                                            "withAssertionState",
                                                                            "withComparatorsForElementPropertyOrFieldNames",
                                                                            "withComparatorsForElementPropertyOrFieldTypes",
                                                                            "withFailMessage",
                                                                            "withIterables",
                                                                            "withRepresentation",
                                                                            "withThreadDumpOnError",
                                                                            "withTypeComparators");

  // <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT
  private static final TypeVariableName BOUNDED_SOFT_ASSERT = TypeVariableName.get("SOFT_ASSERT", TypeName.get(SoftAssert.class));
  private static final TypeVariableName WILDCARD_TYPE = TypeVariableName.get("?");
  private static final TypeVariableName OBJECT_TYPE = TypeVariableName.get("Object");
  // DefaultSoftAssertFactory<?, SOFT_ASSERT>
  private static final ParameterizedTypeName DEFAULT_SOFT_ASSERT_FACTORY_PARAMETERIZED_TYPE = ParameterizedTypeName.get(ClassName.get("",
                                                                                                                                      DefaultSoftAssertFactory.class.getSimpleName()),
                                                                                                                        WILDCARD_TYPE,
                                                                                                                        TypeVariableName.get("SOFT_ASSERT"));

  private static final Map<Class<?>, CompilationUnit> compilationUnitCache = new HashMap<>();

  public static void main(String[] args) {
    // for javadoc generation
    StaticJavaParser.getParserConfiguration().setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);

    // TODO: generate assertions with several parameterized types: map assertions
    // TODO: methods to ignore ?
    // TODO: format code
    // TODO: generate GeneratedSoftAssertions ?
    // TODO: move generator to a different place
    // TODO: DefaultSoftAssertFactory -> SoftAssertFactory
    Stream.of(ObjectAssert.class).forEach(SoftAssertionsGenerator::generateSoftAssertionFor);
    Stream.of(OptionalAssert.class).forEach(SoftAssertionsGenerator::generateSoftAssertionFor);
  }

  private static void generateSoftAssertionFor(Class<? extends Assert> assertClass) {

    var methods = getNonDuplicatedInstanceMethods(assertClass);
    var javadocByMethods = getJavadocOfMethods(methods);

    var assertClassTypeVariables = stream(assertClass.getTypeParameters()).map(TypeVariableName::get)
                                                                          .toArray(TypeVariableName[]::new);
    FieldSpec assertField = generateAssertField(assertClass, assertClassTypeVariables);

    Type realActualType = assertClass.getDeclaredConstructors()[0].getGenericParameterTypes()[0];
    ParameterSpec actualParameter = ParameterSpec.builder(realActualType, "actual").build();

    String softAssertClassName = getSoftAssertClassName(assertClass);
    TypeSpec.Builder softAssertTypeBuilder = TypeSpec.classBuilder(softAssertClassName)
                                                     .addSuperinterface(SoftAssert.class)
                                                     .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                                                     .addField(generateErrorCollectorField())
                                                     .addField(assertField)
                                                     .addMethod(generateConstructor(actualParameter, assertField))
                                                     .addTypeVariables(List.of(assertClassTypeVariables))
                                                     .addAnnotation(Beta.class)
                                                     .addAnnotation(AnnotationSpec.builder(SuppressWarnings.class)
                                                                                  .addMember("value", "$S",
                                                                                             "ResultOfMethodCallIgnored")
                                                                                  .build());

    var softAssertType = ParameterizedTypeName.get(ClassName.get("", softAssertClassName), assertClassTypeVariables);

    for (Method method : methods) {
      MethodSpec.Builder methodBuilder = generateMethod(method, softAssertType, assertField, realActualType);
      if (methodBuilder != null) {
        Parameter[] methodParameters = method.getParameters();
        useVarargsWhenPossible(methodParameters, methodBuilder);
        addSafeVarargsAnnotationAndMakeMethodFinalWhenNeeded(methodParameters, methodBuilder);
        addJavadoc(method, javadocByMethods, methodBuilder);
        softAssertTypeBuilder.addMethod(methodBuilder.build());
      }
    }

    try {
      JavaFile.builder(SOFT_ASSERT_CLASSES_PACKAGE_NAME, softAssertTypeBuilder.build())
              .addStaticImport(Assertions.class, "assertThat")
              .build()
              .writeTo(Path.of("assertj-core/src/main/java"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void addJavadoc(Method method, Map<Method, String> javadocByMethods, MethodSpec.Builder methodBuilder) {
    String javadoc = javadocByMethods.get(method);
    if (!javadoc.isEmpty()) {
      methodBuilder.addJavadoc(javadoc);
    }
  }

  private static Map<Method, String> getJavadocOfMethods(Collection<Method> methods) {
    return methods.stream().collect(toUnmodifiableMap(method -> method, SoftAssertionsGenerator::getJavadoc));
  }

  private static @NonNull String getJavadoc(Method method) {
    var compilationUnit = getCompilationUnit(method.getDeclaringClass());
    var methodDeclarations = compilationUnit.findAll(MethodDeclaration.class, matchMethod(method));
    for (MethodDeclaration methodDeclaration : methodDeclarations) {
      String javadocText = methodDeclaration.getJavadoc().map(Javadoc::toText).orElse("");
      // replace stuff in Javadoc that does not compile since the correct import are not there
      javadocText = javadocText.replaceAll("link Map}", "link java.util.Map}");
      javadocText = javadocText.replaceAll("link Assertions", "link org.assertj.core.api.Assertions");
      javadocText = javadocText.replaceAll(", InstanceOfAssertFactory", ", DefaultSoftAssertFactory");
      javadocText = javadocText.replaceAll(Pattern.quote("@param <ASSERT> the type of the resulting {@code Assert}"),
                                           "@param <SOFT_ASSERT> the type of the resulting {@code SoftAssert}");
      javadocText = javadocText.replaceAll(Pattern.quote("@param assertFactory"), "@param softAssertFactory");
      javadocText = javadocText.replaceAll(Pattern.quote("{@code assertFactory}"), "{@code softAssertFactory}");
      javadocText = javadocText.replaceAll(Pattern.quote("{@link InstanceOfAssertFactory}"), "{@link DefaultSoftAssertFactory}");
      javadocText = javadocText.replaceAll(Pattern.quote("@see #get(InstanceOfAssertFactory)"),
                                           "@see #get(DefaultSoftAssertFactory)");
      javadocText = javadocText.replaceAll(Pattern.quote("{@code Assert}"), "{@code SoftAssert}");
      return javadocText;
    }
    return "";
  }

  private static @NonNull Predicate<MethodDeclaration> matchMethod(Method method) {
    return methodDeclaration -> methodDeclaration.getName().asString().contains(method.getName())
                                && parametersTypesMatch(methodDeclaration, method);
  }

  private static boolean parametersTypesMatch(MethodDeclaration methodDeclaration, Method method) {
    var methodDeclarationParameters = methodDeclaration.getParameters();
    if (methodDeclarationParameters.size() != method.getParameterCount()) return false;

    Class<?>[] parameterTypes = method.getParameterTypes();
    for (int i = 0; i < parameterTypes.length; i++) {
      if (!parsedParameterTypeMatches(methodDeclarationParameters.get(i), parameterTypes[i])) {
        return false;
      }
    }
    return true;
  }

  private static boolean parsedParameterTypeMatches(com.github.javaparser.ast.body.Parameter parameter, Class<?> type) {
    // handle checking varargs vs array
    var parsedParameterType = parameter.getType();
    if (type.isArray()) {
      Class<?> elementType = type.getComponentType();
      if (parameter.isVarArgs()) {
        return typeNamesMatch(parsedParameterType, elementType);
      }
      if (parsedParameterType.isArrayType()) {
        return typeNamesMatch(parsedParameterType, elementType);
      }
      // parameter is not an array nor a varargs, it can't match an array type
      return false;
    }
    if (parameter.isVarArgs() || parsedParameterType.isArrayType()) {
      // since type is not an array it can't match a vararg/array
      return false;
    }
    return typeNamesMatch(parsedParameterType, type);
  }

  private static boolean typeNamesMatch(com.github.javaparser.ast.type.Type parsedParameterType, Class<?> type) {
    String parameterTypeName = parsedParameterType.asString();
    if (parsedParameterType.isClassOrInterfaceType()) {
      parameterTypeName = asClassOrInterfaceTypeString(parsedParameterType);
    } else if (parsedParameterType.isArrayType()) {
      var elementType = parsedParameterType.asArrayType().getElementType();
      // handle generic array like Function<T, U>[], where we want to get Function and not Function<T, U>
      parameterTypeName = elementType.isClassOrInterfaceType() ? asClassOrInterfaceTypeString(elementType)
          : elementType.asString();
    }
    return type.getSimpleName().equals(parameterTypeName);
  }

  private static String asClassOrInterfaceTypeString(com.github.javaparser.ast.type.Type type) {
    // remove the generic types, ex: Predicate in Predicate<? super T>, as type simple name does not show generics
    return type.asClassOrInterfaceType().getName().asString();
  }

  private static CompilationUnit getCompilationUnit(Class<?> assertClass) {
    try {
      if (compilationUnitCache.containsKey(assertClass)) {
        return compilationUnitCache.get(assertClass);
      }
      CompilationUnit compilationUnit = StaticJavaParser.parse(Paths.get("assertj-core/src/main/java/org/assertj/core/api/"
                                                                         + assertClass.getSimpleName() + ".java"));
      compilationUnitCache.put(assertClass, compilationUnit);
      return compilationUnit;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static MethodSpec.Builder generateActualMethod(Method method, Type realActualType, FieldSpec assertField) {
    return MethodSpec.methodBuilder(method.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .returns(realActualType)
                     .addStatement("return $N.actual()", assertField);
  }

  private static @NonNull String getSoftAssertClassName(Class<? extends Assert> assertClass) {
    return "Soft%s".formatted(assertClass.getSimpleName());
  }

  private static boolean isMethodToDelegateTo(Method method) {
    return METHODS_TO_DELEGATE_TO.contains(method.getName());
  }

  private static @NonNull Collection<Method> getNonDuplicatedInstanceMethods(Class<? extends Assert> assertClass) {
    // remove inherited duplicate methods
    Method[] methods = stream(ArrayUtils.removeElements(assertClass.getMethods(), Object.class.getMethods()))
                                                                                                             .filter(SoftAssertionsGenerator::isNotStatic)
                                                                                                             .toArray(Method[]::new);
    Map<String, List<Method>> methodsByMethodWithParametersName = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    methodsByMethodWithParametersName.putAll(stream(methods)
                                                            .collect(groupingBy(method -> method.toString()
                                                                                                .replaceAll(".*("
                                                                                                            + method.getName()
                                                                                                            + ".*)", "$1"))));
    return methodsByMethodWithParametersName.values().stream()
                                            .map(SoftAssertionsGenerator::getMethodWithGenericSignature)
                                            .toList();
  }

  private static Method getMethodWithGenericSignature(List<Method> sameMethods) {
    for (Method method : sameMethods) {
      // return the method with generic info, so that we can generate soft methods honoring generics
      if (hasGenericParameterInformation(method.getGenericParameterTypes())) return method;
    }
    return sameMethods.get(0);
  }

  private static boolean hasGenericParameterInformation(Type[] types) {
    return stream(types).anyMatch(type -> type instanceof ParameterizedType || type instanceof GenericArrayType);
  }

  private static boolean isNotStatic(Method m) {
    return (m.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0;
  }

  private static @NonNull FieldSpec generateAssertField(Class<? extends Assert> assertClass,
                                                        TypeVariableName[] typeVariableNames) {
    var assertClassSimpleName = assertClass.getSimpleName();
    String assertFieldName = assertClassSimpleName.toLowerCase().charAt(0) + assertClassSimpleName.substring(1);
    var parameterizedTypeName = ParameterizedTypeName.get(ClassName.get("", assertClassSimpleName), typeVariableNames);
    return FieldSpec.builder(parameterizedTypeName, assertFieldName, Modifier.PRIVATE, Modifier.FINAL).build();
  }

  private static @NonNull FieldSpec generateErrorCollectorField() {
    return FieldSpec.builder(AssertionErrorCollector.class, "errorCollector", Modifier.PRIVATE, Modifier.FINAL).build();
  }

  private static MethodSpec generateConstructor(ParameterSpec actualParameter, FieldSpec assertField) {
    return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                     .addParameter(actualParameter)
                     .addParameter(AssertionErrorCollector.class, "errorCollector")
                     .addStatement("this.$N = $N", "errorCollector", "errorCollector")
                     .addStatement("this.$N = assertThat(actual)", assertField)
                     .build();
  }

  private static MethodSpec.Builder generateMethod(Method method, ParameterizedTypeName softAssertType, FieldSpec assertField,
                                                   Type realActualType) {
    if (method.getName().equals("actual") && method.getDeclaringClass().equals(AbstractAssert.class)) {
      return generateActualMethod(method, realActualType, assertField);
    } else if (isMethodToDelegateTo(method)) {
      return generateDelegateMethod(method, assertField);
    } else if (isNonAssertionMethod(method)) {
      return generateDelegateMethodReturningThis(method, softAssertType, assertField, realActualType);
    } else if (isNavigationMethod(method)) {
      return generateNavigationMethod(method, assertField);
    } else if (isAssertionMethod(method)) {
      return generateAssertionMethod(method, softAssertType, assertField, realActualType);
    }
    return null;
  }

  private static MethodSpec.Builder generateAssertionMethod(Method assertionMethod, ParameterizedTypeName softAssertType,
                                                            FieldSpec assertField, Type actualType) {
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(assertionMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariables(getMethodTypeVariables(assertionMethod))
                                               .returns(softAssertType)
                                               .beginControlFlow("try")
                                               .addStatement("$N." + assertionMethod.getName() + methodArguments(assertionMethod),
                                                             assertField)
                                               .addStatement("errorCollector.succeeded()")
                                               .nextControlFlow("catch ($T assertionError)", AssertionError.class)
                                               .addStatement("errorCollector.collectAssertionError(assertionError)")
                                               .endControlFlow()
                                               .addStatement("return this");

    Parameter[] methodParameters = assertionMethod.getParameters();
    for (Parameter parameter : methodParameters) {
      addParameter(actualType, parameter, softAssertionMethodBuilder);
    }
    return softAssertionMethodBuilder;
  }

  private static void useVarargsWhenPossible(Parameter[] methodParameters, MethodSpec.Builder methodBuilder) {
    if (isLastParameterAnArray(methodParameters)) {
      methodBuilder.varargs(true);
    }
  }

  private static void addSafeVarargsAnnotationAndMakeMethodFinalWhenNeeded(Parameter[] methodParameters,
                                                                           MethodSpec.Builder methodBuilder) {
    if (isLastParameterAnArray(methodParameters)
        && isLastParameterHasNoWildcardType(methodParameters[methodParameters.length - 1])) {
      methodBuilder.addAnnotation(SafeVarargs.class);
      methodBuilder.addModifiers(Modifier.FINAL);
    }
  }

  // TODo seems to work but could that be simplified
  private static boolean isLastParameterHasNoWildcardType(Parameter parameter) {
    // for parameter like Consumer<? super ACTUAL>... assertions but not Class<?>... types
    return parameter.getParameterizedType() instanceof GenericArrayType genericArrayType
           && genericArrayType.getGenericComponentType() instanceof ParameterizedType parameterizedType
           && stream(parameterizedType.getActualTypeArguments()).anyMatch(t -> !t.getTypeName().equals("?"));
  }

  private static MethodSpec.Builder generateNavigationMethod(Method navigationMethod, FieldSpec assertField) {
    if (isOptionalGet(navigationMethod)) {
      return generateOptionalGetNavigationMethod(navigationMethod, assertField);
    } else if (isAsString(navigationMethod)) {
      return generateAsStringNavigationMethod(navigationMethod);
    } else if (isStronglyTypedOptionalGet(navigationMethod)) {
      return generateStronglyTypedOptionalGetNavigationMethod(navigationMethod);
    } else if (isAsInstanceOf(navigationMethod)) {
      return generateAsInstanceOfNavigationMethod(navigationMethod);
    } else if (isOptionalMap(navigationMethod)) {
      return generateOptionalMapNavigationMethod(navigationMethod, assertField);
    } else if (isOptionalFlatMap(navigationMethod)) {
      return generateOptionalFlatMapNavigationMethod(navigationMethod, assertField);
    } else if (isObjectExtractingWithString(navigationMethod)) {
      return generateObjectExtractingWithSingleStringNavigationMethod(navigationMethod);
    } else if (isObjectExtractingWithMultipleStrings(navigationMethod)) {
      return generateObjectExtractingWithMultipleStringsNavigationMethod(navigationMethod);
    } else if (isStronglyTypedObjectExtractingWithString(navigationMethod)) {
      return generateStronglyTypedObjectExtractingWithSingleStringNavigationMethod(navigationMethod);
    } else if (isObjectExtractingWithFunction(navigationMethod)) {
      return generateObjectExtractingWithSingleFunctionNavigationMethod(navigationMethod);
    } else if (isObjectExtractingWithMultipleFunctions(navigationMethod)) {
      return generateObjectExtractingWithMultipleFunctionsNavigationMethod(navigationMethod);
    } else if (isStronglyTypedObjectExtractingWithFunction(navigationMethod)) {
      return generateStronglyTypedObjectExtractingWithSingleFunctionNavigationMethod(navigationMethod);
    }
    Type genericReturnType = navigationMethod.getGenericReturnType();
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariables(getMethodTypeVariables(navigationMethod))
                                               .returns(genericReturnType)
                                               .addStatement("return $N." + navigationMethod.getName()
                                                             + methodArguments(navigationMethod),
                                                             assertField);

    Parameter[] methodParameters = navigationMethod.getParameters();
    for (Parameter parameter : methodParameters) {
      softAssertionMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    return softAssertionMethodBuilder;
  }

  private static MethodSpec.Builder generateAsStringNavigationMethod(Method navigationMethod) {
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .returns(TypeName.get(SoftStringAssert.class))
                     .addStatement("return new SoftStringAssert(actual().toString(), errorCollector)");
  }

  private static MethodSpec.Builder generateOptionalGetNavigationMethod(Method navigationMethod, FieldSpec assertField) {
    var softObjectAssertType = ParameterizedTypeName.get(ClassName.get("", SoftObjectAssert.class.getSimpleName()),
                                                         TypeVariableName.get("VALUE"));
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .returns(softObjectAssertType)
                     .addStatement("return new SoftObjectAssert<>($N.get().actual(), errorCollector)", assertField);
  }

  private static MethodSpec.Builder generateOptionalMapNavigationMethod(Method navigationMethod, FieldSpec assertField) {
    // <U> SoftOptionalAssert<U>
    var softOptionalAssertType = ParameterizedTypeName.get(ClassName.get("", SoftOptionalAssert.class.getSimpleName()),
                                                           TypeVariableName.get("U"));
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariables(getMethodTypeVariables(navigationMethod))
                                               .returns(softOptionalAssertType)
                                               .addStatement("return new SoftOptionalAssert($N.map(mapper).actual(), errorCollector)",
                                                             assertField);
    for (Parameter parameter : navigationMethod.getParameters()) {
      softAssertionMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    return softAssertionMethodBuilder;
  }

  private static MethodSpec.Builder generateOptionalFlatMapNavigationMethod(Method navigationMethod, FieldSpec assertField) {
    // <U> SoftOptionalAssert<U>
    var softOptionalAssertType = ParameterizedTypeName.get(ClassName.get("", SoftOptionalAssert.class.getSimpleName()),
                                                           TypeVariableName.get("U"));
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariables(getMethodTypeVariables(navigationMethod))
                                               .returns(softOptionalAssertType)
                                               .addStatement("return new SoftOptionalAssert<>($N.flatMap(mapper).actual(), errorCollector)",
                                                             assertField);
    for (Parameter parameter : navigationMethod.getParameters()) {
      softAssertionMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    return softAssertionMethodBuilder;
  }

  private static MethodSpec.Builder generateStronglyTypedOptionalGetNavigationMethod(Method navigationMethod) {
    return generateStronglyTypedNavigationMethod(navigationMethod,
                                                 "return softAssertFactory.createSoftAssert(actual().get(), errorCollector)");
  }

  private static MethodSpec.Builder generateAsInstanceOfNavigationMethod(Method navigationMethod) {
    return generateStronglyTypedNavigationMethod(navigationMethod,
                                                 "return softAssertFactory.createSoftAssert(actual(), errorCollector)");
  }

  private static MethodSpec.Builder generateStronglyTypedNavigationMethod(Method navigationMethod, String returnStatement) {
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .addTypeVariables(List.of(BOUNDED_SOFT_ASSERT))
                     .returns(BOUNDED_SOFT_ASSERT)
                     .addStatement(returnStatement)
                     .addParameter(DEFAULT_SOFT_ASSERT_FACTORY_PARAMETERIZED_TYPE, "softAssertFactory");
  }

  private static MethodSpec.Builder generateObjectExtractingWithSingleStringNavigationMethod(Method navigationMethod) {
    Parameter stringParameter = navigationMethod.getParameters()[0];

    var softObjectAssertType = ParameterizedTypeName.get(ClassName.get("", SoftObjectAssert.class.getSimpleName()),
                                                         WILDCARD_TYPE);
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .returns(softObjectAssertType)
                     .addParameter(stringParameter.getParameterizedType(), stringParameter.getName())
                     .addStatement("return new SoftObjectAssert<>(objectAssert.extracting(propertyOrField).actual(), errorCollector)");
  }

  private static MethodSpec.Builder generateObjectExtractingWithMultipleStringsNavigationMethod(Method navigationMethod) {
    Parameter stringVarargsParameter = navigationMethod.getParameters()[0];

    var softListAssertType = ParameterizedTypeName.get(ClassName.get("", SoftListAssert.class.getSimpleName()), OBJECT_TYPE);
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .returns(softListAssertType)
                     .addParameter(stringVarargsParameter.getParameterizedType(), stringVarargsParameter.getName())
                     .addStatement("return new SoftListAssert(objectAssert.extracting(propertiesOrFields).actual(), errorCollector)");
  }

  private static MethodSpec.Builder generateObjectExtractingWithMultipleFunctionsNavigationMethod(Method navigationMethod) {
    Parameter functionVarargsParameter = navigationMethod.getParameters()[0];

    var softListAssertType = ParameterizedTypeName.get(ClassName.get("", SoftListAssert.class.getSimpleName()), OBJECT_TYPE);
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .returns(softListAssertType)
                     .addParameter(functionVarargsParameter.getParameterizedType(), functionVarargsParameter.getName())
                     .addStatement("return new SoftListAssert(objectAssert.extracting(extractors).actual(), errorCollector)");
  }

  private static MethodSpec.Builder generateStronglyTypedObjectExtractingWithSingleStringNavigationMethod(Method navigationMethod) {
    Parameter stringParameter = navigationMethod.getParameters()[0];
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .addTypeVariable(BOUNDED_SOFT_ASSERT)
                     .returns(BOUNDED_SOFT_ASSERT)
                     .addStatement("return extracting(propertyOrField).asInstanceOf(softAssertFactory)")
                     .addParameter(stringParameter.getParameterizedType(), stringParameter.getName())
                     .addParameter(DEFAULT_SOFT_ASSERT_FACTORY_PARAMETERIZED_TYPE, "softAssertFactory");
  }

  private static MethodSpec.Builder generateObjectExtractingWithSingleFunctionNavigationMethod(Method navigationMethod) {
    TypeVariableName genericT = TypeVariableName.get("T");
    var softObjectAssertType = ParameterizedTypeName.get(ClassName.get("", SoftObjectAssert.class.getSimpleName()), genericT);
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariable(genericT)
                                               .returns(softObjectAssertType)
                                               .addStatement("return new SoftObjectAssert<>(extractor.apply(actual()), errorCollector)");
    for (Parameter parameter : navigationMethod.getParameters()) {
      softAssertionMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    return softAssertionMethodBuilder;
  }

  private static MethodSpec.Builder generateStronglyTypedObjectExtractingWithSingleFunctionNavigationMethod(Method navigationMethod) {
    Parameter functionParameter = navigationMethod.getParameters()[0];
    return MethodSpec.methodBuilder(navigationMethod.getName())
                     .addModifiers(Modifier.PUBLIC)
                     .addTypeVariables(List.of(TypeVariableName.get("T"), BOUNDED_SOFT_ASSERT))
                     .returns(BOUNDED_SOFT_ASSERT)
                     .addStatement("return extracting(extractor).asInstanceOf(softAssertFactory)")
                     .addParameter(functionParameter.getParameterizedType(), functionParameter.getName())
                     .addParameter(DEFAULT_SOFT_ASSERT_FACTORY_PARAMETERIZED_TYPE, "softAssertFactory");
  }

  private static boolean isOptionalGet(Method navigationMethod) {
    return navigationMethod.getDeclaringClass().equals(AbstractOptionalAssert.class)
           && navigationMethod.toString().contains("get()");
  }

  private static boolean isAsString(Method navigationMethod) {
    return navigationMethod.toString().contains("asString()");
  }

  private static boolean isStronglyTypedOptionalGet(Method navigationMethod) {
    return navigationMethod.getDeclaringClass().equals(AbstractOptionalAssert.class)
           && navigationMethod.toString().contains("get(")
           && navigationMethod.getParameterCount() == 1;
  }

  private static boolean isAsInstanceOf(Method navigationMethod) {
    return navigationMethod.getDeclaringClass().equals(AbstractAssert.class)
           && navigationMethod.getName().contains("asInstanceOf");
  }

  private static boolean isObjectExtractingWithString(Method navigationMethod) {
    List<Class<?>> parameterTypes = Arrays.asList(navigationMethod.getParameterTypes());
    return navigationMethod.getDeclaringClass().equals(AbstractObjectAssert.class)
           && navigationMethod.getName().contains("extracting")
           && parameterTypes.size() == 1
           && parameterTypes.contains(String.class);
  }

  private static boolean isObjectExtractingWithMultipleStrings(Method navigationMethod) {
    List<Class<?>> parameterTypes = Arrays.asList(navigationMethod.getParameterTypes());
    return navigationMethod.getDeclaringClass().equals(AbstractObjectAssert.class)
           && navigationMethod.getName().contains("extracting")
           && parameterTypes.size() == 1
           && parameterTypes.contains(String[].class);
  }

  private static boolean isStronglyTypedObjectExtractingWithString(Method navigationMethod) {
    List<Class<?>> parameterTypes = Arrays.asList(navigationMethod.getParameterTypes());
    return navigationMethod.getDeclaringClass().equals(AbstractObjectAssert.class)
           && navigationMethod.getName().contains("extracting")
           && parameterTypes.size() == 2
           && parameterTypes.contains(String.class)
           && parameterTypes.contains(InstanceOfAssertFactory.class);
  }

  private static boolean isObjectExtractingWithFunction(Method navigationMethod) {
    List<Class<?>> parameterTypes = Arrays.asList(navigationMethod.getParameterTypes());
    return navigationMethod.getDeclaringClass().equals(AbstractObjectAssert.class)
           && navigationMethod.getName().contains("extracting")
           && parameterTypes.size() == 1
           && parameterTypes.contains(Function.class);
  }

  private static boolean isObjectExtractingWithMultipleFunctions(Method navigationMethod) {
    List<Class<?>> parameterTypes = Arrays.asList(navigationMethod.getParameterTypes());
    return navigationMethod.getDeclaringClass().equals(AbstractObjectAssert.class)
           && navigationMethod.getName().contains("extracting")
           && parameterTypes.size() == 1
           && parameterTypes.contains(Function[].class);
  }

  private static boolean isStronglyTypedObjectExtractingWithFunction(Method navigationMethod) {
    List<Class<?>> parameterTypes = Arrays.asList(navigationMethod.getParameterTypes());
    return navigationMethod.getDeclaringClass().equals(AbstractObjectAssert.class)
           && navigationMethod.getName().contains("extracting")
           && parameterTypes.size() == 2
           && parameterTypes.contains(Function.class)
           && parameterTypes.contains(InstanceOfAssertFactory.class);
  }

  private static boolean isOptionalFlatMap(Method navigationMethod) {
    return navigationMethod.getDeclaringClass().equals(AbstractOptionalAssert.class)
           && navigationMethod.toString().contains("flatMap");
  }

  private static boolean isOptionalMap(Method navigationMethod) {
    return navigationMethod.getDeclaringClass().equals(AbstractOptionalAssert.class)
           && navigationMethod.toString().contains("map");
  }

  private static @NonNull List<TypeVariableName> getMethodTypeVariables(Method navigationMethod) {
    return stream(navigationMethod.getTypeParameters())
                                                       .map(tp -> TypeVariableName.get(tp.getName(), tp.getBounds())).toList();
  }

  private static MethodSpec.Builder generateDelegateMethod(Method objectMethod, FieldSpec assertField) {

    var softAssertionObjectMethodBuilder = MethodSpec.methodBuilder(objectMethod.getName())
                                                     .addModifiers(Modifier.PUBLIC)
                                                     .returns(objectMethod.getGenericReturnType())
                                                     .addStatement("return $N." + objectMethod.getName()
                                                                   + methodArguments(objectMethod), assertField);
    Parameter[] methodParameters = objectMethod.getParameters();
    for (Parameter parameter : methodParameters) {
      softAssertionObjectMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    for (Annotation annotation : objectMethod.getDeclaredAnnotations()) {
      softAssertionObjectMethodBuilder.addAnnotation(AnnotationSpec.get(annotation));
    }
    return softAssertionObjectMethodBuilder;
  }

  private static MethodSpec.Builder generateDelegateMethodReturningThis(Method methodToCall, ParameterizedTypeName softAssertType,
                                                                        FieldSpec assertField, Type realActualType) {
    var delegateMethodBuilder = MethodSpec.methodBuilder(methodToCall.getName())
                                          .addModifiers(Modifier.PUBLIC)
                                          .returns(softAssertType)
                                          .addStatement("$N." + methodToCall.getName() + methodArguments(methodToCall),
                                                        assertField)
                                          .addStatement("return this");
    Parameter[] methodToCallParameters = methodToCall.getParameters();
    for (Parameter parameter : methodToCallParameters) {
      addParameter(realActualType, parameter, delegateMethodBuilder);
    }
    return delegateMethodBuilder;
  }

  private static boolean isLastParameterAnArray(Parameter[] parameters) {
    return parameters.length > 0 && parameters[parameters.length - 1].getType().isArray();
  }

  private static void addParameter(Type realActualType, Parameter parameter, MethodSpec.Builder methodBuilder) {
    var parameterSpec = resolveRealParameterSpec(parameter, parameter.getParameterizedType(), realActualType);
    parameterSpec.toBuilder().addModifiers(Modifier.PUBLIC);
    methodBuilder.addParameter(parameterSpec);
  }

  private static @NonNull ParameterSpec resolveRealParameterSpec(Parameter parameter, Type parameterType, Type realActualType) {
    if (parameterType instanceof ParameterizedType parameterizedType && referencesACTUALGenericType(parameterizedType)) {
      // some method parameter generic type references ACTUAL, ex: BiPredicate<? super ACTUAL, ? super ACTUAL>
      // -> we need to transform it to the real ACTUAL type, ex: BiPredicate<? super String, ? super String>
      Type[] realGenericTypes = resolveRealGenericTypes(realActualType, parameterizedType.getActualTypeArguments());
      // rebuild the parameter with the real generic types instead of ACTUAL
      var realParameterType = ParameterizedTypeName.get(parameter.getType(), realGenericTypes);
      return ParameterSpec.builder(realParameterType, parameter.getName()).build();
    } else if (parameterType instanceof GenericArrayType genericArrayType
               && genericArrayType.getGenericComponentType() instanceof ParameterizedType parameterizedType
               && parameterizedType.getRawType() instanceof Class<?> rawClass
               && referencesACTUALGenericType(parameterizedType)) {
                 // the array generic type references ACTUAL, ex: Consumer<? super ACTUAL>... requirements, we need to transform
                 // it
                 // to the real ACTUAL type, ex: Consumer<? super String>... requirements
                 Type[] realGenericTypes = resolveRealGenericTypes(realActualType, parameterizedType.getActualTypeArguments());
                 TypeName[] realGenericTypesNames = stream(realGenericTypes).map(TypeName::get).toArray(TypeName[]::new);
                 var realParameterType = ParameterizedTypeName.get(ClassName.get(rawClass), realGenericTypesNames);
                 return ParameterSpec.builder(ArrayTypeName.of(realParameterType), parameter.getName()).build();
               }
    return ParameterSpec.builder(parameterType, parameter.getName()).build();
  }

  private static boolean referencesACTUALGenericType(ParameterizedType parameterizedType) {
    return stream(parameterizedType.getActualTypeArguments()).anyMatch(arg -> arg.getTypeName().contains("ACTUAL"));
  }

  private static Type[] resolveRealGenericTypes(Type actualType, Type... types) {
    return stream(types)
                        .map(t -> {
                          if (t instanceof WildcardType wildcardType) {
                            return wildcardType().withLowerBounds(actualType)
                                                 .withUpperBounds(wildcardType.getUpperBounds())
                                                 .build();
                          } else if (t instanceof ParameterizedType parameterizedType
                                     && parameterizedType.getActualTypeArguments()[0] instanceof WildcardType wildcardType) {
                                       // generic type array has only one parameter
                                       return wildcardType().withLowerBounds(actualType)
                                                            .withUpperBounds(wildcardType.getUpperBounds())
                                                            .build();
                                     } else {
                                       return t;
                                     }
                        })
                        .toArray(Type[]::new);
  }

  private static boolean isAssertionMethod(Method method) {
    return !isNonAssertionMethod(method) && !isNavigationMethod(method) && !isMethodToDelegateTo(method);
  }

  private static boolean isNonAssertionMethod(Method method) {
    return METHODS_NOT_THROWING_ASSERTION_ERRORS.contains(method.getName());
  }

  private static boolean isNavigationMethod(Method method) {
    return NAVIGATION_METHODS.contains(method.getName());
  }

  private static @NonNull String methodArguments(Method method) {
    StringJoiner argsStringJoiner = new StringJoiner(",", "(", ")");
    for (Parameter parameter : method.getParameters()) {
      argsStringJoiner.add(parameter.getName());
    }
    return argsStringJoiner.toString();
  }

}
