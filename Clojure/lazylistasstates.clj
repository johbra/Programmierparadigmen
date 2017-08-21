(ns stateful)
;; vgl. Principles of Programming Languages von Mira Balaban
;; Abschnitt 8
(def make-withdrawals
  (fn []
    (list (read)
          (fn [] (make-withdrawals)))))

(def tke
  (fn [n l]
    (cond
      (<= n 1) (list (first l ))
      :else (cons  (first l) (tke (- n 1) ((second l)))))))


(def account-states
  (fn [balance withdrawals]
    (list balance
          (fn []
            (account-states (- balance (first withdrawals))
                            ((second withdrawals)))))))
(last (tke 2 (account-states 100 (make-withdrawals))))

(tke 3 (make-withdrawals))
