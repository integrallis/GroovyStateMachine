package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase
import com.integrallis.groovystatemachine.State

class StateTest extends GroovyTestCase
{
	def state
	
	void setUp() {
		state = new State("arrive", enter:"doEnter", exit:"doExit")
	}
	
	void testConstructorWithNoOptions() {
		state = new State("arrive")
		assert state.name == "arrive"
		assert state.options == [:]
	}
	
	void testConstructorWithOptions() {
		assert state.name == "arrive"
		assert state.options == [enter:"doEnter", exit:"doExit"]
	}
	
	void testEquals() {
		def otherA = new State("arrive")
		assert state == otherA
	}
	
	void testEquals_WithString() {
		assert state.equals("arrive")
	}
	
	void testCallAction_WithStringOption() {
		def mock = new StateMock()
		state.callAction("enter", mock)
		assert mock.result
	}
	
	void testCallAction_WithClosureOption() {
		State a = new State("arrive", enter: { obj -> obj.doEnter() })
		def mock = new StateMock()
		a.callAction("enter", mock)
		assert mock.result
	}
}

class StateMock {
	def result = false
	
	def doEnter() {
		result = true
	}
	
	def getResult() {
		def ret = result
		result = false
		ret
	}
}
