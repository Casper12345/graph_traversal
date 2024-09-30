(defproject graph_traversal "0.1.0-SNAPSHOT"
  :description "Graph explorer"
  :dependencies [[org.clojure/clojure "1.11.1"] [org.clojure/data.priority-map "1.2.0"]]
  :plugins [[lein-cljfmt "0.9.2"]]
  :main ^:skip-aot app.core
  :test-paths ["test"]
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
