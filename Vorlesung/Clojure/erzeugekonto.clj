;; nimmt einen Betrag als Startkapital eines Kontos
;; und erzeugt eine "belaste-Funktion"
;; erzeuge-konto: (number -> (number -> (mixed number symbol)))
(def erzeuge-konto
  (fn [startwert]
    (let [konto (atom startwert)]
      (fn [betrag]
        (cond
          (>= @konto betrag)
          (do
            (swap! konto - betrag)
            @konto)

          :else 'konto-ueberzogen)))))

(def konto1 (erzeuge-konto 500))
(def konto2 (erzeuge-konto 600))
(konto1 200)  ;==> 300
(konto2 400)  ;==> 200
(konto2 400)  ;==> konto-ueberzogen
(konto2 100)  ;==> 100
