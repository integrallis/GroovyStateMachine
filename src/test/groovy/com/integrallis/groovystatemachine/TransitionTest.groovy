package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase
import com.integrallis.groovystatemachine.Transition

class TransitionTest extends GroovyTestCase {

	def trans

	void setUp() {
		trans = new Transition(from:"single", to:"dating", guard:"isWillingToCommit")
	}
	
	void testConstruction() {
		assert trans.from == "single"
		assert trans.to == "dating"
		assert trans.guard == "isWillingToCommit"
	}

	void testEquals() {
		Transition otherTrans = new Transition(from:"single", to:"dating")
		assert trans == otherTrans
	}
	
	void testEquals_WithDifferentFroms() {
		Transition otherTrans = new Transition(from:"married", to:"dating")
		assert trans != otherTrans
	}

	void testEquals_WithDifferentTos() {
		Transition otherTrans = new Transition(from:"single", to:"exiled")
		assert trans != otherTrans
	}
	
	void testCanTransition_WithNoGuard() {
		trans = new Transition(from:"single", to:"dating")
		
		assert trans.canTransition(null)
	}
	
	void testCanTransition_WithStringGuard() {
		trans = new Transition(from:"single", to:"dating", guard:"isTrue")
		
		def mock = new Object()
		mock.metaClass.isTrue = { true }
		
		assert trans.canTransition(mock)
	}
	
	void testCanTransition_WithClosureGuard() {
		trans = new Transition(from:"single", to:"dating", guard:{ it.isTrue() })
		
		def mock = new Object()
		mock.metaClass.isTrue = { true }
		
		assert trans.canTransition(mock)
	}
}