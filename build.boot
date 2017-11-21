(def global-conf
  {:source-paths #{"src"}
   :dependencies '[[org.clojure/clojure "1.8.0"]
                   [adzerk/boot-cljs "2.1.0" :scope "test"]
                   [powerlaces/boot-figreload "0.1.1-SNAPSHOT" :scope "test"]

                   [pandeiro/boot-http "0.7.6" :scope "test"]
                   [crisptrutski/boot-cljs-test "0.2.2" :scope "test"]
                   [binaryage/dirac "RELEASE" :scope "test"]
                   [binaryage/devtools "RELEASE" :scope "test"]
                   [powerlaces/boot-cljs-devtools "0.2.0" :scope "test"]

                   [adzerk/boot-cljs-repl "0.3.3" :scope "test"]
                   [com.cemerick/piggieback "0.2.1" :scope "test"]
                   [weasel "0.7.0" :scope "test"]
                   [org.clojure/tools.nrepl "0.2.13" :scope "test"]
                   [sparkfund/boot-lein-generate "0.3.0" :scope "test"]

                   ;; App deps
                   [org.clojure/clojurescript "1.9.946"]
                   [prismatic/dommy "1.1.0" :scope "test"]


                   ; node
                   [funcool/bide "1.6.0"]
                   [funcool/cuerdas "2.0.4"]
                   [funcool/cats "2.1.0"]
                   [funcool/promesa "1.8.1"]

                   ]})

(apply set-env! (mapcat identity global-conf))
(set-env!
  :boot.lein/project-clj {:source-paths (-> (:source-paths global-conf)
                                            (conj "src-node" "src-browser"))
                          :dependencies `[~@(:dependencies global-conf)]})

(task-options! pom {:project 'foo
                    :version "0.1.0-SNAPSHOT"
                    :url "https://github.com/arichiardi/figreload-demo"
                    :description "A sample project for trying lein-figwheel integration in boot-reload."
                    :license {:name "Unlicense"
                              :url "http://unlicense.org/"
                              :distribution :repo}})

(require '[adzerk.boot-cljs :refer [cljs]]
         '[adzerk.boot-cljs-repl :refer [cljs-repl]]
         '[powerlaces.boot-figreload :refer [reload]]
         '[crisptrutski.boot-cljs-test :refer [exit! test-cljs]]
         '[powerlaces.boot-cljs-devtools :refer [dirac cljs-devtools]]
         '[pandeiro.boot-http :refer [serve]]
         'boot.lein)

(deftask node []
         (merge-env! :source-paths #{"src-node"}
                     :resource-paths #{"resources-node"})
         (comp (watch)
               (notify)
               (reload :client-opts {:debug true} :asset-path "/public") ; Deprecated
               (cljs)
               (target :dir #{"target/node"})))

(deftask browser [D with-dirac bool "Enable Dirac Devtools."]
         (merge-env! :source-paths #{"src-browser"}
                     :resource-paths #{"resources-browser"})
         (comp (serve :dir "target/browser")
               (watch)
               (notify)
               (reload :client-opts {:debug true} :asset-path "/public") ; Deprecated
               (cljs-repl)
               (cljs)
               (target :dir #{"target/browser"})))

(when (> (.lastModified (clojure.java.io/file "build.boot"))
         (.lastModified (clojure.java.io/file "project.clj")))
  (boot.lein/write-project-clj))
