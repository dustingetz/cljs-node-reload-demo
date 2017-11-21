(ns foo.main
  (:require [cljs.nodejs :as nodejs]
            [foo.server :as server]))


(nodejs/enable-util-print!)

(defn main []
  (server/start))

(set! *main-cli-fn* main)
