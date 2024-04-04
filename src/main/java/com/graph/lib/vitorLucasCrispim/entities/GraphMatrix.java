package com.graph.lib.vitorLucasCrispim.entities;

import java.util.ArrayList;
import java.util.List;

public class GraphMatrix {
    private List<List<Integer>> adjList;
    private int numVertices;

    // Initialize the matrix
    public GraphMatrix(int numVertices) {
        this.numVertices = numVertices;
        adjList = new ArrayList<>();
        for (int i = 0; i < numVertices; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public void addEdge(int i, int j) {
        int maxSize = Math.max(i, j) + 1;
        while (adjList.size() < maxSize) {
            adjList.add(new ArrayList<>());
        }
        adjList.get(i).add(j);
        adjList.get(j).add(i);
    }


    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < numVertices; i++) {
            s.append(i + ": ");
            for (int j : adjList.get(i)) {
                s.append(j + " ");
            }
            s.append("\n");
        }
        return s.toString();
    }
}