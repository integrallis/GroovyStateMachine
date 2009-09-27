package com.integrallis.groovystatemachine

class Event {
	
	String name
	def success
	Map options
	def transitions = []
	
	Event(args = [:], String name, Closure c) {
		this.name = name
		success = args.success
		options = args
		if(c) this.with(c)
	}
	
	def fire(gsm, toState = null, Object... args) {
		def allTrans = transitions.findAll { it.from == gsm.currentState }
		if(!allTrans) throw new InvalidTransitionException("Event ${name} cannot transition from ${gsm.currentState}" as String)
		
		def nextState = null
		for(transition in allTrans) {
			def tos = new ArrayList()
			tos.add(transition.to)
			tos = tos.flatten()
			
			if(toState && !(tos.contains(toState))) {
				continue
			}
			if(transition.canTransition(gsm)) {
				nextState = toState?: tos.first()
				transition.execute(gsm, *args)
				break
			}
		}
		nextState
	}
	
	def executeSuccessCallback(gsm, success = null) {
		def callback = success ?: this.success
		switch(callback) {
		case Closure:
			gsm.with(callback)
			break
		case String:
        	gsm.invokeMethod(callback, null)			
			break
		case List:
			callback.each{ meth -> executeSuccessCallback(gsm, meth) }
			break
		}
	}
	
	def callAction(action, gsm) {
		def act = options[action]
		switch(act) {
	    case Closure:
	        gsm.with(act)
			break
	    case String:
	        gsm.invokeMethod(act, null)			
			break
		case List:
			act.each { meth -> gsm.invokeMethod(meth, null) }
			break
		}
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