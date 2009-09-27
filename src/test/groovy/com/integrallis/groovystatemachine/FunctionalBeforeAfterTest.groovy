package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase
import groovy.mock.interceptor.*


class BeforeAfterTest extends GroovyTestCase {
	
	def beforeAfter
	
	void setUp() {
		println "---- NEW -------"
		beforeAfter = new BeforeAfter()
		println "---- NEW ENDED -------"
	}
	
	void testCloseCallbacks() {
		println "--------------------"
		beforeAfter.fireClose()
		println "--------------------"		
	}
	
	void testOpenCallBacks() {
		beforeAfter.fireClose()
		beforeAfter.fireOpen()
	}
}
//@Mixin(StateMachine)
class BeforeAfter extends StateMachine {
	{
		gsmInitialState("open")
		gsmState("open",
			beforeEnter: "beforeEnterOpen",
			beforeExit: "beforeExitOpen",
			afterEnter: "afterEnterOpen",
			afterExit: "afterExitOpen"
		)
		
		gsmState("closed",
			beforeEnter: "beforeEnterClosed", 
			beforeExit: "beforeExitClosed", 
			afterEnter: "afterEnterClosed", 
			afterExit: "afterExitClosed"
		)
		
		gsmEvent("close", before: "beforeClose", after: "afterClose") {
			transitions from: "open", to: "closed"
		}
		
		gsmEvent("open", before: "beforeOpen", after: "afterOpen") {
			transitions from: "closed", to: "open"
		}
	}    
	

	def beforeClose() {
		println "Called beforeClose"
	}
	
	def afterClose() {
		println "Called afterClose"
	}
	
	def beforeOpen() {
		println "Called beforeOpen"
	}
	
	def afterOpen() {
		println "Called afterOpen"
	}

	def beforeEnterOpen() {
		println "Called beforeEnterOpen"
	}
	
	def beforeExitOpen() {
		println "Called beforeExitOpen"
	}     
	
	def afterEnterOpen() {
		println "Called afterEnterOpen"
	}
	
	def afterExitOpen() {
		println "Called afterExitOpen"
	}

	def beforeEnterClosed() {
		println "Called beforeEnterClosed"
	}
	
	def beforeExitClosed() {
		println "Called beforeExitClosed"
	} 
	
	def afterEnterClosed() {
		println "Called afterEnterClosed"
	} 
	
	def afterExitClosed() {
		println "Called afterExitClosed"
	}
	
	def gsmEventFired(eventName, oldStateName, newStateName) {
		println "Called gsmEventFired"
	}
	
	def gsmEventFailed(eventName, oldStateName) {
		println "Called gsmEventFailed"
	}
	
}
	