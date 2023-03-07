import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    ArrayList<Node> nodes;

    public Board() {
        this.nodes = new ArrayList<>();
    }


    public Board(ArrayList<Node> nodes) {
        this.nodes = new ArrayList<>();
        Node temp;
        for (Node n : nodes) {
            temp = new Node(n.getID());
            temp.setC(n.getC());
            this.nodes.add(temp);
        }
    }
    public static boolean boardEqualCom(Board b1, Board b2){
        for (int i = 0; i < b1.nodes.size(); i++) {
            if (b1.nodes.get(i).getC().isComputer || b2.nodes.get(i).getC().isComputer){
                if (b1.nodes.get(i).getC() != b2.nodes.get(i).getC()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean equal(Board b1, Board b2) {
        for (int i = 0; i < b1.nodes.size(); i++) {
            if (b1.nodes.get(i).getC() != b2.nodes.get(i).getC()) {
                return false;
            }
        }
        return true;
    }

        public void addNode (Node node){
            boolean flag = false;
            for (Node n : nodes) {
                if (Arrays.equals(node.getID(), n.getID())) {
                    flag = true;
                }
            }
            if (!flag) {
                this.nodes.add(node);
            }
        }

        public Board swap (Node node1, Node node2){
            Board board = new Board(this.nodes);
            Color tempC = node1.getC();
            Color tempC2 = node2.getC();
            for (Node node : board.nodes) {
                if (Arrays.equals(node.getID(), node1.getID())) {
                    node.setC(tempC2);
                } else if (Arrays.equals(node.getID(), node2.getID())) {
                    node.setC(tempC);
                }
            }
            return board;
        }
    }
