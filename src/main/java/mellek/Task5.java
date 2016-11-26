package mellek;

import java.util.*;

/**
 * Created by qqcs on 26/11/16.
 */
public class Task5 {
    public static class Node {
        private final int length;
        private final int number;
        private boolean isActive;
        private final ArrayList<Node> parents = new ArrayList<>();
        private final ArrayList<Node> childs = new ArrayList<>();

        public Node(int number) {
            this(0, number);
        }

        public Node(int length, int number) {
            this.length = length;
            this.number = number;
        }

        public void setToInactive() {
            isActive = false;
            for(Node n : childs) {
                n.setToInactive();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        scanner.nextInt();
        int N = scanner.nextInt();
        ArrayList<Integer> divisors = new ArrayList<>();
        divisors.add(N);

        String[] numbers = scanner.nextLine().split(" ");
        for(String number : numbers) {
            divisors.add(Integer.parseInt(number));
        }

        HashMap<Integer, ArrayList<Integer>> parent = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> child = new HashMap<>();
        for(Integer i : divisors) {
            parent.put(i, new ArrayList<>());
        }

        for(Integer i : divisors) {
            for(Integer div : divisors) {
                if(i > div) {
                    if(i % div == 0) {
                        parent.get(div).add(i);
                        child.get(i).add(div);
                    }
                }
            }
        }

        ArrayList<Node> nodes = new ArrayList<>();
        Map<Integer, Node> constructed = new TreeMap<>();
        Node mainNode = new Node(N);
        nodes.add(mainNode);

        constructed.put(N, mainNode);

        int n = 0;

        while(constructed.size() < divisors.size()) {
            Map<Integer, Node> nowConstruct = new TreeMap<>();

            for(Map.Entry<Integer, ArrayList<Integer>> e : parent.entrySet()) {
                if(constructed.keySet().containsAll(e.getValue())) {
                    Node newNode = new Node(e.getKey(), n);
                    nodes.add(newNode);
                    nowConstruct.put(e.getKey(), newNode);
                    for(Integer keys : e.getValue()) {
                        Node parentNode = constructed.get(keys);
                        parentNode.childs.add(newNode);
                        newNode.parents.add(parentNode);
                    }
                }
            }
            ++n;
            constructed.putAll(nowConstruct);
        }

        for(int i = 1; i < n; ++i) {
            for(Node node : nodes) {
                if(node.length == i) {

                }
            }
        }

    }
}
