(ns mutant-tool.operators)
(def opstr [["+" "-" "*" "/"] ["or" "and"] ["empty?" "seq"]])
(defn ^:private createop
  "Used to generate a hashmap of operators and their relationship
  (createop [[\"+\" \"-\" \"*\" \"/\"] [\"or\" \"and\"] [\"empty?\" \"seq\"]])
  that generate: {:+ (- * /), :- (+ * /), :* (+ - /), :/ (+ - *), :or (and), :and (or), :empty? (seq), :seq (empty?)}"
  [value]
  (loop [queue value
         finalval {}] 
    (if (empty? queue)
      finalval
      (recur 
        (rest queue) 
        (into 
          finalval 
          (into [] 
                (for [op (first queue)] 
                  (conj [] (keyword op) (map symbol (filter #(not= op %) (first queue)))))))
        )
      )
    )
  )

(def operators (createop opstr))
