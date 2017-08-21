collatz(N, N).
collatz(N0, N) :-
        N0 mod 2 =:= 0,
        N1 is N0 // 2,
        collatz(N1, N).
collatz(N0, N) :-
        N0 mod 2 =\= 0,
        N1 is 3*N0 + 1,
        collatz(N1, N).