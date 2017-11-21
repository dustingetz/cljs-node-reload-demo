(ns foo.main
  (:require [cljs.nodejs :as nodejs]
            [hello-world.server :as server]))


(nodejs/enable-util-print!)


(defn ^:export main []
  (server/start))

(set! *main-cli-fn* main)
