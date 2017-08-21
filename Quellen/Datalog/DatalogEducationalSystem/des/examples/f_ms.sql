-- PRUEBAS EN MS SQL Server

%%%%%%%%%%%%
% SQL
%%%%%%%%%%%%

CREATE OR REPLACE TABLE flight(origin VARCHAR(10), destination VARCHAR(10), time REAL)
--CREATE OR REPLACE TABLE travel(origin string, destination string, time real)

INSERT INTO flight VALUES('london','ny',9.0);
INSERT INTO flight VALUES('mad','par',1.5);
INSERT INTO flight VALUES('par','ny',10.0);


--CREATE VIEW travel(origin,destination,time) AS SELECT * FROM flight;
DROP VIEW travel;
CREATE VIEW travel(origin,destination,time) AS WITH connected(origin,destination,time) AS (SELECT * FROM flight UNION ALL SELECT flight.origin,connected.destination,flight.time+connected.time FROM flight,connected WHERE flight.destination = connected.origin) SELECT * FROM connected;

-- % ?- flight(mad,london,2) => travel(mad,ny,T).
-- % Con esquema
-- WITH flight(origin,destination,flight) AS 
--        SELECT 'mad','london',2.0 
-- SELECT time 
-- FROM travel 
-- WHERE origin='mad' AND destination='ny'

WITH flight(origin,destination,flight) AS (SELECT 'mad','london',2.0) SELECT time FROM travel WHERE origin='mad' AND destination='ny'

Respuesta:
time
11,5

Conclusión:
No incluye el supuesto flight('mad','london',2.0)

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

% Otro ejemplo.
% Si sólo existe la relación flight y asumimos la relación travel:

DROP VIEW travel;

-- WITH travel(origin,destination,time) AS 
-- (SELECT * FROM flight) 
-- UNION 
-- (SELECT flight.origin,travel.destination,flight.time+travel.time
--  FROM flight, travel 
--  WHERE flight.destination = travel.origin
-- )
-- SELECT * FROM travel;

WITH travel(origin,destination,time) AS 
(
(SELECT * FROM flight) 
UNION ALL
(SELECT flight.origin,travel.destination,flight.time+travel.time FROM flight,travel WHERE flight.destination=travel.origin) 
)
SELECT * FROM travel;

Respuesta:
origin destination time
london	ny	9
mad	par	1,5
par	ny	10
mad	ny	11,5

-- % Pero esto ya funciona en SQL estándar. Otro caso es que sólo existiese la vista:
CREATE VIEW travel(origin,destination,time) AS SELECT * FROM flight;

-- % Añadir la cláusula recursiva sería:
/hypothetical on

-- WITH travel(origin,destination,time) AS 
-- (SELECT flight.origin,travel.destination,flight.time+travel.time 
--  FROM flight,travel 
--  WHERE flight.destination = travel.origin
-- )
-- SELECT * FROM travel;
WITH travel(origin,destination,time) AS (SELECT flight.origin,travel.destination,flight.time+travel.time FROM flight,travel  WHERE flight.destination = travel.origin) SELECT * FROM travel;

Nota:
No es posible emitir esta consulta.

% El sistema tendría que construir la vista asumida como la unión de la nueva 
% definición y la antigua para procesar la consulta hipotética (y después 
% recuperar la vista original)

/nolog
