import java.util.ArrayList;
import java.util.Random;

public class AI {
    private int level = 1;

    private Random rand = new Random();

    private Team t;

    private Team player;

    private ArrayList<ArrayList<CustomButton>> grid;

    public AI(int level, Team bot, Team player, ArrayList<ArrayList<CustomButton>> grid) {
        this.level = level;
        this.t = bot;
        this.grid = grid;
        this.player = player;
    }

    public int getMove() {
        int move;
        if ((move = getLevelThreeMove()) != -1) return move;
        if ((move = getLevelTwoMove()) != -1) return move;
        else return getLevelOneMove();
    }

    /** Gets a random valid move*/
    private int getLevelOneMove() {
        int move = rand.nextInt(7);
        while (cannotMoveHere(move, grid))
            move = rand.nextInt(7);

        return move;
    }

    /** Calculates if any move has win potential for the bot, returns that move */
    private int getLevelTwoMove() {
        if (level < 2) return -1;

        for (int c = 0; c < 7; c++) {
            if (calcHorizontalMove(c, t) >= 4) return c;
            if (calcVerticalMove(c, t) >= 4) return c;
            if (calcDownDiagonalMove(c, t) >= 4) return c;
            if (calcUpDiagonalMove(c, t) >= 4) return c;
        }
        return -1;
    }

    /** Calculates if any move has win potential for the player, returns that move*/
    private int getLevelThreeMove() {
        if (level < 3) return -1;

        for (int c = 0; c < 7; c++) {
            if (calcHorizontalMove(c, player) >= 4) return c;
            if (calcVerticalMove(c, player) >= 4) return c;
            if (calcDownDiagonalMove(c, player) >= 4) return c;
            if (calcUpDiagonalMove(c, player) >= 4) return c;
        }
        return -1;
    }

    /** Simulates playing in the specified column. Returns the amount of boxes that would be connected horizontally if the bot plays here */
    private int calcHorizontalMove(int c, Team team) {
        int playable;
        if ((playable = getPlayable(c)) == -1) return 0;

        int left = 0;
        for (int x1 = c - 1; x1 >= 0; x1--) {
            if (grid.get(x1).get(playable).getTeam() != team) break;
            left++;
        }

        int right = 0;
        for (int x2 = c + 1; x2 < 7; x2++) {
            if (grid.get(x2).get(playable).getTeam() != team) break;
            right++;
        }

        return left + 1 + right;
    }

    /** Simulates playing in the specified column. Returns the amount of boxes that would be connected vertically if the bot plays here */
    private int calcVerticalMove(int c, Team team) {
        if (cannotMoveHere(c, grid)) return 0;
        int connected = 0;

        for (int x = getPlayable(c) + 1; x < 6; x++) {
            if (grid.get(c).get(x).getTeam() != team) break;
            connected++;
        }

        return connected + 1;
    }

    private int calcDownDiagonalMove(int c, Team team) {
        int playable;
        if ((playable = getPlayable(c)) == -1) return 0;

        int left = 0;
        int col = c - 1; int row = playable - 1;
        while (col > 0 && row > 0) {
            if (grid.get(col).get(row).getTeam() != team) break;
            left++;col--; row--;
        }

        int right = 0;
        col = c + 1; row = playable + 1;
        while (col < 7 && row < 6) {
            if (grid.get(col).get(row).getTeam() != team) break;
            right++; col++; row++;
        }

        return left + 1 + right;
    }

    private int calcUpDiagonalMove(int c, Team team) {
        int playable;
        if ((playable = getPlayable(c)) == -1) return 0;

        int left = 0;
        int col = c - 1; int row = playable + 1;
        while (col >= 0 && row < 6) {
            if (grid.get(col).get(row).getTeam() != team) break;
            left++; col--; row++;
        }

        int right = 0;
        col = c + 1; row = playable - 1;
        while (col < 7 && row >= 0) {
            if (grid.get(col).get(row).getTeam() != team) break;
            right++; col++; row--;
        }

        return left + 1 + right;
    }

    /** Checks if there is no available move in a specified location */
    public boolean cannotMoveHere(int i, ArrayList<ArrayList<CustomButton>> grid) {
        for (CustomButton btn : grid.get(i))
            if (btn.getTeam() == Team.NONE) return false;

        return true;
    }

    /** Gets the index of the lowest playable box in specified column, -1 if none */
    private int getPlayable(int x) {
        for (int i = 5; i >= 0; i--) {
            CustomButton btn = grid.get(x).get(i);
            if (btn.getTeam() == Team.NONE) return i;
        }
        return -1;
    }

    public Team getTeam() { return this.t; }

    public int getLevel() { return this.level; }
}
