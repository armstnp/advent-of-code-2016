(ns advent-of-code-2016.day6
  (:require [advent-of-code-2016.core :as ad-core :refer [parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def problem-input (slurp (io/resource "day-6-input.txt")))

(defn max-min-freqs
  [s]
  (->> s
       frequencies
       (sort-by (comp - second))
       (map first)
       (apply (juxt first last))))

(->> problem-input
     str/split-lines
     ad-core/transpose
     (map max-min-freqs)
     ad-core/transpose
     (map (partial apply str) ["Part A: " "Part B: "]))
