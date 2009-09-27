package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase
import com.integrallis.groovystatemachine.Transition

class TransitionTest extends GroovyTestCase {

	def trans

	void setUp() {
		trans = new Transition(from:"single", to:"dating", guard:"isWillingToCommit", onTransition:"buyFlowers")
	}
	
	void testConstruction() {
		assert trans.from == "single"
		assert trans.to == "dating"
		assert trans.guard == "isWillingToCommit"
		assert trans.onTransition == "buyFlowers"
	}
	
	void testConstructor_WithMultipleFroms() {
		trans = new Transition(from:["single", "divorced"], to:"dating")
		assert trans.from == ["single", "divorced"]
		assert trans.to == "dating"
	}
	
	void testConstructor_WithMultipleTos() {
		trans = new Transition(from:"single", to:["dating", "married"])
		assert trans.from == "single"
		assert trans.to == ["dating", "married"]
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
	
	void testExecute_WithStringOnTransition() {
		trans = new Transition(from:"single", to:"dating", onTransition:{ obj, arg -> obj.buyFlowers(arg) })
		
		def mock = new TransitionMock()
		def obj = new Object()
		trans.execute(mock, obj)
		assert mock.result
		assert obj == mock.argsPassed
	}
}

class TransitionMock {
	def result = false
	def argsPassed
	
	def buyFlowers(Object args) {
		result = true
		argsPassed = args
	}
	
	def getResult() {
		def ret = result
		result = false
		ret
	}
}