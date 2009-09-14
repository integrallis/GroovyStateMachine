package com.integrallis.groovystatemachine

class State
{
    String name

	State(name) {
		this.name = name
	}

	boolean equals(other) {
		if(other.respondsTo("getName")) {
			return name == other.name
		}
		name == other
	}
}
