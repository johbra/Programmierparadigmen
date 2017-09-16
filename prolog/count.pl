list_member_occ([], _, 0).       % list is empty, 0 occurrences
list_member_occ([X|Xs], X, N) :- % list has the element at the head
    list_member_occ(Xs, X, N0),  % count number of elements in the tail
    succ(N0, N).                 % the number of occurrences is the
                                 % next natural number
list_member_occ([Y|Xs], X, N) :-
    dif(X, Y),                   % head and the element are different
    list_member_occ(Xs, X, N).   % occurrences in the tail of the list
                                % is the total number
