package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

public class QuantityMeasurementAppTest {
	private static final double EPSILON = 0.0001;
	@Test
    void testEquality_LitreToLitre_SameValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToLitre_DifferentValue() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_LitreToMillilitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_MillilitreToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.LITRE)));
    }
    
    @Test
    void testEquality_LitreToGallon_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(0.264172, VolumeUnit.GALLON)));
    }

    @Test
    void testEquality_GallonToLitre_EquivalentValue() {
        assertTrue(new Quantity<>(1.0, VolumeUnit.GALLON)
                .equals(new Quantity<>(3.78541, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_VolumeVsLength_Incompatible() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    @Test
    void testEquality_NullComparison() {
        assertFalse(new Quantity<>(1.0, VolumeUnit.LITRE).equals(null));
    }

    @Test
    void testEquality_SameReference() {
        Quantity<VolumeUnit> q = new Quantity<>(5.0, VolumeUnit.LITRE);
        assertTrue(q.equals(q));
    }

    @Test
    void testEquality_NullUnit() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(1.0, null));
    }

    @Test
    void testEquality_TransitiveProperty() {
        Quantity<VolumeUnit> a = new Quantity<>(1.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> b = new Quantity<>(1000.0, VolumeUnit.MILLILITRE);
        Quantity<VolumeUnit> c = new Quantity<>(1.0, VolumeUnit.LITRE);

        assertTrue(a.equals(b));
        assertTrue(b.equals(c));
        assertTrue(a.equals(c));
    }

    @Test
    void testEquality_ZeroValue() {
        assertTrue(new Quantity<>(0.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(0.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_NegativeVolume() {
        assertTrue(new Quantity<>(-1.0, VolumeUnit.LITRE)
                .equals(new Quantity<>(-1000.0, VolumeUnit.MILLILITRE)));
    }

    @Test
    void testEquality_LargeVolumeValue() {
        assertTrue(new Quantity<>(1000000.0, VolumeUnit.MILLILITRE)
                .equals(new Quantity<>(1000.0, VolumeUnit.LITRE)));
    }

    @Test
    void testEquality_SmallVolumeValue() {
        assertTrue(new Quantity<>(0.001, VolumeUnit.LITRE)
                .equals(new Quantity<>(1.0, VolumeUnit.MILLILITRE)));
    }
    
    @Test
    void testAddition_SameUnit_MillilitrePlusMillilitre() {
        assertEquals(1000.0,
                new Quantity<>(500.0, VolumeUnit.MILLILITRE)
                        .add(new Quantity<>(500.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_LitrePlusMillilitre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_MillilitrePlusLitre() {
        assertEquals(2000.0,
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .add(new Quantity<>(1.0, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_CrossUnit_GallonPlusLitre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.GALLON)
                        .add(new Quantity<>(3.78541, VolumeUnit.LITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Litre() {
        assertEquals(2.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                                VolumeUnit.LITRE).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Millilitre() {
        assertEquals(2000.0,
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE),
                                VolumeUnit.MILLILITRE).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_ExplicitTargetUnit_Gallon() {
        assertEquals(2.0,
                new Quantity<>(3.78541, VolumeUnit.LITRE)
                        .add(new Quantity<>(3.78541, VolumeUnit.LITRE),
                                VolumeUnit.GALLON).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_Commutativity() {
        Quantity<VolumeUnit> a =
                new Quantity<>(1.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(1000.0, VolumeUnit.MILLILITRE));

        Quantity<VolumeUnit> b =
                new Quantity<>(1000.0, VolumeUnit.MILLILITRE)
                        .add(new Quantity<>(1.0, VolumeUnit.LITRE));

        assertTrue(a.equals(b));
    }

    @Test
    void testAddition_WithZero() {
        assertEquals(5.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(0.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_NegativeValues() {
        assertEquals(3.0,
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .add(new Quantity<>(-2000.0, VolumeUnit.MILLILITRE)).getValue(),
                EPSILON);
    }

    @Test
    void testAddition_LargeValues() {
        assertEquals(2e6,new Quantity<>(1e6, VolumeUnit.LITRE).add(new Quantity<>(1e6, VolumeUnit.LITRE)).getValue(),EPSILON);
    }


    @Test
    void testVolumeUnitEnum_LitreConstant() {
        assertEquals(1.0,
                VolumeUnit.LITRE.convertToBaseUnit(1.0),
                0.0001);
    }

    @Test
    void testVolumeUnitEnum_MillilitreConstant() {
        assertEquals(0.001,
                VolumeUnit.MILLILITRE.convertToBaseUnit(1.0),
                0.0001);
    }

    @Test
    void testVolumeUnitEnum_GallonConstant() {
        assertEquals(3.78541,
                VolumeUnit.GALLON.convertToBaseUnit(1.0),
                0.0001);
    }

    @Test
    void testConvertToBaseUnit_GallonToLitre() {
        assertEquals(3.78541,
                VolumeUnit.GALLON.convertToBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testConvertFromBaseUnit_LitreToMillilitre() {
        assertEquals(1000.0,
                VolumeUnit.MILLILITRE.convertFromBaseUnit(1.0),
                EPSILON);
    }

    @Test
    void testGenericQuantity_VolumeOperations_Consistency() {
        Quantity<VolumeUnit> v1 = new Quantity<>(2.0, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(2000.0, VolumeUnit.MILLILITRE);

        assertTrue(v1.equals(v2));
        assertEquals(4.0, v1.add(v2).getValue(), EPSILON);
    }
    
    @Test
    void testSubtraction_SameUnit_FeetMinusFeet() {
        assertEquals(new Quantity<>(5.0, LengthUnit.FEET),
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_SameUnit_LitreMinusLitre() {
        assertEquals(new Quantity<>(7.0, VolumeUnit.LITRE),
                new Quantity<>(10.0, VolumeUnit.LITRE)
                        .subtract(new Quantity<>(3.0, VolumeUnit.LITRE)));
    }

    @Test
    void testSubtraction_CrossUnit_FeetMinusInches() {
        assertEquals(new Quantity<>(9.5, LengthUnit.FEET),
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(6.0, LengthUnit.INCHES)));
    }

    @Test
    void testSubtraction_CrossUnit_InchesMinusFeet() {
        assertEquals(new Quantity<>(60.0, LengthUnit.INCHES),
                new Quantity<>(120.0, LengthUnit.INCHES)
                        .subtract(new Quantity<>(5.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Feet() {
        assertEquals(new Quantity<>(9.5, LengthUnit.FEET),
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.FEET));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Inches() {
        assertEquals(new Quantity<>(114.0, LengthUnit.INCHES),
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(6.0, LengthUnit.INCHES), LengthUnit.INCHES));
    }

    @Test
    void testSubtraction_ExplicitTargetUnit_Millilitre() {
        assertEquals(new Quantity<>(3000.0, VolumeUnit.MILLILITRE),
                new Quantity<>(5.0, VolumeUnit.LITRE)
                        .subtract(new Quantity<>(2.0, VolumeUnit.LITRE), VolumeUnit.MILLILITRE));
    }

    @Test
    void testSubtraction_ResultingInNegative() {
        assertEquals(new Quantity<>(-5.0, LengthUnit.FEET),
                new Quantity<>(5.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(10.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_ResultingInZero() {
        assertEquals(new Quantity<>(0.0, LengthUnit.FEET),
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(120.0, LengthUnit.INCHES)));
    }

    @Test
    void testSubtraction_WithZeroOperand() {
        assertEquals(new Quantity<>(5.0, LengthUnit.FEET),
                new Quantity<>(5.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(0.0, LengthUnit.INCHES)));
    }

    @Test
    void testSubtraction_WithNegativeValues() {
        assertEquals(new Quantity<>(7.0, LengthUnit.FEET),
                new Quantity<>(5.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(-2.0, LengthUnit.FEET)));
    }

    @Test
    void testSubtraction_NonCommutative() {
        Quantity<LengthUnit> A = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> B = new Quantity<>(5.0, LengthUnit.FEET);

        assertNotEquals(A.subtract(B), B.subtract(A));
    }

    @Test
    void testSubtraction_WithLargeValues() {
        assertEquals(new Quantity<>(500000.0, WeightUnit.KILOGRAM),
                new Quantity<>(1e6, WeightUnit.KILOGRAM)
                        .subtract(new Quantity<>(5e5, WeightUnit.KILOGRAM)));
    }

    @Test
    void testSubtraction_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).subtract(null));
    }

    @Test
    void testSubtraction_AllMeasurementCategories() {
        assertNotNull(new Quantity<>(5.0, LengthUnit.FEET)
                .subtract(new Quantity<>(2.0, LengthUnit.FEET)));

        assertNotNull(new Quantity<>(5.0, WeightUnit.KILOGRAM)
                .subtract(new Quantity<>(2.0, WeightUnit.KILOGRAM)));

        assertNotNull(new Quantity<>(5.0, VolumeUnit.LITRE)
                .subtract(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testSubtraction_ChainedOperations() {
        assertEquals(new Quantity<>(7.0, LengthUnit.FEET),
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(2.0, LengthUnit.FEET))
                        .subtract(new Quantity<>(1.0, LengthUnit.FEET)));
    }

    void testDivision_SameUnit_FeetDividedByFeet() {
        assertEquals(5.0,
                new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testDivision_SameUnit_LitreDividedByLitre() {
        assertEquals(2.0,
                new Quantity<>(10.0, VolumeUnit.LITRE)
                        .divide(new Quantity<>(5.0, VolumeUnit.LITRE)), EPSILON);
    }
    
    @Test
    void testDivision_CrossUnit_FeetDividedByInches() {
        assertEquals(1.0, new Quantity<>(24.0, LengthUnit.INCHES).divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testDivision_CrossUnit_KilogramDividedByGram() {
        assertEquals(1.0,
                new Quantity<>(2.0, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(2000.0, WeightUnit.GRAM)), EPSILON);
    }

    @Test
    void testDivision_RatioGreaterThanOne() {
        assertEquals(5.0,
                new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testDivision_RatioLessThanOne() {
        assertEquals(0.5,
                new Quantity<>(5.0, LengthUnit.FEET)
                        .divide(new Quantity<>(10.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testDivision_RatioEqualToOne() {
        assertEquals(1.0,
                new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(10.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testDivision_NonCommutative() {
        Quantity<LengthUnit> A = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> B = new Quantity<>(5.0, LengthUnit.FEET);
        assertNotEquals(A.divide(B), B.divide(A));
    }

    @Test
    void testDivision_WithLargeRatio() {
        assertEquals(1e6,
                new Quantity<>(1e6, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1.0, WeightUnit.KILOGRAM)), EPSILON);
    }

    @Test
    void testDivision_WithSmallRatio() {
        assertEquals(1e-6,
                new Quantity<>(1.0, WeightUnit.KILOGRAM)
                        .divide(new Quantity<>(1e6, WeightUnit.KILOGRAM)), EPSILON);
    }

    @Test
    void testDivision_NullOperand() {
        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(10.0, LengthUnit.FEET).divide(null));
    }

    @Test
    void testDivision_AllMeasurementCategories() {
        assertNotNull(new Quantity<>(5.0, LengthUnit.FEET)
                .divide(new Quantity<>(2.0, LengthUnit.FEET)));

        assertNotNull(new Quantity<>(5.0, WeightUnit.KILOGRAM)
                .divide(new Quantity<>(2.0, WeightUnit.KILOGRAM)));

        assertNotNull(new Quantity<>(5.0, VolumeUnit.LITRE)
                .divide(new Quantity<>(2.0, VolumeUnit.LITRE)));
    }

    @Test
    void testDivision_Associativity() {
        Quantity<LengthUnit> A = new Quantity<>(20.0, LengthUnit.FEET);
        Quantity<LengthUnit> B = new Quantity<>(5.0, LengthUnit.FEET);
        Quantity<LengthUnit> C = new Quantity<>(2.0, LengthUnit.FEET);

        double left = A.divide(B) / C.getValue();
        double right = A.getValue() / (B.divide(C));

        assertNotEquals(left, right);
    }

    @Test
    void testSubtractionAndDivision_Integration() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.0, LengthUnit.FEET)
                        .subtract(new Quantity<>(2.0, LengthUnit.FEET));

        assertEquals(4.0,
                result.divide(new Quantity<>(2.0, LengthUnit.FEET)), EPSILON);
    }

    @Test
    void testSubtractionAddition_Inverse() {
        Quantity<LengthUnit> A = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> B = new Quantity<>(5.0, LengthUnit.FEET);

        assertEquals(A, A.add(B).subtract(B));
    }

    @Test
    void testSubtraction_Immutability() {
        Quantity<LengthUnit> A = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> B = new Quantity<>(5.0, LengthUnit.FEET);
        A.subtract(B);
        assertEquals(10.0, A.getValue(), EPSILON);
    }

    @Test
    void testDivision_Immutability() {
        Quantity<LengthUnit> A = new Quantity<>(10.0, LengthUnit.FEET);
        Quantity<LengthUnit> B = new Quantity<>(5.0, LengthUnit.FEET);
        A.divide(B);
        assertEquals(10.0, A.getValue(), EPSILON);
    }

    @Test
    void testSubtraction_PrecisionAndRounding() {
        Quantity<LengthUnit> result =
                new Quantity<>(10.555, LengthUnit.FEET)
                        .subtract(new Quantity<>(0.555, LengthUnit.FEET));
        assertEquals(10.0, result.getValue(), EPSILON);
    }

    @Test
    void testDivision_PrecisionHandling() {
        assertEquals(3.33,
                new Quantity<>(10.0, LengthUnit.FEET)
                        .divide(new Quantity<>(3.0, LengthUnit.FEET)), 0.01);
    }
    
    @Test
    void testRefactoring_Add_DelegatesViaHelper() throws Exception {

        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

        Quantity<LengthUnit> result = q1.add(q2);

        assertEquals(15.0, result.getValue());
        assertEquals(LengthUnit.FEET, result.getUnit());
    }

    @Test
    void testRefactoring_Subtract_DelegatesViaHelper() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

        Quantity<LengthUnit> result = q1.subtract(q2);

        assertEquals(5.0, result.getValue());
    }

    @Test
    void testRefactoring_Divide_DelegatesViaHelper() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

        double result = q1.divide(q2);

        assertEquals(2.0, result);
    }

    @Test
    void testValidation_NullOperand_ConsistentAcrossOperations() {

        Quantity<LengthUnit> q = new Quantity<>(10, LengthUnit.FEET);

        Exception addEx = assertThrows(IllegalArgumentException.class, () -> q.add(null));
        Exception subEx = assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
        Exception divEx = assertThrows(IllegalArgumentException.class, () -> q.divide(null));

        assertEquals(addEx.getMessage(), subEx.getMessage());
        assertEquals(addEx.getMessage(), divEx.getMessage());
    }

    @Test
    void testValidation_CrossCategory_ConsistentAcrossOperations() {

        Quantity<LengthUnit> length = new Quantity<>(10, LengthUnit.FEET);
        Quantity<WeightUnit> weight = new Quantity<>(5, WeightUnit.KILOGRAM);

        Exception addEx = assertThrows(IllegalArgumentException.class,
                () -> length.add((Quantity) weight));

        Exception subEx = assertThrows(IllegalArgumentException.class,
                () -> length.subtract((Quantity) weight));

        Exception divEx = assertThrows(IllegalArgumentException.class,
                () -> length.divide((Quantity) weight));

        assertEquals(addEx.getMessage(), subEx.getMessage());
        assertEquals(addEx.getMessage(), divEx.getMessage());
    }

    @Test
    void testValidation_FiniteValue_ConsistentAcrossOperations() {

        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.NaN, LengthUnit.FEET));

        assertThrows(IllegalArgumentException.class,
                () -> new Quantity<>(Double.POSITIVE_INFINITY, LengthUnit.FEET));
    }

    @Test
    void testValidation_NullTargetUnit_AddSubtractReject() {

        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

        assertThrows(IllegalArgumentException.class, () -> q1.add(q2, null));
        assertThrows(IllegalArgumentException.class, () -> q1.subtract(q2, null));
    }

    private Object getEnumConstant(String name) throws Exception {
        Class<?> enumClass = Class.forName("Quantity$ArithmeticOperation");
        return Enum.valueOf((Class<Enum>) enumClass, name);
    }

    private double invokeEnumCompute(Object enumConst, double a, double b) throws Exception {
        Method compute = enumConst.getClass().getDeclaredMethod("compute", double.class, double.class);
        compute.setAccessible(true);
        return (double) compute.invoke(enumConst, a, b);
    }

    @Test
    void testPerformBaseArithmetic_ConversionAndOperation() {

        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

        Quantity<LengthUnit> result = q1.add(q2);

        assertEquals(2.0, result.getValue());
    }

    @Test
    void testAdd_UC12_BehaviorPreserved() {

        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

        assertEquals(2.0, q1.add(q2).getValue());
    }

    @Test
    void testSubtract_UC12_BehaviorPreserved() {

        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(6, LengthUnit.INCHES);

        assertEquals(9.5, q1.subtract(q2).getValue());
    }

    @Test
    void testDivide_UC12_BehaviorPreserved() {

        Quantity<LengthUnit> q1 = new Quantity<>(24, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);

        assertEquals(1.0, q1.divide(q2));
    }
    
    @Test
    void testRounding_AddSubtract_TwoDecimalPlaces() {
        Quantity<LengthUnit> q1 = new Quantity<>(1.2345, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(0.1111, LengthUnit.FEET);

        assertEquals(1.35, q1.add(q2).getValue());
        assertEquals(1.12, q1.subtract(new Quantity<>(0.1145, LengthUnit.FEET)).getValue());
    }

    @Test
    void testRounding_Divide_NoRounding() {
        Quantity<LengthUnit> q1 = new Quantity<>(7, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);

        assertEquals(3.5, q1.divide(q2));
    }

    @Test
    void testImplicitTargetUnit_AddSubtract() {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

        assertEquals(LengthUnit.FEET, q1.add(q2).getUnit());
    }

    @Test
    void testExplicitTargetUnit_AddSubtract_Overrides() {
        Quantity<LengthUnit> q1 = new Quantity<>(1, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

        Quantity<LengthUnit> result = q1.add(q2, LengthUnit.INCHES);

        assertEquals(LengthUnit.INCHES, result.getUnit());
        assertEquals(24.0, result.getValue());
    }

    @Test
    void testImmutability_AfterAdd_ViaCentralizedHelper() {
        Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(5, LengthUnit.FEET);

        q1.add(q2);

        assertEquals(5.0, q1.getValue());
        assertEquals(5.0, q2.getValue());
    }

    @Test
    void testImmutability_AfterSubtract_ViaCentralizedHelper() {
        Quantity<LengthUnit> q1 = new Quantity<>(5, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(3, LengthUnit.FEET);

        q1.subtract(q2);

        assertEquals(5.0, q1.getValue());
        assertEquals(3.0, q2.getValue());
    }

    @Test
    void testImmutability_AfterDivide_ViaCentralizedHelper() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);

        q1.divide(q2);

        assertEquals(10.0, q1.getValue());
        assertEquals(2.0, q2.getValue());
    }

    @Test
    void testAllOperations_AcrossAllCategories() {

        Quantity<LengthUnit> l1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> l2 = new Quantity<>(2, LengthUnit.FEET);
        assertEquals(12.0, l1.add(l2).getValue());

        Quantity<WeightUnit> w1 = new Quantity<>(1, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000, WeightUnit.GRAM);
        assertEquals(2.0, w1.add(w2).getValue());

        Quantity<VolumeUnit> v1 = new Quantity<>(1, VolumeUnit.LITRE);
        Quantity<VolumeUnit> v2 = new Quantity<>(1000, VolumeUnit.MILLILITRE);
        assertEquals(2.0, v1.add(v2).getValue());
    }


    private Object getEnum(String name) throws Exception {
        Class<?> enumClass = Class.forName("Quantity$ArithmeticOperation");
        return Enum.valueOf((Class<Enum>) enumClass, name);
    }

    private double compute(Object enumConst, double a, double b) throws Exception {
        Method compute = enumConst.getClass()
                .getDeclaredMethod("compute", double.class, double.class);
        compute.setAccessible(true);
        return (double) compute.invoke(enumConst, a, b);
    }

    @Test
    void testHelper_BaseUnitConversion_Correct() {
        Quantity<LengthUnit> q1 = new Quantity<>(12, LengthUnit.INCHES);
        Quantity<LengthUnit> q2 = new Quantity<>(12, LengthUnit.INCHES);

        assertEquals(2.0, q1.add(q2, LengthUnit.FEET).getValue());
    }

    @Test
    void testRefactoring_Validation_UnifiedBehavior() {
        Quantity<LengthUnit> q = new Quantity<>(10, LengthUnit.FEET);

        Exception addEx = assertThrows(IllegalArgumentException.class, () -> q.add(null));
        Exception subEx = assertThrows(IllegalArgumentException.class, () -> q.subtract(null));
        Exception divEx = assertThrows(IllegalArgumentException.class, () -> q.divide(null));

        assertEquals(addEx.getMessage(), subEx.getMessage());
        assertEquals(addEx.getMessage(), divEx.getMessage());
    }

    @Test
    void testArithmetic_Chain_Operations() {
        Quantity<LengthUnit> q1 = new Quantity<>(10, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(2, LengthUnit.FEET);
        Quantity<LengthUnit> q3 = new Quantity<>(1, LengthUnit.FEET);

        double result = q1.add(q2).subtract(q3)
                .divide(new Quantity<>(1, LengthUnit.FEET));

        assertEquals(11.0, result);
    }
}
