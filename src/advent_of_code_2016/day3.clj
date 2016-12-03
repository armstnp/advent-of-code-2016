(ns advent-of-code-2016.day3
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def problem-input (slurp (io/resource "day-3-input.txt")))

(defn valid-triangle?
  [a b c]
  (let [[small mid large] (sort [a b c])]
    (> (+ small mid) large)))

(defn parse-int
  [n]
  (Integer/parseInt n))

(defn transpose
  [s]
  (apply map vector s))

(->> problem-input
     str/split-lines
     (map #(re-seq #"\d+" %))
     (map #(map parse-int %))
     (filter #(apply valid-triangle? %))
     count
     prn)

(->> problem-input
     str/split-lines
     (map #(re-seq #"\d+" %))
     (map #(map parse-int %))
     (partition 3)
     (mapcat transpose)
     (filter #(apply valid-triangle? %))
     count
     prn)
