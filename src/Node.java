import java.util.ArrayList;

public class Node {
    private int[] ID; // bottom ,bottom right, top right
    private ArrayList<Node> adjacent;
    private Color c;

    public Node(int[] ID) {
        this.adjacent = new ArrayList<>();
        this.ID = ID;
    }

    public static boolean equal(Node n1, ArrayList<Node> visited) {
        for (Node n : visited) {
            if (n1.getID()[0] == n.getID()[0] && n1.getID()[1] == n.getID()[1] && n1.getID()[2] == n.getID()[2])
                return true;
        }
        return false;
    }

    public int[] getID() {
        return ID;
    }

    public void setID(int[] ID) {
        this.ID = ID;
    }

    public void setAdjacent(Board board) {
        this.adjacent.clear();
        for (Node n : board.nodes) {
            //right
            if (n.getID()[0] == this.getID()[0] && n.getID()[1] == this.getID()[1] - 1 && n.getID()[2] == this.getID()[2] - 1)
                adjacent.add(n);
                //left
            else if ((n.getID()[0] == this.getID()[0] && n.getID()[1] == this.getID()[1] + 1 && n.getID()[2] == this.getID()[2] + 1)) {
                adjacent.add(n);
            }
            //top-right
            else if ((n.getID()[0] == this.getID()[0] + 1 && n.getID()[1] == this.getID()[1] && n.getID()[2] == this.getID()[2] - 1)) {
                adjacent.add(n);
            }

            //top-left
            else if ((n.getID()[0] == this.getID()[0] + 1 && n.getID()[1] == this.getID()[1] + 1 && n.getID()[2] == this.getID()[2])) {
                adjacent.add(n);
            }
            //button-right
            else if ((n.getID()[0] == this.getID()[0] - 1 && n.getID()[1] == this.getID()[1] - 1 && n.getID()[2] == this.getID()[2])) {
                this.adjacent.add(n);
            }
            //button-left
            else if ((n.getID()[0] == this.getID()[0] - 1 && n.getID()[1] == this.getID()[1] && n.getID()[2] == this.getID()[2] + 1)) {
                this.adjacent.add(n);
            }
        }

    }

    public void setColor() {
        if (this.getID()[0] < 5) {
            setC(Color.RED);
        } else if (this.getID()[0] > 13) {
            setC(Color.GREEN);
        } else if (this.getID()[1] < 5) {
            setC(Color.ORANGE);
        } else if (this.getID()[1] > 13) {
            setC(Color.BLUE);
        } else if (this.getID()[2] < 5) {
            setC(Color.YELLOW);
        } else if (this.getID()[2] > 13) {
            setC(Color.PURPLE);
        } else {
            setC(Color.EMPTY);
        }

    }

    public ArrayList<Node> getAdjacent() {
        return adjacent;
    }

    public void setAdjacent(ArrayList<Node> adjacent) {
        this.adjacent = adjacent;
    }

    public Color getC() {
        return c;
    }

    public void setC(Color c) {
        this.c = c;
    }
}
