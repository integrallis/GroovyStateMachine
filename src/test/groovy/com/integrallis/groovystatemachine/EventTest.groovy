package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase
import com.integrallis.groovystatemachine.Event

class EventTest extends GroovyTestCase {

	def event

	void setUp() {
		event = new Event("name") {
			transitions from:["a", "b"], to:"c"
		}
	}
	
	void testName() {
		assert "name", event.name
	}

	void testTransitionsCreated() {
		assertEquals 2, event.transitions.size
	}
}