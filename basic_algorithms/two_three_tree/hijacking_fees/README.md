# 2-3 Tree - Lazy Updates Implementation

[Assignment 2 Instructions](https://www.hackerrank.com/contests/basic-algorithms-spring-2020-pa2/challenges/hijacking-the-fees-2)

The board of directors are very impatient. It has been 5 days since the company restarted and the economy has not collapsed into itself. This suggests that the finance department needs to be sacked and has been thrown into the fourth closest sun.

The board has decided to increase the fees for the parks by at least 4 million altairian dollars. Some others think it might be better to increase each park individually and there are some who think it might be better to increase the fees of multiple parks at the same time. The finer points are still under scrutiny whereas the chairman is nowhere to be found after he went to take a stroll in the park.

They have decided that you, having helped implement the database in the first place, would be a good choice as a programmer to do the updates in the system.

There will be three types of queries to the database

-   insert a planet with name **a** and entrance fee **k** into the database
-   increase the entrance fee for all planets between **a, b** by **k**
-   return the entrance fee for a planet name **a** from the database

#### Input Format

The first line contains the number of queries to be made to the database.

The next lines contain queries of the following 3 types

-   **1 a k** insert a planet with name **a** and entrance fee **k** into the database
-   **2 a b k** increase the entrance fee for all planets between **a, b** by **k**
-   **3 a** return the entrance fee for a planet name **a** from the database

#### Constraints

**n,m <= 100000**
**|a|,|b| <= 10**
**0 <= k <= 10^5**

The entrance fee for any planet will always be less than

YOU MUST USE 2-3 TREE DATA STRUCTURES PROVIDED IN CLASS

As discussed in class, every node in the tree should have a value field. To avoid time-outs, all queries, including range updates (type 2 queries), should run in time .

#### Output Format

For each query of type print the entrance fee of the planet in a new line
