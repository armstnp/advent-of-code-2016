(ns advent-of-code-2016.day8
  (:require [advent-of-code-2016.core :refer [transpose parse-int defn-split]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

(def problem-input (slurp (io/resource "day-8-input.txt")))

(def screen-width 50)
(def screen-height 6)
(def pixel-off \space)
(def pixel-on \#)
(def starting-screen
  (->> pixel-off (repeat screen-width) vec
                 (repeat screen-height) vec))

(defn line
  [row line-width]
  (vec (concat (repeat line-width pixel-on)
               (drop line-width row))))

(defn rectangle
  [screen rect-width rect-height]
  (->> screen
       (map-indexed (fn [row-n row]
                      (if (>= row-n rect-height)
                        row
                        (line row rect-width))))
       vec))

(defn rotate-row
  [screen row-n by-pixels]
  (update screen row-n
    #(let [width (count %)]
       (->> %
            cycle
            (drop (- width by-pixels))
            (take width)
            vec))))

(defn rotate-column
  [screen column by-pixels]
  (-> screen transpose (rotate-row column by-pixels) transpose))

(defn-split gen-parser
  [pattern op | s]
  (if-let [[full-match & args] (re-matches pattern s)]
    {:op op
     :params (map parse-int args)}))

(def parse-rect-instruction (gen-parser #"rect (\d+)x(\d+)" rectangle))

(def parse-rotate-row-instruction
  (gen-parser #"rotate row y=(\d+) by (\d+)" rotate-row))

(def parse-rotate-col-instruction
  (gen-parser #"rotate column x=(\d+) by (\d+)" rotate-column))

(def try-parse-all (juxt parse-rect-instruction
                         parse-rotate-row-instruction
                         parse-rotate-col-instruction))

(defn parse-instruction
  [s]
  (first (filter (complement nil?) (try-parse-all s))))

(defn apply-instruction
  [screen {:keys [op params]}]
  (apply op screen params))

(def ending-screen
  (->> problem-input
       str/split-lines
       (map parse-instruction)
       (reduce apply-instruction starting-screen)))

(defn pixel-on?
  [pixel]
  (= pixel pixel-on))

;; Part A
(->> ending-screen
     (mapcat identity)
     (filter #(= pixel-on %))
     count)

;; Part B
(->> ending-screen
     (map #(apply str %))
     (str/join \newline)
     println)
