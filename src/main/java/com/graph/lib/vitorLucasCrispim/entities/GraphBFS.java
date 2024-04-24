package com.graph.lib.vitorLucasCrispim.entities;

import java.io.BufferedWriter;
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
        adj.computeIfAbsent(w, k -> new LinkedList<>()).add(v);

        nivel.putIfAbsent(v, 0);
        nivel.putIfAbsent(w, 0);
    }

    public  void BFSWriter(int s, BufferedWriter escritor)  {
        try{
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
            escritor.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public int calcularDiametro() {
        int diametro = 0;

        // Iterar sobre todos os vértices para encontrar a distância máxima usando BFS
        for (int v : adj.keySet()) {
            int maxDistancia = calcularDistanciaMaxima(v);
            if (maxDistancia > diametro) {
                diametro = maxDistancia;
            }
        }

        return diametro;
    }

    private int calcularDistanciaMaxima(int start) {
        // Mapa para armazenar as distâncias de BFS
        Map<Integer, Integer> distancia = new HashMap<>();
        Queue<Integer> fila = new LinkedList<>();

        fila.add(start);
        distancia.put(start, 0);

        while (!fila.isEmpty()) {
            int current = fila.poll();

            // Para cada vizinho não visitado, adicione-o à fila e atualize sua distância
            for (int vizinho : adj.get(current)) {
                if (!distancia.containsKey(vizinho)) {
                    fila.add(vizinho);
                    distancia.put(vizinho, distancia.get(current) + 1);
                }
            }
        }

        // Encontrar a maior distância no mapa
        int maxDistancia = 0;
        for (int dist : distancia.values()) {
            if (dist > maxDistancia) {
                maxDistancia = dist;
            }
        }

        return maxDistancia;
    }

    public int distanciaEntreVertices(int v1, int v2) {
        if (!adj.containsKey(v1) || !adj.containsKey(v2)) return -1;
        Map<Integer, Integer> distancia = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.add(v1);
        distancia.put(v1, 0);
        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (current == v2) return distancia.get(current);
            for (int neighbor : adj.get(current)) {
                if (!distancia.containsKey(neighbor)) {
                    distancia.put(neighbor, distancia.get(current) + 1);
                    queue.add(neighbor);
                }
            }
        }
        return -1;
    }
}
