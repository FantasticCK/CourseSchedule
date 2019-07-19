package com.CK;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        int[] a = new int[]{1, 0};
        int[] b = new int[]{2, 0};
        int[] c = new int[]{3, 1};
        int[] d = new int[]{3, 2};
        int[] e = new int[]{1, 3};
        int[][] prerequisites = new int[5][2];
        prerequisites[0] = a;
        prerequisites[1] = b;
        prerequisites[2] = c;
        prerequisites[3] = d;
        prerequisites[4] = e;
        Solution2 solution = new Solution2();
        System.out.println(solution.canFinish(4, prerequisites));
    }
}

class Solution {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        if (prerequisites.length == 0) return true;
        Graph graph = new Graph(numCourses);
        for (int i = 0; i < prerequisites.length; i++) {
            graph.addEdge(prerequisites[i]);
        }
        boolean res = true;
        for (int m = 0; m < graph.vertexMap.size(); m++) {
            Iterator itr = graph.vertexMap.keySet().iterator();
            while (itr.hasNext()) {
                int key = (int) itr.next();
                HashSet<Integer> visited = new HashSet<>();
                HashSet<Integer> recStack = new HashSet<>();
                recStack.add(key);
                if (!dfs(key, visited, recStack, graph, res)) return false;
            }
        }
        return res;
    }

    private boolean dfs(int vertex, HashSet<Integer> visited, HashSet recStack, Graph graph, boolean res) {
        while (!graph.vertexMap.get(vertex).isEmpty()) {
            int next = graph.vertexMap.get(vertex).poll();
            if (recStack.contains(next))
                return false;
            else if (!visited.contains(next)) {
                recStack.add(next);
            }
            res = dfs(next, visited, recStack, graph, res);
        }
        if (recStack.contains(vertex)) {
            recStack.remove(vertex);
            visited.add(vertex);
        }
        return res;
    }

    class Graph {
        int capacity;
        LinkedHashMap<Integer, LinkedList<Integer>> vertexMap;

        Graph(int capacity) {
            this.capacity = capacity;
            this.vertexMap = new LinkedHashMap<>();
        }

        private void addVertex(int vertex) {
            vertexMap.put(vertex, new LinkedList<>());
        }

        private void addEdge(int[] edge) {
            int des = edge[0], src = edge[1];
            if (!vertexMap.containsKey(src)) this.addVertex(src);
            if (!vertexMap.containsKey(des)) this.addVertex(des);
            vertexMap.get(src).add(des);
        }
    }

}

//DFS
class Solution2 {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        ArrayList[] graph = new ArrayList[numCourses];
        for (int i = 0; i < numCourses; i++)
            graph[i] = new ArrayList();

        boolean[] visited = new boolean[numCourses];
        for (int i = 0; i < prerequisites.length; i++) {
            graph[prerequisites[i][1]].add(prerequisites[i][0]);
        }

        for (int i = 0; i < numCourses; i++) {
            if (!dfs(graph, visited, i))
                return false;
        }
        return true;
    }

    private boolean dfs(ArrayList[] graph, boolean[] visited, int course) {
        if (visited[course])
            return false;
        else
            visited[course] = true;
        ;

        for (int i = 0; i < graph[course].size(); i++) {
            if (!dfs(graph, visited, (int) graph[course].get(i)))
                return false;
        }
        visited[course] = false;
        return true;
    }
}

//BFS Topological sort
class Solution3 {
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        int[][] matrix = new int[numCourses][numCourses]; // i -> j
        int[] indegree = new int[numCourses];

        for (int i = 0; i < prerequisites.length; i++) {
            int ready = prerequisites[i][0];
            int pre = prerequisites[i][1];
            if (matrix[pre][ready] == 0)
                indegree[ready]++; //duplicate case
            matrix[pre][ready] = 1;
        }

        int count = 0;
        Queue<Integer> queue = new LinkedList();
        for (int i = 0; i < indegree.length; i++) {
            if (indegree[i] == 0) queue.offer(i);
        }
        while (!queue.isEmpty()) {
            int course = queue.poll();
            count++;
            for (int i = 0; i < numCourses; i++) {
                if (matrix[course][i] != 0) {
                    if (--indegree[i] == 0)
                        queue.offer(i);
                }
            }
        }
        return count == numCourses;
    }
}