package com.example.unitTest;

import android.test.AndroidTestCase;

public class CalcTest extends AndroidTestCase {

	public void testAdd(){
		Calc calc = new Calc();
		int result = calc.add(4, 5);
		assertEquals(9, result);
	}
	
}
