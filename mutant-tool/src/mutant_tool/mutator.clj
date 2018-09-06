(ns mutant-tool.mutator)
(defmacro createop
   "Used to generate a hashmap of operators and their relationship
   (createop varname [\"or\" \"and\" \"+\" \"-\"])
   that generate: {:or \"and\", :and \"or\", :+ \"-\", :- \"+\"}"
  [aka value]
  (let [finalval (loop [queue value
                       finalval {}]
                  (if (empty? queue)
                    finalval
                    (let [[part sec & remaining] queue]
                      (recur 
                        remaining 
                        (into finalval {(keyword part) (identity sec) (keyword sec) (identity part)})))))]
    `(def ~aka ~finalval)))

(createop operators ["or" "and" "+" "-" "empty?" "seq"])
