# project 4

## problem 1

openmp raytracing: using 8 threads
```bash
gcc-14 -fopenmp openmp_ray.c -o openmp_ray.out
./openmp_ray.out 8 result.ppm
```

cuda raytracing: jupyter notebook
```bash
%%writefile cuda_ray.cu
<code>
!nvcc -o cuda_ray.exe cuda_ray.cu
!./cuda_ray.exe
```

cuda raytracing: with nvidia graphics card
```bash
nvcc -o cuda_ray.exe cuda_ray.cu
./cuda_ray.exe
```

## problem 2

cuda raytracing: jupyter notebook
```bash
%%writefile thrust_ex.cu
<code>
!nvcc -o thrust_ex.exe thrust_ex.cu
!./thrust_ex.exe
```

cuda raytracing: with nvidia graphics card
```bash
nvcc -o thrust_ex.exe thrust_ex.cu
./thrust_ex.exe
```