package com.mitresthen.haug.packyears;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by håvard on 31/07/2016.
 */
public class Constants {

    //TobaccoConstants
    private static final double CigarettesInPack = 20;
    private static final double CigaretteBase = 1/CigarettesInPack;

    public static final Map<String, Double> TobaccoFactors;
    static
    {
        TobaccoFactors = new HashMap<String, Double>();
        TobaccoFactors.put("Cigarette", 1*CigaretteBase);
        TobaccoFactors.put("Tobacco(g)", 2*CigaretteBase);
        //TobaccoFactors.put("TobaccoOunce", 50*CigaretteBase);
        TobaccoFactors.put("Pipe", 2.5*CigaretteBase);
        TobaccoFactors.put("CafeCreme", 1.5*CigaretteBase);
        TobaccoFactors.put("Hamlet", 2.5*CigaretteBase);
        TobaccoFactors.put("Havana", 4*CigaretteBase);
    }

    //Duration
    private static final double AverageDaysInMonth = (365.25/12);

    public static final Map<String, Double> TimeFactors;
    static
    {
        TimeFactors = new HashMap<String, Double>();
        TimeFactors.put("Daily", 1.0);
        TimeFactors.put("Weekly", 1.0/7.0);
        TimeFactors.put("Monthly", 1.0/AverageDaysInMonth);
    }
}
