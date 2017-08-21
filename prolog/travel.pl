byCar(auckland,hamilton).
byCar(hamilton,raglan).
byCar(valmont,saarbruecken).
byCar(valmont,metz).

byTrain(metz,frankfurt).
byTrain(saarbruecken,frankfurt).
byTrain(metz,paris).
byTrain(saarbruecken,paris).

byPlane(frankfurt,bangkok).
byPlane(frankfurt,singapore).
byPlane(paris,losAngeles).
byPlane(bangkok,auckland).
byPlane(losAngeles,auckland).
                                % Teil 1
travel(X,Y) :- onestep(X,Y).
travel(X,Y) :- onestep(X,Z),
        travel(Z,Y).

onestep(X,Y) :- byCar(X,Y).
onestep(X,Y) :- byTrain(X,Y).
onestep(X,Y) :- byPlane(X,Y).
                                % Teil 2
travel(X,Y,go(X,Y)) :- onestep(X,Y).
travel(X,Y,go(X,Z,Path)) :- onestep(X,Z),
        travel(Z,Y,Path).

                                % Teil 3
travel(X,Y,go(X,Y,Transport)) :- onestep(X,Y,Transport).
travel(X,Y,go(X,Z,Transport,Path)) :- onestep(X,Z,Transport),
        travel(Z,Y,Path).

onestep(X,Y,byCar) :- byCar(X,Y).
onestep(X,Y,byTrain) :- byTrain(X,Y).
onestep(X,Y,byPlane) :- byPlane(X,Y).