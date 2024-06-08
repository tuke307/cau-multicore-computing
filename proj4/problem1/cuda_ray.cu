/**
 * This program performs ray tracing on the GPU using CUDA.
 */
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <cuda_runtime.h>

#define SPHERES 20
#define INF 2e10f
#define DIM 2048

#define rnd(x) (x * rand() / RAND_MAX)

struct Sphere
{
    float r, g, b; // red, green, and blue color components of the sphere
    float radius;  // radius of the sphere
    float x, y, z; // coordinates of the center of the sphere.

    __device__ float hit(float ox, float oy, float *n)
    {
        float dx = ox - x;
        float dy = oy - y;
        if (dx * dx + dy * dy < radius * radius)
        {
            float dz = sqrtf(radius * radius - dx * dx - dy * dy);
            *n = dz / sqrtf(radius * radius);
            return dz + z;
        }
        return -INF;
    }
};

/**
 * This function calculates the color of a pixel at (x, y) by ray tracing.
 *
 * @param s A pointer to an array of Sphere structs which represent the spheres in the scene.
 * @param x The x-coordinate of the pixel.
 * @param y The y-coordinate of the pixel.
 * @param ptr A pointer to an array where the color of the pixel will be stored.
 *
 * The color of the pixel is determined by the color of the sphere that the ray from the pixel intersects first.
 * If the ray does not intersect any sphere, the pixel is black.
 * The color is stored in the ptr array in RGBA format, with each component as an unsigned char (0-255).
 */
__global__ void kernel(Sphere *s, unsigned char *ptr)
{
    int x = blockIdx.x * blockDim.x + threadIdx.x;
    int y = blockIdx.y * blockDim.y + threadIdx.y;
    int offset = x + y * DIM;
    float ox = (x - DIM / 2);
    float oy = (y - DIM / 2);

    float r = 0, g = 0, b = 0;
    float maxz = -INF;
    for (int i = 0; i < SPHERES; i++)
    {
        float n;
        float t = s[i].hit(ox, oy, &n);
        if (t > maxz)
        {
            float fscale = n;
            r = s[i].r * fscale;
            g = s[i].g * fscale;
            b = s[i].b * fscale;
            maxz = t;
        }
    }

    ptr[offset * 4 + 0] = (int)(r * 255);
    ptr[offset * 4 + 1] = (int)(g * 255);
    ptr[offset * 4 + 2] = (int)(b * 255);
    ptr[offset * 4 + 3] = 255;
}

/**
 * This function writes a bitmap to a file in PPM format.
 *
 * @param bitmap A pointer to an array that contains the bitmap data. The data should be in RGBA format, with each component as an unsigned char (0-255).
 * @param xdim The width of the bitmap in pixels.
 * @param ydim The height of the bitmap in pixels.
 * @param fp A pointer to the file where the bitmap will be written.
 *
 * The PPM file format is a simple uncompressed image format that consists of a header followed by pixel data.
 * The header contains the width and height of the image and the maximum color value (255 in this case).
 * The pixel data consists of red, green, and blue color components for each pixel in the image.
 */
void ppm_write(unsigned char *bitmap, int xdim, int ydim, FILE *fp)
{
    int i, x, y;

    fprintf(fp, "P3\n");
    fprintf(fp, "%d %d\n", xdim, ydim);
    fprintf(fp, "255\n");

    for (y = 0; y < ydim; y++)
    {
        for (x = 0; x < xdim; x++)
        {
            i = x + y * xdim;
            fprintf(fp, "%d %d %d ", bitmap[4 * i], bitmap[4 * i + 1], bitmap[4 * i + 2]);
        }
        fprintf(fp, "\n");
    }
}

/**
 * This is the main function of the program. It is the entry point where the program starts execution.
 * 
 * The main function initializes the scene, performs ray tracing to calculate the color of each pixel in the image, and writes the resulting image to a file in PPM format.
 */
int main()
{
    unsigned char *bitmap;

    srand(time(NULL));

    FILE *fp = fopen("result.ppm", "w");

    Sphere *temp_s = (Sphere *)malloc(sizeof(Sphere) * SPHERES);
    for (int i = 0; i < SPHERES; i++)
    {
        temp_s[i].r = rnd(1.0f);
        temp_s[i].g = rnd(1.0f);
        temp_s[i].b = rnd(1.0f);
        temp_s[i].x = rnd(2000.0f) - 1000;
        temp_s[i].y = rnd(2000.0f) - 1000;
        temp_s[i].z = rnd(2000.0f) - 1000;
        temp_s[i].radius = rnd(200.0f) + 40;
    }

    bitmap = (unsigned char *)malloc(sizeof(unsigned char) * DIM * DIM * 4);

    Sphere *d_s;
    unsigned char *d_bitmap;
    cudaMalloc((void **)&d_s, sizeof(Sphere) * SPHERES);
    cudaMalloc((void **)&d_bitmap, sizeof(unsigned char) * DIM * DIM * 4);
    cudaMemcpy(d_s, temp_s, sizeof(Sphere) * SPHERES, cudaMemcpyHostToDevice);

    dim3 grids(DIM / 16, DIM / 16);
    dim3 threads(16, 16);

    cudaEvent_t start, stop;
    cudaEventCreate(&start);
    cudaEventCreate(&stop);
    cudaEventRecord(start, 0);

    kernel<<<grids, threads>>>(d_s, d_bitmap);

    cudaEventRecord(stop, 0);
    cudaEventSynchronize(stop);

    float elapsedTime;
    cudaEventElapsedTime(&elapsedTime, start, stop);
    printf("CUDA ray tracing: %f sec\n", elapsedTime / 1000);

    cudaMemcpy(bitmap, d_bitmap, sizeof(unsigned char) * DIM * DIM * 4, cudaMemcpyDeviceToHost);

    ppm_write(bitmap, DIM, DIM, fp);

    fclose(fp);
    cudaFree(d_s);
    cudaFree(d_bitmap);
    free(bitmap);
    free(temp_s);

    return 0;
}
