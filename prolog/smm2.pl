% This code works in both YAP and SWI-Prolog using the environment-supplied
                                % CLPFD constraint solver library.  It may require minor modifications to work
                                % in other Prolog environments or using other constraint solvers.
:- use_module(library(clpfd)).
sendmore(Digits) :-
        Digits = [S,E,N,D,M,O,R,Y], 
        Carries = [C1, C2, C3], % Liste der Überträge 
        Digits ins 0..9,      
        S #\= 0,              
        M #\= 0,
        Carries ins 0..1,       % Überträge können nur 0 oder 1 sein
        all_different(Digits), 
        E + D #= Y + 10 *  C1,
        C1 + N + R #= E + 10 * C2,
        C2 + E + O #= N + 10 * C3,
        C3 + S + M #= 10 * M + O,
        label(Digits).          
