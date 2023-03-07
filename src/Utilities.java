import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Utilities {

    public class State {
        Board board;
        int heuristic;
        int alpha;
        int beta;


        State(Board board, int heuristic, int alpha, int beta) {
            this.board = board;
            this.heuristic = heuristic;
            this.alpha = alpha;
            this.beta = beta;
        }

        State(Board board, int heuristic) {
            this.board = board;
            this.heuristic = heuristic;
        }

    }

    public Board initializeBoard() {
        Board board = new Board();
        for (int i = 1; i < 14; i++) {
            for (int j = 1; j < i + 1; j++) {
                Node n = new Node(new int[]{i, 5 + i - j, 14 - j});
                board.addNode(n);
            }
        }
        for (int i = 17; i >= 5; i--) {
            for (int j = 1; j < 19 - i; j++) {
                Node n = new Node(new int[]{i, 14 - j, 23 - j - i});
                board.addNode(n);
            }
        }

        for (Node n : board.nodes) {
            n.setAdjacent(board);
            n.setColor();
        }
        return board;
    }


    boolean isTerminal(Board board) {
        return isTerminalPlayer(board) || isTerminalComputer(board);
    }

    boolean isTerminalPlayer(Board board) {
        for (Node node : board.nodes) {
            if (node.getC().isPlayer) {
                int i = switch (node.getC()) {
                    case RED -> 0;
                    case YELLOW -> 2;
                    case ORANGE -> 1;
                    default -> -1;
                };
                if (i != -1 && node.getID()[i] <= 13) {
                    return false;
                }
            }
        }
        return true;
    }

    boolean isTerminalComputer(Board board) {
        for (Node n : board.nodes) {
            if (n.getC().isComputer) {
                int i = switch (n.getC()) {
                    case GREEN -> 0;
                    case PURPLE -> 2;
                    case BLUE -> 1;
                    default -> -1;
                };
                if (i != -1 && n.getID()[i] >= 5) {
                    return false;
                }
            }
        }
        return true;
    }

    int input;

    public void startGame() {
        Board board = initializeBoard();
        GUI.initialize(board);
        input = GUI.inputInit();
        if (input == -1) {
            return;
        }
        while (!isTerminal(board)) {
            GUI.changeTitle(0);
            board = humanTurn(board);
            GUI.showBoard(board);
            GUI.setCurrBoard(board);

            if (!isTerminal(board)) {
                GUI.changeTitle(1);
                board = computerTurn(board, false, input);
                GUI.showBoard(board);
                GUI.setCurrBoard(board);
            } else break;
        }
        if (isTerminalPlayer(board)) {
            GUI.endOption(0);
        } else if (isTerminalComputer(board)) {
            GUI.endOption(1);
        }
        // end game
    }

    public Board humanTurn(Board board) {
        boolean valid = false;
        Board playedBoard = null;
        while (!valid) {
            playedBoard = GUI.getUserBoard();
            valid = isValid(board, playedBoard);
            if (!valid) {
                GUI.inValid();
            }
        }
        return playedBoard;
    }


    public Board computerTurn(Board board, boolean isPlayer, int input) {
        //return minimax(board,1,isPlayer);
        return alphaBeta(board, input, isPlayer);
    }

    public Board alphaBeta(Board board, int depth, boolean isPlayer) {
        State start = new State(board, getHeuristic(board), Integer.MIN_VALUE, Integer.MAX_VALUE);
        State nextMove = alphaBeta(start, depth, isPlayer);
        return nextMove.board;
    }


    public State alphaBeta(State state, int depth, boolean isPlayer) {
        if (state.alpha > state.beta) {
            return state;
        }
        State s = null;
        ArrayList<State> newStates = new ArrayList<>();
        if (depth != 0) {
            ArrayList<State> Children = getAllChildren(state.board, isPlayer); //..
            for (State child : Children) {
                child.alpha = state.alpha;
                child.beta = state.beta;
                State newState = alphaBeta(child, depth - 1, !isPlayer);
                newStates.add(newState);
                state.alpha = newState.alpha;
                state.beta = newState.beta;
            }
        }

        if (isPlayer && newStates.size() > 0 && depth == 1) {
            int oldBeta = state.beta;
            s = newStates.get(0);
            state.beta = Integer.min(s.heuristic, oldBeta);
        } else if (!isPlayer && newStates.size() > 0 && depth == 1) {
            int oldAlpha = state.alpha;
            s = newStates.get(0);
            state.alpha = Integer.max(s.heuristic, oldAlpha);
        }
        else if (isPlayer && newStates.size() > 0 && depth > 1) {
            int oldBeta = state.beta;
            s = newStates.get(0);
            state.beta = Integer.min(s.beta,Integer.min(oldBeta,s.alpha));
        } else if (!isPlayer && newStates.size() > 0 && depth > 1) {
            int oldAlpha = state.alpha;
            s = newStates.get(0);
            state.alpha = Integer.max(s.alpha,Integer.max(oldAlpha,s.beta));
        }

        if (s!= null){
            state.heuristic = s.heuristic;
        }
        if (depth == input){
            state = newStates.get(0);
        }
        return state;
    }
    /*public Board minimax(Board board, int depth, boolean isPlayer) {
        State start = new State(board, getHeuristic(board));
        State nextMove = minimax(start, depth, isPlayer);
        return nextMove.board;
    }

    public Board minimax(Board board, int depth) {
        State start = new State(board, getHeuristic(board));
        State nextMove = minimax(start, depth, false);
        return nextMove.board;
    }

    public State minimax(State state, int depth, boolean isPlayer) {
        State bestState = state;
        ArrayList<State> Children = getAllChildren(state.board, isPlayer); //..
        ArrayList<State> newStates = new ArrayList<>();
        if (depth != 0) {
            for (State child : Children) {
                newStates.add(minimax(child, depth - 1, !isPlayer));
            }
        }

        if (isPlayer && newStates.size() > 0) {
            bestState = getMin(newStates);
        } else if (!isPlayer && newStates.size() > 0) {
            bestState = getMax(newStates);
        }
        return bestState;
    }*/


    ArrayList<State> getAllChildren(Board board, boolean isPlayer) {
        ArrayList<Board> boards = move(board, isPlayer);
        ArrayList<State> states = new ArrayList<>();
        for (Board b : boards) {
            states.add(new State(b, getHeuristic(b)));
        }
        Collections.sort(states, new MinNodeComparator());
        if (!isPlayer)
            Collections.reverse(states);
        return states;
    }

    public class MinNodeComparator implements Comparator<State> {
        @Override
        public int compare(State o1, State o2) {
            if (o1.heuristic > o2.heuristic)
                return 1;
            else if (o1.heuristic < o2.heuristic)
                return -1;
            return 0;
        }
    }

    public Node getNodeById(Board board, int id1, int id2, int id3) {
        //Node checkedNode;
        for (Node n : board.nodes) {
            if (n.getID()[0] == id1 && n.getID()[1] == id2 && n.getID()[2] == id3) {
                //checkedNode = n;
                return n;
            }
        }
        return null;
    }

    public boolean isValid(Board b1, Board b2) { // before move
        ArrayList<Board> chiledren = move(b1, true);
        for (Board child : chiledren) {
            if (Board.equal(b2, child))
                return true;
        }
        return false;
    }


    public ArrayList<Board> move(Board board, boolean isPlayer) {
        ArrayList<Board> boards = new ArrayList<>();
        for (Node node : board.nodes) {
            if (isPlayer) {
                if (node.getC().isPlayer) {   // Make Sure
                    ArrayList<Board> move1 = move1(board, node);
                    boards.addAll(move1);
                    ArrayList<Board> move2 = move2(board, node);
                    boards.addAll(move2);
                }
            } else {
                if (node.getC().isComputer) {   // Make Sure
                    ArrayList<Board> move1 = move1(board, node);
                    boards.addAll(move1);
                    ArrayList<Board> move2 = move2(board, node);
                    boards.addAll(move2);
                }
            }
        }
        return boards;
    }

    public ArrayList<Board> move1(Board board, Node node) {
        node.setAdjacent(board);
        ArrayList<Node> adjacents = node.getAdjacent();
        ArrayList<Board> result = new ArrayList<>();
        for (Node adjacent : adjacents) {
            if (adjacent.getC() == Color.EMPTY && checkPos(adjacent.getID(), node.getC())) {
                result.add(board.swap(node, adjacent));
            }
        }
        return result;
    }


    public ArrayList<Board> move2(Board board, Node node) {
        ArrayList<Node> visited = new ArrayList<>();
        visited.add(node);
        return move2(board, node, visited);
    }

    public ArrayList<Board> move2(Board board, Node node, ArrayList<Node> visited) {
        ArrayList<Board> result = new ArrayList<>();
        Node[] adj = new Node[6];
        Node[] jumpPos = new Node[6];
        Board temp;
        adj[0] = getNodeById(board, node.getID()[0], node.getID()[1] - 1, node.getID()[2] - 1);
        jumpPos[0] = getNodeById(board, node.getID()[0], node.getID()[1] - 2, node.getID()[2] - 2);

        adj[1] = getNodeById(board, node.getID()[0], node.getID()[1] + 1, node.getID()[2] + 1);
        jumpPos[1] = getNodeById(board, node.getID()[0], node.getID()[1] + 2, node.getID()[2] + 2);

        adj[2] = getNodeById(board, node.getID()[0] + 1, node.getID()[1], node.getID()[2] - 1);
        jumpPos[2] = getNodeById(board, node.getID()[0] + 2, node.getID()[1], node.getID()[2] - 2);

        adj[3] = getNodeById(board, node.getID()[0] + 1, node.getID()[1] + 1, node.getID()[2]);
        jumpPos[3] = getNodeById(board, node.getID()[0] + 2, node.getID()[1] + 2, node.getID()[2]);

        adj[4] = getNodeById(board, node.getID()[0] - 1, node.getID()[1] - 1, node.getID()[2]);
        jumpPos[4] = getNodeById(board, node.getID()[0] - 2, node.getID()[1] - 2, node.getID()[2]);

        adj[5] = getNodeById(board, node.getID()[0] - 1, node.getID()[1], node.getID()[2] + 1);
        jumpPos[5] = getNodeById(board, node.getID()[0] - 2, node.getID()[1], node.getID()[2] + 2);

        for (int i = 0; i < 6; i++) {
            if (adj[i] != null && jumpPos[i] != null && jumpPos[i].getC() == Color.EMPTY && adj[i].getC() != Color.EMPTY && !Node.equal(jumpPos[i], visited)) {
                temp = board.swap(node, jumpPos[i]);
                ArrayList<Node> newVisited = new ArrayList<>(visited);
                newVisited.add(jumpPos[i]);
                if (checkPos(jumpPos[i].getID(), node.getC())) {
                    result.add(temp);
                }
                result.addAll(move2(temp, getNodeById(temp, jumpPos[i].getID()[0], jumpPos[i].getID()[1], jumpPos[i].getID()[2]), newVisited));
            }
        }
        return result;
    }

    public boolean checkPos(int[] id, Color c) {
        if (c == Color.BLUE || c == Color.ORANGE) {
            if (id[0] < 5 || id[2] < 5 || id[0] > 13 || id[2] > 13) {
                return false;
            }
        } else if (c == Color.GREEN || c == Color.RED) {
            if (id[1] < 5 || id[2] < 5 || id[1] > 13 || id[2] > 13) {
                return false;
            }
        } else if (c == Color.PURPLE || c == Color.YELLOW) {
            if (id[0] < 5 || id[1] < 5 || id[0] > 13 || id[1] > 13) {
                return false;
            }
        }
        return true;
    }

    public int getHeuristic(Board board) {
        int heuristic = 0;
        for (Node node : board.nodes) {
            if ((node.getC() != Color.EMPTY)) {
                heuristic += getHeuristic(node);
            }
        }
        return heuristic;
    }

    public int getHeuristic(Node node) {
        int h = 0;

        if (node.getC() == Color.GREEN) {
            h = 17 - node.getID()[0];
            if (h < 2) h *= -5;
            if (h >= 13) h *= 5;
        } else if (node.getC() == Color.BLUE) {
            h = 17 - node.getID()[1];
            if (h < 2) h *= -5;
            if (h >= 13) h *= 5;
        } else if (node.getC() == Color.PURPLE) {
            h = 17 - node.getID()[2];
            if (h < 2) h *= -5;
            if (h >= 13) h *= 5;
        } else if (node.getC() == Color.RED) {
            h = 1 - node.getID()[0];
            if (h > -2) h *= -5;
            if (h <= -13) h *= 5;
        } else if (node.getC() == Color.ORANGE) {
            h = 1 - node.getID()[1];
            if (h > -2) h *= -5;
            if (h <= -13) h *= 5;
        } else if (node.getC() == Color.YELLOW) {
            h = 1 - node.getID()[2];
            if (h > -2) h *= -5;
            if (h <= -13) h *= 5;
        }
        return h;
    }
}
