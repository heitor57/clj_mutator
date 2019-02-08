(ns mutant-tool.core
  (:require [mutant-tool.mutator :as mut]
            [mutant-tool.filehelper :as fh]
            [rewrite-clj.zip :as z]
            [jansi-clj [core :as jansi] auto]
            )

  (:gen-class))
(def code-test "(+ 1 (- 3 1))
(+ 3 2)
(or nil 1)")
(defn -main
  "I don't do a whole lot ... yet."
  [& args]
   (mut/mutate-proj (read-line) (read-line))
  #_(->>  (mut/mutate (z/of-string code-test)) (mut/mutations-print-diff code-test))
  )
