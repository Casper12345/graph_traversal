(ns app.core
  (:require [graph-traversal.generate-random-graph :refer [generate-graph]]
            [graph-traversal.eccentricity-tools :refer [eccentricity radius diameter]]
            [graph-traversal.dijkstra :refer [shortest-path]])
  (:gen-class))

(defn usage []
  (println
   "Usage: lein run [--print-graph] <flag> <N> <S> [start-node end-node]\n"
   "Where flags are:\n"
   "  --shortest-path        : Calculate the shortest path between the start and end nodes. Requires start-node and end-node.\n"
   "  --eccentricity         : Calculate the eccentricity of a node (optional start-node, defaults to first node).\n"
   "  --radius               : Calculate the radius of the graph.\n"
   "  --diameter             : Calculate the diameter of the graph.\n"
   "  --print-graph          : Optional flag to print the generated graph before calculations.\n"
   "  <N> is the number of vertices.\n"
   "  <S> is the number of edges.\n"
   "  [start-node] and [end-node] are optional parameters for the shortest-path action.\n"
   "  [start-node]  is optional parameter for eccentricity action."))

(defn print-graph [graph]
  "Helper function to print the generated graph in a readable format."
  (println "Generated Graph:")
  (doseq [[node neighbors] graph]
    (println (str node " -> " neighbors))))

(defn -main
  "Main function to process flags, generate a random graph, and perform requested operations."
  [& args]
  ;; Ensure at least three arguments (flag, N, and S) are provided
  (if (< (count args) 3)
    (do
      (usage)
      (System/exit 1))
    (let [print-graph? (= "--print-graph" (first args))  ;; Check if --print-graph is the first argument
          arg-start (if print-graph? 1 0)                ;; Adjust argument index based on print flag
          flag (nth args arg-start)                      ;; First argument is the flag (operation)
          N (Integer/parseInt (nth args (+ arg-start 1))) ;; Second argument is the number of vertices
          S (Integer/parseInt (nth args (+ arg-start 2))) ;; Third argument is the number of edges
          random-graph (generate-graph N S)              ;; Generate the random graph
          first-node (first (keys random-graph))         ;; Get the first node in the graph
          last-node (last (keys random-graph))]          ;; Get the last node in the graph

      ;; Print the generated graph if --print-graph is passed
      (when print-graph?
        (print-graph random-graph))

      ;; Check which flag was passed and execute the corresponding operation
      (cond
        ;; Handle the shortest-path flag
        (= flag "--shortest-path")
        (let [start-node (if (> (count args) (+ arg-start 3))
                           (keyword (nth args (+ arg-start 3))) ;; Use passed start-node
                           first-node)                        ;; Default to the first node
              end-node (if (> (count args) (+ arg-start 4))
                         (keyword (nth args (+ arg-start 4))) ;; Use passed end-node
                         last-node)]                          ;; Default to the last node
          (let [result (shortest-path random-graph start-node end-node)]
            (println "Shortest path from" start-node "to" end-node ":")
            (println "Path:" (:path result))
            (println "Distance:" (:distance result))))

        ;; Handle the eccentricity flag with optional start node
        (= flag "--eccentricity")
        (let [start-node (if (> (count args) (+ arg-start 3))
                           (keyword (nth args (+ arg-start 3))) ;; Use passed start-node
                           first-node)]                        ;; Default to the first node
          (let [ecc (eccentricity random-graph start-node)]
            (println "Eccentricity of node" start-node ": " ecc)))

        ;; Handle the radius flag
        (= flag "--radius")
        (let [graph-radius (radius random-graph)]
          (println "Radius of the graph: " graph-radius))

        ;; Handle the diameter flag
        (= flag "--diameter")
        (let [graph-diameter (diameter random-graph)]
          (println "Diameter of the graph: " graph-diameter))

        ;; If no valid flag is passed, display usage
        :else
        (do
          (usage)
          (System/exit 1))))))
