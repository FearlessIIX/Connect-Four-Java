import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Utils {

    /** Checks if there is a win in Horizontal direction '--' dir: > */
    public static int[] winHorizontal(int x, int y, ArrayList<ArrayList<CustomButton>> grid, Team t) {

        int[] ret_info = new int[5];

        // Finds the last box connected to (x, y) that matches its team
        int i = x;
        for (; i > 0; i--) {
            if (grid.get(i - 1).get(y).getTeam() != t) break;
        }

        // Left-most x y
        ret_info[1] = i; ret_info[2] = y;

        int count = 0;

        // Count the amount of connected boxes that match the clicked box's team
        for (; i < 7; i++) {
            if (grid.get(i).get(y).getTeam() != t) break;
            else count++;
        }

        // Connected count
        ret_info[0] = count;
        // Right-most x y
        ret_info[3] = i - 1; ret_info[4] = y;

        // Return if win
        return ret_info;
    }

    /** Checks if there is a win in the vertical direction '|' dir: V*/
    public static int[] winVertical(int x, int y, ArrayList<ArrayList<CustomButton>> grid, Team t) {

        int[] ret_info = new int[5];

        // Finds the last box connected to (x, y) that matches its team
        int i = y;
        for (; i > 0; i--) {
            if (grid.get(x).get(i - 1).getTeam() != t) break;
        }

        // Top-most x y
        ret_info[1] = x; ret_info[2] = i;

        int count = 0;

        // Count the amount of connected boxes that match the clicked box's team
        for (; i < 6; i++) {
            if (grid.get(x).get(i).getTeam() != t) break;
            else count++;
        }

        // Connected count
        ret_info[0] = count;
        // Bottom-most x y
        ret_info[3] = x; ret_info[4] = i - 1;

        // Return if win
        return ret_info;
    }

    /** Checks if there is a win Diagonally to the right '\' dir: V, > */
    public static int[] winDiagonalRight(int x, int y, ArrayList<ArrayList<CustomButton>> grid, Team t) {

        int[] ret_info = new int[5];

        int r = y; int c = x;
        while (r > 0 && c > 0) {
            if (grid.get(c - 1).get(r - 1).getTeam() != t) break;
            r--; c--;
        }

        // Top_Right-most x y
        ret_info[1] = c; ret_info[2] = r;

        int count = 0;

        while (r < 6 && c < 7) {
            if (grid.get(c).get(r).getTeam() != t) break;
            else count++;
            r++; c++;
        }

        // Connected count
        ret_info[0] = count;
        // Bottom-Left-most x y
        ret_info[3] = c - 1; ret_info[4] = r - 1;

        return ret_info;
    }

    /** Checks if there is a win diagonally to the left '/' dir: ^, > */
    public static int[] winDiagonalLeft(int x, int y, ArrayList<ArrayList<CustomButton>> grid, Team t) {

        int[] ret_info = new int[5];

        int r = y; int c = x;
        while (r < 5 && c > 0) {
            if (grid.get(c - 1).get(r + 1).getTeam() != t) break;
            r++; c--;
        }

        // Bottom_Right-most x y
        ret_info[1] = c; ret_info[2] = y;

        int count = 0;

        while (r >= 0 && c <= 6) {
            if (grid.get(c).get(r).getTeam() != t) break;
            else count++;
            r--; c++;
        }

        // Connected count
        ret_info[0] = count;
        // Top_Left-most x y
        ret_info[3] = c - 1; ret_info[4] = r + 1;

        return ret_info;
    }

    /** Sets the team and background for a Button */
    public static void setButtonTeam(CustomButton btn, Team t) {
        btn.setTeam(t);
        btn.setBackground((t == Team.RED) ? Color.red : Color.YELLOW);
    }

    /** Updates the Team Indicator to show which players turn it currently is */
    public static void updateTeamIndicator(JButton team, Team t) {
        team.setText(t == Team.YELLOW ? "Red" : "Yellow");
        team.setForeground(t == Team.YELLOW ? Color.red : Color.yellow);
    }

    /** Generates the Exit Button for this Window with specified properties set, then returns it */
    public static JButton getExitButton(Dimension size) {
        // Re-Creating the exit button for this Window
        JButton exit = new JButton("X");
        exit.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        exit.setFocusPainted(false);    // Button doesn't change when only focused
        exit.setForeground(new Color(225, 25, 25));    // Font color
        exit.setMargin(new Insets(0,0,0,0));    // Disable Margin
        exit.setBounds(size.width - 37, 12, 25, 25);    // Size and Position of Button
        // Exit button listener
        exit.addActionListener(e -> System.exit(0));

        return exit;
    }

    /** Generates the Team Indicator for the Game, with specified properties, then returns it */
    public static JButton getTeamIndicator(Team t) {
        JButton team = new JButton(t == Team.YELLOW ? "Yellow" : "Red");
        team.setForeground(t == Team.YELLOW ? Color.yellow : Color.red);
        team.setBounds(25, 0, 100, 50);
        team.setFocusPainted(false);
        team.setBorderPainted(false);
        team.setFocusable(false);

        return team;
    }

    public static JButton getWinIndicator() {
        JButton win = new JButton();
        Font font = win.getFont();
        win.setFont(new Font(font.getFontName(), Font.PLAIN, font.getSize() + 5));
        win.setBounds(175, 0, 200, 50);
        win.setFocusPainted(false);
        win.setBorderPainted(false);
        win.setFocusable(false);
        win.setVisible(false);

        return win;
    }
}
