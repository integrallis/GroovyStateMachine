package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase
import com.integrallis.groovystatemachine.State

class StateTest extends GroovyTestCase
{
	void testEquals() {
		State a = new State("a")
		State otherA = new State("a")
		
		assert a == otherA
	}
	
	void testEquals_WithString() {
		State a = new State("a")
		
		assert a.equals("a")
	}
}
