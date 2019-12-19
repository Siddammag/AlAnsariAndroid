package app.alansari.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;

//Qualys Problem
public class OptimumPath {

    public static void main22(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        String[] output = null;
        int ip1 = Integer.parseInt(in.nextLine().trim());
        int ip2_size = 0;
        ip2_size = Integer.parseInt(in.nextLine().trim());
        String[] ip2 = new String[ip2_size];
        String ip2_item;
        for (int ip2_i = 0; ip2_i < ip2_size; ip2_i++) {
            try {
                ip2_item = in.nextLine();
            } catch (Exception e) {
                ip2_item = null;
            }
            ip2[ip2_i] = ip2_item;
        }
        //output = getTolls(ip1, ip2);
        for (int output_i = 0; output_i < output.length; output_i++) {
            System.out.println(String.valueOf(output[output_i]));
        }
    }

    public static void main(String[] args) {
        String output[] = getTolls(6,
                new String[]{"1#2#8", "1#4#7", "1#5#12", "2#3#4", "2#4#2", "3#6#6", "4#6#8", "5#6#10"});
        if (output != null)
            System.out.println(Arrays.asList(output));
        output = getTolls(4, new String[]{"1#2#7", "1#2#8", "1#3#10", "2#4#4", "2#4#3", "3#4#15"});
        if (output != null)
            System.out.println(Arrays.asList(output));

        output = getTolls(4, new String[]{"1#2#7", "1#3#10", "2#4#4", "3#4#15"});
        if (output != null)
            System.out.println(Arrays.asList(output));
    }

    static class Edge {
        private int startIndex;
        private int endIndex;
        private int weight;
        private int used = 0;
        private int tollWeight = 0;
        private int roadCount;

        public int getTollWeight() {
            return tollWeight;
        }

        public int getRoadCount() {
            return roadCount;
        }

        public void setTollWeight(int tollWeight) {
            this.tollWeight = tollWeight;
        }

        public int getStartIndex() {
            return startIndex;
        }

        public int getEndIndex() {
            return endIndex;
        }

        public int getWeight() {
            return weight;
        }

        public int getUsed() {
            return used;
        }

        public Edge(int startIndex, int endIndex, int weight, int used, int roadCount) {
            super();
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.weight = weight;
            this.used = used;
            this.roadCount = roadCount;
        }

        @Override
        public String toString() {
            return "Edge [startIndex=" + startIndex + ", endIndex=" + endIndex + ", weight=" + weight + ", used=" + used
                    + ", tollWeight=" + tollWeight + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + endIndex;
            result = prime * result + startIndex;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Edge other = (Edge) obj;
            if (endIndex != other.endIndex)
                return false;
            if (startIndex != other.startIndex)
                return false;
            return true;
        }

    }

