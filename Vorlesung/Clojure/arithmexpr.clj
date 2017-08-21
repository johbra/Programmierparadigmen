;; section2sum.pdf
(ns arithmExpr)
(use 'clojure.test)
;; datatype exp = Cons of int
;; | Neg of exp
;; | Add of exp * exp
;; | Mul of exp * exp
(defrecord Con [i])
(defrecord Neg [e])
(defrecord Add [e1 e2])
(defrecord Mul [e1 e2])


;; fun eval e =
;; case e of
;; Constant i => i
;; | Neg e2  => ~ (eval e2)
;; | Add(e1,e2) => (eval e1) + (eval e2)
;; | Multiply(e1,e2) => (eval e1) * (eval e2)

(def eval
  (fn [e]
    (cond
      (instance? Con e) (:i e)
      (instance? Neg e) (- (eval (:e e)))
      (instance? Add e) (+ (eval (:e1 e)) (eval (:e2 e)))
      (instance? Mul e) (* (eval (:e1 e)) (eval (:e2 e))))))

(deftest test-eval
  (is (= (eval (->Con 2)) 2))
  (is (= (eval (->Neg (->Con 2))) -2))
  (is (= (eval (->Add (->Con 3) (->Neg (->Con 2)))) 1))
  (is (= (eval (->Mul (->Add (->Con 3) (->Neg (->Con 1))) (->Con 3))) 6)))


;;; Aufgaben:
;; There are many functions we might write over values of
;; type exp and most of them will use pattern-matching and recursion in a
;; similar way. Here are other functions you could write that process an
;; exp argument:

;; a) The largest constant in an expression A list of all the constants in
;;    an expression (use list append)
;; b) A bool indicating whether there is least one multiplication in the expression
;; c  The number of addition expressions in an expression

;; Here is the last one:
;; fun number_of_adds e =
;; case e of
;; =>0
;; => number_of_adds e2
;; => 1 + number_of_adds e1 + number_of_adds e2
;; Conant i
;; | Neg e2
;; | Add(e1,e2)
;; | Multiply(e1,e2) => number_of_adds e1 + number_of_adds e2


;; aus section3sum.pdf
;; Here we use an is_even function to see if all the constants in an arithmetic expression are even.
;; We could easily reuse true_of_all_constants for any other property we wanted to check.
;; datatype exp = Conant of int | Neg of exp | Add of exp * exp | Multiply of exp * exp
;; fun is_even v =
;; (v mod 2 = 0)
;; fun true_of_all_constants(f,e) =
;; case e of
;; Conant i
;; | Neg e1
;; | Add(e1,e2)
;; | Multiply(e1,e2) => true_of_all_constants(f,e1) andalso true_of_all_constants(f,e2)
;; => f i
;; => true_of_all_constants(f,e1)
;; => true_of_all_constants(f,e1) andalso true_of_all_constants(f,e2)
;; fun all_even e = true_of_all_constants(is_even,e)

;;;;;;;;;;;;;
;; aus section6sum.pdf
;;
(def eval-exp
  (fn [e]
    (cond
      (instance? Con e) e ; note returning an exp, not a number
      (instance? Neg e) (->Con (- (:i (eval-exp (:e e)))))
      (instance? Add e) (let [v1 (:i (eval-exp (:e1 e)))
                              v2 (:i (eval-exp (:e2 e)))]
                          (->Con (+ v1 v2)))
      (instance? Mul e) (let [v1 (:i (eval-exp (:e1 e)))
                              v2 (:i (eval-exp (:e2 e)))]
                          (->Con (* v1 v2)))
      :else (throw (Exception. "eval-exp expected an exp")))))


(deftest test-eval-exp
  (is (= (->Mul (->Neg (->Add (->Con 2) (->Con 2))) (->Con 7))) (->Con -28)))

(run-tests)
