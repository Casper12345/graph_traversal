(ns graph-traversal.dijkstra-test
  (:require [clojure.test :refer :all]
            [graph-traversal.dijkstra :refer :all]))

(deftest test-dijkstra
  (let [graph {:a {:b 1, :c 4}
               :b {:a 1, :c 2, :d 5}
               :c {:a 4, :b 2, :d 1}
               :d {:b 5, :c 1}}
        start :a
        [distances previous] (dijkstra graph start)]

    ;; Test distances
    (is (= {:a 0, :b 1, :c 3, :d 4} distances) "Checking computed distances")

    ;; Test previous nodes
    (is (= {:b :a, :c :b, :d :c} previous) "Checking previous nodes for path reconstruction")))

(deftest test-reconstruct-path
  (let [previous {:b :a, :c :b, :d :c}]

    ;; Test path from :a to :d
    (is (= [:a :b :c :d] (reconstruct-path previous :d)) "Checking path reconstruction from :a to :d")

    ;; Test path from :a to :b
    (is (= [:a :b] (reconstruct-path previous :b)) "Checking path reconstruction from :a to :b")

    ;; Test path where no previous node exists (e.g. from :a to :a)
    (is (= [:a] (reconstruct-path {} :a)) "Checking path reconstruction for start node")))

(deftest test-shortest-path
  (let [graph {:a {:b 1, :c 4}
               :b {:a 1, :c 2, :d 5}
               :c {:a 4, :b 2, :d 1}
               :d {:b 5, :c 1}}]

    ;; Test shortest path from :a to :d
    (is (= {:path [:a :b :c :d], :distance 4}
           (shortest-path graph :a :d))
        "Checking shortest path from :a to :d")

    ;; Test shortest path from :a to :b
    (is (= {:path [:a :b], :distance 1}
           (shortest-path graph :a :b))
        "Checking shortest path from :a to :b")

    ;; Test no path from :a to a non-existing node
    (is (= "No path from a to e"
           (shortest-path graph :a :e))
        "Checking no path from :a to :e")))

(run-tests)
