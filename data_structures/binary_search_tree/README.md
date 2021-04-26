# Binary Search Tree (Java)

#### Assignment Instructions:

This assignment involves modeling a community of people using the data structures we covered in class to be able to answer certain questions about a community and its members.

Each member `x` of the community is a person that has a `unique SSN`, a `name (first and last name)`, a `mother`, a `father`, and a `list of people (from within the community)` that x considers to be a friend.  
_Note: if x considers y a friend, it does not necessarily follow that y considers x a friend_

Your program should be able to answer the following queries: (detailed later in instructions)

-   For a given person x, find all the children of x;
-   For a given person x, find all the half-siblings of x;
-   For a given person x, find all the full-siblings of x;
-   For a given person x, find the friends of x that consider x as their friend as well;
-   For a given person x, find all the Persons that consider x as their friend;
-   Who has the most mutual friends;

#### Input Format:

**2 files from command prompt**

_Note: files must be read in this respective order_  
_First input file:_ Contains the information about the community (one paragraph per person) with at least one blank between successive paragraphs.

The format of each person paragraph is:

-   FIRST NAME: `word` //one word representing the first name
-   LAST NAME: `word` //one word representing the last name
-   SSN: `number` //social security number (any unsigned integer)
-   FATHER: `number` //representing the SSN of the father
-   MOTHER: `number` //representing the SSN of the mother
-   FRIENDS: `a coma-separated list of SSNs of this person’s friends`  
    _Note: The list after FRIENDS is the list of the SSNs of the people that the person in question considers to be his/her friends. Those SSNs are not necessarily sorted_

_Second input file:_ Contains the a sequence of queries (one query per line) to be done by this program.
The types of queries are:

-   `NAME-OF SSN`
-   `MOTHER-OF SSN`
-   `FATHER-OF SSN`
-   `HALF-SIBLINGS-OF SSN`
-   `FULL-SIBLINGS-OF SSN`
-   `CHILDREN-OF SSN`
-   `MUTUAL-FRIENDS-OF SSN`
-   `INVERSE-FRIENDS-OF SSN`
-   `WHO-HAS-MOST-MUTUAL-FRIENDS`  
    _Note that SSN in the above queries stands for an unsigned int_

#### Output Format:

> QUERY PERFORMED: QUERY ANSWER

The same number of lines as there are query lines (the nth output line should be the answer for the nth query line in file queriesFileName)

-   The query-answer for the first 3 types of query should be two words: the first and last name of the answer-person
-   The query-answer for the next 5 types of query should be a list of first name and last name pairs, separated by commas, and ordered so that their SSNs are in _increasing_ order.

    -   Example output for `CHILDREN-OF 3155`:  
        `CHILDREN-OF 3155: John Smith, Adam Smith, Susan Smith`
    -   Example output for `MUTUAL-FRIENDS-OF 3155`:  
        `MUTUAL-FRIENDS-OF 3155: John Redman, Janet Fisher, Joe Fisher`

-   The answer to `INVERSE-FRIENDS-OF SSN` query should be the people that consider the person with the specified SSN to be their friend
-   The answer to a `WHO-HAS-MOST-MUTUAL-FRIENDS` query should be the full name of the person who has the most mutual friends  
    _In case of a tie: choose the one with the smallest SSN._
