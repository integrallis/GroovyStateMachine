package com.integrallis.groovystatemachine

import groovy.util.GroovyTestCase

class RelationshipStateMachineFunctionalTest extends GroovyTestCase {
	
	def relationshipGSM
	
	void setUp() {
		relationshipGSM = new RelationshipStateMachine()
	}
	
	void testDefaultInitialState() {
		assertEquals "dating", relationshipGSM.getCurrentState()
		assertTrue relationshipGSM.isHappy()
	}
	
	void testOptionalInitialState() {
		relationshipGSM = new RelationshipStateMachine(strictlyForFun:true)
		assertEquals "intimate", relationshipGSM.getCurrentState()
		assertTrue relationshipGSM.isVeryHappy()
	}
	
	void testGettingIntimate_WhenNotDrunk() {
		relationshipGSM.fireGetIntimate()
		assertEquals "dating", relationshipGSM.getCurrentState()
	}
	
	void testGettingIntimate_WhenDrunk() {
		//is drunk... go straight to intimate
		relationshipGSM = new RelationshipStateMachine(drunk:true)
		relationshipGSM.fireGetIntimate()
		assertEquals "intimate", relationshipGSM.getCurrentState()
		assertTrue relationshipGSM.isVeryHappy()
	}
	
	void testGettingDivorced_VerifyExceptionIfCurrentStateNotMarried() {
		shouldFail(InvalidTransitionException) {
			relationshipGSM.fireWorkTooHard()
		}
	}
	
	void testGettingMarried_FromInitialState() {
		// being paranoid - verify initial state
		assertEquals "dating", relationshipGSM.getCurrentState()
		assertTrue relationshipGSM.isWillingToGiveUpManhood()
		
		relationshipGSM.fireGetMarried()
		assertTrue relationshipGSM.isDepressed()
		assertEquals "married", relationshipGSM.currentState
		assertFalse relationshipGSM.isIntimate()
	}
	
	void testGettingMarried_FromIntimate() {
		relationshipGSM = new RelationshipStateMachine(drunk:true)
		relationshipGSM.fireGetIntimate()
		// being paranoid - verify initial state
		assertEquals "intimate", relationshipGSM.getCurrentState()
		assertTrue relationshipGSM.isWillingToGiveUpManhood()
		
		relationshipGSM.fireGetMarried()
		assertTrue relationshipGSM.isDepressed()
		assertEquals "married", relationshipGSM.currentState
		assertFalse relationshipGSM.isIntimate()
	}
	
	void testGettingDivorced() {
		relationshipGSM.fireGetMarried()
		
		relationshipGSM.fireWorkTooHard()
		assertTrue relationshipGSM.isGivenUp()
	}
}


class RelationshipStateMachine extends StateMachine {
	
	boolean strictlyForFun = false;
	boolean drunk = false;
	boolean willingToGiveUpManhood = true;
	boolean givenUp = false;
	boolean intimate = true;
	boolean notOnSpeakingTerms = false;
	boolean veryHappy = false;
	boolean depressed = false;
	boolean happy = false;
	
	{
		gsmInitialState { it.isStrictlyForFun() ? "intimate" : "dating" }

		gsmState "dating",   enter:"makeHappy",       exit:"makeDepressed"
		gsmState "intimate", enter:"makeVeryHappy",   exit:"neverSpeakAgain"
		gsmState "married",  enter:"giveUpIntimacy",  exit:"buyExoticCarAndWearACombover"
		gsmState "divorced"

		gsmEvent "getIntimate", {
			transitions to:"intimate", from:"dating", guard:"isDrunk"
		}
		
		gsmEvent "getMarried", {
			transitions to:"married", from:["dating", "intimate"], guard:"isWillingToGiveUpManhood"
		}
		
		gsmEvent "workTooHard", {
			transitions to:"divorced", from:"married"
		}
	}
	
	def makeHappy() {
		happy = true
	}
	
	def makeDepressed() {
	    depressed = true
	}
	
	def makeVeryHappy() {
	    veryHappy = true
	}
	
	def neverSpeakAgain() {
	    notOnSpeakingTerms = true
	}
	
	def giveUpIntimacy() {
	    intimate = false
	}
	
	def buyExoticCarAndWearACombover() {
	    givenUp = true
	}

}






