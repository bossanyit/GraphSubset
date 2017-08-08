import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

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
public class GraphSubset {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        long[] ar = new long[n];
        for(int i=0;i<n;i++){
            ar[i]=in.nextInt(); 
        }
        long output = getSubsetOuput(ar); 
        System.out.println(output);
        
        in.close();
	}
	
	/*
	 * Count of connected components, where there are 
	 * - 0 edges: 64 (every node is a component by itself )
	 * 
	 * Examples: 
	 * {5} => The Binary Representation of 5 is 00000101. 
	 * There is a bit at the 0th and 2nd position. => 
	 * So there is an edge: (0, 2) in the graph => 
	 * There is one component with a pair of nodes (0,2) in the graph. 
	 * Apart from that, all remaining 62 vertices are independent components 
	 * of one node each (1,3,4,5,6...63) => Number of connected-components = 63.
	 * 
	 * {9} => The Binary Representation of 9 is 00001001. 
	 * => There is a 1-bit at the 0th and 3rd position 
	 * in this binary representation. => edge: (0, 3) in the graph => 
	 * Number of components = 63
	 * 
	 * {2, 9} => This has edge (0,3) in the graph => 
	 *  Similar to examples above, this has 63 connected components
	 *  
	 * {5, 9} => This has edges (0, 2) and (0, 3) in the graph
	 *  => Similar to examples above, this has 62 connected components
	 */
	public static long getSubsetOuput(long[] ar) {
		HashSet<Subset> set = getAllSubset( ar );
		long sum = 0;	// sum of connected components
		sum = getConnectedComponents( set );		   
		return sum;
		
	}
	
	/*
	 * @param set contains one subset. From each number will be represented in binary. 
	 * Every bit on the binary representation covers a node in the graph. 
	 * Between two nodes we have one edge => one connection.
	 * The other nodes are connected to themselves 
	 * 
	 * @return the connected components
	 * 
	 * @see getSubsetOuput for examples
	 */
	public static long getConnectedComponents( HashSet<Subset> set) {
		// create an iterator
		Iterator<Subset> iterator = set.iterator(); 		     

		long sum_connections = 0;
		Subset subset;
		while (iterator.hasNext()) {
			subset = (Subset) iterator.next();
			sum_connections += ( 64 - getEdgesFromSubset(subset) );
		}
		return sum_connections;
	}
	
	/*
	 * get the long array from the Subset object
	 * 
	 * @return the number of edges from the subset
	 */
	public static long getEdgesFromSubset( Subset subset ) {
		long[] numbers = subset.getAr();		

		int count_nodes = 0;
		
		// this is the empty set
		if (numbers.length == 0) {
			return 0;
		}
			
		for (long number : numbers) {
			count_nodes += getEdgesFromBinary( number );
		}
		int edges = count_nodes - 1;
		return edges;
	}
	
	/* 
	 * Converts the @param number first in the binary form
	 * 
	 * Example 
	 * 
	 * 7 binary is 00000111
	 * it has 3 nodes: 1, 2, 4
	 * it has 2 edges between the 3 nodes
	 * 
	 * 2 binary is 000000010
	 * it has 1 node: 2
	 * it has 0 edges 
	 * 
	 * @param number
	 * @return the number of edges of the number 
	 */
	public static long getEdgesFromBinary( long number ) {
		long count_nodes = 0;
		String binary = Long.toBinaryString(number);
		int size = binary.length();
		for (int i = 0; i < size; i++) {
			if (binary.charAt(i) == '1') {
				count_nodes++;
			}
		}
		return count_nodes;
	}
	
	/*
	 * Get All Subset of the array
	 * 
	 * @return HashSet array
	 */
	public static HashSet<Subset> getAllSubset( long[] ar ) {		
		int size = ar.length;	
		HashSet<Subset> set = new HashSet<Subset>();
		
		// first: the empty set
		long[] empty = new long[0];
		set.add( (Subset) new Subset(empty) );
		// last: all elements
		//set.add( (Subset) new Subset(ar) );
		
		/* 
		 * create subsets, begin with 1 (standalone numlets)
		 */
		for (int startIndex = 0; startIndex < size; startIndex++) {
			for (int endIndex = startIndex; endIndex < size; endIndex++) {
				findSubset(startIndex, endIndex, set, ar);
			}
		}
		
		return set;
	}
	
	/*
	 * Create one subset from @param startIndex to @param endIndex
	 * @param set where the created subset is stored
	 * @param ar the original array from input
	 * 
	 * @return void since everything is created in the HashSet set, referenced from the caller function
	 */
	public static void findSubset(int startIndex, int endIndex, HashSet<Subset> set, long[] ar) {
		long lSubset[] = new long[endIndex - startIndex + 1]; 
		int arrayIndex = 0;
		for (int i = startIndex; i <= endIndex; i++) {
			lSubset[arrayIndex] = ar[i];
			arrayIndex++;
		}
		set.add( new Subset(lSubset));		
	}	

	
}

/*
 * Subset object
 * 
 * It contains a subset of numbers in an array
 */
class Subset {
	private long ar[];
	
	/*
	 * constructor with empty array
	 */
	public Subset() {
		
	}
	
	public Subset(long[] subset) {
		this.ar = subset;
	}

	/*
	 * getter
	 */
	public long[] getAr() {
		return this.ar;
	}	
}
