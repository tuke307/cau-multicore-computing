#ifndef PROB2_FUNCTIONS_H
#define PROB2_FUNCTIONS_H

/**
 * Calculates the value of pi using the Monte Carlo method.
 * 
 * This function calculates the value of pi using the Monte Carlo method. It
 * uses the specified number of steps and scheduling type to determine the
 * number of threads to use and the execution time of the program.
 * 
 * @param scheduling_type The type of scheduling to use.
 * @param chunk_size The number of steps to use in the calculation.
 * @param num_threads The number of threads to use.
 * @param pi The calculated value of pi.
 * @param execution_time The execution time of the program.
 * @return void
 */
void calculate_pi(int scheduling_type, int chunk_size, int num_threads, double *pi, double *execution_time);

#endif