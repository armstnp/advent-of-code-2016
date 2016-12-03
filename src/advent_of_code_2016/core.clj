(ns advent-of-code-2016.core)

(defn parse-int
  [n]
  (Integer/parseInt n))

(defn transpose
  "Transposes the given nested sequence into nested vectors, as
  in matrix transposition.  E.g., (transpose [[1 2 3] [4 5 6]])
  would return [[1 4] [2 5] [3 6]]."
  [s]
  (vec (apply map vector s)))

(defn clamp
  [n min-val max-val]
  (max min-val (min max-val n)))
