package fa.nfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import fa.State;
import fa.dfa.DFA;
import fa.dfa.DFAState;

/**
 * Non-deterministic finite automata object. Includes add and get methods for states and 
 * transitions. Additionally, it can create a DFA given a valid NFA.
 * @author Luke Ptomey
 * @author Kyle Epperson
 */

public class NFA implements NFAInterface {
    private LinkedHashSet<NFAState> Q;              // Set for NFA states within the NFA
    private HashSet<Character> alphabet;            // Set of alphabet symbols for the language
    private LinkedHashSet<String> origTranstion;    // Set of transition characters
    private ArrayList<NFAState> visited;            // Used during eClosure. Indicates NFAStates that have already been visited
    private Set<String> finalStateString = new HashSet<String>();
    
    /**
     * NFA constructor
     */
    public NFA(){
        Q = new LinkedHashSet<NFAState>();
        alphabet = new HashSet<Character>();
        origTranstion = new LinkedHashSet<String>();
        visited = new ArrayList<NFAState>();
    }

    @Override
    public void addStartState(String name) {
        NFAState state = new NFAState(name);

        

        state.setStartState(true);
        // startState=name;
        @SuppressWarnings("unchecked")
        Set<NFAState> fn = (Set<NFAState>) getFinalStates();

        boolean finalAndStart = false;
        for (NFAState f : fn) {
            if (f.getName().equals(name)) {
                finalAndStart = true;
                f.setStartState(true);
            }
        }        

        // Goes into if state already exists in Q.
        if(!(Q.add(state)) && !finalAndStart){
            Iterator<NFAState> it = Q.iterator();
            while(it.hasNext()) {
                state = it.next();
                if(state.getName().equals(name)) {
                    state.setStartState(true);
                }
            }
        }  
     }
    

    @Override
    public void addState(String name) {
        NFAState state = new NFAState(name);
        Q.add(state);

    }

    @Override
    public void addFinalState(String name) {
        NFAState state = new NFAState(name);
        state.setFinal(true);
        Q.add(state);
        finalStateString.add(name);
    }

    @Override
    public void addTransition(String fromState, char onSymb, String toState) {
        // Add to alphabet if new symbol. Add transition to list.
        // NOTE: e (empty transition) is not valid alphabet symbol
        if(onSymb != 'e') { 
            alphabet.add(onSymb);
        }
        origTranstion.add(fromState + onSymb + toState);

        Iterator<NFAState> it = Q.iterator();

        // Iterates over Q, adds transition 'onSymb' and 'toState' to state 'fromState' when found.
        while(it.hasNext()){
            NFAState temp = it.next();
                if(temp.getName().equals(fromState)){

                // Gets the NFA toState from Q and saves to ts so can be added to temp's transitions
                NFAState ts = null;
                for (NFAState t : Q){
                    if(t.getName().equals(toState)){
                        ts = t;
                        break;
                    }
                }

                // Updates temp's transitions with new symb and destination NFAState ts.
                temp.addTransition(onSymb, ts);
                break;
             }
        }
    }

    @Override
    public Set<? extends State> getStates() {
        LinkedHashSet<NFAState> states = new LinkedHashSet<NFAState>();

        Iterator<NFAState> it = Q.iterator();

        while(it.hasNext()){
            NFAState insert =it.next();
            states.add(insert);
        }
        return states;
    }

    @Override
    public Set<? extends State> getFinalStates() {
            LinkedHashSet<NFAState> finalStates = new LinkedHashSet<NFAState>();
    
            Iterator<NFAState> it = Q.iterator();
    
            while(it.hasNext()){
                NFAState temp = it.next();
    
                if(temp.isFinal() == true){
                    finalStates.add(temp);
                }
            }
            return finalStates;
        }
    

    @Override
    public State getStartState() {
        NFAState startState;
        Iterator<NFAState> it = Q.iterator();

        while(it.hasNext()){
            NFAState temp = it.next();

            if(temp.isStartState() == true){
               startState=temp;
               return startState;
            }
        }
        return null;
    }

