vater(klaus, paul).
vater(claudia, paul).
vater(monika, paul).
mutter(klaus, elfriede).
mutter(claudia, elfriede).
mutter(monika, elfriede).
verheiratet(paul, elfriede).
weiblich(claudia).
weiblich(monika).
weiblich(elfriede).

schwester(X, Y):- weiblich(Y), 
        mutter(X, Z), mutter(Y, Z), 
        vater(X, W), vater(Y, W),
        X \= Y.

eheleute(X, Y) :- verheiratet(X, Y); verheiratet(Y, X).




