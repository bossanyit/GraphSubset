import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

public class SolutionTest {

	@Test
	public void testEdgesFromBinary() {
		assertEquals (Solution.getEdgesFromBinary(1), 0);
		assertEquals (Solution.getEdgesFromBinary(2), 0);
		assertEquals (Solution.getEdgesFromBinary(3), 1);
		assertEquals (Solution.getEdgesFromBinary(4), 0);
		assertEquals (Solution.getEdgesFromBinary(7), 2);
		assertEquals (Solution.getEdgesFromBinary(33), 1);
	}
	
	@Test
	public void testCreateSubsets() {
		long ar[] = {1,2};
		HashSet<Subset>set = Solution.getAllSubset(ar);
		assertEquals (4, set.size());
		long ar2[] = {1,2,3};
		HashSet<Subset>set2 = Solution.getAllSubset(ar2);
		assertEquals (8, set2.size());		
	}

}
