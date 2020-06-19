package include;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Equipe GL2
 * @date 2020
 */
public class MathSimu {

    public static void main(String[] args)  {
        getMaxValueSimu();
        getMinValueSimu();
        getExponentSimu();
        absSimu();
        powSimu();
    }

    private static void getMaxValueSimu() {
        System.out.println("float getMaxValue() :");
        System.out.println(String.format("%6.5e",Float.MAX_VALUE));
        System.out.println("--------------------");
    }

    private static void getMinValueSimu() {
        System.out.println("float getMinValue() :");
        System.out.println(String.format("%6.5e",Float.MIN_VALUE));
        System.out.println("--------------------");
    }

    private static void getExponentSimu() {
        System.out.println("int getExponent(float x) :");
        // lot de valeurs
        List<Float> lf = new ArrayList<>();
        lf.add(1.0f);
        lf.add(-1.0f);
        lf.add(1.5f);
        lf.add(-1.5f);
        lf.add(2.0f);
        lf.add(-2.0f);
        lf.add(16.22f);
        lf.add(-16.22f);
        lf.add(27.39f);
        lf.add(-27.39f);
        lf.add(275467.7f);
        lf.add(-275467.7f);
        lf.add(0.0f);
        lf.add(Float.MAX_VALUE);
        lf.add(Float.MIN_VALUE);

        for (Float f : lf) {
            // on applique notre fonction
            int val = java.lang.Math.getExponent(f);

            System.out.println(val);
        }
        System.out.println("--------------------");
    }

    private static void absSimu() {
        System.out.println("float abs(float x) :");
        // lot de valeurs
        List<Float> lf = new ArrayList<>();
        lf.add(16f);
        lf.add(-3f);
        lf.add(134.4f);
        lf.add(-246.22221f);

        for (Float f : lf) {
            // on applique notre fonction
            Float val = java.lang.Math.abs(f);

            System.out.println(String.format("%6.5e",val));
        }
        System.out.println("--------------------");
    }

    private static void powSimu() {
        System.out.println("float pow(float x, int e) :");
        System.out.println(String.format("%6.5e",java.lang.Math.pow(0,0)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(2,0)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(1,126)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(2,3)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(2,-3)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(-2,3)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(-2,-3)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(-99456,1)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(2.5,2)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(Double.MAX_VALUE,5)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(Double.MIN_VALUE,5)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(512,Double.MAX_EXPONENT)));
        System.out.println(String.format("%6.5e",java.lang.Math.pow(675,Double.MIN_EXPONENT)));
        System.out.println("--------------------");
    }
}
