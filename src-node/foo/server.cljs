(ns foo.server
  (:require [bide.core :as bide]
            [cljs.nodejs :as node]
            [foo.service :as service]))


(def routes
  {:one service/http-one
   :two service/http-two})

(def router
  (bide/router
    [["/two" :two]
     ["/" :one]]))

(defn express-router [req res]
  (let [[route & args] (bide/match router (.-path req))]
    (js/console.log "router: " (pr-str route) (pr-str (.-path req)))
    (apply (get routes route) req res args)))

;(def node-util (node/require "util"))
;(def path (node/require "path"))
(def express (node/require "express"))
(def expressBodyParser (node/require "body-parser"))
(def st (node/require "st"))
(def winston (node/require "winston"))
(def expressWinston (node/require "express-winston"))
(def cookieParser (node/require "cookie-parser"))

(defn start []
  (doto (express)
    (.use (cookieParser))
    (.use (.text expressBodyParser #js {"type" "*/*"}))
    ;(.use (st #js {:url "/static" :path "../browser/"}))    ; serve the browser client assets
    (.use (.logger expressWinston #js {"transports" #js [(new winston.transports.Console #js {:json false :colorize true})]}))
    (.get "/*" express-router)
    (.listen "3000"))
  (println (str "Root server started on port 3000")))
