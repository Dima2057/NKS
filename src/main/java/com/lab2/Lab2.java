package com.lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import static com.lab2.AppConstants.*;

public class Lab2 {
    public final int[][] linksTable;
    public final double[] probabilities;
    public final int[] inIndexes;
    public final int[] outIndexes;
    public double pSys;
    public List<int[]> workStates;
    public List<Double> pStates;

    public Lab2(int[][] linksTable, double[] probabilities,
                int[] inIndexes, int[] outIndexes) {
        this.linksTable = linksTable;
        this.probabilities = probabilities;
        this.inIndexes = inIndexes;
        this.outIndexes = outIndexes;
        this.workStates = new ArrayList<>();
        this.pStates = new ArrayList<>();
    }

    public void findProbabilitySystem() {
        findWorkStates();
        findProbabilityStates();
        pSys = 0;
        pStates.forEach(state -> pSys += state);
    }

    public void printResult() {
        System.out.println("Links table:");
        Arrays.stream(linksTable)
                .map(Arrays::toString)
                .forEach(System.out::println);
        System.out.println("Probabilities: " + Arrays.toString(probabilities));
        System.out.println("Work states:");
        IntStream.range(0, workStates.size())
                .mapToObj(i -> Arrays.toString(workStates.get(i)) + " | P = " + pStates.get(i))
                .forEach(System.out::println);
        System.out.println("P(system) = " + pSys);
    }

    private int[][] findStates() {
        var states = new int[linksTable.length][(int) Math.pow(2, linksTable.length)];
        var i = 0;
        while (i < states.length) {
            var a = (int) Math.pow(2, i + 1) >> 1;
            var j = 0;
            var k = 0;
            var inverted = true;
            while (j < states[i].length) {
                if (k == a) {
                    inverted = !inverted;
                    k = 0;
                }
                states[i][j] = inverted ? 0 : 1;
                j++;
                k++;
            }
            i++;
        }
        return states;
    }

    private void findWorkStates() {
        var states = findStates();
        var i = 0;
        while (i < states[0].length) {
            var state = new int[states.length];
            var j = 0;
            while (j < state.length) {
                state[j] = states[j][i];
                j++;
            }
            if (isWorkState(state)) workStates.add(state);
            i++;
        }
    }

    private void findProbabilityStates() {
        workStates.forEach(state -> pStates.add(probabilityState(state)));
    }

    private double probabilityState(int[] state) {
        return IntStream.range(0, state.length)
                .mapToDouble(i -> state[i] == 0 ? 1.0 - probabilities[i] : probabilities[i])
                .reduce(1, (a, b) -> a * b);
    }

    private boolean isWorkState(int[] states) {
        return Arrays.stream(inIndexes)
                .anyMatch(k -> Arrays.stream(outIndexes)
                .mapToObj(i -> findPath(k, i, states, new ArrayList<>()))
                .anyMatch(path -> !path.isEmpty()));
    }

    private List<Integer> findPath(int from, int to, int[] states, List<Integer> prev) {
        List<Integer> path = new ArrayList<>();
        if (states[from] == 0 || states[to] == 0) return path;
        if (linksTable[from][to] == 1) {
            path.add(from);
            path.add(to);
            return path;
        }
        var i = 0;
        while (i < linksTable[from].length) {
            if (linksTable[from][i] == 1 &&
                          states[i] == 1 &&
                          !prev.contains(i)) {
                List<Integer> nPrev = new ArrayList<>(prev);
                nPrev.add(i);
                List<Integer> p = findPath(i, to, states, nPrev);
                if (!p.isEmpty()) {
                    path.add(from);
                    path.addAll(p);
                    return path;
                }
            }
            i++;
        }
        return path;
    }

    public static void main(String[] args) {
        Lab2 lab2 = new Lab2(LINKS_TABLE, PROBABILITY, IN_INDEXES, OUT_INDEXES);

        lab2.findProbabilitySystem();
        lab2.printResult();
    }
}
