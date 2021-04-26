# Demand Paging (Java)

This program simulates **demand paging** to see how the number of page faults depends on page size, program size, replacement algorithm, and job mix.

### Running this program:

1. Compile:
   `javac Paging.java`

2. Run:
   `java Paging <input>`

##### Input Format

> > <machine-size> <page-size> <size-per-process> <job-mix> <number-of-references-per-process> <replacement-algorithm (FIFO, RANDOM, LRU)>
> > Example: `10 10 20 1 10 lru`

##### Output Format

```
The machine size is ___.
The page size is ___.
The process size is ___.
The job mix number is ___.
The number of references per process is ___.
The replacement algorithm is ___.
The level of debugging output is ___.
```

If applicable, displayed per process:

```
Process ___ had ___ faults and ___ average residency

The total number of faults is ___ and the overall average residency is ___.
```
