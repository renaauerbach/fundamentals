# 2-3 Tree Implementation

[Assignment 1 Instructions](https://www.hackerrank.com/contests/basic-algorithms-spring-2020-pa1/challenges/fords-dilemma-1)

While Arthur is spending time trying to get back to Earth in different dimensions, Ford has been thinking of crashing the different planets made by the Magrathean staff. Ford is too much of a frood to not have fun visiting any of them in the name of research for the guide.

He found you sitting in a bar drinking a pan galactic gargle blaster, blown out of your mind and talked to you about the planet database of the Magratheans. He would like to know the entrance fees of all the planets between some planet names. Help him get the information when the hangover passes.

There will be a single type of query to the database

-   given two planet names return the list of planet names and entrance fees of all planets between the two queried names, in lexicographic order

#### Input Format:

The first line contains a number **n** which is the size of the database.

The next **n** lines are of the form **a k**

**a** - the planet name, **k** - the entrance fee of the planet, separated by a single space.

The next line contains a number **m** the number of queries to the database.

The next **m** lines are of the form **a b**

**a,b** - planet names.

#### Constraints:

**n,m <= 1000**
**|a|,|b| <= 10**
**0 <= k <= 10^9**

YOU MUST USE 2-3 TREE DATA STRUCTURES PROVIDED IN CLASS

To avoid time-outs, each range query **a b** should run in time **O(log n)** plus the number of planets in the range **a ... b**

#### Output Format:

For each query write down the names of the planets and the entrance fees, separated by a single space, in between (inclusive) **a,b** in separate lines.
