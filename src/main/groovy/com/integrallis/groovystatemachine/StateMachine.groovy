package com.integrallis.groovystatemachine

class StateMachine {
	
	def initialState
	String currentState
	def states = []
	def events = [:]
	
	def createState(args=[:], String name) {
		if(!states.find { it == name }) {
			states << new State(args, name)
		}
	}
	
	def getCurrentState() {
		if(currentState) return currentState;
		
		def stateName = determineStateName(initialState)
		def state = stateObjectForState(stateName)

		state.callAction("before_enter", this)
		state.callAction("enter", this)
		this.currentState = stateName
		state.callAction("after_enter", this)

		return stateName
	}
	
	def setCurrentState(state) {
		currentState = state
	}
	
	def determineStateName(state) {
		switch(state) {
		case Closure:
			return this.with(state)
		case String:
        	return state			
		}
	}
	
	def gsmInitialState(initialState) {
		this.initialState = initialState
	}
	
	def gsmState(args=[:], String state) {
		createState(args, state)
		//set initial set if not already set
		if(!initialState) {
			initialState = state
		}
		
		StateMachine.metaClass."is${state.getAt(0).toUpperCase()}${state.substring(1)}" = {
			currentState == state
		}
	}
	
	def stateObjectForState(String name) {
		def obj = states.find { it == name }
		if(!obj) throw new UndefinedStateException("State ${name} does not exist" as String)
		obj
	}
	
	def gsmEvent(options = [:], name, Closure transitions) {
		if(!events[name]) {
			events[name] = new Event(options, name, transitions)
		}
		
		StateMachine.metaClass."fire${name.getAt(0).toUpperCase()}${name.substring(1)}" = { Object... args ->
			fireEvent(name, args)
		}
	}
	
	def gsmEventFired(eventName, oldStateName, newStateName) {
		//override for post-fire callback on success
	}
	
	def gsmEventFailed(eventName, oldStateName) {
		//override for post-fire callback on failure
	}
	
	def fireEvent(name, args) {
		def oldState = stateObjectForState(getCurrentState())
		def event = events[name]
		
		oldState.callAction("exit", this)
		
		event.callAction("before", this)		
		
		def newStateName = event.fire(this, null, args)
		
		if(newStateName) {
			def newState = stateObjectForState(newStateName)
						
			oldState.callAction("beforeExit", this)
			newState.callAction("beforeEnter", this)
			newState.callAction("enter", this)
			
			this.currentState = newStateName
			
			oldState.callAction("afterExit", this)
			newState.callAction("afterEnter", this)
			
			gsmEventFired(name, oldState.name, getCurrentState())
		} else {
			gsmEventFailed(name, oldState.name)
		}
	}
}