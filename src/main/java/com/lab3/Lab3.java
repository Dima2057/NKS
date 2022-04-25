package com.lab3;

import com.lab2.Lab2;

import java.util.Arrays;
import java.util.stream.LongStream;

import static com.lab3.AppConstants.*;
import static com.lab3.AppConstants.OUT_INDEXES;
import static java.lang.Math.*;

public class Lab3 extends Lab2 {

    private final int hours;
    private double qSys;
    private int tSys;
    private double qRSys;
    private double pRSys;
    private int tRSys;
    private double gQ;
    private double gP;
    private double gT;

    public Lab3(int[][] linksTable, double[] probabilities,
                int[] inIndexes, int[] outIndexes, int hours) {
        super(linksTable, probabilities, inIndexes, outIndexes);
        this.hours = hours;
    }

    public void findGeneralLoad(boolean loaded, int multiplicity) {
        findProbabilitySystem();

        qSys = 1.0 - pSys;
        tSys = (int) (-hours / log(pSys));

        qRSys = !loaded ? qSys / factorial(multiplicity + 1L) : pow(qSys, multiplicity + 1L);
        pRSys = 1.0 - qRSys;
        tRSys = (int) (-hours / log(pRSys));

        gQ = qRSys / qSys;
        gP = pRSys / pSys;
        gT = (double) tRSys / tSys;
    }

    public void findSeparateLoad(boolean loaded, int multiplicity) {
        long f = 0;
        findProbabilitySystem();

        qSys = 1.0 - pSys;
        tSys = (int) (-hours / log(pSys));

        var q = new double[probabilities.length];
        var pReserved = new double[probabilities.length];
        var qReserved = new double[probabilities.length];

        if (!loaded) f = factorial(multiplicity + 1L);

        for (int i = 0; i < probabilities.length; i++) {
            q[i] = 1.0 - probabilities[i];
            qReserved[i] = !loaded ? q[i] / f : pow(q[i], multiplicity + 1.0);
            pReserved[i] = 1.0 - qReserved[i];
            System.out.println("Q(" + (i + 1) + ")r = " + qReserved[i] +
                            "   P(" + (i + 1) + ")r = " + pReserved[i]);
        }

        var schemaReserved = new Lab2(linksTable, pReserved, inIndexes, outIndexes);
        schemaReserved.findProbabilitySystem();

        pRSys = schemaReserved.pSys;
        qRSys = 1.0 - pRSys;
        tRSys = (int) (-hours / log(pRSys));

        gQ = qRSys / qSys;
        gP = pRSys / pSys;
        gT = (double) tRSys / tSys;
    }

    public void printData() {
        for (String s : Arrays.asList("Psys(" + hours + ") = " + pSys,
                "Qsys(" + hours + ") = " + qSys,
                "Tsys = " + tSys,
                "Pr.sys(" + hours + ") = " + pRSys,
                "Qr.sys(" + hours + ") = " + qRSys,
                "Tr.sys = " + tRSys,
                "Gq = " + gQ, "Gp = " + gP, "Gt = " + gT, "\n")) {
            System.out.println(s);
        }
    }

    private long factorial(long num) {
        return LongStream.iterate(num, i -> i > 0, i -> (i - 1)).reduce(1, (a, b) -> a * b);
    }

    public static void main(String[] args) {
        Lab3 lab3 = new Lab3(LINKS_TABLE, PROBABILITY, IN_INDEXES, OUT_INDEXES, HOURS);

        lab3.findGeneralLoad(false, MULTIPLICITIES[0]);
        lab3.printData();

        lab3.findSeparateLoad(false, MULTIPLICITIES[1]);
        lab3.printData();
    }
}
