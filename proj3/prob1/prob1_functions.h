// PROB1.h
#ifndef PROB1_FUNCTIONS_H
#define PROB1_FUNCTIONS_H

/**
 * Sets up the program based on command line arguments.
 *
 * This function parses the command line arguments to determine the scheduling
 * type and number of threads to use. It then sets the OpenMP scheduling type
 * and number of threads accordingly.
 *
 * @param scheduling_type The type of scheduling to use.
 * @param num_threads The number of threads to use.
 * @param prime_count The number of prime numbers found.
 * @param execution_time The execution time of the program.
 * @return void
 */
void setup(int scheduling_type, int num_threads, int *prime_count, double *execution_time);

#endif // PROB1_FUNCTIONS_H