(use 'clojure.test)
(def memrest
  (fn [elem lst]
    (cond
      (empty? lst) ()
      (= elem (first lst)) lst
      :else (memrest elem (rest lst)))))

(deftest mr-test
  (is (= (memrest 'd '(c d b)) '(d b)))
  (is (= (memrest 'a '(x a y a z)) '(a y a z))))

(def skelett
  (fn [s l]
    (cond
      (empty? s) true
      (empty? l) false
      (empty? (memrest (first s) l)) false
      :else (skelett (rest s) (rest (memrest (first s) l))))))

(deftest sk-test
  (is (not (skelett '(a b c d ) '(1 a a 2 b c 4))))
  (is (not(skelett '(a b c d ) '(x a a y b c z))))
  (is (skelett '(a b c  ) '(x a a y b c z))))

(run-tests)
