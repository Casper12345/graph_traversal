(ns graph-traversal.generate-test
  (:require [clojure.test :refer :all]
            [graph-traversal.generate-random-graph :refer :all]))

(deftest test-valid-input
  (testing "valid input validation"
    (is (true? (valid-input? 4 5)))   ; Valid case
    (is (false? (valid-input? 4 2)))  ; Invalid case: S < N-1
    (is (false? (valid-input? 4 13))) ; Invalid case: S > N(N-1)
    (is (true? (valid-input? 4 12))))) ; Edge case: S = N(N-1)

(deftest test-minimal-tree
  (testing "minimal tree generation"
    (let [N 4
          tree (generate-minimal-tree N)
          node-set (set (flatten (map #(take 2 %) tree)))]
      ;; Check that the minimal tree has exactly N-1 edges
      (is (= (dec N) (count tree)))

      ;; Check that the minimal tree contains all nodes
      (is (= N (count node-set)))

      ;; Check that there are no duplicate edges and no self-loops
      (is (= (count tree) (count (distinct tree))))
      (is (not-any? (fn [[u v _]] (= u v)) tree))

      ;; Check that weights are between 0 and 10
      (is (every? #(and (integer? (nth % 2)) (<= 0 (nth % 2) 10)) tree)))))

(deftest test-random-edges
  (testing "additional random edges"
    (let [existing-edges (generate-minimal-tree 4)
          random-edges (generate-random-edges 4 5 existing-edges)
          total-edges (concat existing-edges random-edges)]
      (is (= 5 (count total-edges))) ; Total edges should be S = 5
      (is (every? #(and (integer? (nth % 2)) (<= 0 (nth % 2) 10)) random-edges))))) ; Weights in the correct range

(deftest test-build-graph
  (testing "graph structure"
    (let [edges [[1 2 5] [2 3 6] [3 4 2]]
          expected-graph {:1 [[:2 5]], :2 [[:3 6]], :3 [[:4 2]], :4 []}
          graph (build-graph edges)]
      ;; Check that the graph keys are correctly generated
      (is (= (set (keys expected-graph)) (set (keys graph))))

      ;; Check that the edges from each node are correct
      (doseq [[node expected-edges] expected-graph]
        (is (= expected-edges (get graph node)))))))

(defn reachable-nodes [graph start]
  "Performs a DFS or BFS to find all reachable nodes from the start node."
  (loop [to-visit [start]
         visited #{}]
    (if (empty? to-visit)
      visited
      (let [current (first to-visit)
            neighbors (map first (get graph current))
            new-to-visit (concat (rest to-visit) (remove visited neighbors))]
        (recur new-to-visit (conj visited current))))))

(defn connected-graph? [graph]
  "Checks if all nodes in the graph are reachable from any node (e.g., node 1)."
  (let [all-nodes (set (keys graph))
        reachable-from-1 (reachable-nodes graph (keyword "1"))]
    (= all-nodes reachable-from-1)))

(deftest test-generate-graph
  (testing "graph generation with valid input"
    (let [graph (generate-graph 4 5)
          nodes (keys graph)
          edges (apply concat (vals graph))]

      ;; Check that there are N nodes in the graph
      (is (= 4 (count nodes)))

      ;; Check that there are exactly S edges in the graph
      (is (= 5 (count edges)))

      ;; Check that all weights are between 0 and 10
      (is (every? #(<= 0 (second %) 10) edges))

      ;; Ensure all nodes are initialized in the graph
      (is (every? #(contains? graph %) (map keyword (map str (range 1 5)))))

      ;; Ensure the graph is connected by checking reachability
      (is (connected-graph? graph)))))

(deftest test-invalid-input
  (testing "handling invalid input"
    (try
      (generate-graph 4 2)
      (is false "Expected an ExceptionInfo to be thrown")
      (catch clojure.lang.ExceptionInfo e
        (is (= "Invalid input: S must be between N-1 and N(N-1) inclusive." (.getMessage e)))))
    (try
      (generate-graph 4 13)
      (is false "Expected an ExceptionInfo to be thrown")
      (catch clojure.lang.ExceptionInfo e
        (is (= "Invalid input: S must be between N-1 and N(N-1) inclusive." (.getMessage e)))))))

(run-tests)