    @Override
    public Set<Character> getABC() {
       return this.alphabet;
    }

    @Override
    public DFA getDFA() {
        //Create DFA
        DFA conversionDFA = new DFA();

        //State Queue creation
        Queue <Set<NFAState>> stateQueue = new LinkedList<Set<NFAState>>();

        //BFS (Start with start state)
        Set<NFAState> s = eClosure((NFAState)getStartState());
        visited.clear();

        // Adds the start state (root) to the queue
        stateQueue.add(s);

        while(stateQueue.isEmpty()==false){
            Set<NFAState> grabbedState = stateQueue.remove();
            // System.out.println("GrabbedState: " + grabbedState);
            // System.out.println("Checking grabbedState : " + grabbedState.toString());
            boolean finalState = false;

            for(NFAState ecloseState : grabbedState){
                if(ecloseState.isFinal()){
                     finalState=true;
                } 
                else {
                    for(String fss : finalStateString) {
                        // System.out.println("fss: " + fss);
                        if(ecloseState.getName().contains(fss)) {
                            finalState=true;
                            break;
                        }
                    }

                    if(finalState == true) {
                        break;
                    }
                }
            }

            //If start but not final
            if(conversionDFA.getStartState() == null && finalState == false){
            
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                int count = 0;
                int setSize = grabbedState.size(); 
                for(NFAState temp :grabbedState) {
                    sb.append(temp.getName());
                    if(!(count==setSize-1) ){
                    count++;
                    sb.append(", ");
                    }
                }
                sb.append("]");
                //System.out.println("CHECKING");
                conversionDFA.addStartState(sb.toString());
            }

            //If start and final state
            else if(conversionDFA.getStartState() == null && finalState == true){
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                int count = 0;
                int setSize = grabbedState.size(); 
                for(NFAState temp :grabbedState) {
                    sb.append(temp.getName());
                    if(!(count==setSize-1) ){
                    count++;
                    sb.append(", ");
                    }
                }
                sb.append("]");
                // System.out.println("Adding start and final state on same state");
                System.out.println("CHECKING");
                conversionDFA.addFinalState(sb.toString());
                System.out.println("CHECKING");
                conversionDFA.addStartState(sb.toString());
                
            }

            // Loop through the transitions for each NFAState within grabbedState set
            for (Character transitionCharacter : alphabet) {
                Set<NFAState> toStatesOnTransChar = new HashSet<NFAState>();
                // System.out.println("Checking transition character: " + transitionCharacter);

                for (NFAState state : grabbedState) {

                    Set<NFAState> toStates = state.getTo(transitionCharacter); // Get the toStates from state on transition character
                    if(toStates != null) {  // If true, transitions on that character exists

                        for (NFAState toState : toStates) { 

                            Set<NFAState> extendedToStates = eClosure(toState);  // Check if more nodes can be reached after transition
                            visited.clear();
                            toStatesOnTransChar.addAll(extendedToStates);        // Add to set of possible transitions on that character
                        }
                    }
                }

                // Checks to see if the state of toStatesOnTransChar exists within the DFA already
                boolean doesStateAlreadyExistsInDFA = checkIfDFAStateExists(toStatesOnTransChar, conversionDFA);

                // Build the string for state name of grabbedState
                StringBuilder sb = new StringBuilder();
                sb.append("[");
                int count = 0;
                int setSize = grabbedState.size(); 
                for(NFAState temp :grabbedState) {
                    sb.append(temp.getName());
                    if(!(count==setSize-1) ){
                    count++;
                    sb.append(", ");
                    }
                }
                sb.append("]");

                // DEBUG
                // System.out.println("ToString for toStatesOnTransChar: " + toStatesOnTransChar.toString());

                // if(toStatesOnTransChar.isEmpty() == true) { // Dead state
                if(toStatesOnTransChar.toString().equals("[]")) { // Dead state
                    if(doesStateAlreadyExistsInDFA == true) {   // Transition to dead state

                        conversionDFA.addTransition(sb.toString(), transitionCharacter, toStatesOnTransChar.toString());    // Add transition to dead state from state
                        // DEBUG
                        // System.out.println("Printing toStates empty toString: " + toStatesOnTransChar.toString());

                    } else {    // If needed in DFA, create a dead state

                        // DEBUG
                        // System.out.println("Adding empty state");
                        conversionDFA.addState("[]"); // Add dead state to DFA
                        stateQueue.add(toStatesOnTransChar); // Add to queue so the transition can be checked from the dead state.
                        conversionDFA.addTransition(sb.toString(), transitionCharacter, "[]");    // Add transition to dead state from state
                    }

                } else if(doesStateAlreadyExistsInDFA == false) {   // state does not already in DFA. Add state to queue to check for transitions
                    stateQueue.add(toStatesOnTransChar);    // Add the newly found state to the queue to be processed.
                    boolean stateContainsFinalStateCharacter = false;

                    // Loops through the states within transitions set to see if any of the names of the NFAStates contains the name of final state in NFA
                    for(NFAState state : toStatesOnTransChar) {
                        for(String stringIndicatingFinalState : finalStateString) {
                            if(state.getName().contains(stringIndicatingFinalState)) {  // Checks if final state symbol is within the name of the new state
                                stateContainsFinalStateCharacter = true;
                                break;
                            }
                        }

                        if(stateContainsFinalStateCharacter == true) {  // Final state is true, don't need to check anymore
                            break;
                        }
                    }

                    // If its a final state, add as such.
                    if(stateContainsFinalStateCharacter == true) {  // Add state to DFA as final state

                        conversionDFA.addFinalState(toStatesOnTransChar.toString());

                    } else {    // Add state to DFA as normal state

                        conversionDFA.addState(toStatesOnTransChar.toString());

                    }

                }

                // Add the transition to the DFA after the necessary states have been created
                conversionDFA.addTransition(sb.toString(), transitionCharacter, toStatesOnTransChar.toString());

            }

        }
        
        // DEBUG
        // System.out.println(conversionDFA.toString());

        return conversionDFA;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        return from.getTo(onSymb);
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {

        // System.out.println("On node: " + s.getName());  // DEBUG

        Set<NFAState> retVal = new LinkedHashSet<>();
        retVal.add(s); // Always add itself

        Set<NFAState> toStates = s.getEStates();    // Gets the NFAStates that can be taken reached from 'e' transitions

        // Adds to visited array when seen. Stops infinate loop such as p2tc3.txt
        addVisitedState(s);

        if(toStates.isEmpty()) {    // Empty therefore just return itself
            return retVal;

        } else {    // Has e transitions therefore try them

            // Goes through each node and adds its e transition
            for (NFAState state : toStates) {
                if(!didVisitedState(state)) {   // Checks if that NFAState has already been visted
                    retVal.addAll(eClosure(state)); // Appends returned set to existing set
                }
            }
            visited.clear();    // Clear after eClosure
            return retVal;
        }

    }

    /**
     * Checks if an NFAState has already been visited during eClosure, indicated by its existance in visited list.
     * @param s A NFAState
     * @return An array of visited states by eclosure method
     */
    private boolean didVisitedState(NFAState s){
        return visited.contains(s);
    }

    /**
     * Adds an NFAState to visited list
     * @param s A NFA state
     */
    private void addVisitedState(NFAState s) {
        visited.add(s);
    }
    
    /*
     * Checks to see if the checkingStates string is already a state within the DFA.
     * Returns true is states exists in DFA, otherwise false
     */
    private boolean checkIfDFAStateExists(Set<NFAState> checkingStates, DFA dfa) {
        boolean retVal = false;
        Set<DFAState> dfaStates = dfa.getStates();

        for(DFAState dfaState : dfaStates) {
            if(dfaState.getName().equals(checkingStates.toString())) {  // Equals because DFA does not return set
                retVal = true;
                break;
            }
        }

        return retVal;
    }
}
