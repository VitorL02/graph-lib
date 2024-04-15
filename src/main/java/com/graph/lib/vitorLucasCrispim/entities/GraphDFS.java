package com.graph.lib.vitorLucasCrispim.entities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class GraphDFS {
    private Map<Integer, List<Integer>> adj;
    private HashMap<Integer, Integer> pai;
    private Map<Integer, Integer> nivel;

    public GraphDFS() {
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

    public void DFSWriter(int s, BufferedWriter escritor) {
        try {
            if (!adj.containsKey(s)) return;

            Map<Integer, Boolean> visited = new HashMap<>();
            Stack<Integer> stack = new Stack<>();

            visited.put(s, true);
            stack.push(s);
            nivel.put(s, 0);

            while (!stack.isEmpty()) {
                int n = stack.peek();
                escritor.write("Vertice: " + n + ", Nivel: " + nivel.get(n) + ", Parente: " + pai.getOrDefault(n, null) + "\n");
                stack.pop();
                for (int a : adj.get(n)) {
                    if (!visited.getOrDefault(a, false)) {
                        visited.put(a, true);
                        stack.push(a);
                        pai.put(a, n);
                        nivel.put(a, nivel.get(n) + 1);
                    }
                }
            }
            escritor.flush();
            escritor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}