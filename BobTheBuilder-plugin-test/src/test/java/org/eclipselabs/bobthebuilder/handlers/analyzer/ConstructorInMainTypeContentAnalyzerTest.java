package org.eclipselabs.bobthebuilder.handlers.analyzer;

import java.util.Set;

import org.eclipse.jdt.core.IField;
import org.eclipselabs.bobthebuilder.handlers.analyzer.AnalyzerResult.ForMethod;

/**
 * To test {@link MethodContentAnalyzer.ConstructorWithBuilderInMainType} and
 * {@link FieldPredicate.FieldAssignment}
 */
public class ConstructorInMainTypeContentAnalyzerTest extends MethodContentAnalyzerTest {

  @Override
  protected MethodContentAnalyzer getMethodContentAnalyzer(Set<IField> fields,
    ForMethod analyzedMethodResult) {
    return new MethodContentAnalyzer.ConstructorWithBuilderInMainType(fields, analyzedMethodResult);
  }

  @Override
  protected String getSourceToMakeAllFieldsPassPredicate(Set<IField> fields) {
    StringBuilder resultBuilder = new StringBuilder();
    for (IField each : fields) {
      resultBuilder.append(FieldPredicateTest.createAssignment(each.getElementName()));
    }
    return resultBuilder.toString();
  }

  @Override
  protected String getSourceToMakeAllFieldsFailPredicate(Set<IField> fields) {
    StringBuilder resultBuilder = new StringBuilder();
    for (IField each : fields) {
      resultBuilder.append(each.getElementName().substring(0, 3));
    }
    return resultBuilder.toString();
  }

}