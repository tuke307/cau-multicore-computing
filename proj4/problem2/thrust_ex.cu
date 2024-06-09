#include <thrust/host_vector.h>
#include <thrust/device_vector.h>
#include <thrust/transform_reduce.h>
#include <thrust/functional.h>
#include <thrust/iterator/counting_iterator.h>
#include <iostream>
#include <cmath>
#include <chrono>

// Function to compute the value of the integrand at a given point
struct integrand
{
    double step;
    integrand(double _step) : step(_step) {}

    __host__ __device__
    double operator()(int i) const
    {
        double x = (i + 0.5) * step;
        return 4.0 / (1.0 + x * x);
    }
};

int main()
{
    long num_steps = 1000000000;
    double step = 1.0 / static_cast<double>(num_steps);

    // Start timing
    auto start = std::chrono::high_resolution_clock::now();

    // Create a counting iterator to generate indices
    thrust::counting_iterator<int> begin(0);
    thrust::counting_iterator<int> end(num_steps);

    // Use thrust::transform_reduce to apply the integrand function and sum the results
    double sum = thrust::transform_reduce(
        begin, end,
        integrand(step),
        0.0,
        thrust::plus<double>()
    );

    double pi = step * sum;

    // End timing
    auto end_time = std::chrono::high_resolution_clock::now();
    std::chrono::duration<double> elapsed = end_time - start;

    std::cout << "Execution Time: " << std::chrono::duration_cast<std::chrono::milliseconds>(elapsed).count() << " ms" << std::endl;
    std::cout << "pi = " << pi << std::endl;

    return 0;
}
