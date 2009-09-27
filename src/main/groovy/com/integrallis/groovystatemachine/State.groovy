package com.integrallis.groovystatemachine

class State
{
    String name
	Map options

	State(args=[:], String name) {
		this.name = name
		options = args
	}

	boolean equals(other) {
		if(other.respondsTo("getName")) {
			return name == other.name
		}
		name == other
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
		}
	}
}
