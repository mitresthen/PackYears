package com.mitresthen.haug.packyears;

/**
 * Created by håvard on 31/07/2016.
 */
public class Calculator {

    public static double Calculate(double packFactor, double quantity, double frequency, double duration){
        return ((packFactor*quantity)/Constants.CigarettesInPack)*duration;
    }
}
