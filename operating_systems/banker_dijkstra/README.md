# Dijkstra - Banker's Algorithm (Java)

This program simulates resource allocation using an optimistic resource manager (FIFO) and the banker's algorithm of Dijkstra.

### Running this program:

1. Compile:
   `javac Banker`

2. Run:
   `java Banker <input-filename>`

##### Input Format

> > Example: `10 10 20 1 10 lru`

##### Output Format

This will prompt the program to read the input from file "input-1". When the program is finished executing, the turnover time, waiting time, and percentage of time spent waiting will be printed to the consul for each task, along with the totals of those 3 for all tasks in the program. This will be printed for each resource allocator (manager and banker).
