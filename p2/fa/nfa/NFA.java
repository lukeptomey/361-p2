package fa.nfa;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import fa.State;
import fa.dfa.DFA;

/**
 * Non-deterministic finite automata object. Includes add and get methods for states and 
 * transitions. Additionally, it can create a DFA given a valid NFA.
 * @author Luke Ptomey
 * @author Kyle Epperson
 */

public class NFA implements NFAInterface {
    private LinkedHashSet<NFAState> Q;
    private HashSet<Character> alphabet;
    private LinkedHashSet<String> origTranstion;
    
    /**
     * NFA constructor
     */
    public NFA(){
        Q = new LinkedHashSet<NFAState>();
        alphabet = new HashSet<Character>();
        origTranstion = new LinkedHashSet<String>();

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
        alphabet.add(onSymb);
        origTranstion.add(fromState + onSymb + toState);

        Iterator<NFAState> it = Q.iterator();

        while(it.hasNext()){
            NFAState temp = it.next();
             if(temp.getName().equals(fromState)){
                 //fromState creates toState and adds it to its list of states fromstate can go to
                // NFAState goState = new NFAState(toState);
                 temp.addTransition(onSymb, toState);
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
       return alphabet;
    }

    @Override
    public DFA getDFA() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<NFAState> getToState(NFAState from, char onSymb) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<NFAState> eClosure(NFAState s) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
