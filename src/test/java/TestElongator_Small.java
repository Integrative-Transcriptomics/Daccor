

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import elongation.Elongator;
/**
 * 
 * @author Friederike Hanssen
 *
 */
public class TestElongator_Small {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Sequence1 Sequence1 = new Sequence1();
		Sequence1.setUp();
		Sequence2 Sequence2 = new Sequence2();
		Sequence2.setUp();
		Sequence3 Sequence3 = new Sequence3();
		Sequence3.setUp();
		Sequence4 Sequence4 = new Sequence4();
		Sequence4.setUp();
		Sequence5 Sequence5 = new Sequence5();
		Sequence5.setUp();
		Sequence6 Sequence6 = new Sequence6();
		Sequence6.setUp();
		Sequence7 Sequence7 = new Sequence7();
		Sequence7.setUp();
		Sequence8 Sequence8 = new Sequence8();
		Sequence8.setUp();
		Sequence9 Sequence9 = new Sequence9();
		Sequence9.setUp();
		Sequence10 Sequence10 = new Sequence10();
		Sequence10.setUp();
		Sequence11 Sequence11 = new Sequence11();
		Sequence11.setUp();
		Sequence12 Sequence12 = new Sequence12();
		Sequence12.setUp();
		Sequence13 Sequence13 = new Sequence13();
		Sequence13.setUp();
		Sequence14 Sequence14 = new Sequence14();
		Sequence14.setUp();
		Sequence15 Sequence15 = new Sequence15();
		Sequence15.setUp();
		Sequence16 Sequence16 = new Sequence16();
		Sequence16.setUp();
		Sequence17 Sequence17 = new Sequence17();
		Sequence17.setUp();
		Sequence18 Sequence18 = new Sequence18();
		Sequence18.setUp();
		Sequence19 Sequence19 = new Sequence19();
		Sequence19.setUp();
		Sequence20 Sequence20 = new Sequence20();
		Sequence20.setUp();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testExtender() {
	}

	@Test
	public void testInitialize() {
		
	}

