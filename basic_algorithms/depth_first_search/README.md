# Depth First Search (DFS) Implementation

[Assignment 4 Instructions](https://www.hackerrank.com/contests/basic-algorithms-spring-2020-pa4/challenges/crossing-khazad-dum-1)

The fellowship has decided upon crossing Moria for the paths in the mountains are occupied by wargs. Unfortunately Gandalf is having a hard time remembering the paths inside the great caves as it has been a long time since he has passed through here.

The fellowship is currently being pursued by hordes of orcs and needs to escape as fast as possible from the dungeons. Gandalf has given you the all important task of identifying the perils ahead. As there are too many orcs pursuing the fellowship, if you return to any room where you have previously been, it will be filled with orcs. Gandalf wants you to identify if there is a path which will lead to your doom (by making you visit some room where you already walked through) so that he may take measures to avoid it (assume the passages are one way for simplicity).

#### Input Format:

The map of Moria is presented as a series of passages which connect two rooms in the great labyrinth.

The first line has **n,m** - the number of rooms and number of passages in the kingdom. The next **m** lines have two numbers **a,b** which tell that there is a passage from **a** to **b**.

#### Constraints:

**1 <= n <= 10^4**
**0 <= m <= 10^6**
**a,b exists [1, n]**

#### Output Format:

If there is no loop, output "0" on a line by itself.

If there is a loop, output "1" on a line by itself, and then, on the following line, output the rooms appearing in one such loop, as follows. If the loop is x1 -> x2 -> ... -> xK -> x1, where xi -> xj means there is a passage from xi to xj, then output "x1 x2 ... xK", with a single space between each room number. If there are several loops, it doesn't matter which one you print out. It also doesn't matter which room in the loop you start with, but the ordering should respect the passages in the loop. For example, if you output "17 6 8", there should passages from 17 to 6, from 6 to 8, and from 8 to 17.
