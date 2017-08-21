/abolish
/log f.log
%%%%%%%%%%%%
% DATALOG
%%%%%%%%%%%%

-- :-type(flight(origin:string,destination:string,time:real)).

-- /assert flight(london,ny,9).
-- /assert flight(mad,par,1.5).
-- /assert flight(par,ny,10.0).

-- :-type(travel(origin:string,destination:string,time:real)).
-- /assert travel(X,Y,T) :- flight(X,Y,T).
-- /assert travel(X,Y,T) :- flight(X,Z,T1), travel(Z,Y,T2), T=T1+T2.

-- % Assuming a flight between Madrid and London in 2 hours, how long will it take to reach New York from Madrid?
-- % ?- flight(mad,london,2) => travel(mad,ny,T).


%%%%%%%%%%%%
% SQL
%%%%%%%%%%%%

----------------------------------------------------
-- PRIMERA PROPUESTA: SOBRECARGA DE WITH
----------------------------------------------------


/hypothetical on
/show_compilations on
/compact_listings on

CREATE OR REPLACE TABLE flight(origin string, destination string, time real)
--CREATE OR REPLACE TABLE travel(origin string, destination string, time real)

INSERT INTO flight VALUES('london','ny',9.0);
INSERT INTO flight VALUES('mad','par',1.5);
INSERT INTO flight VALUES('par','ny',10.0);


--CREATE VIEW travel(origin,destination,time) AS SELECT * FROM flight;
CREATE OR REPLACE VIEW travel(origin,destination,time) AS WITH connected(origin,destination,time) AS SELECT * FROM flight UNION SELECT flight.origin,connected.destination,flight.time+connected.time FROM flight,connected WHERE flight.destination = connected
.origin SELECT * FROM connected;

----------------------------------------------------
-- EJEMPLO 1
----------------------------------------------------
-- % ?- flight(mad,london,2) => travel(mad,ny,T).
-- % Con esquema
-- WITH flight(origin,destination,flight) AS 
--        SELECT 'mad','london',2.0 
-- SELECT time 
-- FROM travel 
-- WHERE origin='mad' AND destination='ny'

WITH flight(origin,destination,flight) AS SELECT 'mad','london',2.0 SELECT time FROM travel WHERE origin='mad' AND destination='ny'

-- % Sin esquema
-- WITH flight AS SELECT 'mad','london',2.0 
-- SELECT time 
-- FROM travel 
-- WHERE origin='mad' AND destination='ny'

-- % Más en general
-- WITH Schema1 AS AssumedRelation1, 
--      ... ,
--      SchemaN AS AssumedRelationN
-- SELECT ProjList 
-- FROM Relation1, ... , RelationM

% Notas:
% - Todas las relaciones nuevas se asumen temporalmente en el WITH según el estándar SQL
% - En la propuesta, para añadir extensional o intensionalmente tuplas a una relación, 
%   se usa el mismo nombre de una relación existente. El estándar SQL no lo soporta, claro

----------------------------------------------------
-- EJEMPLO 2
----------------------------------------------------
% Otro ejemplo.
% Si sólo existe la relación flight y asumimos la relación travel:

DROP VIEW travel

-- WITH travel(origin,destination,time) AS 
-- (SELECT * FROM flight) 
-- UNION 
-- (SELECT flight.origin,travel.destination,flight.time+travel.time
--  FROM flight, travel 
--  WHERE flight.destination = travel.origin
-- )
-- SELECT * FROM travel;

/hypothetical off

WITH travel(origin,destination,time) AS (SELECT * FROM flight) UNION (SELECT flight.origin,travel.destination,flight.time+travel.time FROM flight, travel WHERE flight.destination = travel.origin) SELECT * FROM travel;

-- % Pero esto ya funciona en SQL estándar. 

----------------------------------------------------
-- EJEMPLO 3
----------------------------------------------------

--Otro caso es que sólo existiese la vista:
CREATE VIEW travel(origin,destination,time) AS SELECT * FROM flight

-- % Añadir la cláusula recursiva sería:
/hypothetical on

-- WITH travel(origin,destination,time) AS 
-- (SELECT flight.origin,travel.destination,flight.time+travel.time 
--  FROM flight,travel 
--  WHERE flight.destination = travel.origin
-- )
-- SELECT * FROM travel;
WITH travel(origin,destination,time) AS (SELECT flight.origin,travel.destination,flight.time+travel.time FROM flight,travel  WHERE flight.destination = travel.origin) SELECT * FROM travel;

% El sistema tendría que construir la vista asumida como la unión de la nueva 
% definición y la antigua para procesar la consulta hipotética (y después 
% recuperar la vista original)

/nolog

----------------------------------------------------
-- SEGUNDA PROPUESTA: NUEVA CONSTRUCCIÓN ASSUME
----------------------------------------------------

----------------------------------------------------
-- CASO 1
----------------------------------------------------

% 
% Se asume un átomo A para la relación R en una consulta G
% G ::= D => G
% D ::= A 

% ASSUME A' IN R G'

-- EJEMPLO 1

-- ASSUME 
--   SELECT 'mad','london',2.0 
-- IN 
--   flight(origin,destination,time) 
-- SELECT * 
-- FROM travel;

ASSUME SELECT 'mad','london',2.0 IN flight(origin,destination,time) SELECT * FROM travel;

----------------------------------------------------
-- CASO 2
----------------------------------------------------

% Se asume una cláusula D para la relación R en una consulta G
% G ::= D => G
% D ::= G => A

% ASSUME D' IN R G'

-- EJEMPLO 3

ASSUME
  (SELECT flight.origin,travel.destination,flight.time+travel.time 
   FROM flight,travel  
   WHERE flight.destination = travel.origin)
IN
  travel(origin,destination,time)
SELECT * 
FROM travel;

ASSUME
  (SELECT flight.origin,connected.destination
   FROM flight,connected  
   WHERE flight.destination = connected.origin)
IN
  connected(origin,destination)
SELECT * 
FROM connected;

ASSUME (SELECT flight.origin,connected.destination FROM flight,connected WHERE flight.destination = connected.origin) IN connected(origin,destination) SELECT * FROM connected;

