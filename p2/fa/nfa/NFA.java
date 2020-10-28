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
    private LinkedHashSet<String> previousTranstion;
    
    /**
     * NFA constructor
     */
    public NFA(){
        Q = new LinkedHashSet<NFAState>();
        alphabet = new HashSet<Character>();
        previousTranstion = new LinkedHashSet<String>();

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
        // TODO Auto-generated method stub

    }

    @Override
    public Set<? extends State> getStates() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<? extends State> getFinalStates() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public State getStartState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Character> getABC() {
        // TODO Auto-generated method stub
        return null;
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
