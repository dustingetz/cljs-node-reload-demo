(ns foo.service
  (:require))


(defn http-one [req res]
  (doto res (.send "one")))

(defn http-two [req res]
  (doto res (.send "two")))
