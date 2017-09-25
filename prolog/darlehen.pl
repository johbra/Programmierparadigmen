:- use_module(library(clpr)).

darlehen(D, T, Z, R, S) :- {T=0, D=S}.
darlehen(D, T, Z, R, S) :- {T>0, T1 = T-1, D1 = D + D*Z - R},
        darlehen(D1, T1, Z, R, S).

                                % Fragen
                                % darlehen(100000,360,0.01,1025,S).
                                % darlehen(D,360,0.01,1025,0).
                                % {S=<0}, darlehen(100000,T,0.01,1025,S). 
                                % darlehen(D,360,0.01,R,0).
