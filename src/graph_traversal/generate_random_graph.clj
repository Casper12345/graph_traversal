(ns graph-traversal.generate-random-graph
  (:require [clojure.set :as set]))

(defn random-weight []
  "Generates a random weight between 0 and 10."
  (rand-int 11))

(defn valid-input? [N S]
  "Checks if the input N and S are valid."
  (and (>= S (dec N)) (<= S (* N (dec N)))))

(defn generate-minimal-tree [N]
  "Generates a minimal tree (N-1 edges) to ensure the graph is connected."
  (loop [remaining-nodes (set (range 2 (inc N)))
         edges []
         current-node 1]
    (if (empty? remaining-nodes)
      edges
      (let [next-node (rand-nth (vec remaining-nodes))]
        (recur (disj remaining-nodes next-node)
               (conj edges [current-node next-node (random-weight)])
               next-node)))))

(defn generate-random-edges [N S existing-edges]
  "Generates random additional edges to meet the sparseness requirement."
  (let [all-pairs (for [i (range 1 (inc N))
                        j (range 1 (inc N))
                        :when (not= i j)] [i j])
        existing-pairs (set (map #(take 2 %) existing-edges))
        possible-pairs (set/difference (set all-pairs) existing-pairs)
        additional-edges (take (- S (count existing-edges))
                               (shuffle possible-pairs))]
    (map #(conj % (random-weight)) additional-edges)))

(defn build-graph [edges]
  "Builds the graph in the required format."
  (let [nodes (into #{} (concat (map first edges) (map second edges)))]
    (reduce (fn [graph [u v w]]
              (update graph (keyword (str u)) conj [(keyword (str v)) w]))
            (into {} (for [n nodes] [(keyword (str n)) []]))
            edges)))

(defn generate-graph [N S]
  "Generates a random simple, connected graph with N vertices and S edges."
  (if (valid-input? N S)
    (let [minimal-tree (generate-minimal-tree N)
          random-edges (generate-random-edges N S minimal-tree)
          all-edges (concat minimal-tree random-edges)]
      (build-graph all-edges))
    (throw (ex-info "Invalid input: S must be between N-1 and N(N-1) inclusive." {:N N :S S}))))
