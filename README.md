# 361-p2
# Project 2: (Nondeterministic Finite Automata)

* Author: Luke Ptomey and Kyle Epperson
* Class: CS361 Section 1
* Semester: Fall 2020

## Overview

This Java application models a nondeterministic finite automaton using DFA states. Also has option to convert to a deterministic finite automata.

## Compiling and Using

To compile fa.dfa.DFADriver from the top directory of these files:
```
$ javac fa/nfa/NFADriver.java
```

To run fa.dfa.DFADriver (with inputfile):
```
$ java fa.nfa.NFADriver ./tests/p2tc0.txt

```

No user input besides input file in run command.

## Discussion

When first working on the project, we initially had few problems creating NFA.java because
we had a good idea of the functionality each NFA state had to have based on the last project.
However, we were aware that the NFA itslef required more functionality compared to a DFA. For
example, there is a need to keep track of previous states when determining the eClosure for 
each state. Also, we need to keep track of the finalStateString for states so the program 
can determine final states for a created DFA.

Creating the eClosure step took a fair amount of time because it was critical to get it right
before moving foward. Simply we added the state itself to the eClosure array and appended it 
with states that transtioned on the symbol 'e', this required recursive method calls.

While working on the getDFA method, we knew how to convert an NFA to a DFA, but we had difficulty 
organizing the logic so BFS is used. One of the main struggles we encountered is when to append a new
state to our working queue. To get around this we created a method called checkIfDFAStateExists. The 
method does the action implied by its name and it helps because it prevents creating duplicate states.

Overall, the project was just as challenging as anticipated. Despite having a good idea of the concepts
taught in class we struggled with the implementation because of the many corner cases. This required lots
of testing and revisions but we were prepared to take the time to do so.

## Extra Credit

No extra credit was attempted.

## Sources used

- Java Documentation was researched for reference.
[Java Platform, Standard Edition 8 API Specification](https://docs.oracle.com/javase/8/docs/api/)
