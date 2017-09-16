father(abraham,isaac).
father(haran,lot).
father(haran,milcah).
father(haran,yiscah).
male(isaac).
male(lot).
female(milcah).
female(yiscah).
son(X,Y) :- father(Y,X), male(X).
daughter(X,Y) :- father(Y,X), female(X).
sister(X, Y) :- father(Z, X), father(Z, Y), female(X).
brother(X, Y) :- father(Z, X), father(Z, Y), male(X).
siblings(X, Y) :- father(Z, X), father(Z, Y).
