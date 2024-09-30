(ns graph-traversal.dijkstra
  (:require [clojure.data.priority-map :refer [priority-map]]))

(defn dijkstra [graph start]
  "Dijkstra's algorithm to find the shortest path from a given start node"
  (let [start-node start
        ;; Initialize distances: all nodes start with infinity, except the start node
        distances (assoc (into {} (map #(vector % ##Inf) (keys graph))) start-node 0)
        ;; Previous nodes to reconstruct the path
        previous {}
        ;; Priority queue with the start node at distance 0
        pq (priority-map start-node 0)]

    ;; Main Dijkstra loop
    (loop [distances distances
           previous previous
           pq pq]
      (if (empty? pq)
        [distances previous]  ;; Return distances and previous map when finished
        (let [[current-node current-dist] (peek pq)  ;; Get the closest node
              neighbors (get graph current-node)      ;; Get neighbors of current node
              pq (pop pq)]  ;; Remove the current node from the queue
          (recur
            ;; Update distances for neighbors
           (reduce (fn [distances [neighbor weight]]
                     (let [neighbor-key neighbor
                           alt (+ current-dist weight)]
                       (if (< alt (get distances neighbor-key ##Inf))
                         (assoc distances neighbor-key alt)  ;; Update distance if a shorter path is found
                         distances)))
                   distances
                   neighbors)
            ;; Update previous nodes for path reconstruction
           (reduce (fn [previous [neighbor weight]]
                     (let [neighbor-key neighbor
                           alt (+ current-dist weight)]
                       (if (< alt (get distances neighbor-key ##Inf))
                         (assoc previous neighbor-key current-node) ;; Track the path
                         previous)))
                   previous
                   neighbors)
            ;; Update priority queue with new distances
           (reduce (fn [pq [neighbor weight]]
                     (let [neighbor-key neighbor
                           alt (+ current-dist weight)]
                       (if (< alt (get distances neighbor-key ##Inf))
                         (assoc pq neighbor-key alt)
                         pq)))
                   pq
                   neighbors)))))))

(defn reconstruct-path [previous end]
  "Function to reconstruct the shortest path from start node to the end node"
  (let [path (loop [path [] current end]
               (if (nil? current)
                 path
                 (recur (conj path current) (get previous current))))]
    (if (empty? path)
      nil  ;; No path found
      (reverse path))))  ;; Return the path in correct order

(defn shortest-path [graph start end]
  "Function to find shortest path given a start and a end node"
  (let [[distances previous] (dijkstra graph start)
        distance (get distances end ##Inf)] ;; Use ##Inf as the default
    (if (= distance ##Inf)
      (str "No path from " (name start) " to " (name end))
      (let [path (reconstruct-path previous end)]
        {:path path :distance distance}))))
