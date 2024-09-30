(ns graph-traversal.eccentricity-tools-test
  (:require [clojure.test :refer :all]
            [graph-traversal.eccentricity-tools :refer :all]))

(deftest test-eccentricity-directed-graph
  (let [graph {:a [[:b 1] [:c 2]]
               :b [[:d 2]]
               :c [[:d 1]]
               :d []}]
    ;; Node :a can reach all other nodes with maximum distance 3 (a -> b -> d or a -> c -> d)
    (is (= 3 (eccentricity graph :a)))
    ;; Node :b can reach :d with maximum distance 2
    (is (= 2 (eccentricity graph :b)))
    ;; Node :c can reach :d with maximum distance 1
    (is (= 1 (eccentricity graph :c)))
    ;; Node :d can't reach any other node (it has no outgoing edges), so its eccentricity should be 0
    (is (= 0 (eccentricity graph :d)))))

(deftest test-eccentricities-directed-graph
  (let [graph {:a [[:b 1] [:c 2]]
               :b [[:d 2]]
               :c [[:d 1]]
               :d []}]
    ;; Test eccentricities for all nodes in the graph
    (is (= {:a 3
            :b 2
            :c 1
            :d 0}
           (eccentricities graph)))))

(deftest test-radius-directed-graph
  (let [graph {:a [[:b 1] [:c 2]]
               :b [[:d 2]]
               :c [[:d 1]]
               :d []}]
    ;; The radius is the smallest eccentricity, which should be 0 (node :d can't reach any other node)
    (is (= 0 (radius graph)))))

(deftest test-diameter-directed-graph
  (let [graph {:a [[:b 1] [:c 2]]
               :b [[:d 2]]
               :c [[:d 1]]
               :d []}]
    ;; The diameter is the largest eccentricity, which should be 3 (maximum distance from :a)
    (is (= 3 (diameter graph)))))

;; Test with a weakly connected but directed graph where some nodes can't reach others
(deftest test-weakly-connected-directed-graph
  (let [graph {:a [[:b 1]]
               :b [[:c 1]]
               :c []
               :d [[:e 2]]
               :e []}]
    ;; Node :a can only reach :b and :c, so its eccentricity is 2
    (is (= 2 (eccentricity graph :a)))
    ;; Node :d can only reach :e, so its eccentricity is 2
    (is (= 2 (eccentricity graph :d)))
    ;; Node :c can't reach any other node, so its eccentricity is 0
    (is (= 0 (eccentricity graph :c)))
    ;; Node :e can't reach any other node, so its eccentricity is 0
    (is (= 0 (eccentricity graph :e)))
    ;; Test eccentricities for all nodes
    (is (= {:a 2
            :b 1
            :c 0
            :d 2
            :e 0}
           (eccentricities graph)))
    ;; The radius of the graph is the smallest eccentricity, which should be 0
    (is (= 0 (radius graph)))
    ;; The diameter of the graph is the largest eccentricity, which should be 2
    (is (= 2 (diameter graph)))))

;; Edge case: isolated node
(deftest test-isolated-node
  (let [graph {:a [[:b 1]]
               :b [[:c 1]]
               :c []
               :d []}]  ;; Node :d is isolated (no incoming or outgoing edges)
    ;; Node :a can reach :b and :c with eccentricity 2
    (is (= 2 (eccentricity graph :a)))
    ;; Node :d is isolated, so its eccentricity should be 0
    (is (= 0 (eccentricity graph :d)))
    ;; Test eccentricities for all nodes
    (is (= {:a 2
            :b 1
            :c 0
            :d 0}
           (eccentricities graph)))
    ;; The radius should be 0 (smallest eccentricity)
    (is (= 0 (radius graph)))
    ;; The diameter should be 2 (largest eccentricity)
    (is (= 2 (diameter graph)))))

;; Edge case: single node graph
(deftest test-single-node-graph
  (let [graph {:a []}]  ;; Single node with no outgoing edges
    ;; Eccentricity of a single node is 0 (it can't reach any other node)
    (is (= 0 (eccentricity graph :a)))
    ;; Test eccentricities
    (is (= {:a 0}
           (eccentricities graph)))
    ;; The radius and diameter should both be 0
    (is (= 0 (radius graph)))
    (is (= 0 (diameter graph)))))

(run-tests)
