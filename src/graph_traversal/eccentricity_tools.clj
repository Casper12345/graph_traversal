(ns graph-traversal.eccentricity-tools
  (:require [graph-traversal.dijkstra :refer [dijkstra]]))

(defn eccentricity [graph node]
  "Function to compute eccentricity for one node"
  (let [[distances _] (dijkstra graph node)]                ;; Run Dijkstra to get distances
    (->> (vals distances)                                   ;; Get the distances as a list
         (remove #(= ##Inf %))                              ;; Remove infinite distances (unreachable nodes)
         (apply max))))                                     ;; Find the maximum distance

(defn eccentricities [graph]
  "Function to compute eccentricity for all nodes"
  (reduce (fn [ecc-map node]
            (assoc ecc-map node (eccentricity graph node)))
          {}
          (keys graph)))

(defn radius [graph]
  "Function to calculate the radius of the graph"
  (let [eccentricity-map (eccentricities graph)]            ;; Calculate eccentricities for all nodes
    (apply min (vals eccentricity-map))))                   ;; Return the minimum eccentricity

(defn diameter [graph]
  "Function to calculate the diameter of the graph"
  (let [eccentricity-map (eccentricities graph)]            ;; Calculate eccentricities for all nodes
    (apply max (vals eccentricity-map))))                   ;; Return the maximum eccentricity
