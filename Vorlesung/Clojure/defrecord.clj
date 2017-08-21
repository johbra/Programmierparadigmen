(defrecord punkt
    [x y])
(->punkt 2 3)
(:x (->punkt 2 3))
(instance? punkt (->punkt 2 3))
