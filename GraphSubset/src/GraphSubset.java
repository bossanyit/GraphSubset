import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String[] ar = new String[n];
        for(int i=0;i<n;i++){
            ar[i]=in.next(); 
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
	public static long getSubsetOuput(String[] ar) {
		HashSet<String> set = getAllSubset( ar );
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
	public static long getConnectedComponents( HashSet<String> set) {
		// create an iterator
		Iterator<String> iterator = set.iterator(); 		     

		long sum_connections = 0;
		long edges = 0;
		String subset;
		while (iterator.hasNext()) {
			subset = (String) iterator.next();
			edges = getEdgesFromSubset( subset );
			sum_connections += edges;
		}
		return sum_connections;
	}
	
	/*
	 * get the long array from the Subset object
	 * 
	 * @return the number of edges from the subset
	 */
	public static long getEdgesFromSubset( String subset ) {
		long orig_node_count = 0;
		BigInteger[] numbers = getLongArrayFromSubset( subset );
		ArrayList<String> nodes = new ArrayList<String>();
		long edges = 0;		
		
		// this is the empty set
		if (numbers.length == 0) {
			return 0;
		}
			
		ArrayList<String> listNodes;
		for ( BigInteger number : numbers ) {
			listNodes = getNodesFromBinary( number );
			orig_node_count++;
			nodes.addAll( listNodes );
		}

		// Sorting with lambda expression	
		Collections.sort( nodes, (node1, node2) -> node1.compareTo( node2 ) );		
		
		nodes = normalizeNodes( nodes );
		
		edges = (long)64 - orig_node_count + (long)nodes.size() - 1;
		return edges;
	}
	
	/*
	 * For test lambda expression sort
	 */
	public static ArrayList<String> sortArray (ArrayList<String> nodes) {
		// Sorting with lambda expression	
		Collections.sort( nodes, (node1, node2) -> node1.compareTo( node2 ) );
		return nodes;
	}
	
	/* 
	 * Converts the @param number first in the binary form
	 * 
	 * Example 
	 * 
	 * 7 binary is 00000111
	 * it has 3 nodes: 1, 2, 4
	 * it has 1 edge {1,2} {1,3} {2,3} => {1,2,3}
	 * 
	 * 2 binary is 000000010
	 * it has 1 node: 2
	 * it has 0 edges 
	 * 
	 * @param number
	 * @return the nodes of the graph 
	 */
	public static ArrayList<String> getNodesFromBinary( BigInteger number ) {
		ArrayList<BigInteger> indexes = new ArrayList<BigInteger>();
		
		String binary = BigIntegertoBinaryString( number );
		int size = binary.length();
		for (int i = 0; i < size; i++) {
			if (binary.charAt(i) == '1') {
				indexes.add(BigInteger.valueOf( i ));
			}
		}
		ArrayList<String>nodes = getAllNodes( indexes ); 
		
		return nodes;
	}
	
	/*
	 * Normalize the nodes in the way that merge the nodes they are connected
	 * ie. {1,2} {2,4}  => {1,2,4}
	 * 
	 * @param input nodes
	 * @return normalized nodes
	 */
	public static ArrayList<String> normalizeNodes( ArrayList<String> nodes ) {
		ArrayList<String> normalizedNodes = new ArrayList<String>();
		ArrayList<Integer> deletedIndex = new ArrayList<Integer>(); 
		 
		int size = nodes.size();
		String nodeToCheck;
		for (int i = 0; i < size; i++) {
			if (deletedIndex.contains(i)) {
				continue;
			}
			nodeToCheck = nodes.get( i );
			String[] nodeIndexToCheck = nodeToCheck.split( ";" );
			if ( existNode(nodeIndexToCheck, normalizedNodes) ) {
				continue;
			}
			for (int j = i; j < size; j++) {
				String[] nodeIndex = nodes.get( j ).split(";");
				if (i != j && nodeIndexToCheck[ 1 ].equals( nodeIndex[0]) ) {
					deletedIndex.add(j);
					nodeToCheck += ";" + nodeIndex[1];
					nodeIndexToCheck = nodes.get( j ).split(";");			
				}
			}
			normalizedNodes.add(nodeToCheck);
		}
		
		
		return normalizedNodes;
	}
	
	/*
	 * Checks, whether the node exists in the normalized node list
	 * 
	 * @param the node to check
	 * @param the normalized list
	 * @return true if exists
	 */
	public static boolean existNode(String[] nodeToCheckArray, ArrayList<String>normalized) {
		boolean exists = false;
		
		String[] normalizedArray;
		for (String node: normalized) {
			normalizedArray = node.split(";");
			
			int foundToCheck = 0;
			for (String nodeToCheck : nodeToCheckArray) {						
				for (String normalizedNode : normalizedArray) {
					if (nodeToCheck.equals( normalizedNode) ) {
						foundToCheck++;
					}
				}
			}			
			if (foundToCheck == 2) {
				exists = true;
				break;
			}
		}
		
		return exists;
	}
	
	/*
	 * create all node-pairs (edges) from the graph
	 * @param indexes from the graph
	 * 
	 * @return the created nodes
	 */
	public static ArrayList<String> getAllNodes(ArrayList<BigInteger> indexes) {
		ArrayList<String>nodes = new ArrayList<String>();
		String sep = ";";		
		int size = indexes.size();
		for (int i = 0; i < size; i++) {
			for (int j = i; j < size; j++) {
				if (i == j) {
					continue;
				}
				nodes.add(indexes.get(i) + sep + indexes.get(j));
			}
		}
		return nodes;
	}
	
	public static String toBinaryString(BigInteger i) {
		return i.toString(2);
     }
	
	
	public static String BigIntegertoBinaryString(BigInteger number) {
		return toBinaryString(number);
	}
	
	/*
	 * Get All Subset of the array
	 * 
	 * @return HashSet array
	 */
	public static HashSet<String> getAllSubset( String[] ar ) {		
		HashSet<String> set = new HashSet<String>();
		HashSet<String> setIndex = new HashSet<String>();	
		String[] arIndex = new String[ar.length];
		
		for (int i = 0; i < ar.length; i++) {
			arIndex[ i ] = String.valueOf(i) ;
		}
		
		// first: the empty set
		String empty = "";
		set.add( empty );
			
		// get all indexes as subset
		findSubsets( setIndex, arIndex );
		
		createSubsetsFromIndex( setIndex, set, ar );
		
		return set;
	}
	
	/* 
	 * We created all subset variants from the indexes of the original String array
	 * 
	 * Now we create real subset from the index set.
	 * 
	 * @param the index list contains all indexes of all subsets
	 * @param the list of all subset
	 * @param the original input
	 * 
	 */
	public static void createSubsetsFromIndex( HashSet<String> setIndex, HashSet<String> set, String[] ar ) {
		String sSubset; // created subset
		int arrayIndex;
		for (String sIndex : setIndex) {
			sSubset = ".";
			String[] index = sIndex.split("[.]{1}");
			for (String i : index) {
				if ( i.isEmpty() ) {
					continue;
				}
				arrayIndex = Integer.valueOf( i );
				sSubset += ar [ arrayIndex ] + ".";
			}
			set.add(sSubset);
		}
	}
	
	/*
	 * We create from the long array a string pattern which can be check 
	 * against regex.
	 * The template of the string pattern is: .<number>{1,6}.
	 * 
	 * By shifting of the pattern string we find all subset combinations and add them to
	 * the HashSet (excludes the duplicates automatically)
	 * 
	 *  @param the HashSet which contains the subsets
	 *  @param long array from the system input
	 */
	public static void findSubsets(HashSet<String> set, String[] ar) {
		String sStringForPattern = getStringForPattern(ar);	
		//System.out.println( "String input " + sStringForPattern);
		int length = ar.length;
		String sPattern = "[.]{1}";
		for (int j = 0; j < length; j++) {
			sPattern += "\\d{1,20}[.]{1}";
			//System.out.println( "pattern " + (sPattern));
			for (int i = 0; i < length; i++) {
				Pattern pattern = Pattern.compile(sPattern);
				Matcher matcher = pattern.matcher(sStringForPattern);

				while(matcher.find()) {
		            String sSubset = sStringForPattern.substring(matcher.start(), matcher.end());
		            //System.out.println( "sPattern: " + sPattern + " subset " + (sSubset) + " j: " + j + " i " + i);
		            
		            set.add( sSubset );
		        }
		        if (j == length - 1) break;

		        sStringForPattern = shiftByDot(sStringForPattern);
			}
		}	
	}
	
	/*
	 * create a long array from the string pattern
	 * 
	 * Reverse of the function getStringForPattern
	 * 
	 * @param string pattern
	 * 
	 * @return the long array
	 */
	public static BigInteger[] getLongArrayFromSubset(String sStringPattern) {
		String[] sNumbers = sStringPattern.split("[.]{1}");
		BigInteger[] lArray = new BigInteger [sNumbers.length - 1]; //because split creates an empty element
		
		int count = 0;
		for (String sElem : sNumbers) {
			if (!sElem.isEmpty()) {
				lArray[count] = new BigInteger(sElem) ;
				count++;
			}
		}
		return lArray;
	}
	
	/*
	 * Shift the pattern string numbers to right
	 * 
	 * @param the pattern string to shift
	 * 
	 * @return the shifted pattern string
	 */
	public static String shiftByDot(String s) {
		String[] sNumbers = s.split("[.]{1}");
		
		String sTemp = sNumbers[sNumbers.length - 1];
		sNumbers[sNumbers.length - 1] = sNumbers[0];
		sNumbers[0] = sTemp;
		String sNew = ".";
		for (String sElem : sNumbers) {
			// at the end we have a dot, and the split function creates an element with it
			if (!sElem.isEmpty()) {
				sNew += sElem + ".";
			}
		}
		return sNew;
	}
	
	/*
	 * Create a pattern from the long array
	 * 
	 * example .1.2.3.44.55.
	 * 
	 * @return the pattern
	 */
	public static String getStringForPattern(String[] ar) {
		String sForPattern = ".";
		
		for (String element : ar) {
			sForPattern += String.valueOf(element) + ".";
		}
		return sForPattern;
	}
	

}


