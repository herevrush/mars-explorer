package com.example.explorer;

import com.example.explorer.graph.QueueNode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main Simulation class to execute.
 */
public class Simulation {

    private static final String PLACE_COMMAND = "PLACE";
    private static final String BLOCK_COMMAND = "BLOCK";
    private static final String REPORT_COMMAND = "REPORT";
    private static final String EXPLORER_COMMAND = "EXPLORE";
    private static final int VISITED = 3;
    private static final int HEIGHT = 5;
    private static final int WIDTH = 5;
    private Pattern commandPattern = Pattern.compile("^([(PLACE)|(BLOCK)|(EXPLORER)|(REPORT)]+)\\s?(\\d?)[,]?(\\d?)$");
    private List<String> commands = new ArrayList<>();
    private List<Position> blockPositions;
    private Position toyPosition;
    private Position endPosition;
    private int rowNum[] = {-1, 0, 0, 1};
    private int colNum[] = {0, -1, 1, 0};

    /**
     * Constructor
     */
    Simulation() {
        this.blockPositions = new ArrayList<>();
        this.toyPosition = new Position(-1, -1);
    }

    /**
     * Place the toy at specified position.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void placeToy(int x, int y) {
        // place the toy on tableTop
        toyPosition = new Position(x, y);
        //remove all blocks
        blockPositions.clear();
    }

    /**
     * Add Block at specified location.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void putBlock(int x, int y) {
        //Add block for x,y
        blockPositions.add(new Position(x, y));
    }

    /**
     * Report the status.
     */
    private void report() {
        StringBuilder reportStr = new StringBuilder();
        reportStr.append("E:(").append(toyPosition.getX()).append(",").append(toyPosition.getY()).append(") ");
        reportStr.append("B: ");
        for (Position pos : blockPositions) {
            reportStr.append("(").append(pos.getX()).append(",").append(pos.getY()).append(") ");
        }
        System.out.print(reportStr.toString());
    }

    /**
     * explore the toy to specified position.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    private void exploreToy(int x, int y) {
        endPosition = new Position(x, y);
        if (this.commands.contains(PLACE_COMMAND)) {
            int tableTop[][] = convertToMatrix();
            QueueNode node = exploreTable(tableTop);
            if(node != null){
                toyPosition.setX(x);
                toyPosition.setY(y);
                StringBuilder pathStr = new StringBuilder("PATH: ");
                for (Position pos : node.getPath()) {
                    pathStr.append("(").append(pos.getX()).append(",").append(pos.getY()).append(") ");
                }
                System.out.print(pathStr.toString());
            }
            else{
                System.out.println("Path is not reachable.");
            }
        }
    }

    /**
     * Explore loop
     *
     * @param tableTop     - adjacency matrix representation
     * @return true Queue node with path details.
     */
    private QueueNode exploreTable(int tableTop[][]) {
        tableTop[toyPosition.getX()][toyPosition.getY()] = VISITED;
        Queue<QueueNode> q = new LinkedList<>();
        QueueNode queueNode = getQueueNode(toyPosition, 0);
        queueNode.addPath(new Position(toyPosition.getX(),toyPosition.getY()));
        ((LinkedList<QueueNode>) q).addFirst(queueNode);
        while (!q.isEmpty()) {
            QueueNode currentNode = q.element();
            Position currPos = currentNode.getPosition();

            // If we have reached the destination cell,
            // we are done
            if (isEnd(currPos)) {
                return currentNode;
            }

            ((LinkedList<QueueNode>) q).removeFirst();
            for (int i = 0; i < 4; i++) {
                int row = currPos.getX() + rowNum[i];
                int col = currPos.getY() + colNum[i];
                // if adjacent cell is valid, has path and
                // not visited yet, enqueue it.
                if (isValid(tableTop, row, col)) {
                    // mark cell as visited and enqueue it
                    tableTop[row][col] = VISITED;
                    Position newPos = new Position(row, col);
                    QueueNode neighbour = getQueueNode(newPos, currentNode.getDistance() + 1);
                    neighbour.getPath().addAll(currentNode.getPath());
                    neighbour.addPath(newPos);
                    ((LinkedList<QueueNode>) q).addLast(neighbour);
                }
            }
        }
        return null;
    }

    private QueueNode getQueueNode(Position pos, int dist) {
        QueueNode queueNode = new QueueNode();
        queueNode.setPosition(pos);
        queueNode.setDistance(dist);
        return queueNode;
    }

    private int[][] convertToMatrix() {
        int tableTop[][] = new int[HEIGHT][WIDTH];
        for (int[] row : tableTop)
            Arrays.fill(row, 1);
        for (Position pos : blockPositions) {
            tableTop[pos.getX()][pos.getY()] = 0;
        }
        return tableTop;
    }

    /**
     * Checks whether the current position is valid to explore.
     *
     * @param tableTop - adjacency matrix representation
     * @param x        x coordinate
     * @param y        y coordinate
     * @return true if position is visited.
     */
    private boolean isValid(int tableTop[][], int x, int y) {
        return isOnTableTop(x, y) && tableTop[x][y] == 1 && tableTop[x][y] != VISITED;
    }


    /**
     * checks whether the specified position is end position.
     * @param position x coordinate and y coordinate
     * @return true if its end position.
     */
    private boolean isEnd(Position position) {
        if (endPosition != null) {
            return position.getY() == endPosition.getY() && position.getX() == endPosition.getX();
        }
        return false;
    }

    /**
     * Verify whether command is valid
     * @param command command string to check.
     * @return true if valid command
     */
    private boolean isValidCommand(String command) {
        if (command.startsWith(PLACE_COMMAND)) {
            return true;
        } else return this.commands.contains(PLACE_COMMAND);
    }

    /**
     * Returns true if current position is valid table top position.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return if toy is on table top and current position is on table top
     */
    private boolean isOnTableTop(int x, int y) {
        return (x < WIDTH && y < HEIGHT && x >= 0 && y >= 0);
    }


    /**
     * Read system input and process commands.
     */
    public void readInputStream() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().replaceAll("\n", "");
            // return pressed
            if (line.length() != 0) {
                try {
                    executeCommand(line);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(" Invalid Command. Please try again.");
                }
            }
        }
    }

    /**
     * Execute the specified command.
     *
     * @param command - command to execute
     */
    private void executeCommand(String command) {
        Matcher matcher = commandPattern.matcher(command);

        if (matcher.matches()) {
            String commandName = matcher.group(1);
            if (isValidCommand(commandName)) {
                this.commands.add(commandName);
                int x = 0;
                int y = 0;
                if (!matcher.group(2).isEmpty()) {
                    x = Integer.parseInt(matcher.group(2));
                }
                if (!matcher.group(3).isEmpty()) {
                    y = Integer.parseInt(matcher.group(3));
                }
                if (x >= WIDTH || y >= HEIGHT) {
                    System.out.println(" Invalid X and Y specified.");
                } else {
                    switch (commandName) {
                        case PLACE_COMMAND: {
                            placeToy(x, y);
                            break;
                        }
                        case BLOCK_COMMAND: {
                            putBlock(x, y);
                            break;
                        }
                        case EXPLORER_COMMAND: {
                            exploreToy(x, y);
                            break;
                        }
                        case REPORT_COMMAND: {
                            report();
                        }
                    }
                }

            } else {
                //First command should be PLACE
                System.out.println(" First command should be PLACE. Please try again.");
            }
        }
    }

    /**
     * Entry point to the application.
     * @param args - args
     */
    public static void main(String[] args) {
        Simulation simulation = new Simulation();
        simulation.readInputStream();
    }
}
