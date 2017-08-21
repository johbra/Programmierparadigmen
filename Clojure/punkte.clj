(use 'clojure.test)

;; (defrecord point [x y])
;; (def make-point ->point)
;; (def point-x :x)
;; (def point-y :y)

(def make-point
  (fn [x y] [x y]))
(def point-x
  (fn [p] (get p 0)))
(def point-y
  (fn [p] (get p 1)))
(def add-point
  (fn [p1 p2]
    (make-point
     (+ (point-x p1) (point-x p2))
     (+ (point-y p1) (point-y p2)))))

(def distance-to-0
  (fn [p]
    (Math/sqrt
     (+ (Math/pow (point-x p) 2)
        (Math/pow (point-y p) 2)))))

(deftest point-test
  (is (= (add-point (make-point 2 3) (make-point 20 30))
         (make-point 22 33)))
  (is (= (distance-to-0 (make-point 3 4)) 5.0)))
(run-tests)
