:- use_module(library(clpfd)).
list_member_occ([], _, 0).       % list is empty, 0 occurrences
list_member_occ([X|Xs], X, N) :- % list has the element at the head
    list_member_occ(Xs, X, N0),  % count number of elements in the tail
    succ(N0, N).                 % the number of occurrences is the
                                 % next natural number
list_member_occ([Y|Xs], X, N) :-
    dif(X, Y),                   % head and the element are different
    list_member_occ(Xs, X, N).   % occurrences in the tail of the list
                                % is the total number

zahl(Ziffern) :- Ziffern = [Nu, Ei, Zw, Dr, Vi, Fu, Se, Si, Ac, Ne],
        Ziffern ins 0..9,
        list_member_occ(Ziffern, 0, Nu),
        list_member_occ(Ziffern, 1, Ei),
        list_member_occ(Ziffern, 2, Zw),
        list_member_occ(Ziffern, 3, Dr),
        list_member_occ(Ziffern, 4, Vi),
        list_member_occ(Ziffern, 5, Fu),
        list_member_occ(Ziffern, 6, Se),
        list_member_occ(Ziffern, 7, Si),
        list_member_occ(Ziffern, 8, Ac),
        list_member_occ(Ziffern, 9, Ne),
        label(Ziffern).

