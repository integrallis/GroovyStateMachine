package com.integrallis.groovystatemachine

class StateMachine {
	
	String initialState
	String currentState
	def states = []
	def events = []
	
	def createState(name) {
		if(!states.find { it == name }) {
			states << new State(name)
		}
	}
	
	def gsmInitialState(initialState=null) {
		this.initialState = initialState
	}
	
	def gsmState(state) {
		createState(state)
		//set initial set if not already set
		if(!initialState) {
			initialState = state
		}
		
		StateMachine.metaClass."is${state.getAt(0).toUpperCase()}${state.substring(1)}" = {
			currentState == state
		}
	}
	
	def stateObjectForState(name) {
		def obj = states.find { it == name }
		if(!obj) throw new UndefinedStateException("State ${name} does not exist" as String)
		obj
	}
}