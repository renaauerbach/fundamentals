# Dynamic Programming Implementation - Longest Common Subsequence

[Assignment 5 Instructions](https://www.hackerrank.com/contests/basic-algorithms-spring-2020-pa5/challenges/dna-matching-1-2)

Supreme Chancellor Palpatine wants to create more clones for his army to fight the Jedi Order. He has a proposed DNA sequence, but his clone production machinery has been scrambled and he has been left with a sequence of DNA strands where some subsequence of these strands can (hopefully) be stitched together (i.e., concatenated) to form the proposed DNA sequence.

Supreme Chancellor Palpatine has tasked his underlings to find the most efficient way to stitch together a subsequence of the scrambled DNA strands in order to recover the proposed DNA sequence.

#### Input Format:

Supreme Chancellor Palpatine has provided you with the proposed DNA sequence as well as a sequence of DNA strands.

The first line contains a target string (i.e., proposed DNA sequence) **t**.

The next line specifices a number of tiles (i.e., DNA strands) **k**.

The final **k** lines contain the tiles **s1, s2,..., sk**.

NOTE: The tiles are 1-indexed, meaning that (as above) we denote the first tile as **s1** (with the subscript indicating index 1).

#### Constraints:

**|t| <= 5000**
**|k| <= 5000**
**Sum(i=1, k)|si| <= 5000**

To avoid time-outs, your algorithm should run in time **O(|t| \* Sum(i=1, k)|si|)**. In addition, in Java, you should use the built-in method **regionMatches**, and in Python, you should use the built-in method **endswith**.

To avoid running out of space, in addition to the string **t** and an array of strings storing the tiles **s1, ..., sk**, the main data structure you should use is a 2-dimensional array of size **(t + 1) x (k + 1)**, where each entry is an **int**.

#### Output Format:

If there is no subsequence of tiles that covers **t**, output "0" and nothing else.

Otherwise, output the size **l** of a shortest subsequence of tiles, **si1, si2,..., sil**, where **i1 < i2 < ... < il**, that covers **t**, i.e. **t = si1 || si2 ||...|| sit**, followed by the indices of the **l** tiles: **l i1 i2 ... il**

Here, a shortest subsequence means one with the least number of tiles. There may be more than one shortest subsequence of tiles that covers **t**. Your program should just output one such subsequence.

_Note:_ Again, the tiles are 1-indexed, so, for example if **i1** is the index of the first of the **k** tiles, **i1 = 1**.
