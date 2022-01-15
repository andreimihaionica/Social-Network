package com.example.social_network.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Graph {
    /**
     * Pair of two entities
     *
     * @param <T>
     * @param <V>
     */
    static class Pair<T, V> {
        T first;
        V second;

        /**
         * Constructor with parameters
         *
         * @param first  - entity
         * @param second - entity
         */
        Pair(T first, V second) {
            this.first = first;
            this.second = second;
        }
    }

    int V;
    ArrayList<ArrayList<Integer>> adjListArray;

    /**
     * Constructor with parameters
     *
     * @param V - no. of nodes
     */
    public Graph(int V) {
        this.V = V;
        adjListArray = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adjListArray.add(i, new ArrayList<>());
        }
    }

    /**
     * Add edge in graph
     *
     * @param src  - node1
     * @param dest - node2
     */
    public void addEdge(int src, int dest) {
        adjListArray.get(src).add(dest);
        adjListArray.get(dest).add(src);
    }

    /**
     * Depth First Search
     *
     * @param v       - node
     * @param visited - isVisited
     * @param graph   - subGraph
     */
    void DFSUtil(int v, boolean[] visited, ArrayList<Integer> graph) {
        visited[v] = true;
        graph.add(v);

        for (int x : adjListArray.get(v)) {
            if (!visited[x])
                DFSUtil(x, visited, graph);
        }
    }

    /**
     * Determinate no. of connected components
     *
     * @return no. of connected components
     */
    public int ConnectedComponents() {
        boolean[] visited = new boolean[V];
        int noConnectedComponents = 0;
        ArrayList<Integer> graph;

        for (int v = 0; v < V; ++v) {
            if (!visited[v]) {
                graph = new ArrayList<>();
                DFSUtil(v, visited, graph);
                noConnectedComponents++;
            }
        }

        return noConnectedComponents;
    }

    /**
     * Breadth First Search
     *
     * @param u - start node
     * @return farthest node and its distance from node u
     */
    public Pair<Integer, Integer> bfs(int u) {
        int[] dis = new int[V];
        Arrays.fill(dis, -1);
        Queue<Integer> q = new LinkedList<>();
        q.add(u);

        dis[u] = 0;
        while (!q.isEmpty()) {
            int t = q.poll();

            for (int i = 0; i < adjListArray.get(t).size(); ++i) {
                int v = adjListArray.get(t).get(i);

                if (dis[v] == -1) {
                    q.add(v);
                    dis[v] = dis[t] + 1;
                }
            }
        }

        int maxDis = 0;
        int nodeIdx = 0;

        for (int i = 0; i < V; ++i) {
            if (dis[i] > maxDis) {
                maxDis = dis[i];
                nodeIdx = i;
            }
        }

        return new Pair<>(nodeIdx, maxDis);
    }

    /**
     * Determinate longest path
     *
     * @param u - start node
     * @return - start node and last node
     */
    public Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> longestPathLength(int u) {
        Pair<Integer, Integer> t1, t2;
        t1 = bfs(u);
        t2 = bfs(t1.first);

        return new Pair<>(t1, t2);
    }

    /**
     * Determinate longest path for graph
     *
     * @return start node, last node, length
     */
    public ArrayList<Integer> longestPath() {
        boolean[] visited = new boolean[V];
        ArrayList<Integer> longestPath = new ArrayList<>();
        int maxLength = 0;
        Pair<Integer, Integer> maxPath = new Pair<>(0, 0);
        ArrayList<Integer> graph;

        for (int v = 0; v < V; ++v) {
            if (!visited[v]) {
                graph = new ArrayList<>();
                DFSUtil(v, visited, graph);

                Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> pair = longestPathLength(graph.get(0));
                if (maxLength < pair.second.second) {
                    maxLength = pair.second.second;
                    maxPath = new Pair<>(pair.first.first, pair.second.first);
                }
            }
        }

        longestPath.add(maxPath.first);
        longestPath.add(maxPath.second);
        longestPath.add(maxLength);

        return longestPath;
    }
}
