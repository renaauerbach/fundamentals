# Min Heap Implementation

[Assignment 3 Instructions](https://www.hackerrank.com/contests/basic-algorithms-spring-2020-pa3/challenges/making-the-spartan-1)

The SPARTAN-II program has been restarted and needs to get more soldiers out as the war is starting to look bad for the humans. Dr.Catherine Halsey has decided that instead of the normal procedure of choosing the survivors of the tests of the SPARTAN-II program it would be better to administer repeated evaluations of the candidates and disqualify the ones which fail to meet the mark.

In the new system each soldier gets an initial score when he enrolls. Throughout the course of the program they need to keep improving their score, which will be appraised at the next evaluation. Every soldier who does not meet the standard will be permanently discarded at that evaluation. Only those who remain at the end of all the evaluations will get to be called SPARTAN-II commandos.

Help Dr.Catherine decide the eligible candidates who remain at the end.

#### Input Format:

The first line of the input is a number **n** which is the number candidates enrolled.

The next **n** lines are of the format
**s a**

where **s** is the name of the candidate and **a** is the original score of the candidate.

The next line is a number **m** which is the number of subsequent lines in the input.

The next **m** lines can be of 2 types:

**1 s b** - which tells that the score of candidate **s** has improved by **b**

**2 k** which tells that an evaluation has been conducted with a standard **k**. All candidates with score less than **k** will be disqualified and permanently discarded in this evaluation.

#### Constraints:

**1 <= n,m <= 10^5**
**|s| <= 10**
**a,b <= 10^9**
**k <= 10^18**

YOU MUST USE A MIN HEAP (WHICH YOU IMPLEMENT YOURSELF), TOGETHER WITH A JAVA HASHMAP. OTHER THAN THE HASHMAP AND STANDARD I/O METHODS, YOU SHOULD NOT USE ANY OTHER CLASSES OR METHODS FROM THE JAVA STANDARD LIBRARY

#### Output Format:

The output should contain the number of remaining soldiers after each query of type 2.
