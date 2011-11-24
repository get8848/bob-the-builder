package org.eclipselabs.bobthebuilder.handlers.analyzer;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.handlers.ValidationFramework;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class ValidationFrameworkAnalyzerTest {

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IMethod validateMethod;

  private AnalyzerResult.ForMethod analyzedValidateMethod;

  private Collection<ValidationFramework> expected;

  private Collection<ValidationFramework> actual;

  @Mock
  private IImportDeclaration commonsLang2Import;

  @Mock
  private IImportDeclaration commonsLang3Import;

  @Mock
  private IImportDeclaration anotherImport;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    analyzedValidateMethod = AnalyzerResult.ForMethod.getPresentInstance(validateMethod);
    Mockito.when(commonsLang2Import.getElementName()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getFullClassName());
    Mockito.when(commonsLang3Import.getElementName()).thenReturn(
      ValidationFramework.COMMONS_LANG3.getFullClassName());
    Mockito.when(anotherImport.getElementName()).thenReturn(
      "org.eclipselabs.bobthebuilder.rocks");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() {
    new ValidationFrameworkAnalyzer(analyzedValidateMethod, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullAnalyzedResult() {
    new ValidationFrameworkAnalyzer(null, compilationUnit);
  }

  @Test
  public void testAnalyzedResultNotPresent() throws JavaModelException {
    analyzedValidateMethod = AnalyzerResult.ForMethod.NOT_PRESENT;
    Collection<ValidationFramework> actual = new ValidationFrameworkAnalyzer(
        analyzedValidateMethod, compilationUnit).analyze();
    assertEquals(ValidationFramework.values().length, actual.size());
  }

  @Test
  public void testGuavaPresent() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.GOOGLE_GUAVA.getCheckArgument());
    actual = new ValidationFrameworkAnalyzer(
        analyzedValidateMethod, compilationUnit).analyze();
    expected = Arrays.asList(ValidationFramework.GOOGLE_GUAVA);
    assertEquals(expected, actual);
  }

  @Test
  public void testCommonsLang2Present() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getCheckArgument());
    Mockito.when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { commonsLang2Import });
    actual = new ValidationFrameworkAnalyzer(
        analyzedValidateMethod, compilationUnit).analyze();
    expected = Arrays.asList(ValidationFramework.COMMONS_LANG2);
    assertEquals(expected, actual);
  }

  @Test
  public void testCommonsLang3Present() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG3.getCheckArgument());
    Mockito.when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { commonsLang3Import });
    actual = new ValidationFrameworkAnalyzer(
        analyzedValidateMethod, compilationUnit).analyze();
    expected = Arrays.asList(ValidationFramework.COMMONS_LANG3);
    assertEquals(expected, actual);
  }

  @Test
  public void testDefaultCommonsLang2() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn(
      ValidationFramework.COMMONS_LANG2.getCheckArgument());
    Mockito.when(compilationUnit.getImports()).thenReturn(
      new IImportDeclaration[] { anotherImport });
    actual = new ValidationFrameworkAnalyzer(
        analyzedValidateMethod, compilationUnit).analyze();
    expected = Arrays.asList(ValidationFramework.COMMONS_LANG2);
    assertEquals(expected, actual);
  }

  @Test
  public void testValidatePresentButNoFrameworkUsed() throws JavaModelException {
    Mockito.when(validateMethod.getSource()).thenReturn("whatever");
    actual = new ValidationFrameworkAnalyzer(
        analyzedValidateMethod, compilationUnit).analyze();
    assertEquals(ValidationFramework.values().length, actual.size());
  }
}
