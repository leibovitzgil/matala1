import static org.junit.Assert.*;

import java.sql.Array;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

public class test {

	long start = new Date().getTime();
	
	@Test
	public void testTriangleProp(){
		String name_file = "myTest2.txt";
		assertTrue(Graph.hasTriangleProp(name_file));
		name_file = "myTest.txt";
		assertFalse(Graph.hasTriangleProp(name_file));
	}
	
	@Test
	public void testDiameter(){
		String name_file = "myTest2.txt";
		Graph g = new Graph(name_file);
		assertEquals(1.5,g.findDiameterNew(),0.00001);
		name_file = "test1graph.txt";
		g = new Graph(name_file);
		assertEquals(6106.319717290001,g.findDiameterNew(),0.00001);
	}
	@Test
	public void testRadius(){
		String name_file = "myTest2.txt";
		Graph g = new Graph(name_file);
		assertEquals(0.9,g.findRadiusNew(),0.00001);
		name_file = "test1graph.txt";
		g = new Graph(name_file);
		assertEquals(3211.78581843,g.findRadiusNew(),0.00001);
	}
	@Test
	public void testMinPrice_tinyEWD() {
		String name_file = "tinyGraph.txt";
		assertEquals(0.26,Graph.MinPrice2(name_file,0,2),0.00001);
		assertEquals(0.6,Graph.MinPrice2(name_file,0,7),0.00001);
		assertEquals(0.73,Graph.MinPrice2(name_file,0,5),0.00001);
		assertEquals(0.74,Graph.MinPrice2(name_file,7,6),0.00001);
		assertEquals(0.62,Graph.MinPrice2(name_file,2,5),0.00001);
		assertEquals(0.32,Graph.MinPrice2(name_file,1,5),0.00001);
		assertEquals(0.6,Graph.MinPrice2(name_file,7,0),0.00001);

	}
	@Test
	public void testPath_tinyEWD()
	{
		String name_file = "tinyGraph.txt";
		assertTrue(Graph.GetPath2(name_file, 0, 7).equals("0->2->7"));
		assertTrue(Graph.GetPath2(name_file, 7, 6).equals("7->2->6"));
		assertTrue(Graph.GetPath2(name_file, 2, 5).equals("2->7->5"));
		assertTrue(Graph.GetPath2(name_file, 1, 5).equals("1->5"));
		assertTrue(Graph.GetPath2(name_file, 5, 1).equals("5->1"));
	}

	@Test
	public void testBlackList_tinyEWD()
	{
		String name_file = "tinyGraph.txt";
		String name_file_BL = "test1.txt";
		double[]black=Graph.GetMinPriceWithBL(name_file, name_file_BL);

		assertEquals(0.35,black[0],0.00001);
		assertEquals(0.37,black[1],0.00001);
		assertEquals(0.28,black[2],0.00001);
		assertEquals(0.6000,black[3],0.00001);

	}
	

	@Test
	public void testMinPrice_tinyEWG() {
		String name_file = "tinyGraph2.txt";

		assertEquals(0.58,Graph.MinPrice2(name_file,0,6),0.00001);
		assertEquals(0.56,Graph.MinPrice2(name_file,1,4),0.00001);
		assertEquals(0.34,Graph.MinPrice2(name_file,2,7),0.00001);
		assertEquals(0.74,Graph.MinPrice2(name_file,7,6),0.00001);
		assertEquals(0.61,Graph.MinPrice2(name_file,3,5),0.00001);
		assertEquals(0.32,Graph.MinPrice2(name_file,1,5),0.00001);

	}

	@Test
	public void testPath_tinyEWG()
	{
		String name_file = "tinyGraph2.txt";

		assertTrue(Graph.GetPath2(name_file, 0, 7).equals("0->7"));
		assertTrue(Graph.GetPath2(name_file, 3, 5).equals("3->1->5"));
		assertTrue(Graph.GetPath2(name_file, 2, 4).equals("2->0->4"));
		assertTrue(Graph.GetPath2(name_file, 1, 0).equals("1->7->0"));
		assertTrue(Graph.GetPath2(name_file, 5, 6).equals("5->7->2->6"));
	}
	
	
	@Test
	public void testBlackList_tinyEWG()
	{
		String name_file = "mediumGraph2.txt";
		String name_file_BL = "test2.txt";
		double[]black=Graph.GetMinPriceWithBL(name_file, name_file_BL);

		assertEquals(0.71478,black[0],0.00001);
		assertEquals(0.73099,black[1],0.00001);
		assertEquals(0.71478,black[2],0.00001);
		assertEquals(0.76025,black[3],0.00001);

	}
	
	long s2 = new Date().getTime();
//	System.out.println("Done!!!  Total time: " + (s2 - start) + "  ms");

}
