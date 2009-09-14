package com.integrallis.groovystatemachine

class Transition {

	def from
	def to
	def guard
	def opts
	
	Transition(args) {
		(from, to, guard) = [args.from, args.to, args.guard]
		opts = args
	}
	
	boolean equals(other) {
		(from == other.from) && (to == other.to)
	}
	
	boolean canTransition(callee) {
		switch(guard) {
	    case Closure:
	        return callee.identity(guard)
	    case String:
	        return callee.metaClass.getMetaMethod(guard, null).invoke(callee, null)
	    default:
	        return true
	    }
	}
}