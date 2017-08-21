natural(z).
natural(s(N)):- natural(N).
pred(N, X) :- plus( X,  s(z), N).
plus(z, X, X):- natural(X).
plus(s(N), X, s(Y)) :- plus(N, X, Y).
