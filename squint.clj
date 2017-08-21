(defn integers-starting-at [n]
  (cons n  (lazy-seq (integers-starting-at (inc n)))))

(defn integers []
  (integers-starting-at 1))

(defn square [n] (* n n))

(defn squares-of [list]
  (map square list))

(defn squint [n]
  (take n (squares-of (integers))))

(squint 5)
(first (integers-starting-at 1))
;;;;

(defn ints [n]
  (cons n `(ints (inc ~n))))

(eval (rest (ints 1)))

(defn ints2 [n]
  (cons n (cons 'ints2 (cons (inc n) ()))))

(eval (rest (ints2 1)))

(defn lazy-rest [l] (eval (rest l)))

(defn nimm [n l]
  (cond
    (= n 0) ()
    :else (cons (first l) (nimm (dec n) (lazy-rest l)))))
(nimm 50 (ints2 1))


(first (ints 1))
(eval (rest (ints 1)))

(nth (ints 1) 5)

(map #(* % %) (nimm 5 (ints 1)))

(map #(* % %) (ints 1)) ;; geht so nicht!!

;;;;;
(def + -)
(+ 4 5)
(clojure.core/+ 4 5)

((fn [ a + b] (+ a b)) 3 - 4)
