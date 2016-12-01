(ns advent-of-code-2016.day1
  (:require [clojure.set :as set]
            [clojure.java.io :as io]))

(def char->rotation
  {\R :right
   \L :left})

(def rotate-right
  {:north :east
   :east :south
   :south :west
   :west :north})

(def rotate-left (set/map-invert rotate-right))

(def initial-state
  {:coords [0 0]
   :dir :north})

(defn str->movement
  [s]
  (let [[rot-char & steps-str] s]
    {:rotation (char->rotation rot-char)
     :steps (Integer/parseInt (apply str steps-str))}))

(defn parse-input
  [s]
  (->> s
       (re-seq #"\w+")
       (map str->movement)))

(defn rotate
  [dir rotation]
  (let [rotator (if (= :right rotation) rotate-right rotate-left)]
    (rotator dir)))

(defn move
  [[x y] dir steps]
  (case dir
    :north [x (+ y steps)]
    :east [(+ x steps) y]
    :south [x (- y steps)]
    :west [(- x steps) y]))

(defn rotate-and-move
  [{:keys [coords dir]} {:keys [rotation steps]}]
  (let [new-dir (rotate dir rotation)
        new-coords (move coords new-dir steps)]
    {:coords new-coords
     :dir new-dir}))

(defn walk
  [movements]
  (reduce rotate-and-move initial-state movements))

(defn distance
  [{[x y] :coords}]
  (+ (Math/abs x) (Math/abs y)))

(def walk-distance (comp distance walk parse-input))

(def test-1 (walk-distance "R2, L3"))
(assert (= test-1 5))

(def test-2 (walk-distance "R2, R2, R2"))
(assert (= test-2 2))

(def test-3 (walk-distance "R5, L5, R5, R3"))
(assert (= test-3 12))

(def problem-1-input (slurp (io/resource "day-1-input.txt")))
(def problem-1-output (walk-distance problem-input))

(prn problem-1-output)
