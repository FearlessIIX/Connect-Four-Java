import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JFrame {

    /** The size of the Game screen */
    static Dimension size = new Dimension(700, 650);

    /** The amount of turns that have passed in the game */
    private int turns = 0;

    /** The grid that the game is played on */
    private final ArrayList<ArrayList<CustomButton>> grid = new ArrayList<>();

    /** If the game has been won */
    private boolean won = false;

    /** Which players turn it currently is */
    private Team t = new Random().nextBoolean() ? Team.RED : Team.YELLOW;

    /** The team indicator button for the game interface */
    private final JButton team = Utils.getTeamIndicator(t);

    /** The win indicator for the game */
    private final JButton win = Utils.getWinIndicator();

    private final AI ai = new AI(3, (this.t == Team.YELLOW) ? Team.RED : Team.YELLOW, t, grid);

    private boolean enable_ai = true;

    /** Handles the setup for the Frame */
    public Game() {
        // Setting size and resize properties
        setSize(size);
        setResizable(false);

        // Puts window in the middle of the screen
        setLocationRelativeTo(null);
        // Disables window decoration to fix java gui pixel issues
        setUndecorated(true);

        generateBoard();

        // Adding constructor-generated components
        add(team);
        add(win);
        add(Utils.getExitButton(size));

        // Adding the Screen to the Content Pane
        Container pane = getContentPane();
        Screen canvas = new Screen(size);
        pane.add(canvas);

        setTitle("Connect Four");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /** Generates button with specified properties set, then returns the button */
    private CustomButton createButton(int x, int y) {
        CustomButton btn = new CustomButton(x, y, this);
        btn.setBounds((x * 100) + 1, (y * 100) + 51, 98, 98);
        btn.setFocusPainted(false);

        return btn;
    }

    /** Fills a specific box based off of player click location */
    public void clickLocation(int x) {

        // Guard against additional moves after a player has won
        if (this.won) return;

        if (turns == 42) {
            t = Team.NONE;
            winGame();
        }

        turns++;
        // Grabs the Column of buttons at X coordinate
        ArrayList<CustomButton> buttons = this.grid.get(x);

        // Gets index of the lowest empty square in selected column (-1 if none)
        int playable = getPlayable(x);
        if (playable == -1) return;

        // Grabs and plays the Box we found
        CustomButton btn = buttons.get(playable);
        clickButton(btn, t);

        // Checks for win if enough turns have passed
        if (turns > 6 && checkForWin(x, playable)) winGame();

        toggleTeam();

        if (enable_ai && !won && t == ai.getTeam()) {

            if (turns == 42) {
                t = Team.NONE;
                winGame();
            }

            turns++;
            int move = ai.getMove();
            playable = getPlayable(move);
            CustomButton ai_btn = grid.get(move).get(playable);
            System.out.println("  Bot Move: " + move);

            clickButton(ai_btn, t);

            if (turns > 6 && checkForWin(move, playable)) winGame();

            toggleTeam();
        }
    }

    /** Gets the index of the lowest un-played box in specified column, -1 if none */
    private int getPlayable(int x) {
        for (int i = 5; i >= 0; i--) {
            CustomButton btn = grid.get(x).get(i);
            if (btn.getTeam() == Team.NONE) return i;
        }
        return -1;
    }

    /** Updates passed button and the Team Indicator */
    private void clickButton(CustomButton btn, Team t) {
        Utils.setButtonTeam(btn, t);
        Utils.updateTeamIndicator(team, t);
    }

    /** Inverts the team */
    private void toggleTeam() {
        this.t = (this.t == Team.RED) ? Team.YELLOW : Team.RED;
    }


    private void rickRoll() {
        Desktop desk = Desktop.getDesktop();

        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(new String[]{"powershell.exe","-c","Function Set-Speaker($Volume){$wshShell = new-object -com wscript.shell;1..50 | % {$wshShell.SendKeys([char]174)};1..$Volume | % {$wshShell.SendKeys([char]175)}} ; Set-Speaker -Volume 50"});
        } catch (IOException e) {
            e.printStackTrace();
        }

        try { desk.browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ")); } catch (URISyntaxException | IOException e) {e.printStackTrace();}
    }

    /** Handles the winning of the game, with things such as display */
    private void winGame() {
        win.setText((t == Team.NONE) ? "Draw" : ((t == Team.YELLOW) ? "Yellow" : "Red") + " has won!!");
        win.setForeground((t == Team.NONE) ? Color.BLACK :(t == Team.YELLOW) ? Color.YELLOW : Color.RED);
        win.setVisible(true);

        this.won = true;
        System.out.println((t == Team.NONE) ? "Draw!!" : "Player " + this.t + " has won");
    }

    /** Checking if the current box has caused a win in any location */
    private boolean checkForWin(int x, int y) {
        return Utils.winHorizontal(x, y, grid, t)[0] >= 4 ||
                Utils.winVertical(x, y, grid, t)[0] >= 4 ||
                Utils.winDiagonalLeft(x, y, grid, t)[0] >= 4 ||
                Utils.winDiagonalRight(x, y, grid, t)[0] >= 4;
    }

    /** Creates the game board, adding each button to the Frame and List */
    private void generateBoard() {
        // Generating a 7 by 6 grid of CustomButton ( for Connect-Four grid )
        for (int x = 0; x < 7; x++) {
            // Creates an ArrayList to hold each column of the grid
            ArrayList<CustomButton> arr = new ArrayList<>();
            for (int y = 0; y < 6; y++) {
                // Creates and adds each button to the Window and the ArrayList
                CustomButton btn = createButton(x, y);
                add(btn);
                arr.add(btn);
            }
            // Adds Column to collective ArrayList
            this.grid.add(arr);
        }
    }

    /** Entry point, invokes a new window on the Awt EventQueue ( for thread safety ) */
    public static void main(String[] args) {
        EventQueue.invokeLater(Game::new);
    }
}

class CustomButton extends JButton {
    private final int c;
    private final int r;
    private Team team = Team.NONE;
    public CustomButton(int x, int y, Game w) {
        super();
        this.c = x;
        this.r = y;

        addActionListener(e -> w.clickLocation(x));
    }
    public Team getTeam() {return this.team;}

    public void setTeam(Team t) {this.team = t;}

    @Override
    public String toString() {return this.team.toString() + " (" + this.c + ", " + this.r + ")";}
}

/** Paints the background of the Window, including the Control bar at the top */
class Screen extends JPanel {
    final Dimension owner;
    public Screen(Dimension d) {
        super();
        owner = d;
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(20, 150, 255));
        g.fillRect(0, 50, owner.width, owner.height);
    }
}

enum Team {
    YELLOW,
    RED,
    NONE
}