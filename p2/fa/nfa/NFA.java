package fa.nfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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

        // Goes into if state already exists in Q.
        if(!(Q.add(state))){
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
                 //fromState creates toState and adds it to its list of states fromstate can go to
                // NFAState goState = new NFAState(toState);

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
        // TODO Auto-generated method stub
        
        // DELETE ME -- DEBUG FOR TESTING eClosure(), NOT PART OF SOLUTION
        // for(NFAState s : Q) {
        //     eClosure(s);
        //     visited = new ArrayList<>();
        // }


        //Create DFA states using closure of each NFA state


        DFA conversionDFA = new DFA();

        Iterator<NFAState> it = Q.iterator();
        while(it.hasNext()) {
            // Grab state from Q
           NFAState grabState = it.next();
            // Find closure of that state
            Set<NFAState> closeStates = eClosure(grabState);

            Iterator<NFAState> it2 = closeStates.iterator();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            while(it2.hasNext()) {
                NFAState temp = it2.next();
                sb.append(temp.getName());
                if(it2.hasNext()){
                sb.append(", ");
                }
            }
            sb.append("]");
            //DEBUG
            System.out.println(sb.toString());

            conversionDFA.addState(sb.toString());


        }

        return null;
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
            visited.clear();
            return retVal;
        }

    }

    // Checks if an NFAState has already been visited during eClosure, indicated by its existance in visited list.
    private boolean didVisitedState(NFAState s){
        return visited.contains(s);
    }

    // Adds an NFAState to visited list
    private void addVisitedState(NFAState s) {
        visited.add(s);
    }
    
}
