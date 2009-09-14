package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase

class StateMachineTest extends GroovyTestCase {
	
	def stateMachine
	
	void setUp() {
		stateMachine = new StateMachine()
		stateMachine.gsmState("state")
	}
	
	void testCreateState() {
		stateMachine = new StateMachine()
		stateMachine.createState("state")
		assertEquals 1, stateMachine.states.size()
		assertEquals "state", stateMachine.states.first().name
	}
	
	void testCreateState_AssertNoDuplicates() {
		stateMachine = new StateMachine()
		stateMachine.createState("state")
		stateMachine.createState("state") //duplicate
		assertEquals 1, stateMachine.states.size() //assert did not add duplicate
	}
	
	void testGsmInitialState() {
		stateMachine.gsmInitialState("initialState")
		assertEquals "initialState", stateMachine.initialState
	}
	
	void testGsmState_AssertInitialStateSet() {
		assertEquals 1, stateMachine.states.size()
		assertEquals "state", stateMachine.states.first().name
		//initial state should be set
		assertEquals "state", stateMachine.initialState
	}
	
	void testGsmStateWithInitialStateSet_AssertInitialStateNotSet() {
		stateMachine.gsmInitialState("initial")
		stateMachine.gsmState("state") //set state again to make sure
		
		//initial state should be set
		assertEquals "initial", stateMachine.initialState
	}
	
	void testGsmState_RespondsToGetter() {
		assert stateMachine.respondsTo("isState")
	}
	
	void testGsmState_AssertCurrentState() {
		stateMachine.currentState = "state" //set current state
		assert stateMachine.isState()
	}
	
	void testStateObjectForState() {
		stateMachine.gsmState("otherState")
		
		def obj = stateMachine.states.find { it.name == "otherState"}
		assert obj == stateMachine.stateObjectForState("otherState")
	}
	
	void testStateObjectForState_WithNonExistentState() {
		shouldFail(UndefinedStateException) { 
			stateMachine.stateObjectForState("non-existent") 
		}
	}
}