    public static String[] getTolls(int input1, String[] input2) {
        try {
            String splits[];

            int numberOfJunctions = input1;
            int numberOfRoads = input2.length;

            Map<Integer, List<Edge>> hashMap = new HashMap<Integer, List<Edge>>();

            int roadCount = 1;
            for (int i = 0; i < numberOfRoads; i++) {
                splits = input2[i].split("#");
                int start = Integer.parseInt(splits[0]);
                int end = Integer.parseInt(splits[1]);
                if (hashMap.get(start) == null) {
                    List<Edge> edges = new ArrayList<Edge>();
                    edges.add(new Edge(start, end, Integer.parseInt(splits[2]), 0, roadCount++));
                    hashMap.put(start, edges);
                } else {
                    hashMap.get(start).add(new Edge(start, end, Integer.parseInt(splits[2]), 0, roadCount++));
                }
            }

            List<Point> tollToBeAdded = new ArrayList<Point>();
            Queue<Point> queue = new LinkedList<Point>();
            Point point = new Point(1);
            queue.add(point);
            while (!queue.isEmpty()) {
                Point popped = queue.remove();
                if (popped.getX() == numberOfJunctions) {
                    tollToBeAdded.add(popped);
                }

                if (hashMap.get(popped.getX()) != null) {
                    for (Edge edge : hashMap.get(popped.getX())) {
                        edge.used++;
                        Point newP = new Point(edge.endIndex);
                        newP.edge.addAll(popped.edge);
                        newP.edge.add(edge);
                        queue.add(newP);
                    }
                }
            }
            Collections.sort(tollToBeAdded);
            List<Point> maxWeightedPath = new ArrayList<Point>();
            int maxWeight = Integer.MIN_VALUE;
            for (Point pt : tollToBeAdded) {
                if (maxWeight <= pt.getTotalWeight()) {
                    maxWeight = pt.getTotalWeight();
                    maxWeightedPath.clear();
                    maxWeightedPath.add(pt);
                }
            }
            tollToBeAdded.removeAll(maxWeightedPath);

            Map<Integer, Integer> treeMap = new TreeMap<Integer, Integer>();
            Boolean result = settled(tollToBeAdded, maxWeight, maxWeightedPath, 0, treeMap, hashMap);
            if (result) {
                String output[] = new String[treeMap.size() + 1];
                output[0] = treeMap.size() + "#" + maxWeight;
                int count = 1;
                for (Map.Entry<Integer, Integer> entry : treeMap.entrySet()) {
                    output[count] = entry.getKey() + "#" + entry.getValue();
                    count++;
                }
                return output;
            } else {
                System.out.println("No Solution");
            }
            return null;
        } catch (Exception ex) {
            System.out.println("No Solution");
            return null;
        }
    }

    private static Boolean isBalanced(List<Point> finalPaths, int maxPathSum, Edge edge) {
        for (Point finalPoint : finalPaths) {
            if (finalPoint.edge.contains(edge) && finalPoint.getTotalWeight() != maxPathSum) {
                return false;
            }
        }
        return true;
    }

    private static Boolean settled(List<Point> finalPaths, int maxPathSum, List<Point> finalPath, int index,
                                   Map<Integer, Integer> treeMap, Map<Integer, List<Edge>> hashMap) {
        if (index >= finalPaths.size()) {
            return true;
        }
        Point point = finalPaths.get(index);
        for (int i = point.edge.size() - 1; i >= 0; i--) {
            Edge edge = point.edge.get(i);
            Boolean maxWeightContainsEdge = false;
            for (Point finalPoint : finalPath) {
                if (finalPoint.edge.contains(edge)) {
                    maxWeightContainsEdge = true;
                    break;
                }
            }
            if (!maxWeightContainsEdge && edge.getTollWeight() == 0) {
                edge.setTollWeight(maxPathSum - point.getTotalWeight());
                if (!isBalanced(finalPaths, maxPathSum, edge)) {
                    edge.setTollWeight(0);
                } else {
                    Boolean result = settled(finalPaths, maxPathSum, finalPath, index + 1, treeMap, hashMap);
                    if (!result) {
                        edge.setTollWeight(0);
                    } else {
                        treeMap.put(edge.getRoadCount(), edge.getTollWeight());
                        return result;
                    }
                }
            }
        }
        return false;
    }

    static class Point implements Comparable<Point> {
        private int x;
        private List<Edge> edge = new ArrayList<Edge>();

        public int getTotalWeight() {
            int total = 0;
            for (Edge ed : edge) {
                total += (ed.weight + ed.getTollWeight());
            }
            return total;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public List<Edge> getEdge() {
            return edge;
        }

        public void setEdge(List<Edge> edge) {
            this.edge = edge;
        }

        @Override
        public String toString() {
            return "Point [x=" + x + ", edge=" + edge + " Total= " + getTotalWeight() + "]\n";
        }

        public Point(int x) {
            super();
            this.x = x;
        }

        @Override
        public int compareTo(Point o) {
            Integer firstMin = Integer.MAX_VALUE;
            for (Edge ed : this.edge) {
                if (firstMin > ed.getUsed()) {
                    firstMin = ed.getUsed();
                }
            }
            Integer secondMin = Integer.MAX_VALUE;
            for (Edge ed : o.edge) {
                if (secondMin > ed.getUsed()) {
                    secondMin = ed.getUsed();
                }
            }
            return firstMin.compareTo(secondMin);
        }

    }
}