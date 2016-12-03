(ns advent-of-code-2016.day3
  (:require [advent-of-code-2016.core :as ad-core]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def problem-input (slurp (io/resource "day-3-input.txt")))

(defn valid-triangle?
  [a b c]
  (let [[small mid large] (sort [a b c])]
    (> (+ small mid) large)))

(->> problem-input
     str/split-lines
     (map #(re-seq #"\d+" %))
     (map #(map ad-core/parse-int %))
     (filter #(apply valid-triangle? %))
     count
     prn)

(->> problem-input
     str/split-lines
     (map #(re-seq #"\d+" %))
     (map #(map ad-core/parse-int %))
     (partition 3)
     (mapcat ad-core/transpose)
     (filter #(apply valid-triangle? %))
     count
     prn)
