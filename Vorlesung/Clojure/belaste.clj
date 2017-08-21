(def belaste
  (let [konto (atom 100)]
    (fn [betrag]
      (cond
        (>= @konto betrag)
        (do
          (swap! konto - betrag)
          @konto)

        :else 'konto-ueberzogen))))

(belaste 30)

(belaste 40)
