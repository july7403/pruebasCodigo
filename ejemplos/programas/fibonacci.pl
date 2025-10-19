fibo(0, 0).
fibo(1, 1).

fibo(N, F) :-gt(N,1),N1 is sub(N,1),N2 is sub(N,2), fibo(N1,F1), fibo(N2,F2),F is add(F1,F2).