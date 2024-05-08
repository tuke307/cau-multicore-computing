# project 1

## problem 1

use evaluation script (using 1, 2, 4, 8, 16, 32 threads)
```bash
java evaluation.java 200000 10
```

use standalone scripts
```bash
java pc_static_block.java 4 200000
java pc_static_cyclic.java 4 200000 10
java pc_dynamic.java 4 200000 10
```

## problem 2

use evaluation script (using 1, 2, 4, 8, 16, 32 threads)
```bash
java evaluation.java < mat1000.txt
```

use standalone scripts
```bash
java MatmultD_static_block.java 4 < mat1000.txt
```
