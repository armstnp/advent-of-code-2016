(ns advent-of-code-2016.day8
  (:require [advent-of-code-2016.core :as ad-core :refer [transpose parse-int]]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

(def problem-input (slurp (io/resource "day-8-input.txt")))

(defn rectangle
  [screen rect-width rect-height]
  (vec (map-indexed (fn [row-n row]
                      (if (< row-n rect-height)
                        (vec (concat (repeat rect-width \#) (drop rect-width row)))
                        row))
                    screen)))

(defn rotate-row
  [screen row by-pixels]
  (update screen row
    #(let [width (count %)]
       (->> %
            cycle
            (drop (- width by-pixels))
            (take width)
            vec))))

(defn rotate-column
  [screen column by-pixels]
  (-> screen transpose (rotate-row column by-pixels) transpose))

(defn parse-rect-instruction
  [s]
  (if-let [match (re-matches #"rect (\d+)x(\d+)" s)]
    {:op rectangle :params (map parse-int (next match))}))

(defn parse-rotate-row-instruction
  [s]
  (if-let [match (re-matches #"rotate row y=(\d+) by (\d+)" s)]
    {:op rotate-row :params (map parse-int (next match))}))

(defn parse-rotate-col-instruction
  [s]
  (if-let [match (re-matches #"rotate column x=(\d+) by (\d+)" s)]
    {:op rotate-column :params (map parse-int (next match))}))

(def try-parse-all (juxt parse-rect-instruction
                         parse-rotate-row-instruction
                         parse-rotate-col-instruction))

(defn parse-instruction
  [s]
  (first (filter (complement nil?) (try-parse-all s))))

(defn apply-instruction
  [screen {:keys [op params]}]
  (apply op screen params))

(def starting-screen (vec (repeat 6 (vec (repeat 50 \space)))))

(->> problem-input
     str/split-lines
     (map parse-instruction)
     (reduce apply-instruction starting-screen)
     (mapcat identity)
     (filter #(= \# %))
     count)

(->> problem-input
     str/split-lines
     (map parse-instruction)
     (reduce apply-instruction starting-screen)
     (map #(apply str %))
     (str/join \newline)
     println)
