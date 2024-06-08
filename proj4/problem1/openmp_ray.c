#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
#include <omp.h>

#define CUDA 0
#define OPENMP 1
#define SPHERES 20

#define rnd(x) (x * rand() / RAND_MAX)
#define INF 2e10f
#define DIM 2048

struct Sphere
{
    float r, g, b;                              // red, green, and blue color components of the sphere
    float radius;                               // radius of the sphere
    float x, y, z;                              // coordinates of the center of the sphere.
    float (*hit)(float ox, float oy, float *n); // pointer to a function that calculates the intersection of a ray with the sphere.
};

/**
 * This function calculates the color of a pixel at (x, y) by ray tracing.
 *
 * @param x The x-coordinate of the pixel.
 * @param y The y-coordinate of the pixel.
 * @param s A pointer to an array of Sphere structs which represent the spheres in the scene.
 * @param ptr A pointer to an array where the color of the pixel will be stored.
 *
 * The color of the pixel is determined by the color of the sphere that the ray from the pixel intersects first.
 * If the ray does not intersect any sphere, the pixel is black.
 * The color is stored in the ptr array in RGBA format, with each component as an unsigned char (0-255).
 */
void kernel(int x, int y, struct Sphere *s, unsigned char *ptr)
{
    int offset = x + y * DIM;
    float ox = (x - DIM / 2);
    float oy = (y - DIM / 2);

    // printf("x:%d, y:%d, ox:%f, oy:%f\n",x,y,ox,oy);

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
 * This function calculates the intersection of a ray with a sphere.
 *
 * @param sphere A pointer to the Sphere struct which represents the sphere.
 * @param ox The x-coordinate of the origin of the ray.
 * @param oy The y-coordinate of the origin of the ray.
 * @param n A pointer to a float where the normalized distance from the ray origin to the intersection point will be stored.
 *
 * @return The z-coordinate of the intersection point if the ray intersects the sphere, -INF otherwise.
 */
float hit(struct Sphere *sphere, float ox, float oy, float *n)
{
    float dx = ox - sphere->x;
    float dy = oy - sphere->y;
    if (dx * dx + dy * dy < sphere->radius * sphere->radius)
    {
        float dz = sqrtf(sphere->radius * sphere->radius - dx * dx - dy * dy);
        *n = dz / sqrtf(sphere->radius * sphere->radius);
        return dz + sphere->z;
    }
    return -INF;
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
 * @param argc The number of arguments passed to the program from the command line.
 * @param argv An array of pointers to strings representing the arguments passed to the program.
 *
 * The main function initializes the scene, performs ray tracing to calculate the color of each pixel in the image, and writes the resulting image to a file in PPM format.
 */
int main(int argc, char *argv[])
{
    int no_threads;
    int option;
    int x, y;
    unsigned char *bitmap;

    srand(time(NULL));

    if (argc != 3)
    {
        printf("> a.out [option] [filename.ppm]\n");
        printf("[option] 0: CUDA, 1~16: OpenMP using 1~16 threads\n");
        printf("for example, '> a.out 8 result.ppm' means executing OpenMP with 8 threads\n");
        exit(0);
    }
    FILE *fp = fopen(argv[2], "w");

    if (strcmp(argv[1], "0") == 0)
        option = CUDA;
    else
    {
        option = OPENMP;
        no_threads = atoi(argv[1]);
    }

    struct Sphere *temp_s = (struct Sphere *)malloc(sizeof(struct Sphere) * SPHERES);
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

    double start_time = omp_get_wtime();

    // Perform ray tracing to calculate the color of each pixel in the image
#pragma omp parallel for num_threads(no_threads) schedule(dynamic)
    for (int x = 0; x < DIM; x++)
    {
        for (int y = 0; y < DIM; y++)
        {
            kernel(x, y, temp_s, bitmap);
        }
    }

    double end_time = omp_get_wtime();

    printf("OpenMP (%d threads) ray tracing: %f sec\n", no_threads, end_time - start_time);

    ppm_write(bitmap, DIM, DIM, fp);

    fclose(fp);
    free(bitmap);
    free(temp_s);

    return 0;
}
