package org.assertj.core.generator;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.lang3.reflect.TypeUtils.wildcardType;

import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.TreeMap;
import java.util.stream.Stream;
import javax.lang.model.element.Modifier;

import org.apache.commons.lang3.ArrayUtils;
import org.assertj.core.annotation.Beta;
import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assert;
import org.assertj.core.api.AssertionErrorCollector;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ObjectAssert;
import org.assertj.core.api.OptionalAssert;
import org.assertj.core.api.soft.DefaultSoftAssertFactory;
import org.assertj.core.api.soft.SoftAssert;
import org.assertj.core.api.soft.SoftObjectAssert;
import org.assertj.core.api.soft.SoftOptionalAssert;
import org.jspecify.annotations.NonNull;
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

  public static void main(String[] args) {

    // TODO: generate assertions with several parameterized types: map assertions
    // TODO: generate GeneratedSoftAssertions ?
    // TODO: methods to ignore ?
    // TODO: format code
    // TODO: soft assert factories
    Stream.of(ObjectAssert.class).forEach(SoftAssertionsGenerator::generateSoftAssertionFor);
    Stream.of(OptionalAssert.class).forEach(SoftAssertionsGenerator::generateSoftAssertionFor);
  }

  private static void generateSoftAssertionFor(Class<? extends Assert> assertClass) {
    var methods = getNonDuplicatedInstanceMethods(assertClass);

    var assertClassTypeVariables = stream(assertClass.getTypeParameters()).map(TypeVariableName::get).toArray(TypeVariableName[]::new);
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
                                                     .addAnnotation(Beta.class);


    var softAssertType = ParameterizedTypeName.get(ClassName.get("", softAssertClassName), assertClassTypeVariables);


    for (Method method : methods) {
      if (method.getName().equals("actual") && method.getDeclaringClass().equals(AbstractAssert.class)) {
        generateActualMethod(method, realActualType, assertField, softAssertTypeBuilder);
      } else if (isMethodToDelegateTo(method)) {
        softAssertTypeBuilder.addMethod(generateDelegateMethod(method, assertField));
      } else if (isNonAssertionMethod(method)) {
        softAssertTypeBuilder.addMethod(generateDelegateMethodReturningThis(method, softAssertType, assertField, realActualType));
      } else if (isNavigationMethod(method)) {
        softAssertTypeBuilder.addMethod(generateNavigationMethod(method, assertField, assertClass));
      } else if (isAssertionMethod(method)) {
        softAssertTypeBuilder.addMethod(generateAssertionMethod(method, softAssertType, assertField, realActualType));
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

  private static void generateActualMethod(Method method, Type realActualType, FieldSpec assertField, TypeSpec.Builder softAssertTypeBuilder) {
    var softAssertionObjectMethodBuilder = MethodSpec.methodBuilder(method.getName())
                                                     .addModifiers(Modifier.PUBLIC)
                                                     .returns(realActualType)
                                                     .addStatement("return $N.actual()", assertField);
    softAssertTypeBuilder.addMethod(softAssertionObjectMethodBuilder.build());
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
                                               .collect(groupingBy(method -> method.toString().replaceAll(".*(" + method.getName() + ".*)", "$1"))));
    return methodsByMethodWithParametersName.values().stream().map(sameMethods -> sameMethods.get(0)).toList();
  }

  private static boolean isNotStatic(Method m) {
    return (m.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0;
  }

  private static @NonNull FieldSpec generateAssertField(Class<? extends Assert> assertClass, TypeVariableName[] typeVariableNames) {
    var assertClassSimpleName = assertClass.getSimpleName();
    String assertFieldName = assertClassSimpleName.toLowerCase().charAt(0) + assertClassSimpleName.substring(1);
    var parameterizedTypeName = ParameterizedTypeName.get(ClassName.get("", assertClassSimpleName), typeVariableNames);
    return FieldSpec.builder(parameterizedTypeName, assertFieldName, Modifier.PRIVATE, Modifier.FINAL).build();
  }

  private static @NonNull FieldSpec generateErrorCollectorField() {
    return FieldSpec.builder(AssertionErrorCollector.class, "errorCollector", Modifier.PRIVATE, Modifier.FINAL).build();
  }

  private static @NonNull MethodSpec generateConstructor(ParameterSpec actualParameter, FieldSpec assertField) {
    return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC)
                     .addParameter(actualParameter)
                     .addParameter(AssertionErrorCollector.class, "errorCollector")
                     .addStatement("this.$N = $N", "errorCollector", "errorCollector")
                     .addStatement("this.$N = assertThat(actual)", assertField)
                     .build();
  }

  private static @NonNull MethodSpec generateAssertionMethod(Method assertionMethod, ParameterizedTypeName softAssertType, FieldSpec assertField, Type actualType) {
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

    for (Parameter parameter : assertionMethod.getParameters()) {
      addParameter(actualType, parameter, softAssertionMethodBuilder);
    }
    return softAssertionMethodBuilder.build();
  }

  private static @NonNull MethodSpec generateNavigationMethod(Method navigationMethod, FieldSpec assertField, Class<? extends Assert> assertClass) {
    if (isOptionalGet(navigationMethod, assertClass)) {
      return generateOptionalGetNavigationMethod(navigationMethod, assertField);
    } else if (isStronglyTypedOptionalGet(navigationMethod, assertClass)) {
      return generateStronglyTypedOptionalGetNavigationMethod(navigationMethod, assertField);
    } else if (isOptionalFlatMap(navigationMethod, assertClass)) {
      return generateOptionalFlatMapNavigationMethod(navigationMethod, assertField);
    }
    Type genericReturnType = navigationMethod.getGenericReturnType();
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariables(getMethodTypeVariables(navigationMethod))
                                               .returns(genericReturnType)
                                               .addStatement("return $N." + navigationMethod.getName()
                                                               + methodArguments(navigationMethod),
                                                             assertField);

    for (Parameter parameter : navigationMethod.getParameters()) {
      softAssertionMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    return softAssertionMethodBuilder.build();
  }

  private static @NonNull MethodSpec generateOptionalGetNavigationMethod(Method navigationMethod, FieldSpec assertField) {
    var softObjectAssertType = ParameterizedTypeName.get(ClassName.get("", SoftObjectAssert.class.getSimpleName()), TypeVariableName.get("VALUE"));
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .returns(softObjectAssertType)
                                               .addStatement("return new SoftObjectAssert<>($N.get().actual(), errorCollector)", assertField);
    return softAssertionMethodBuilder.build();
  }

  private static @NonNull MethodSpec generateOptionalFlatMapNavigationMethod(Method navigationMethod, FieldSpec assertField) {
    // <U> SoftOptionalAssert<U>
    var softOptionalAssertType = ParameterizedTypeName.get(ClassName.get("", SoftOptionalAssert.class.getSimpleName()), TypeVariableName.get("U"));
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariables(getMethodTypeVariables(navigationMethod))
                                               .returns(softOptionalAssertType)
                                               .addStatement("return new SoftOptionalAssert<>($N.flatMap(mapper).actual(), errorCollector)", assertField);
    for (Parameter parameter : navigationMethod.getParameters()) {
      softAssertionMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    return softAssertionMethodBuilder.build();
  }

  private static @NonNull MethodSpec generateStronglyTypedOptionalGetNavigationMethod(Method navigationMethod, FieldSpec assertField) {
    // <SOFT_ASSERT extends SoftAssert> SOFT_ASSERT
    var softAssertType = TypeVariableName.get("SOFT_ASSERT", TypeName.get(SoftAssert.class));
    var softAssertionMethodBuilder = MethodSpec.methodBuilder(navigationMethod.getName())
                                               .addModifiers(Modifier.PUBLIC)
                                               .addTypeVariables(List.of(softAssertType))
                                               .returns(softAssertType)
                                               .addStatement("return softAssertFactory.createSoftAssert(actual().get(), errorCollector)");
    // DefaultSoftAssertFactory<?, SOFT_ASSERT>
    var parameterizedType = ParameterizedTypeName.get(ClassName.get("", DefaultSoftAssertFactory.class.getSimpleName()), TypeVariableName.get("?"), TypeVariableName.get("SOFT_ASSERT"));
    softAssertionMethodBuilder.addParameter(parameterizedType, "softAssertFactory");
    return softAssertionMethodBuilder.build();
  }

  private static boolean isOptionalGet(Method navigationMethod, Class<? extends Assert> assertClass) {
    return assertClass.equals(OptionalAssert.class) && navigationMethod.toString().contains("get()");
  }

  private static boolean isStronglyTypedOptionalGet(Method navigationMethod, Class<? extends Assert> assertClass) {
    return assertClass.equals(OptionalAssert.class)
      && navigationMethod.toString().contains("get(")
      && navigationMethod.getParameterCount() == 1;
  }

  private static boolean isOptionalFlatMap(Method navigationMethod, Class<? extends Assert> assertClass) {
    return assertClass.equals(OptionalAssert.class) && navigationMethod.toString().contains("flatMap");
  }

  private static @NonNull List<TypeVariableName> getMethodTypeVariables(Method navigationMethod) {
    return stream(navigationMethod.getTypeParameters())
      .map(tp -> TypeVariableName.get(tp.getName(), tp.getBounds())).toList();
  }

  private static @NonNull MethodSpec generateDelegateMethod(Method objectMethod, FieldSpec assertField) {
    var softAssertionObjectMethodBuilder = MethodSpec.methodBuilder(objectMethod.getName())
                                                     .addModifiers(Modifier.PUBLIC)
                                                     .returns(objectMethod.getGenericReturnType())
                                                     .addStatement("return $N." + objectMethod.getName()
                                                                     + methodArguments(objectMethod), assertField);
    for (Parameter parameter : objectMethod.getParameters()) {
      softAssertionObjectMethodBuilder.addParameter(parameter.getParameterizedType(), parameter.getName());
    }
    return softAssertionObjectMethodBuilder.build();
  }

  private static MethodSpec generateDelegateMethodReturningThis(Method methodToCall, ParameterizedTypeName softAssertType, FieldSpec assertField, Type realActualType) {
    var delegateMethodBuilder = MethodSpec.methodBuilder(methodToCall.getName())
                                          .addModifiers(Modifier.PUBLIC)
                                          .returns(softAssertType)
                                          .addStatement("$N." + methodToCall.getName() + methodArguments(methodToCall),
                                                        assertField)
                                          .addStatement("return this");
    for (Parameter parameter : methodToCall.getParameters()) {
      addParameter(realActualType, parameter, delegateMethodBuilder);
    }
    return delegateMethodBuilder.build();
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
      // the array generic type references ACTUAL, ex: Consumer<? super ACTUAL>... requirements, we need to transform it
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
        } else if (t instanceof ParameterizedType parameterizedType && parameterizedType.getActualTypeArguments()[0] instanceof WildcardType wildcardType) {
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
