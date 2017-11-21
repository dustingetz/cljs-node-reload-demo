(ns foo.main
  (:require [cljs.nodejs :as nodejs]
            [foo.server :as server]))


(nodejs/enable-util-print!)

(defn on-js-reload []
  (js/console.info "Reloading Javascript..."))

(defn main []
  (server/start))

(set! *main-cli-fn* main)
