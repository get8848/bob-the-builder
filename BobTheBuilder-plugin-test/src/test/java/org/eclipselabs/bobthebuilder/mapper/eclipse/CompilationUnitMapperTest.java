package org.eclipselabs.bobthebuilder.mapper.eclipse;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipselabs.bobthebuilder.model.ImportStatement;
import org.eclipselabs.bobthebuilder.model.JavaClassFile;
import org.eclipselabs.bobthebuilder.model.MainType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

public class CompilationUnitMapperTest {

  @Mock
  private MainTypeMapper mainTypeMapper;

  @Mock
  private MainTypeSelector mainTypeSelector;

  @Mock
  private ImportStatementMapper importStatementMapper;

  private CompilationUnitMapper compilationUnitMapper;

  @Mock
  private ICompilationUnit compilationUnit;

  @Mock
  private IType type;

  @Mock
  private MainType mainType;

  private String mainTypeName = "main type";

  private Set<ImportStatement> imports;

  private ImportStatement importStatement;
  
  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    compilationUnitMapper = new CompilationUnitMapper(mainTypeMapper, mainTypeSelector,
        importStatementMapper);
    Mockito.when(mainTypeSelector.map(compilationUnit)).thenReturn(type);
    Mockito.when(mainTypeMapper.map(type, compilationUnit)).thenReturn(mainType);
    Mockito.when(type.getElementName()).thenReturn(mainTypeName);
    importStatement = new ImportStatement("import name");
    imports = Sets.newHashSet(importStatement);
    Mockito.when(importStatementMapper.map(compilationUnit)).thenReturn(imports);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullCompilationUnit() throws JavaModelException {
    compilationUnitMapper.map(null);
  }

  @Test
  public void testMapCompilationUnit() throws JavaModelException {
    JavaClassFile actual = compilationUnitMapper.map(compilationUnit);
    assertEquals(mainType, actual.getMainType());
    assertEquals(mainTypeName, actual.getName());
    assertEquals(Sets.newHashSet(importStatement), actual.getImports());
  }

}