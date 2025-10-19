%Consultas encadenadas

% Definimos algunos hechos solo para testing
padre(juan, maria).
padre(juan, jose).
edad(juan, 50).
edad(maria, 20).
edad(jose, 18).

mayorEdad(X) :- edad(X, E), gt(E,18).