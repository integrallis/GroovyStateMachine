package com.integrallis.groovystatemachine

class Transition {

	def from
	def to
	def guard
	def onTransition
	def opts
	
	Transition(args) {
		(from, to, guard, onTransition) = [args.from, args.to, args.guard, args.onTransition]
		opts = args
	}
	
	boolean equals(other) {
		(from == other.from) && (to == other.to)
	}
	
	boolean canTransition(gsm) {
		switch(guard) {
	    case Closure:
	        return gsm.with(guard)
	    case String:
	        return gsm.invokeMethod(guard, null)
	    default:
	        return true
	    }
	}
	
	def execute(gsm, Object... args) {
		switch(onTransition) {
		case Closure:
			onTransition.delegate = gsm
			onTransition.call(gsm, *args)
			break
		case String:
			gsm.invokeMethod(onTransition, args)
			break
		}
	}
}