%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Constraint-Programmierung 
%%% Sommersemester 2005
%%% C. Beckstein, C. Knuepfer
%%%
%%% FRIEDRICH-SCHILLER-UNIVERSITAET JENA
%%% Fakultaet fuer Mathematik und Informatik
%%% Institut fuer Informatik
%%%
%%% http://www.informatik.uni-jena.de/www/fakultaet/beckstein/clprog.html
%%%
%%% tral@minet.uni-jena.de
%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% Programmcodes (SICStus Prolog) zum 1. Uebungsblatt
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%Einbinden der Constraint-Solver
:- use_module(library(clpq)). %fuer rationale Zahlen und Baeume
:- use_module(library(clpb)). %fuer boolescher Constraints

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% zu Aufgabe 1.1
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

serial(V,I,R1,V1,I1,R2,V2,I2) :- {
 V1=I1*R1,
 V2=I2*R2,
 V-V1-V2=0,
 I-I1=0,
 I-I2=0,
 I1-I2=0
}.

%Spannungen und Stroeme fuer R1=10, R2=5:
%%% | ?- serial(V,I,10,V1,I1,5,V2,I2).
%%% {I1=1/10*V1},
%%% {V2=1/2*V1},
%%% {I2=1/10*V1},
%%% {V=3/2*V1},
%%% {I=1/10*V1} ? 

%Verhaeltnis Spannung und Strom fuer R1=3, R2=7:
%%% | ?- serial(V,I,3,_,_,7,_,_).
%%% {I=1/10*V} ?

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% zu Aufgabe 1.2
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

unify_a(U,V,X,Y) :- p(X,X,Y)=p(f(U),Y,f(V)).

%%% | ?- unify_a(U,V,X,Y).
%%% V = U,
%%% X = f(U),
%%% Y = f(U) ?

unify_b(U,X,Y) :- p(X,X,Y)=p(f(U),Y,a).

%%% | ?- unify_b(U,X,Y).
%%% no

unify_c(U,V,X,Y) :- p(f(X,Y),f(X,Z),Z)=p(f(U,a),f(V,V),f(U,a)).

% ohne Occurs Check:
%%% | ?- unify_c(U,V,X,Y).
%%% U = f(f(f(f(f(f(f(f(f(f(...),a),a),a),a),a),a),a),a),a),
%%% V = f(f(f(f(f(f(f(f(f(f(...),a),a),a),a),a),a),a),a),a),
%%% X = f(f(f(f(f(f(f(f(f(f(...),a),a),a),a),a),a),a),a),a),
%%% Y = a ? 

unify_c2(U,V,X,Y) :-
  unify_with_occurs_check(
			  p(f(X,Y),f(X,Z),Z),
			  p(f(U,a),f(V,V),f(U,a))
  ).

% mit Occurs Check:
%%% | ?- unify_c2(U,V,X,Y).
%%% no

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%% zu Aufgabe 1.4
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

guilty(H, D, L) :- 
  sat(		    
    (D=:=L)  * %Aussage von Holger
    (H=<D)   * %Aussage von Dennis
    (~(D*L))   %Aussage von Lars
  ).

% Wer ist schuldig?
%%% | ?- guilty(Holger, Dennis, Lars).
%%% Lars = 0,
%%% Dennis = 0,
%%% Holger = 0 ? 
