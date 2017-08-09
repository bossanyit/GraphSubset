import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class GraphSubsetTest {

	@Test
	public void testEdgesFromBinary() {
		assertEquals (GraphSubset.getNodesFromBinary(1), 1);
		assertEquals (GraphSubset.getNodesFromBinary(2), 1);
		assertEquals (GraphSubset.getNodesFromBinary(3), 2);
		assertEquals (GraphSubset.getNodesFromBinary(4), 1);
		assertEquals (GraphSubset.getNodesFromBinary(7), 3);
		assertEquals (GraphSubset.getNodesFromBinary(33), 2);
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
		for (String element : set) {
			System.out.println(element);
			
		}
		assertTrue(count > 0);
		//Pattern.matches("(.*)d+(.*)", "25");
		
	}
	
	@Test
	public void testStringForPattern() {
		long [] al = {1,2};
		assertEquals(GraphSubset.getStringForPattern( al ), ".1.2." );
		
		long[] al2 = {2,5,7};
		assertEquals(GraphSubset.getStringForPattern( al2 ), ".2.5.7." );

		long[] al3 = {23,235,2567, 6755};
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
		long [] lResult = GraphSubset.getLongArrayFromSubset( ".1.2." );
		for (int i = 0; i < al.length; i++) {
			assertEquals(lResult[i], al[i] );
		}
		
		long[] al2 = {2,5,7};
		long [] lResult2 = GraphSubset.getLongArrayFromSubset( ".2.5.7." );
		for (int i = 0; i < al2.length; i++) {
			assertEquals(lResult2[i], al2[i] );
		}

		long[] al3 = {23,235,2567, 6755};
		long[] lResult3 = GraphSubset.getLongArrayFromSubset( ".23.235.2567.6755." );
		for (int i = 0; i < al3.length; i++) {
			assertEquals(lResult3[i], al3[i] );
		}
	}
	
	@Test
	public void testgetAllSubset() {
		long[] al1 = {2,5,7};
		

		
		HashSet<String>subset = GraphSubset.getAllSubset(al1);
		int count = 1;
		for (String sSet : subset) {
			System.out.println("-- subset count: " + count++ + " value " + sSet);
		}
		assertEquals(subset.size(), 8);
	}
}
