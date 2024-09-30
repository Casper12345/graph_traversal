(ns repl.core
  (:require [graph-traversal.generate-random-graph :refer [generate-graph]]
            [graph-traversal.eccentricity-tools :refer [eccentricity radius diameter] :rename {eccentricity ec radius ra diameter dm}]
            [graph-traversal.dijkstra :refer [shortest-path] :rename {shortest-path sp}]))

(defn make-graph [n s]
  "Generate a random graph"
  (generate-graph n s))

(defn shortest-path [graph start-node end-node]
  "Shortest path function to be used in the REPL"
  (sp graph start-node end-node))

(defn eccentricity [graph start-node]
  "Eccentricity function to be used in the REPL"
  (ec graph start-node))

(defn radius [graph]
  "Find the radius of the graph"
  (ra graph))

(defn diameter [graph]
  "Find the diameter of the graph"
  (dm graph))
