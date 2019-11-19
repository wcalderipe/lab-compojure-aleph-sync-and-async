(defproject lab-compojure-aleph-async "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/core.async "0.4.500"]
                 [aleph "0.4.6"]
                 [manifold "0.1.8"]
                 [compojure "1.6.1"]
                 [byte-streams "0.2.4"]]

  :main ^:skip-aot lab-compojure-aleph-async.core

  :target-path "target/%s"

  :profiles {:uberjar {:aot :all}})
