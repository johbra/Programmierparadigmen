(ns mylists)
(defrecord liste [first rest])
(->liste 3 (->liste 4 ()))
(def cons
  (fn [e l]
    (->liste e l)))
(def first :first)
(def rest :rest)
(instance? liste (cons 1 (cons 2 ())))
(def sum
  (fn [lon]
    (cond
      (empty? lon) 0
      :else (+ (first lon) (sum (rest lon))))))
(sum (cons 1 (cons 2 (cons 3 ()))))

(empty? (cons 1 (cons 2 ())))
