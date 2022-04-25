package com.lab3;

interface AppConstants {
    // Input table of links
    int[][] LINKS_TABLE = {
            {0, 1, 1, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 1, 0, 0},
            {0, 0, 0, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 1, 1},
            {0, 0, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0, 0}};

    // Probability trouble-free operation of system elements
    double[] PROBABILITY = {0.84, 0.84, 0.91, 0.60, 0.44, 0.74, 0.57, 0.79};
    int[] IN_INDEXES = {0};  // Indexes of input
    int[] OUT_INDEXES = {7}; // Indexes of output

    int HOURS = 711;
    int[] MULTIPLICITIES = {3, 2};
}
