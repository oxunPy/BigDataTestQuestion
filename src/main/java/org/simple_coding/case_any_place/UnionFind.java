package org.simple_coding.case_any_place;

import java.util.*;

public class UnionFind {
    int[] parents;

    public UnionFind(int n) {
        parents = new int[n];
        for (int i = 0; i < n; i++) {
            parents[i] = i;
        }
    }

    public int find(int a) {
        if (a == parents[a]) {
            return a;
        }

        return find(parents[a]);
    }

    public void union(int a, int b) {
        int pa = find(a);
        int pb = find(b);
        if (pa != pb) {
            parents[pb] = parents[pa];
        }
    }

    public Set<Integer> findOnlyRoots() {
        Set<Integer> roots = new HashSet<>();
        for(int i = 0; i < parents.length; i++) {
            if (i != parents[i]) {
                roots.add(find(i));
            }
        }

        return roots;
    }
}
