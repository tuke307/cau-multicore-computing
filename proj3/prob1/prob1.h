// PROB1.h
#ifndef PROB1_H
#define PROB1_H

/**
 * Sets up the program based on command line arguments.
 *
 * This function parses the command line arguments to determine the scheduling
 * type and number of threads to use. It then sets the OpenMP scheduling type
 * and number of threads accordingly.
 *
 * @param argc The number of command line arguments.
 * @param argv The command line arguments.
 * @param scheduling_type A pointer to an integer where the scheduling type will be stored.
 * @param num_threads A pointer to an integer where the number of threads will be stored.
 * @return The execution time of the prime number calculation, or -1.0 if an error occurred.
 */
double setup(int argc, char *argv[], int *scheduling_type, int *num_threads);

#endif // PROB1_H