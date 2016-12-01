(ns advent-of-code-2016.day1
  (:require [clojure.set :as set]
            [clojure.java.io :as io]))

(def problem-input (slurp (io/resource "day-1-input.txt")))

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
    :east  [(+ x steps) y]
    :south [x (- y steps)]
    :west  [(- x steps) y]))

(defn rotate-and-move
  [{:keys [coords dir]} {:keys [rotation steps]}]
  (let [new-dir (rotate dir rotation)
        new-coords (move coords new-dir steps)]
    {:coords new-coords
     :dir new-dir}))

(defn walk
  [movements]
  (reduce rotate-and-move initial-state movements))

(defn abs
  [x]
  (max x (- x)))

(defn distance
  [{[x y] :coords}]
  (+ (abs x) (abs y)))

(defn walk-distance
  [input]
  (-> input
      parse-input
      walk
      distance))

(def test-1 (walk-distance "R2, L3"))
(assert (= test-1 5))

(def test-2 (walk-distance "R2, R2, R2"))
(assert (= test-2 2))

(def test-3 (walk-distance "R5, L5, R5, R3"))
(assert (= test-3 12))

(def problem-A-output (walk-distance problem-input))
(prn problem-A-output)

;; Day 1-B - code edited above where non-interfering with existing logic

(defn repeat-n
  [n x]
  (take n (repeat x)))

(defn walk-sequence
  "Given a current direction, takes a series of turns and step-sizes
  and converts it into a series of single steps in concrete directions.
  E.g. given a current direction of north and movements starting with
  'Right 3', the returned sequence will start with [:east :east :east ...]"
  [curr-dir movements]
  (lazy-seq
    (if (empty? movements)
      []
      (let [[next & rest] movements
            {:keys [rotation steps]} next
            new-dir (rotate curr-dir rotation)]
        (concat (repeat-n steps new-dir)
                (walk-sequence new-dir rest))))))

(defn step
  [coords dir]
  (move coords dir 1))

(defn position-sequence
  "Given a series of movements, returns a sequence of all coordinates
  visited in order starting from the initial position."
  [movements]
  (reductions
    step
    (:coords initial-state)
    (walk-sequence (:dir initial-state) movements)))

(defn first-duplicate
  "Returns the first duplicate in a sequence, or undefined output if no
  duplicate exists."
  [s]
  (reduce (fn [acc x]
            (if (contains? acc x)
              (reduced x)
              (conj acc x)))
          #{}
          s))

(defn wrap-coords
  [coords]
  {:coords coords})

(defn first-loopback-distance
  [input]
  (-> input
      parse-input
      position-sequence
      first-duplicate
      wrap-coords
      distance))

(def test-4 (first-loopback-distance "R8 R4 R4 R8"))
(assert (= test-4 4))

(def problem-B-output (first-loopback-distance problem-input))
(prn problem-B-output)
