# Graph Explorer

A graph generation and traversal tool written in Clojure that randomly generates connected, weighted digraphs. 
It can then find the shortest path from one vertex to another, given that one exists. 
Additionally, it can find the eccentricity, radius, and diameter of the graph.

## Discussion

Given the instructions to generate a simple, connected, weighted, directional graph with N vertices and
between N-1 and N(N-1) edges, the random generator will in some cases only be able to generate a weakly/unilaterally
connected digraph. Hence, in some cases, the concept of eccentricity is limited to the greatest
distance between a vertex and the vertices where a path exists.
For example, in a directed graph with 4 vertices and 3 edges, strong connectivity is not possible due to the given
number of vertices and edges. This means that the radius of the graph will
be zero, as there will be vertices that cannot be reached from any vertex in the graph.
In other cases, where the number of edges is closer to N(N-1), it is likely that a strongly connected graph will appear, 
and thus the actual eccentricity, radius and diameter of the graph can be calculated.

## Limitations

The graph representation is constructed using a map containing keywords and vectors. This is a rather inefficient
way of representing the graph, and thus the application can only handle relatively small graphs due to memory 
constraints. If the application were required to represent larger graphs, an adjacency list representation would 
be a more suitable choice. Additionally, using a properly indexed priority queue rather than the priority-map 
would improve Dijkstra's algorithm's running time.

## Installation

Make sure to install the Leiningen build tool:

https://leiningen.org/#install

And a Java sdk:

https://sdkman.io

## Usage

#### Running it from the repl from the root folder:

    $ lein repl

    app.core=> (require '[repl.core :as cli])
    app.core=> (def random-graph (cli/make-graph 10 10))
    app.core=> (cli/shortest-path random-graph (first (keys random-graph)) (last (keys random-graph)))
    app.core=> (cli/eccentricity random-graph (first (keys random-graph)))
    app.core=> (cli/radius random-graph)
    app.core=> (cli/diameter random-graph)

#### Running it with lein run from the root folder:

To get the commandline instructions:

    $ lein run  

Example of running the shortest path algorithm on a graph where N=100 and S=400 with start-node=30 and end-node=80:

    $ lein run --print-graph --shortest-path 100 400 30 80 

Example of running eccentricity from node 10 on a graph where N=20 and S=20 not printing out the graph

    $ lein run --eccentricity 20 20 10 

## Tests

Suits of unit tests have been added to this project.
To run the tests:

    $ lein test
