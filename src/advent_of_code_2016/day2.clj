(ns advent-of-code-2016.day2
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

;; Coords format optimized for indexing: [row col]
;; Clamped 0-2
    
(def problem-input (slurp (io/resource "day-2-input.txt")))

(def translation
  {\U [-1  0]
   \D [ 1  0]
   \L [ 0 -1]
   \R [ 0  1]})

(def simple-pad
  {:layout [[1 2 3]
            [4 5 6]
            [7 8 9]]
   :start [1 1]})

(def complex-pad
  {:layout [[nil nil   1 nil nil]
            [nil   2   3   4 nil]
            [  5   6   7   8   9]
            [nil  \A  \B  \C nil]
            [nil nil  \D nil nil]]
   :start [2 2]})

(defn clamp
  [n min-val max-val]
  (max min-val (min max-val n)))

(defn translate-in-pad
  [button-pad [row col :as button] dir]
  (let [[d-row d-col] (translation dir)
        new-button [(+ row d-row) (+ col d-col)]
        new-button-val (get-in button-pad new-button)]
    (if-not (nil? new-button-val)
      new-button
      button)))

(defn move-button
  [button-pad button dir]
  (if (not= dir :press)
    (translate-in-pad button-pad button dir)
    button))

(defn press-button
  [button-pad button pressed dir]
  (if (= dir :press)
    (conj pressed (get-in button-pad button))
    pressed))

(defn move
  "Returns a function that moves across or presses a button on
  the provided button pad."
  [button-pad]
  (let [move-button (partial move-button button-pad)
        press-button (partial press-button button-pad)]
    (fn [{:keys [button pressed]} dir]
      {:button (move-button button dir)
       :pressed (press-button button pressed dir)})))

(defn enter-pad
  [button-pad start-button directions]
  (reduce (move button-pad)
          {:button start-button :pressed []}
          directions))

(defn parse-input
  [s]
  (mapcat #(concat % [:press]) (str/split-lines s)))

(defn solve-problem
  [s {:keys [layout start]}]
  (->> s
       parse-input
       (enter-pad layout start)
       :pressed
       (apply str)
       prn))

(solve-problem problem-input simple-pad)
(solve-problem problem-input complex-pad)
