import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class GraphSubsetTest {

	@Test
	public void testConvertToBinary() {
		String binary = GraphSubset.BigIntegertoBinaryString( BigInteger.valueOf(1) );
		assertEquals ( binary, "1" );
		binary = GraphSubset.BigIntegertoBinaryString( BigInteger.valueOf(2) );
		assertEquals ( binary, "10" );
		assertEquals (GraphSubset.BigIntegertoBinaryString( BigInteger.valueOf(3) ), "11");
		assertEquals (GraphSubset.BigIntegertoBinaryString( BigInteger.valueOf(4) ), "100");
		assertEquals (GraphSubset.BigIntegertoBinaryString( BigInteger.valueOf(7) ), "111");
		assertEquals (GraphSubset.BigIntegertoBinaryString( BigInteger.valueOf(33) ), "100001");
	}
	
	// creating
	
	public void testAbcPattern() {
		//String s = ".2.611.1.45.6.";
		String s = ".2.5.6.";
		TreeSet<String> set = new TreeSet<String>();
		int count = 0;
		int length = 5;
		String sPattern = "[.]{1}";
		for (int j = 0; j < length; j++) {
			sPattern += "\\d{1,6}[.]{1}";
			//System.out.println( "pattern " + (sPattern));
			for (int i = 0; i < s.length(); i++) {
				Pattern pattern = Pattern.compile(sPattern);
				Matcher matcher = pattern.matcher(s);
		
				
		        while(matcher.find()) {
		            count++;
		            String subset = s.substring(matcher.start(), matcher.end());
		            //System.out.println("found: " + count + " : " + subset );
		            set.add(subset);
		            //System.out.println("found: " + subset + " " + count + " : " + matcher.start() + " - " + matcher.end());
		        }
		        // shifting
		        //
		        
		        if (j == length - 1) break;
		        s = GraphSubset.shiftByDot(s);
		        
			}
			
			
		}

		assertTrue(count > 0);
		
	}
	
	@Test
	public void testStringForPattern() {
		String [] al = {"1","2"};
		assertEquals(GraphSubset.getStringForPattern( al ), ".1.2." );
		
		String[] al2 = {"2","5","7"};
		assertEquals(GraphSubset.getStringForPattern( al2 ), ".2.5.7." );

		String[] al3 = {"23","235","2567", "6755"};
		assertEquals(GraphSubset.getStringForPattern( al3 ), ".23.235.2567.6755." );
		
	}
	
	@Test
	public void testShiftByDot() {		
		String shifted = GraphSubset.shiftByDot(".1.2.");
		//System.out.println("shifted? " + shifted);
		assertEquals(shifted, ".2.1.");
		
		shifted = GraphSubset.shiftByDot(".1.12.");
		//System.out.println("shifted? " + shifted);
		assertEquals(shifted, ".12.1.");	
		
		shifted = GraphSubset.shiftByDot(".5.212.8577.784.");
		//System.out.println("shifted? " + shifted);
		assertEquals(shifted, ".784.5.212.8577.");		

		shifted = GraphSubset.shiftByDot(".1.");
		//System.out.println("shifted? " + shifted);
		assertEquals(shifted, ".1.");	
	}
	
	@Test
	public void testGetLongArrayFromSubset() {
		long [] al = {1,2};
		BigInteger [] lResult = GraphSubset.getLongArrayFromSubset( ".1.2." );
		for (int i = 0; i < al.length; i++) {
			assertEquals(lResult[i].longValue(), al[i] );
		}
		
		long[] al2 = {2,5,7};
		BigInteger [] lResult2 = GraphSubset.getLongArrayFromSubset( ".2.5.7." );
		for (int i = 0; i < al2.length; i++) {
			assertEquals(lResult2[i].longValue(), al2[i] );
		}

		long[] al3 = {23,235,2567, 6755};
		BigInteger[] lResult3 = GraphSubset.getLongArrayFromSubset( ".23.235.2567.6755." );
		for (int i = 0; i < al3.length; i++) {
			assertEquals(lResult3[i].longValue(), al3[i] );
		}
	}
	
	
	public void testgetAllSubset() {
		String[] al1 = {"2","5","7"};
		

		
		HashSet<String>subset = GraphSubset.getAllSubset(al1);
		int count = 1;
		for (String sSet : subset) {
			System.out.println("-- subset count: " + count++ + " value " + sSet);
		}
		assertEquals(subset.size(), 8);
	}
	
	@Test
	public void testGetEdgesFromSubset() {
		long edges = GraphSubset.getEdgesFromSubset(".3.");
		assertEquals( edges, 63);
		assertEquals(GraphSubset.getEdgesFromSubset(".12."), 63);
		assertEquals(GraphSubset.getEdgesFromSubset(".48."), 63);
		assertEquals(GraphSubset.getEdgesFromSubset(".192."), 63);
		edges = GraphSubset.getEdgesFromSubset(".193.");
		System.out.println("193 edges " + edges);
		assertEquals( edges, 63);
		edges = GraphSubset.getEdgesFromSubset(".45.");
		assertEquals( edges, 63);
	}
	
	@Test
	/*
	 * Test 2
	 * 10
	 * 1443715956382423 10257371478716904273 6044488427742238629 8678199957186201573 14627605043303437847 15403414528741127855 5712098152006364585 9861930995262443927 13124744789263455573 15387471612175301265
	 * 5713
	 * 
	 * Test 12
	 * 20
	 * 3 12 48 192 768 3072 12288 49152 196608 786432 3145728 12582912 50331648 201326592 805306368 3221225472 12884901888 51539607552 206158430208 824633720832
	 * 56623104
	 */
	public void testWhole() {
		String[] ar0 = {"2","5","9"};
		assertEquals(GraphSubset.getSubsetOuput(ar0), (long)504);
			
		 String[] ar1 = {"1443715956382423", "10257371478716904273",  "6044488427742238629", 
						"8678199957186201573", "14627605043303437847", "15403414528741127855", "5712098152006364585",
						"9861930995262443927",  "13124744789263455573", "15387471612175301265"};
		assertEquals(GraphSubset.getSubsetOuput(ar1), (long)5713);
		
		String[] ar2 = {"3", "12", "48", "192", "768", "3072", "12288", "49152", "196608",
				"786432", "3145728", "12582912", "50331648", "201326592", "805306368", "3221225472",
				"12884901888", "51539607552", "206158430208", "824633720832"};
		assertEquals(GraphSubset.getSubsetOuput(ar2), (long)56623104);
		
		
		String [] ar3 = {"0", "0", "0", "0", "0", "0", "0", "0", "0",
				"0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"};
				long output = GraphSubset.getSubsetOuput(ar3);
				System.out.println("Testhowe " +output);
				assertEquals(output, (long)67108864);
		
	}
	
	@Test
	public void testBigIntegerToBinary() {
		BigInteger bi1 = new BigInteger("2");
		assertEquals(GraphSubset.toBinaryString(bi1), "10");
		BigInteger bi2 = new BigInteger("129");
		assertEquals(GraphSubset.toBinaryString(bi2), "10000001");
		BigInteger bi3 = new BigInteger("15387471612175301265");
		System.out.println("bigint binary " + GraphSubset.toBinaryString(bi3));			
	}
	
	@Test 
	public void testGetAllNodes() {
		ArrayList<BigInteger>ind1 = new ArrayList<BigInteger>();
		ind1.add(new BigInteger("1")); ind1.add(new BigInteger("2")); ind1.add(new BigInteger("5")); ind1.add(new BigInteger("7"));
		ArrayList<String>nodes1 = new ArrayList<String>();
		nodes1.add("1;2");
		nodes1.add("1;5");
		nodes1.add("1;7");
		nodes1.add("2;5");
		nodes1.add("2;7");
		nodes1.add("5;7");
		assertEquals(GraphSubset.getAllNodes(ind1), nodes1);
		
		ind1.clear();
		nodes1.clear();
		ind1.add(new BigInteger("333")); ind1.add(new BigInteger("232")); 
		ind1.add(new BigInteger("5685")); ind1.add(new BigInteger("22344354327"));
		ind1.add(new BigInteger("15403414528741127855"));
		nodes1.add("333;232");
		nodes1.add("333;5685");
		nodes1.add("333;22344354327");
		nodes1.add("333;15403414528741127855");
		nodes1.add("232;5685");
		nodes1.add("232;22344354327");
		nodes1.add("232;15403414528741127855");
		nodes1.add("5685;22344354327");
		nodes1.add("5685;15403414528741127855");
		nodes1.add("22344354327;15403414528741127855");
		assertEquals(GraphSubset.getAllNodes(ind1), nodes1);
	}
	
	@Test
	public void testNormalizeNodes() {
		ArrayList<String> nodes = new ArrayList<String>();
		nodes.add("1;2");
		nodes.add("3;4");
		nodes.add("2;5");
		nodes.add("2;9");
		nodes.add("4;7");
		nodes.add("5;8");
		nodes.add("6;10");
		nodes.add("6;11");
		ArrayList<String> normalizedNodes = GraphSubset.normalizeNodes( nodes );
		int index = 0;
		for (String node : normalizedNodes) {
			System.out.println("normalized node " + index + ": " + node);
			switch (index) {
				case 0: assertEquals(node, "1;2;5;8"); break;
				case 1: assertEquals(node, "3;4;7"); break;
				case 2: assertEquals(node, "2;9"); break;
				case 3: assertEquals(node, "6;10"); break;
				case 4: assertEquals(node, "6;11"); break;
			}
			index++;
		}
		assertTrue(index > 0);
	}
	
	@Test
	public void testSortArray() {
		ArrayList<String> nodes = new ArrayList<String>();
		nodes.add("1;2");
		nodes.add("6;10");
		nodes.add("3;4");
		nodes.add("6;11");		
		nodes.add("2;5");
		nodes.add("2;9");
		nodes.add("4;7");
		nodes.add("5;8");
		GraphSubset.sortArray(nodes);
		int index = 0;
		for (String node : nodes) {
			System.out.println("sorted node " + index + ": " + node);
			switch (index) {
				case 0: assertEquals(node, "1;2"); break;
				case 1: assertEquals(node, "2;5"); break;
				case 2: assertEquals(node, "2;9"); break;
			}
			index++;
		}
		
	}
	
	@Test
	public void testFindSubsets() {
		HashSet<String> setIndex = new HashSet<String>();
		String[] ar = {"0", "1", "2", "3", "4"};
		GraphSubset.findSubsets( setIndex, ar );

		for (String subset : setIndex) {
			System.out.println("Found subset " + subset);
		}
		assertTrue(true);
	}
	
	@Test
	public void testCreateSubsetsFromIndex() {
		HashSet<String> setIndex = new HashSet<String>();
		HashSet<String> set = new HashSet<String>();
		String[] arIndex = {"0", "1", "2"};
		String[] ar = {"2", "5", "9"};
		GraphSubset.findSubsets( setIndex, arIndex );
		GraphSubset.createSubsetsFromIndex(setIndex, set, ar);

		for (String subset : set) {
			System.out.println("Created subset " + subset);
		}
		assertTrue(true);
	}
}
