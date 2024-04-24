import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Node {
    private double clockTime;
    private final int id;
    private final Random random;

    public Node(int id, double initialTime) {
        this.id = id;
        this.clockTime = initialTime;
        this.random = new Random();
    }

    public double getCurrentTime() {
        return clockTime;
    }

    public void adjustTime(double adjustment) {
        clockTime += adjustment;
    }

    public void drift() {
        clockTime += random.nextGaussian() * 0.1;
    }

    public int getId() {
        return id;
    }
}

class MasterNode extends Node {
    private final List<Node> nodes;

    public MasterNode(int id, double initialTime) {
        super(id, initialTime);
        nodes = new ArrayList<>();
    }

    public void addNode(Node node) {
        nodes.add(node);
    }

    public void synchronizeClocks() {
        List<Double> times = new ArrayList<>();
        times.add(getCurrentTime());
        for (Node node : nodes) {
            times.add(node.getCurrentTime());
        }

        double averageTime = times.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);

        for (Node node : nodes) {
            double adjustment = averageTime - node.getCurrentTime();
            node.adjustTime(adjustment);
        }

        adjustTime(averageTime - getCurrentTime());
    }
}

public class Main {
    public static void main(String[] args) {
        MasterNode masterNode = new MasterNode(1, 0.0);

        Node node2 = new Node(2, 1.5);
        Node node3 = new Node(3, -1.0);
        Node node4 = new Node(4, 2.0);

        masterNode.addNode(node2);
        masterNode.addNode(node3);
        masterNode.addNode(node4);

        for (int i = 0; i < 5; i++) {
            System.out.println("Clock times at iteration " + (i + 1));
            System.out.println("Master Node Time: " + masterNode.getCurrentTime());
            System.out.println("Node 2 Time: " + node2.getCurrentTime());
            System.out.println("Node 3 Time: " + node3.getCurrentTime());
            System.out.println("Node 4 Time: " + node4.getCurrentTime());

            masterNode.drift();
            node2.drift();
            node3.drift();
            node4.drift();

            masterNode.synchronizeClocks();

            System.out.println("Adjusted times:");
            System.out.println("Master Node Time: " + masterNode.getCurrentTime());
            System.out.println("Node 2 Time: " + node2.getCurrentTime());
            System.out.println("Node 3 Time: " + node3.getCurrentTime());
            System.out.println("Node 4 Time: " + node4.getCurrentTime());
            System.out.println("-----");
        }
    }
}
