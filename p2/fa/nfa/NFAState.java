package fa.nfa;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.State;

/**
 * Non- deterministic finite automata object. Includes hash map containing transitions, getters
 * and setters for level and previous states. Each NFAState also incudes final state instance
 * variable.
 * @author Luke Ptomey
 * @author Kyle Epperson
 */

public class NFAState extends State {
    

    private HashMap<Character, LinkedHashSet<NFAState>> delta;//delta
	private boolean isStart, isFinal;//remembers its type
    private NFAState previousState; //previous state that used transition to reach this current state
    private int level; //minimum number of transitions from start state not including empty transitons
	
	/**
	 * Default constructor
	 * @param name the state name
	 */
	public NFAState(String name){
		initDefault(name);
		isFinal = isStart = false;
		this.level=0;
	}
	
	/**
	 * Overlaoded constructor that sets the state type
	 * @param name the state name
	 * @param isFinal the type of state: true - final, false - nonfinal.
	 */
	public NFAState(String name, boolean isStart, boolean isFinal){
		initDefault(name);
		this.isFinal = isFinal;
		this.isStart = isStart;
		this.level=0;

	}
	
	private void initDefault(String name ){
		this.name = name;
		delta = new HashMap<Character, LinkedHashSet<NFAState>>();
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

		if(!delta.containsKey(onSymb)){	// Need to add new symb and set in map

			// Creates a new set for onSymb in map.
			LinkedHashSet<NFAState> newSet = new LinkedHashSet<>();
			newSet.add(toState);	// Adds the toState to that set

			// Puts onSymb and newSet in the map
			delta.put(onSymb, newSet);

		} else {	// Symb exists in map. Append toState to set.
			
			delta.get(onSymb).add(toState);
		}

	}

	/**
	 * Retrieves the state that <code>this</code> transitions to
	 * on the given symbol
	 * @param symb - the alphabet symbol
	 * @return The set of NFAStates that can be taken with symb
	 */
	public Set<NFAState> getTo(char symb){
		LinkedHashSet<NFAState> ret = delta.get(symb);
		if(ret == null){
			System.err.println("ERROR: DFAState.getTo(char symb) returns null on " + symb + " from " + name);
			System.exit(2);
		}
		return ret;
	}

	public Set<NFAState> getEStates(){

		if(delta.containsKey('e')) {
			return delta.get('e'); 
		}

		// Returns empty set if no transitions on symb e found (size == 0)
		return new LinkedHashSet<>();
	}
	
	/**
	 * Retrieves the state that <code>this</code> transitions to
	 * on the given symbol
	 * @param symb - the alphabet symbol
	 * @return the new state 
	 */
	// public NFAState getTo(char symb){
	// 	NFAState ret = delta.get(symb);
	// 	if(ret == null){
	// 		 System.err.println("ERROR: NFAState.getTo(char symb) returns null on " + symb + " from " + name);
	// 		 System.exit(2);
	// 		}
	// 	return delta.get(symb);
	// }
	
	/**
	 * Sets previous state
	 * @param previousState
	 */
    public void setPreviousState(NFAState oldState){
        previousState = oldState;
	}
	
	/**
	 * Appends level of current state
	 */
	public void addLevel(){
		level++;
	}

	/**
	 * Gets current level of node
	 * @return current level
	 */
	public int getLevel(){
		return level;
	}

	/**
	 * Gets previous state
	 * @return previous state
	 */
	public NFAState getPreviouState(){
		return previousState;
	}

	/**
	 * Sets start state
	 * @param value Dictiates whether state is start state
	 */
	public void setStartState(boolean value){
		isStart=value;
	}

	/**
	 * Checks whether state is start state
	 * @return true if startstate, false otherwise
	 */
	public boolean isStartState(){
		return isStart;
	}

	/**
	 * Sets state to final state
	 * @param value true if set finalstate, false otherwise
	 */
	public void setFinal(boolean value){
		isFinal = value;
	}

	/**
	 * Get name of NFA state
	 * @return name of NFA state
	 */
	public String getName(){
		return name;
	}

}
