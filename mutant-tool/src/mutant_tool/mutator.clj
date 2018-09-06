(ns mutant-tool.mutator)
(defn createop
   "Used to generate a hashmap of operators and their relationship
   (createop [\"or\" \"and\" \"+\" \"-\"])
   that generate: {:or 'and, :and 'or, :+ '-, :- '+}"
  [value]
  (loop [queue value
                       finalval {}]
                  (if (empty? queue)
                    finalval
                    (let [[part sec & remaining] queue]
                      (recur 
                        remaining 
                        (into finalval {(keyword part) (symbol sec) (keyword sec) (symbol part)}))))))
    

(def operators (createop ["or" "and" "empty?" "seq"]))
