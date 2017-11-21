(ns ^:figwheel-load foo.main
  (:require [dommy.core :as dommy]
            [foo.core :as foo]))

(enable-console-print!)

(defonce app-state (atom {:name "User"}))

;; (swap! app-state assoc :name "Andrea")

(defn on-js-reload []
      (.info js/console "Reloading Javascript...")
      (dommy/set-text! (dommy/sel1 ".greetee-name") (foo/hello) #_(:name @app-state)))