	@Test
	public void testRun() {
		Elongator extender1 = new Elongator(Sequence1.seq,4,0,1,true);
		extender1.initialize();
		extender1.run();
		assertEquals(Sequence1.finalResult_small, extender1.getRepeats());
		
		extender1 = new Elongator(Sequence1.seq,4,1,1,true);
		extender1.initialize();
		extender1.run();
		assertEquals(Sequence1.finalResult_small, extender1.getRepeats());

		extender1 = new Elongator(Sequence1.seq,4,2,1,true);
		extender1.initialize();
		extender1.run();
		assertEquals(Sequence1.finalResult_small, extender1.getRepeats());

		Elongator extender2 = new Elongator(Sequence2.seq,4,0,1,true);
		extender2.initialize();
		extender2.run();
		assertEquals(Sequence2.finalResult_small, extender2.getRepeats());

		extender2 = new Elongator(Sequence2.seq,4,1,1,true);
		extender2.initialize();
		extender2.run();
		assertEquals(Sequence2.finalResult_small, extender2.getRepeats());

		extender2 = new Elongator(Sequence2.seq,4,2,1,true);
		extender2.initialize();
		extender2.run();
		assertEquals(Sequence2.finalResult_small, extender2.getRepeats());

		Elongator extender3 = new Elongator(Sequence3.seq,4,0,1,true);
		extender3.initialize();
		extender3.run();
		assertEquals(Sequence3.finalResult_small, extender3.getRepeats());

		extender3 = new Elongator(Sequence3.seq,4,1,1,true);
		extender3.initialize();
		extender3.run();
		assertEquals(Sequence3.finalResult_small, extender3.getRepeats());

		extender3 = new Elongator(Sequence3.seq,4,2,1,true);
		extender3.initialize();
		extender3.run();
		assertEquals(Sequence3.finalResult_small, extender3.getRepeats());

		Elongator extender4 = new Elongator(Sequence4.seq,4,0,1,true);
		extender4.initialize();
		extender4.run();
		assertEquals(Sequence4.finalResult_small, extender4.getRepeats());

		extender4 = new Elongator(Sequence4.seq,4,1,1,true);
		extender4.initialize();
		extender4.run();
		assertEquals(Sequence4.finalResult_small, extender4.getRepeats());

		extender4 = new Elongator(Sequence4.seq,4,2,1,true);
		extender4.initialize();
		extender4.run();
		assertEquals(Sequence4.finalResult_small, extender4.getRepeats());

		Elongator extender5 = new Elongator(Sequence5.seq,4,0,1,true);
		extender5.initialize();
		extender5.run();
		assertEquals(Sequence5.finalResult_small, extender5.getRepeats());

		extender5 = new Elongator(Sequence5.seq,4,1,1,true);
		extender5.initialize();
		extender5.run();
		assertEquals(Sequence5.finalResult_small, extender5.getRepeats());

		extender5 = new Elongator( Sequence5.seq,4,2,1,true);
		extender5.initialize();
		extender5.run();
		assertEquals(Sequence5.finalResult_small, extender5.getRepeats());

		Elongator extender6 = new Elongator(Sequence6.seq,4,0,1,true);
		extender6.initialize();
		extender6.run();
		assertEquals(Sequence6.finalResult_small, extender6.getRepeats());

		extender6 = new Elongator(Sequence6.seq,4,1,1,true);
		extender6.initialize();
		extender6.run();
		assertEquals(Sequence6.finalResult_small, extender6.getRepeats());

		extender6 = new Elongator(Sequence6.seq,4,2,1,true);
		extender6.initialize();
		extender6.run();
		assertEquals(Sequence6.finalResult_small, extender6.getRepeats());

		Elongator extender7 = new Elongator(Sequence7.seq,4,0,1,true);
		extender7.initialize();
		extender7.run();
		assertEquals(Sequence7.finalResult_small, extender7.getRepeats());

		extender7 = new Elongator(Sequence7.seq,4,1,1,true);
		extender7.initialize();
		extender7.run();
		assertEquals(Sequence7.finalResult_small, extender7.getRepeats());

		extender7 = new Elongator(Sequence7.seq,4,2,1,true);
		extender7.initialize();
		extender7.run();
		assertEquals(Sequence7.finalResult_small, extender7.getRepeats());
	
		Elongator extender8 = new Elongator(Sequence8.seq,4,1,1,true);
		extender8.initialize();
		extender8.run();
		assertEquals(Sequence8.finalResult_small, extender8.getRepeats());

		extender8 = new Elongator(Sequence8.seq,4,2,1,true);
		extender8.initialize();
		extender8.run();
		assertEquals(Sequence8.finalResult_small, extender8.getRepeats());
		
		Elongator extender9 = new Elongator(Sequence9.seq,4,1,1,true);
		extender9.initialize();
		extender9.run();
		assertEquals(Sequence9.finalResult_small, extender9.getRepeats());

		extender9 = new Elongator(Sequence9.seq,4,2,1,true);
		extender9.initialize();
		extender9.run();
		assertEquals(Sequence9.finalResult_small, extender9.getRepeats());
		
		Elongator extender10 = new Elongator(Sequence10.seq,4,0,1,true);
		extender10.initialize();
		extender10.run();
		assertEquals(Sequence10.finalResult_small, extender10.getRepeats());
		extender10 = new Elongator(Sequence10.seq,4,1,1,true);
		extender10.initialize();
		extender10.run();
		assertEquals(Sequence10.finalResult_small, extender10.getRepeats());

		extender10 = new Elongator(Sequence10.seq,4,2,1,true);
		extender10.initialize();
		extender10.run();
		assertEquals(Sequence10.finalResult_small, extender10.getRepeats());
		
		Elongator extender11 = new Elongator(Sequence11.seq,4,1,1,true);
		extender11.initialize();
		extender11.run();
		assertEquals(Sequence11.finalResult_small, extender11.getRepeats());

		extender11 = new Elongator(Sequence11.seq,4,2,1,true);
		extender11.initialize();
		extender11.run();
		assertEquals(Sequence11.finalResult_small, extender11.getRepeats());
		
		Elongator extender12 = new Elongator(Sequence12.seq,4,1,1,true);
		extender12.initialize();
		extender12.run();
		assertEquals(Sequence12.finalResult_small, extender12.getRepeats());

		extender12 = new Elongator(Sequence12.seq,4,2,1,true);
		extender12.initialize();
		extender12.run();
		assertEquals(Sequence12.finalResult_small, extender12.getRepeats());

		Elongator extender13 = new Elongator(Sequence13.seq,4,1,1,true);
		extender13.initialize();
		extender13.run();
		assertEquals(Sequence13.finalResult_small, extender13.getRepeats());

		extender13 = new Elongator(Sequence13.seq,4,2,1,true);
		extender13.initialize();
		extender13.run();
		assertEquals(Sequence13.finalResult_small, extender13.getRepeats());

		Elongator extender14 = new Elongator(Sequence14.seq,4,4,1,true);
		extender14.initialize();
		extender14.run();
		assertEquals(Sequence14.finalResult_small, extender14.getRepeats());
		
		Elongator extender15 = new Elongator(Sequence15.seq,4,7,1,true);
		extender15.initialize();
		extender15.run();
		assertEquals(Sequence15.finalResult_small, extender15.getRepeats());
		
//		Elongator extender16 = new Elongator(Sequence16.seq,4,1,1,true);
//		extender16.initialize();
//		extender16.run();
//		assertEquals(Sequence16.finalResult_small, extender16.getRepeats());
		
		Elongator extender17= new Elongator(Sequence17.seq,4,4,1,true);
		extender17.initialize();
		extender17.run();
		assertEquals(Sequence17.finalResult_small, extender17.getRepeats());
		
//		System.out.println("\n##################################\n");
//
//		System.out.println("Sequence 18\n");
//		System.err.println("Sequence 18\n");
//		
//		Elongator extender18= new Elongator(Sequence18.seq,4,4,1);
//		extender18.initialize();
//		extender18.run();
//		System.out.println(extender18.getRepeats().getMapRepeats().toString());
//		System.out.println("expected: "+ Sequence17.finalResult_small.getMapRepeats().toString());
////		assertEquals(Sequence18.finalResult_small, extender17.getRepeats());
//		
//		System.out.println("\n##################################\n");
//
//		System.out.println("Sequence 19\n");
//
//		Elongator extender19 = new Elongator(Sequence19.seq,4,4,1,true);
//		extender19.initialize();
//		extender19.run();
//		System.out.println(extender19.getRepeats().getMapRepeats().toString());
//		System.out.println("expected: "+ Sequence19.finalResult_small.getMapRepeats().toString());
//		assertEquals(Sequence19.finalResult_small, extender19.getRepeats());
//
//		
//		System.out.println("\n##################################\n");
//
//		Elongator extender20 = new Elongator(Sequence20.seq,4,4,1);
//		extender20.initialize();
//		extender20.run();
//		System.out.println(extender20.getRepeats().getMapRepeats().toString());
//		//System.out.println("expected: "+ Sequence17.finalResult_small.getMapRepeats().toString());
//		//(assertEquals(Sequence17.finalResult_small, extender17.getRepeats());
//
//		
//		System.err.flush();;
	}

	@Test
	public void testGetRepeats() {
		
	}

}