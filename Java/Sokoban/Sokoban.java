import ecs100.Trace;
import ecs100.UI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Sokoban {

    private Cell[][] cells;             // the array representing the warehouse
    private int level = 1;              // current level

    private Position workerPos;         // the position of the worker
    private String workerDir = "left";  // the direction the worker is facing

    /**
     * HistoryStack And RedoStack
     * Undo function will pop the action from HistoryStack, undo the action and add the action to RedoStack
     * Redo function will pop the action from RedoStack, redo the action and add the action back to HistoryStack
     */
    Stack<ActionRecord> historyStack = new Stack<ActionRecord>();
    Stack<ActionRecord> redoStack = new Stack<ActionRecord>();

    /**
     *  Constructor: load the 0th level.
     */
    public Sokoban() {
        doLoad();
    }

    /**
     *  Moves the worker in the given direction, if possible.
     *  If there is box in front of the Worker and a space in front of the box,
     *  then push the box.
     *  Otherwise, if the worker can't move, do nothing.
     */
    public void moveOrPush(String direction) {
        workerDir = direction;                       // turn worker to face in this direction

        Position nextP = workerPos.next(direction);  // where the worker would move to
        Position nextNextP = nextP.next(direction);  // where a box would be pushed to

        // is there a box in that direction which can be pushed?
        if ( cells[nextP.row][nextP.col].hasBox() && cells[nextNextP.row][nextNextP.col].isFree() ) {
            push(direction);
            historyStack.push(new ActionRecord("push", direction));

            if (isSolved()) {
                reportWin();
            }
        }
        // is the next cell free for the worker to move into?
        else if ( cells[nextP.row][nextP.col].isFree() ) {
            move(direction);
            historyStack.push(new ActionRecord("move", direction));
        }

        redoStack.clear();

    }

    /**
     * Moves the worker into the new position (guaranteed to be empty)
     * @param direction the direction the worker is heading
     */
    public void move(String direction) {
        workerDir = direction;
        drawCell(workerPos);                   // redisplay cell under worker
        workerPos = workerPos.next(direction);// put worker in new position
        drawWorker();                          // display worker at new position

        Trace.println("Move " + direction);    // for debugging
    }

    /**
     * Push: Moves the Worker, pushing the box one step
     *  @param direction the direction the worker is heading
     */
    public void push(String direction) {
        workerDir = direction;
        Position boxPos = workerPos.next(direction);   // where box is
        Position newBoxPos = boxPos.next(direction);   // where box will go

        cells[boxPos.row][boxPos.col].removeBox();     // remove box from current cell
        cells[newBoxPos.row][newBoxPos.col].addBox();  // place box in its new position

        drawCell(workerPos);                           // redisplay cell under worker
        drawCell(boxPos);                              // redisplay cell without the box
        drawCell(newBoxPos);                           // redisplay cell with the box

        workerPos = boxPos;                            // put worker in new position
        drawWorker();                                  // display worker at new position

        Trace.println("Push " + direction);   // for debugging
    }

    /**
     * Pull: (could be useful for undoing a push)
     *  move the Worker in the direction,
     *  pull the box into the Worker's old position
     */

    public void pull(String direction) {
        workerDir = direction;
        Position boxPos = workerPos.next(direction);   // where box is
        Position newBoxPos = workerPos;                // where box will go

        cells[boxPos.row][boxPos.col].removeBox();     // remove box from current cell
        cells[newBoxPos.row][newBoxPos.col].addBox();  // place box in its new position

        drawCell(workerPos);                           // redisplay cell under worker
        drawCell(boxPos);                              // redisplay cell without the box
        drawCell(newBoxPos);                          // redisplay cell with the box

        workerPos = newBoxPos.previous(direction);   // put worker in new position
        drawWorker();
    }

    /**
     * Moves the worker into the old position
     */
    public void moveBack(String dir){
        workerDir = dir;
        drawCell(workerPos);                   // redisplay cell under worker
        workerPos = workerPos.previous(dir);   // put worker in new position
        drawWorker();
    }

    /**
     * Undo the given action
     */
    public void undo(ActionRecord action){
        if(action.isPush()){
            pull(action.direction());
        }
        else{
            moveBack(action.direction());
        }
        redoStack.push(action);
    }

    /**
     * Redo the given action
     */
    public void redo(ActionRecord action){
        if(action.isPush()){
            push(action.direction());
        }
        else{
            move(action.direction());
        }
        historyStack.push(action);
    }

    /**
     * Will not undo if the historyStack is empty
     */
    private void undoHandler() {
        if(!historyStack.empty()) {
            undo(historyStack.pop());
        }
    }

    /**
     * Will not redo if the redoStack is empty
     */
    private void redoHandler() {
        if(!redoStack.empty()){
            redo(redoStack.pop());
        }
    }

    /**
     * Report a win by flickering the cells with boxes
     */
    public void reportWin(){
        for (int i=0; i<12; i++) {
            for (int row=0; row<cells.length; row++)
                for (int column=0; column<cells[row].length; column++) {
                    Cell cell=cells[row][column];

                    // toggle shelf cells
                    if (cell.hasBox()) {
                        cell.removeBox();
                        drawCell(row, column);
                    }
                    else if (cell.isEmptyShelf()) {
                        cell.addBox();
                        drawCell(row, column);
                    }
                }

            UI.sleep(100);
        }
    }

    /**
     *  Returns true if the warehouse is solved,
     *  i.e., all the shelves have boxes on them
     */
    public boolean isSolved() {
        for (Cell[] cell : cells) {
            for (int col = 0; col < cell.length; col++)
                if (cell[col].isEmptyShelf())
                    return false;
        }

        return true;
    }


    // Drawing the warehouse
    private static final int LEFT_MARGIN = 40;
    private static final int TOP_MARGIN = 40;
    private static final int CELL_SIZE = 25;

    /**
     * Draw the grid of cells on the screen, and the Worker
     */
    public void drawWarehouse() {
        UI.clearGraphics();
        // draw cells
        for(int row = 0; row<cells.length; row++)
            for(int col = 0; col<cells[row].length; col++)
                drawCell(row, col);

        drawWorker();
    }

    /**
     * Draw the cell at a given position
     */
    private void drawCell(Position pos) {
        drawCell(pos.row, pos.col);
    }

    /**
     * Draw the cell at a given row,col
     */
    private void drawCell(int row, int col) {
        double left = LEFT_MARGIN+(CELL_SIZE* col);
        double top = TOP_MARGIN+(CELL_SIZE* row);
        cells[row][col].draw(left, top, CELL_SIZE);
    }

    /**
     * Draw the worker at its current position.
     */
    private void drawWorker() {
        double left = LEFT_MARGIN+(CELL_SIZE* workerPos.col);
        double top = TOP_MARGIN+(CELL_SIZE* workerPos.row);
        UI.drawImage("worker-"+workerDir+".gif",
            left, top, CELL_SIZE,CELL_SIZE);
    }

    /**
     * Load a grid of cells (and Worker position) for the current level from a file
     */
    public void doLoad() {
        historyStack.clear();
        redoStack.clear();
        Path path = Path.of("warehouse" + level + ".txt");

        if (! Files.exists(path)) {
            UI.printMessage("Run out of levels!");
            level--;
        }
        else {
            List<String> lines = new ArrayList<String>();
            try {
                Scanner sc = new Scanner(path);
                while (sc.hasNext()){
                    lines.add(sc.nextLine());
                }
                sc.close();
            } catch(IOException e) {UI.println("File error: " + e);}

            int rows = lines.size();
            cells = new Cell[rows][]; // When creating a cell you need to pass in Str not int so how does this work

            for(int row = 0; row < rows; row++) {
                String line = lines.get(row);
                int cols = line.length();
                cells[row]= new Cell[cols];
                for(int col = 0; col < cols; col++) {
                    char ch = line.charAt(col);
                    if (ch=='w'){
                        cells[row][col] = new Cell("empty");
                        workerPos = new Position(row,col);
                    }
                    else if (ch=='.') cells[row][col] = new Cell("empty");
                    else if (ch=='#') cells[row][col] = new Cell("wall");
                    else if (ch=='s') cells[row][col] = new Cell("shelf");
                    else if (ch=='b') cells[row][col] = new Cell("box");
                    else {
                        throw new RuntimeException("Invalid char at "+row+","+col+"="+ch);
                    }
                }
            }
            drawWarehouse();
            UI.printMessage("Level "+level+": Push the boxes to their target positions. Use buttons or put mouse over warehouse and use keys (arrows, wasd, ijkl, u)");
        }
    }

    /**
     * Add the buttons and set the key listener.
     */
    public void setupGUI(){
        UI.addButton("New Level", () -> {level++; doLoad();});
        UI.addButton("Restart",   this::doLoad);
        UI.addButton("left",      () -> {moveOrPush("left");});
        UI.addButton("up",        () -> {moveOrPush("up");});
        UI.addButton("down",      () -> {moveOrPush("down");});
        UI.addButton("right",     () -> {moveOrPush("right");});
        UI.addButton("Undo",      this::undoHandler);
        UI.addButton("Redo",      this::redoHandler);
        UI.addButton("Quit",      UI::quit);

        UI.setKeyListener(this::doKey);
        UI.setDivider(0.0);
    }

    /**
     * Respond to key actions
     */
    public void doKey(String key) {
        key = key.toLowerCase();
        if (key.equals("i")|| key.equals("w") ||key.equals("up")) {
            moveOrPush("up");
        }
        else if (key.equals("k")|| key.equals("s") ||key.equals("down")) {
            moveOrPush("down");
        }
        else if (key.equals("j")|| key.equals("a") ||key.equals("left")) {
            moveOrPush("left");
        }
        else if (key.equals("l")|| key.equals("d") ||key.equals("right")) {
            moveOrPush("right");
        }
        else if(key.equals("u")){
            undoHandler();
        }
        else if(key.equals("r")){
            redoHandler();
        }
    }

    public static void main(String[] args) {
        Sokoban skb = new Sokoban();
        skb.setupGUI();
    }
}
