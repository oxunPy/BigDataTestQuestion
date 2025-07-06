package org.simple_coding.case_any_place;

public class UnionFind {
    int[] parents;

    public UnionFind(int n) {
        parents = new int[n];
        for(int i = 0; i < n; i++) {
            parents[i] = i;
        }
    }

    public int find(int a) {
        if(a == parents[a]) {
            return a;
        }

        return find(parents[a]);
    }

    public void union(int a, int b) {
        int pa = find(a);
        int pb = find(b);
        if(pa != pb) parents[pb] = parents[pa];
    }
}
