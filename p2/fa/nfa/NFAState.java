package fa.nfa;

import java.util.HashMap;

import fa.State;

/**
 * Non- deterministic finite automata object. 
 * @author Luke Ptomey
 * @author Kyle Epperson
 */

public class NFAState extends State {
    

    private HashMap<Character,NFAState> delta;//delta
	private boolean isFinal;//remembers its type
    private NFAState previousState; //previous state that used transition to reach this current state
    private int level; //minimum number of transitions from start state not including empty transitons
	
	/**
	 * Default constructor
	 * @param name the state name
	 */
	public NFAState(String name){
		initDefault(name);
		isFinal = false;
		this.level=0;
	}
	
	/**
	 * Overlaoded constructor that sets the state type
	 * @param name the state name
	 * @param isFinal the type of state: true - final, false - nonfinal.
	 */
	public NFAState(String name, boolean isFinal){
		initDefault(name);
		this.isFinal = isFinal;
		this.level=0;
	}
	
	private void initDefault(String name ){
		this.name = name;
		delta = new HashMap<Character, NFAState>();
	}
	
	/**
	 * Accessor for the state type
	 * @return true if final and false otherwise
	 */
	public boolean isFinal(){
		return isFinal;
	}
	

	/**
	 * Add the transition from <code> this </code> object
	 * @param onSymb the alphabet symbol
	 * @param toState to DFA state
	 */
	public void addTransition(char onSymb, NFAState toState){
		delta.put(onSymb, toState);
	}
	
	/**
	 * Retrieves the state that <code>this</code> transitions to
	 * on the given symbol
	 * @param symb - the alphabet symbol
	 * @return the new state 
	 */
	public NFAState getTo(char symb){
		NFAState ret = delta.get(symb);
		if(ret == null){
			 System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
			 System.exit(2);
			}
		return delta.get(symb);
	}
	
	/**
	 * Sets previous state
	 * @param previousState
	 */
    public void setPreviousState(NFAState previousState){
        this.previousState = previousState;
	}
	
	/**
	 * Appends level of current state
	 */
	public void addLevel(){
		this.level++;
	}

	/**
	 * Gets current level of node
	 * @return current level
	 */
	public int getLevel(){
		return this.level;
	}

	/**
	 * Gets previous state
	 * @return previous state
	 */
	public NFAState getPreviouState(){
		return this.previousState;
	}

}
