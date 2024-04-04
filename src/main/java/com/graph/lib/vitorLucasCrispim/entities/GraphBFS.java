package com.graph.lib.vitorLucasCrispim.entities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class GraphBFS {
    private Map<Integer, List<Integer>> adj;
    private HashMap<Integer, Integer> pai;
    private Map<Integer, Integer> nivel;

    public GraphBFS() {
        adj = new HashMap<>();
        pai = new HashMap<>();
        nivel = new HashMap<>();
    }

    public void addEdge(int v, int w) {
        adj.computeIfAbsent(v, k -> new LinkedList<>()).add(w);
        adj.computeIfAbsent(w, k -> new LinkedList<>());

        nivel.putIfAbsent(v, 0);
        nivel.putIfAbsent(w, 0);
    }

    public void BFSWriter(int s, BufferedWriter escritor) throws IOException {
        if (!adj.containsKey(s)) return;

        Map<Integer, Boolean> visited = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.put(s, true);
        queue.add(s);
        nivel.put(s, 0);

        while (!queue.isEmpty()) {
            int n = queue.poll();
            escritor.write("Vertice: " + n + ", Nivel: " + nivel.get(n) + ", Parente: " + pai.getOrDefault(n, null) + "\n");

            for (int a : adj.get(n)) {
                if (!visited.getOrDefault(a, false)) {
                    visited.put(a, true);
                    queue.add(a);
                    pai.put(a, n);
                    nivel.put(a, nivel.get(n) + 1);
                }
            }
        }
        escritor.flush();
    }
}
