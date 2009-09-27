package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase
import com.integrallis.groovystatemachine.Event

class EventTest extends GroovyTestCase {

	def event

	void setUp() {
		event = new Event("name", 	success:"doSuccessCallback",  someString:"doSomething") {
			transitions from:["a", "b"], to:"c"
		}
	}
	
	void testConstruction() {
		assert "name", event.name
		assertEquals 2, event.transitions.size
		assertEquals "doSuccessCallback", event.success
		assert [success:"doSuccessCallback", someString:"doSomething"] == event.options
	}
	
	void testConstructor_WithNoOptionsOrTransitions() {
		event = new Event("name") {
			transitions from:["a", "b"], to:"c"
		}
		assert "name", event.name
		assertNull event.success
		assert [:] == event.options
	}
	
	void testExecuteSuccessCallback_WithStringOption() {
		use(EventMock) {
			def mock = new EventMock()
			event.executeSuccessCallback(mock)
			assert mock.result
		}
	}
	
	void testExecuteSuccessCallback_WithClosureOption() {
		event = new Event("name", success: { obj -> obj.doAnotherSuccessCallBack() } ){
			transitions from:["a", "b"], to:"c"
		}
		
		use(EventMock) {
			def mock = new EventMock()
			event.executeSuccessCallback(mock)
			assert mock.result
		}
	}
	
	void testExecuteSuccessCallback_WithArrayOption() {
		event = new Event("name", success: [{ obj -> obj.doSuccessCallback() }, "doAnotherSuccessCallBack"] ) {
			transitions from:["a", "b"], to:"c"
		}
		
		use(EventMockForSuccessiveMethodCalls) {
			def mock = new EventMockForSuccessiveMethodCalls()
			event.executeSuccessCallback(mock)
			assertEquals 2, mock.result
		}
	}
	
	void testExecuteSuccessCallback_WithSuccessCallbackSupplied() {
		use(EventMock) {
			def mock = new EventMock()
			event.executeSuccessCallback(mock, 'doSuccessCallback')
			assert mock.result
		}
	}
	
	void testCallAction_WithStringOption() {
		use(EventMock) {
			def mock = new EventMock()
			event.callAction("someString", mock)
			assert mock.result
		}
	}
	
	void testCallAction_WithClosureOption() {
		event = new Event("name", someString: { obj -> obj.doSomething() } ) {
			transitions from:["a", "b"], to:"c"
		}
		
		use(EventMock) {
			def mock = new EventMock()
			event.callAction("someString", mock)
			assert mock.result
		}
	}
	
	void testCallAction_WithArrayOption() {
		event = new Event("name", someArray: ["doSuccessCallback", "doAnotherSuccessCallBack"] ) {
			transitions from:["a", "b"], to:"c"
		}
		
		use(EventMockForSuccessiveMethodCalls) {
			def mock = new EventMockForSuccessiveMethodCalls()
			event.callAction("someArray", mock)
			assert mock.result
		}
	}
	
	void testFire_VerifyException() {
		event = new Event("test") { transitions from:["a", "b"], to:"c" }
		def gsm = new StateMachine()
		gsm.currentState = "d"
		shouldFail (InvalidTransitionException) { event.fire(gsm) }
	}
	
	void testFire_WithSingleToTransition() {
		event = new Event("test") { transitions from:"a", to:"b" }
		def gsm = new StateMachine()
		gsm.currentState = "a"
		assert "b" == event.fire(gsm)
	}
	
	void testFire_WithMultipleToTransition() {
		event = new Event("test") { transitions from:"a", to:["d", "e"] }
		def gsm = new StateMachine()
		gsm.currentState = "a"
		assert "d" == event.fire(gsm)
	}
	
	void testFire_WithMultipleFromTransition() {
		event = new Event("test") { transitions from:["a", "b"], to:"d" }
		def gsm = new StateMachine()
		gsm.currentState = "b"
		assert "d" == event.fire(gsm)
	}
	
	void testFire_WithMultipleFromAndToTransition() {
		event = new Event("test") { transitions from:["a", "b"], to:["d", "e"] }
		def gsm = new StateMachine()
		gsm.currentState = "b"
		assert "d" == event.fire(gsm)
	}
	
	void testFire_WithMultipleFromAndToTransitionAndToStateSupplied() {
		event = new Event("test") { transitions from:["a", "b"], to:["d", "e"] }
		def gsm = new StateMachine()
		gsm.currentState = "b"
		assert "e" == event.fire(gsm, "e")
	}
}

class EventMock {
	def result = false
	
	def doSuccessCallback(EventMock self) {
		result = true
	}
	
	def doAnotherSuccessCallBack(EventMock self) {
		result = true
	}
	
	def doSomething(EventMock self) {
		result = true
	}
	
	def getResult() {
		def ret = result
		result = false
		ret
	}
}

class EventMockForSuccessiveMethodCalls {
	def result = 0
	
	def doSuccessCallback(EventMock self) {
		result++
	}
	
	def doAnotherSuccessCallBack(EventMock self) {
		result++
	}
	
	def getResult() {
		def ret = result
		result = 0
		ret
	}
}