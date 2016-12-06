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
       ((juxt first last))))

(defn get-min-max-codes
  [s]
  (->> s
       str/split-lines
       ad-core/transpose
       (map max-min-freqs)
       ad-core/transpose
       (map (partial apply str))))

(assert (= '("easter" "advent")
           (get-min-max-codes
             (str/join \newline
                       ["eedadn"
                        "drvtee"
                        "eandsr"
                        "raavrd"
                        "atevrs"
                        "tsrnev"
                        "sdttsa"
                        "rasrtv"
                        "nssdts"
                        "ntnada"
                        "svetve"
                        "tesnvt"
                        "vntsnd"
                        "vrdear"
                        "dvrsen"
                        "enarar"]))))

(->> problem-input
     get-min-max-codes
     (map (partial apply str) ["Part A: " "Part B: "]))
