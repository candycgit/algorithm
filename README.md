# algorithm

### graph

##### :apple: [A*, or A star](https://en.wikipedia.org/wiki/A*_search_algorithm)
Find path between source and destination on a map with obstacles

##### :apple: [Hungarian algorithm](https://en.wikipedia.org/wiki/Hungarian_algorithm)
See also https://cp-algorithms.com/graph/hungarian-algorithm.html.

The Hungarian algorithm solves the linear assignment problem - 
finding a perfect matching with minimum cost in a bipartite graph
in polynomial time O(N^3).

Can be mentioned as:
- Assignment Problem, or
- Minimum Weight Perfect Bipartite Matching

Special application:
- Maximum Weight Bipartite Matching (MWBM) - with weights adjustment first
- Minimum Cost Maximum Flow (MCMF) - is a specialized case of Assignment Problem

Note: Ford-Fulkerson is a general method for finding the maximum flow in a network,
and it can be used to solve assignment problem by transforming it into a min-cost flow problem.

Example: Given N workers and N jobs, and a cost matrix C where C{i,j} 
is the cost of assigning worker i to job j, what is the minimum total cost assignment 
such that every worker is assigned to exactly one job?

Step-by-Step Procedure:
1. Row Reduction (Create Zeros): 
For each row, find the smallest element and subtract it from all elements in that row. 
(This creates at least one zero in every row.)
2. Column Reduction (Create More Zeros):
For each column in the resulting matrix, find the smallest element and 
subtract it from all elements in that column. 
(This ensures at least one zero in every row and column.)
3. Test for Optimality (Line Coverage): 
Cover all the zeros in the matrix using the minimum number of horizontal and vertical lines.
   - If the number of lines equals the size of the matrix (N), 
     an optimal assignment exists among the covered zeros. STOP.
   - If the number of lines is less than N, the current zeros are not enough for a perfect assignment. 
     Continue to Step 4.
4. Create Additional Zeros (Matrix Adjustment):
   - Find the smallest uncovered value h.
   - Subtract h from every uncovered element.
   - Add h to every element covered by two lines (the intersection of a row and column line).
   - Note: elements covered by exactly one line remain unchanged.
5. Repeat: Return to Step 3 with the new matrix.

Explanation for step 3 above:
- To solve "minimum set of lines" problem:
  - find Maximum Bipartite Matching with **Kuhn's Algorithm**, then
  - derive [**Minimum Vertex Cover**](https://en.wikipedia.org/wiki/Vertex_cover) 
    (minimum set of lines) from the unmatched vertices using 
    **Augmenting Path Search** (Minimum Cover Derivation).
  - Alternating Path runs using DFS from all unassigned rows:
      - Covered Rows: Are all rows **not** reached by the final DFS. (Horizontal Lines)
      - Covered Columns: Are all columns reached by the final DFS. (Vertical Lines)

  For Minimum Line Cover see **König's Theorem**: in any bipartite graph, 
  the number of edges in a maximum matching 
  (maximum number of independent zero assignments) 
  is equal to the minimum number of vertices 
  (minimum number of horizontal and vertical lines) required to cover all edges.


### leetcode

##### :apple: [29. Divide Two Integers](https://leetcode.com/problems/divide-two-integers/description/)

> Given two integers dividend and divisor, divide two integers without using multiplication, division and mod operator.