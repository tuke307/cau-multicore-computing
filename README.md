# multicore-computing

* [project 1 (Java)](proj1/README.md)
* [project 2 (Java)](proj2/README.md)
* [project 3 (C)](proj3/README.md)
* [project 4 (C++, CUDA)](proj4/README.md)

## java
deleting java compilation files (.class)
```bash
find . -name "*.class" -type f -delete
```

compiling java files
```bash
javac *.java
```

## C 
deleting C compilation files (.out) and outputs
```bash
find . \( -name "*.out" -o -name "*.o" -o -name "output.csv" \) -type f -delete
```

