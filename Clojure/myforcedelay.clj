;; Beispiele zu delay und force
(def factorial
  (fn [n]
    (loop [cnt n acc 1]
      (if (zero? cnt)
        acc
        (recur (dec cnt) (* acc cnt))))))

(def my-delay
  (fn [f]
    (atom [false f])))


(factorial 100M)

(def my-force
  (fn [th]
    (if (@th 0)
      (@th 1)
      (do (swap! th assoc 0 true 1 ((@th 1)))
          (@th 1)))))

(def d (my-delay (fn [] (+ 3 4))))

(my-force d)

(def my-mult
  (fn [x y]
    (cond (= x 0) 0
          (= x 1) y
          :else (+ y (my-mult (- x 1) y)))))

(my-mult 200 100)

(def my-mult
  (fn [x y-thunk]
    (cond (= x 0) 0
          (= x 1) (y-thunk)
          :else (+ (y-thunk) (my-mult (- x 1) y-thunk)))))


(my-mult 200 (fn [] (factorial 100M)))

(my-mult 200 (let [x (my-delay (fn [] (factorial 100M)))] (fn [] (my-force x))))

(def my-mult
  (fn [x y-promise]
    (cond (= x 0) 0
          (= x 1) (my-force y-promise)
          :else (+ (my-force y-promise) (my-mult (- x 1) y-promise)))))

(my-mult 200 (my-delay (fn [] (factorial 100M))))



