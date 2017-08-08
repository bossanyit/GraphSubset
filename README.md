# GraphSubset
Sulotion for the Graph subset problem

/*
 * GRAPH - Subset Component
 * 
 * You are given an array with  n 64 bit integers: d[0], d[1].. d[n-1] .
 * BIT(x, i) = (x >> i) & 1. (where B(x,i) is i. the  lower bit of x in binary form.)
 * 
 * If we regard every bit as a vertex of a graph G, there exists one undirected 
 * edge between vertex i and vertex j if there exists at least one k 
 * such that BIT(d[k], i) == 1 && BIT(d[k], j) == 1.
 * 
 * For every subset of the input array, how many connected-components are there in that graph?
 * 
 * The number of connected-components in a graph are the sets of nodes, which are accessible to each other, 
 * but not to/from the nodes in any other set.
 * 
 * For example if a graph has six nodes, labeled [1,2,3,4,5,6}. 
 * And contains the edges {1,2}, {2,3}, {3,5}
 * There are three connected-components: {1,2,4}, {3,5} and {6}. 
 * 
 * Because {1,2,4} can be accessed from each other through one or more edges,  
 * {3,5} can access each other and {6} is isolated from everyone else.
 * 
 * Output the sum of the number of connected-component(S) in every graph.
 * 
 * Task from Hackerrank.com / Algorithm / Graphs
 * @Author Tibor Bossanyi
 * @date 2017-08-07
 */
