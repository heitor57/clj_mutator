(defproject mutant-tool "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [rewrite-clj "0.6.1"]
                 [clj-diffmatchpatch "0.0.9.3"]
                 [jansi-clj "0.1.1"]
                 [org.clojure/tools.namespace "0.2.11"]
                 ]
  :main ^:skip-aot mutant-tool.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
