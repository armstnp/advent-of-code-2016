(ns advent-of-code-2016.day4
  (:require [advent-of-code-2016.core :as ad-core]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :as set]))

(def problem-input (slurp (io/resource "day-4-input.txt")))

(defn match-room
  [s]
  (let [[name id checksum] (rest (re-matches #"([a-z-]+)-(\d+)\[(\w{5})\]" s))]
    {:name name
     :id (ad-core/parse-int id)
     :checksum checksum}))

(defn get-name-freqs
  [name]
  (sort-by (juxt (comp - second) first)
           (map #(vector (str (first %)) (second %))
                (dissoc (frequencies name) \-))))

(defn get-expected-checksum
  [name]
  (apply str (map first (take 5 (get-name-freqs name)))))

(defn matches-checksum?
  [{:keys [name checksum]}]
  (= checksum (get-expected-checksum name)))

(->> problem-input
     str/split-lines
     (map match-room)
     (filter matches-checksum?)
     (map :id)
     (reduce +)
     prn)

;; Quick inspiration for Caesar cipher implementation from
;; https://clojurebridge.github.io/community-docs/docs/exercises/caesar-cipher/
;; - Thanks!
(def alphabet "abcdefghijklmnopqrstuvwxyz")

(defn cipher
  [shift-n]
  (zipmap alphabet (take 26 (drop (mod shift-n 26) (cycle alphabet)))))

(defn shift-by-id
  [{:keys [name id] :as room}]
  (let [room-cipher (assoc (cipher id) \- \-)]
    (assoc room :name (apply str (map room-cipher name)))))

(->> problem-input
     str/split-lines
     (map match-room)
     (filter matches-checksum?)
     (map shift-by-id)
     (map #(select-keys % [:name :id]))
     (filter #(re-matches #".+(pole).+" (:name %)))
     prn)
