(defprotocol Fooable
  (foo [x]))

(defrecord AType [avalue]
  Fooable 
  (foo [x]
    (println (str "A value: " (:avalue x)))))

(defrecord BType [bvalue]
  Fooable 
  (foo [x]
    (println (str "B value: " (:bvalue x)))))

(foo (->AType "AAAAAA"))
