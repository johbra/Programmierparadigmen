                                % Teil 1
am_boden(a).
am_boden(d).
am_boden(f).
am_boden(j).

auf(c, d).                      % c liegt auf d
auf(b, c).
auf(e, f).
auf(i, j).
auf(h, i).
auf(g, h).

links_von(a, d).                % a liegt links von d
links_von(d, f).
links_von(f, j).

                                % Teil 2

basis(X, X):-
        am_boden(X).
basis(X, Y):-                   % die Basis von X ist Y
        auf(X, Z), 
        basis(Z, Y).

basis_rechts(X, Y):-            % Y liegt rechts von X
        links_von(X, Y).
basis_rechts(X, Y):-
        links_von(X, Z),
        basis_rechts(Z, Y).

objekt_rechts(X, Y):-           % Y liegt in einem Turm, der rechts
                                % von dem liegt der X enthält
        basis(X, Xb),
        basis(Y, Yb),
        basis_rechts(Xb, Yb).

                                % Teil 3
typ_von(a, pyramide).
typ_von(b, reifen).
typ_von(c, wuerfel).
typ_von(d, kugel).
typ_von(e, wuerfel).
typ_von(f, reifen).
typ_von(g, reifen).
typ_von(h, kugel).
typ_von(i, reifen).
typ_von(j, pyramide).

kein_reifen(O):- typ_von(O, pyramide).
kein_reifen(O):- typ_von(O, wuerfel).
kein_reifen(O):- typ_von(O, kugel).

convex(O):- typ_von(O, pyramide). % convex sind Pyramiden,
convex(O):- typ_von(O, kugel).    % Kugeln und
convex(O):-                       % Reifen direkt auf einer Pyramide
        typ_von(O, reifen),      
        auf(O, O1),
        typ_von(O1, pyramide).

instabil(O):-                   % instabil sind Kugeln auf dem Boden,
        typ_von(O, kugel),
        am_boden(O).
instabil(O):-                   % eine Kugel auf einem Würfel
        typ_von(O, kugel),
        auf(O, O1),
        typ_von(O1, wuerfel).
instabil(O):-                   % und alle Nicht-Reifen auf einer
                                % convexen Grundlage
        kein_reifen(O),
        auf(O, O1),
        convex(O1).