public class Unit {

    final int[] currentFleet = new int[6];
    final char[][] field = new char[12][12];
    Direction moveDirection;
    int x, y, dx, dy, movesCount;
    String name = "#Bot";

    private void setShips(int length, int count) {
        final int MAX_SET_TRIES = 2000;
        int triesToSet;
        for (int i = 0; i < count; i++) {
            triesToSet = 0;
            do {
                x = 1 + SeaBattle.random.nextInt(SeaBattle.SIZE);
                y = 1 + SeaBattle.random.nextInt(SeaBattle.SIZE);
                dx = SeaBattle.random.nextInt(2);
                dy = dx == 0 ? 1 : 0;
                triesToSet++;
            }
            while (!isAvailablePlace(length) && triesToSet < MAX_SET_TRIES);
            if (triesToSet == MAX_SET_TRIES) System.exit(13);
            for (int j = 0; j < length; j++) {
                x -= dx;
                y -= dy;
                field[x][y] = Cell.ship;
            }
            currentFleet[length]++;
            currentFleet[0]++;
        }
    }
    private boolean isAvailablePlace(int length) {
        for (int i = 0; i < length; i++) {
            if (field[x][y] != Cell.empty ||
                    field[x - 1][y - 1] == Cell.ship ||
                    field[x - 1][y] == Cell.ship ||
                    field[x - 1][y + 1] == Cell.ship ||
                    field[x + 1][y - 1] == Cell.ship ||
                    field[x][y - 1] == Cell.ship ||
                    field[x + 1][y] == Cell.ship ||
                    field[x][y + 1] == Cell.ship ||
                    field[x + 1][y + 1] == Cell.ship)
                return false;
            x += dx;
            y += dy;
        }
        return true;
    }

    Unit() {
        for (int i = 0; i < SeaBattle.SIZE + 2; i++) {
            field[0][i] = Cell.border;
            field[i][0] = Cell.border;
            field[SeaBattle.SIZE + 1][i] = Cell.border;
            field[i][SeaBattle.SIZE + 1] = Cell.border;
        }
    }
    void init() {
        for (int i = 1; i <= SeaBattle.SIZE; i++) {
            for (int j = 1; j <= SeaBattle.SIZE; j++) {
                field[i][j] = Cell.empty;
            }
        }
        setShips(4, 1);
        setShips(3, 2);
        setShips(2, 3);
        setShips(1, 4);
        moveDirection = Direction.none;
        x = 0;
        movesCount = 0;
    }
    int shipsAliveCount() { return currentFleet[0]; }
    int maxAliveShipLength() {
        int length = 4;
        while (true) {
            if (currentFleet[length] > 0) { return length; }
            length--;
        }
    }
}