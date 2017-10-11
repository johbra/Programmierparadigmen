:- use_module(library(clpr)).  
                                %darlehen(D, T, Z, R, S) :- T=0, D=S. 
                                %darlehen(D, T, Z, R, S) :- T>0, T1 = T-1, D1 = D + D*Z - R,
                                %darlehen(D1, T1, Z, R, S).  

darlehen(D, T, Z, R, S) :- {T=0, D=S}.
darlehen(D, T, Z, R, S) :- {T>0, T1 = T-1, D1 = D + D*Z - R},
        darlehen(D1, T1, Z, R, S).

mortgage(D, T, I, R, S) :-
        {T = 0,
         D = S} ;
        {T > 0,
         T1 = T - 1},
        {D1 = D + D*I - R},
        mortgage(D1, T1, I, R, S).