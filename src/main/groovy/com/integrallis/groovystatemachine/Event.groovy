package com.integrallis.groovystatemachine

class Event {
	
	String name
	def transitions = []
	
	Event(name, Closure c) {
		this.name = name
		this.identity(c)
	}
	
	private void transitions(args) {
		def froms = new ArrayList()
		froms.add(args.from)
		froms.flatten().each {
			args.from = it
			transitions << new Transition(args)
		}
	}
	
	/**
	 * Make sure the setters do nothing
	 * Make them private so they don't show up in the declaredMethods() list
	 */
	private void setTransitions(transitions) { /*do nothing here*/ }
}