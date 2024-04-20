package com.graph.lib.vitorLucasCrispim.entities;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class GraphDFS {
    private Map<Integer, List<Integer>> adj;
    private HashMap<Integer, Integer> pai;
    private Set<Integer> visitados;

    public GraphDFS() {
        adj = new HashMap<>();
        pai = new HashMap<>();
    }

    public void addEdge(int v, int w) {
        adj.computeIfAbsent(v, k -> new LinkedList<>()).add(w);
        adj.computeIfAbsent(w, k -> new LinkedList<>());
    }

    public void DFSWriter(int s, BufferedWriter escritor) {
        try {
            if (!adj.containsKey(s)) return;

            visitados = new HashSet<>();
            Map<Integer, Integer> nivel = new HashMap<>();
            Stack<Integer> stack = new Stack<>();

            visitados.add(s);
            stack.push(s);
            nivel.put(s, 0);

            while (!stack.isEmpty()) {
                int n = stack.peek();
                escritor.write("Vertice: " + n + ", Nivel: " + nivel.get(n) + ", Parente: " + pai.getOrDefault(n, null) + "\n");
                stack.pop();
                for (int a : adj.get(n)) {
                    if (!visitados.contains(a)) {
                        visitados.add(a);
                        stack.push(a);
                        pai.put(a, n);
                        nivel.put(a, nivel.get(n) + 1);
                    }
                }
            }
            escritor.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<List<Integer>> encontrarComponentesConexas() {
        List<List<Integer>> componentes = new ArrayList<>();
        visitados = new HashSet<>();
        for (int v : adj.keySet()) {
            if (!visitados.contains(v)) {
                List<Integer> componente = new ArrayList<>();
                DFSComponente(v, componente);
                if (!componente.isEmpty()) {
                    componentes.add(componente);
                }
            }
        }
        return componentes;
    }

    private void DFSComponente(int s, List<Integer> componente) {
        Stack<Integer> stack = new Stack<>();
        stack.push(s);
        visitados.add(s);

        while (!stack.isEmpty()) {
            int n = stack.pop();
            componente.add(n);
            for (int a : adj.get(n)) {
                if (!visitados.contains(a)) {
                    stack.push(a);
                    visitados.add(a);
                }
            }
        }
    }

    public void escreverComponentes(BufferedWriter escritor) {
        try {
            List<List<Integer>> componentes = encontrarComponentesConexas();
            escritor.newLine();
            escritor.write("Quantidade de componentes conexos: " + componentes.size() + "\n");
            for (List<Integer> componente : componentes) {
                escritor.write("Componente Conexo: " + componente.size() + " vértices - " + componente + "\n");
            }
            escritor.newLine();
            escritor.flush();
            escritor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
