import java.util.Random;
import java.util.Scanner;

public class SeaBattle {

    static final int SIZE = 10;
    static final Random random = new Random();

    private final Scanner scanner = new Scanner(System.in);
    private final GameState gameState = new GameState();
    private final Unit unit1 = new Unit();
    private final Unit unit2 = new Unit();

    private boolean isShipAlive(char[][] field, int x, int y, Direction checkingDirection) {
        if (checkingDirection == Direction.none) {
            if (field[x - 1][y] == Cell.ship ||
                    field[x + 1][y] == Cell.ship ||
                    field[x][y - 1] == Cell.ship ||
                    field[x][y + 1] == Cell.ship)
                return true;
            else {
                if (field[x - 1][y] == Cell.hit) {
                    return isShipAlive(field, x - 1, y, Direction.up);
                }
                if (field[x + 1][y] == Cell.hit) {
                    return isShipAlive(field, x + 1, y, Direction.down);
                }
                if (field[x][y - 1] == Cell.hit) {
                    return isShipAlive(field, x, y - 1, Direction.left);
                }
                if (field[x][y + 1] == Cell.hit) {
                    return isShipAlive(field, x, y + 1, Direction.right);
                }
            }
        } else {
            switch (checkingDirection) {
                case left -> {
                    if (field[x][y - 1] == Cell.ship) return true;
                    if (field[x][y - 1] == Cell.hit) return isShipAlive(field, x, y - 1, Direction.left);
                }
                case right -> {
                    if (field[x][y + 1] == Cell.ship) return true;
                    if (field[x][y + 1] == Cell.hit) return isShipAlive(field, x, y + 1, Direction.right);
                }
                case up -> {
                    if (field[x - 1][y] == Cell.ship) return true;
                    if (field[x - 1][y] == Cell.hit) return isShipAlive(field, x - 1, y, Direction.up);
                }
                case down -> {
                    if (field[x + 1][y] == Cell.ship) return true;
                    if (field[x + 1][y] == Cell.hit) return isShipAlive(field, x + 1, y, Direction.down);
                }
            }
        }
        return false;
    }
    private void showCoreBattleField() {
        System.out.print("    ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.print("     ");
        for (int i = 1; i < 11; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 1; i <= SIZE; i++) {
            System.out.printf("%2d  ", i);
            for (int j = 1; j <= SIZE; j++) {
                System.out.print(unit1.field[i][j] + " ");
            }
            System.out.printf("  %2d  ", i);
            for (int j = 1; j <= SIZE; j++) {
                System.out.print(unit2.field[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    private void showPublicBattleField() {
        System.out.print("    ");
        for (int i = 1; i <= SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.print("     ");
        for (int i = 1; i < 11; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 1; i <= SIZE; i++) {
            System.out.printf("%2d  ", i);
            for (int j = 1; j <= SIZE; j++) {
                if (unit1.field[i][j] != Cell.ship) {
                    System.out.print(unit1.field[i][j] + " ");
                } else {
                    System.out.print(Cell.empty + " ");
                }
            }
            System.out.printf("  %2d  ", i);
            for (int j = 1; j <= SIZE; j++) {
                if (unit2.field[i][j] != Cell.ship) {
                    System.out.print(unit2.field[i][j] + " ");
                } else {
                    System.out.print(Cell.empty + " ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
    private void destroyedShipProcessing(Unit victim, int x, int y, Direction dir) {
        victim.currentFleet[5]++;
        if (dir == Direction.none) {
            for (int i = x - 1; i <= x + 1; i++) {
                for (int j = y - 1; j <= y + 1; j++) {
                    if (victim.field[i][j] == Cell.empty) {
                        victim.field[i][j] = Cell.marked;
                    }
                }
            }
            if (victim.field[x - 1][y] == Cell.hit) {
                destroyedShipProcessing(victim, x - 1, y, Direction.up);
            }
            if (victim.field[x + 1][y] == Cell.hit) {
                destroyedShipProcessing(victim, x + 1, y, Direction.down);
            }
            if (victim.field[x][y - 1] == Cell.hit) {
                destroyedShipProcessing(victim, x, y - 1, Direction.left);
            }
            if (victim.field[x][y + 1] == Cell.hit) {
                destroyedShipProcessing(victim, x, y + 1, Direction.right);
            }
            victim.currentFleet[victim.currentFleet[5]]--;
            victim.currentFleet[5] = 0;
            victim.currentFleet[0]--;
        } else {
            switch (dir) {
                case left -> {
                    if (victim.field[x - 1][y - 1] == Cell.empty) {
                        victim.field[x - 1][y - 1] = Cell.marked;
                    }
                    if (victim.field[x + 1][y - 1] == Cell.empty) {
                        victim.field[x + 1][y - 1] = Cell.marked;
                    }
                    if (victim.field[x][y - 1] == Cell.empty) {
                        victim.field[x][y - 1] = Cell.marked;
                    } else {
                        if (victim.field[x][y - 1] == Cell.hit) {
                            destroyedShipProcessing(victim, x, y - 1, Direction.left);
                        }
                    }
                }
                case right -> {
                    if (victim.field[x + 1][y + 1] == Cell.empty) {
                        victim.field[x + 1][y + 1] = Cell.marked;
                    }
                    if (victim.field[x - 1][y + 1] == Cell.empty) {
                        victim.field[x - 1][y + 1] = Cell.marked;
                    }
                    if (victim.field[x][y + 1] == Cell.empty) {
                        victim.field[x][y + 1] = Cell.marked;
                    } else {
                        if (victim.field[x][y + 1] == Cell.hit) {
                            destroyedShipProcessing(victim, x, y + 1, Direction.right);
                        }
                    }
                }
                case up -> {
                    if (victim.field[x - 1][y - 1] == Cell.empty) {
                        victim.field[x - 1][y - 1] = Cell.marked;
                    }
                    if (victim.field[x - 1][y + 1] == Cell.empty) {
                        victim.field[x - 1][y + 1] = Cell.marked;
                    }
                    if (victim.field[x - 1][y] == Cell.empty) {
                        victim.field[x - 1][y] = Cell.marked;
                    } else {
                        if (victim.field[x - 1][y] == Cell.hit) {
                            destroyedShipProcessing(victim, x - 1, y, Direction.up);
                        }
                    }
                }
                case down -> {
                    if (victim.field[x + 1][y + 1] == Cell.empty) {
                        victim.field[x + 1][y + 1] = Cell.marked;
                    }
                    if (victim.field[x + 1][y - 1] == Cell.empty) {
                        victim.field[x + 1][y - 1] = Cell.marked;
                    }
                    if (victim.field[x + 1][y] == Cell.empty) {
                        victim.field[x + 1][y] = Cell.marked;
                    } else {
                        if (victim.field[x + 1][y] == Cell.hit) {
                            destroyedShipProcessing(victim, x + 1, y, Direction.down);
                        }
                    }
                }
            }
        }
    }
    private void reverseDirection(Unit attacker, char[][] victimField) {
        switch (attacker.moveDirection) {
            case left -> {
                attacker.moveDirection = Direction.right;
                while (victimField[attacker.x][attacker.y + 1] == Cell.hit) attacker.y++;
            }
            case right -> {
                attacker.moveDirection = Direction.left;
                while (victimField[attacker.x][attacker.y - 1] == Cell.hit) attacker.y--;
            }
            case up -> {
                attacker.moveDirection = Direction.down;
                while (victimField[attacker.x + 1][attacker.y] == Cell.hit) attacker.x++;
            }
            case down -> {
                attacker.moveDirection = Direction.up;
                while (victimField[attacker.x - 1][attacker.y] == Cell.hit) attacker.x--;
            }
        }
    }
    private void misHit(Unit attacker, char[][] victimField) {
        victimField[attacker.x][attacker.y] = Cell.misHit;
        gameState.isFirstUnitMove = !gameState.isFirstUnitMove;
        if (gameState.printBattleTrace) {
            System.out.println(" " + attacker.name + " missed at [" + attacker.x + "][" + attacker.y + "]");
        }
    }
    private void hit(Unit attacker, Unit victim) {
        victim.field[attacker.x][attacker.y] = Cell.hit;
        if (gameState.printBattleTrace) {
            System.out.println(" " + attacker.name + " hit at [" + attacker.x + "][" + attacker.y + "]");
        }
        if (!isShipAlive(victim.field, attacker.x, attacker.y, Direction.none)) {
            destroyedShipProcessing(victim, attacker.x, attacker.y, Direction.none);
            attacker.x = 0;
            attacker.moveDirection = Direction.none;
            if (gameState.printBattleTrace) {
                System.out.println(" Ship destroyed! Ships alive:");
                System.out.println(" " + unit1.shipsAliveCount() + " " + unit1.name);
                System.out.println(" " + unit2.shipsAliveCount() + " " + unit2.name);
            }
            if (victim.shipsAliveCount() == 0) {
                gameState.isGameOver = true;
            }
        }
    }
    private void botMove(Unit attacker, Unit victim) {
        attacker.movesCount++;
        if (attacker.x == 0) {
            do {
                attacker.x = 1 + random.nextInt(SIZE);
                attacker.y = 1 + random.nextInt(SIZE);
            }
            while (!(victim.field[attacker.x][attacker.y] == Cell.ship || victim.field[attacker.x][attacker.y] == Cell.empty));
            if (victim.field[attacker.x][attacker.y] == Cell.empty) {
                misHit(attacker, victim.field);
                attacker.x = 0;
            } else {
                hit(attacker, victim);
            }
        } else {
            finishingMove(attacker, victim);
        }
    }
    private void cleverBotMove(Unit attacker, Unit victim) {
        attacker.movesCount++;
        if (attacker.x == 0) {

            int[][] observer = new int[11][11];
            boolean isClear;
            int max = victim.maxAliveShipLength();
            for (int x = 1; x < 11; x++) {
                for (int y = 1; y < 11; y++) {
                    if (x + max - 1 < 11) {
                        isClear = true;
                        for (int z = x; z < x + max; z++) {
                            if (!(victim.field[z][y] == Cell.empty) && !(victim.field[z][y] == Cell.ship)) { isClear = false; break; } }
                        if (isClear) { for (int z = x; z < x + max; z++) { observer[z][y]++; } }
                    }
                    if (y + max - 1 < 11) {
                        isClear = true;
                        for (int z = y; z < y + max; z++) {
                            if (!(victim.field[x][z] == Cell.empty) && !(victim.field[x][z] == Cell.ship)) { isClear = false; break; } }
                        if (isClear) { for (int z = y; z < y + max; z++) { observer[x][z]++; } }
                    }
                }
            }
            max = 0;
            for (int x = 1; x < 11; x++) {
                for (int y = 1; y < 11; y++) {
                    if (observer[x][y] > max) { max = observer[x][y]; }
                }
            }
            do {
                attacker.x = 1 + random.nextInt(SIZE);
                attacker.y = 1 + random.nextInt(SIZE);
            }
            while (observer[attacker.x][attacker.y] < max);
            if (victim.field[attacker.x][attacker.y] == Cell.empty) {
                misHit(attacker, victim.field);
                attacker.x = 0;
            } else { hit(attacker, victim); }
        } else { finishingMove(attacker, victim); }
    }
    private void playerMove(Unit attacker, Unit victim) {
        attacker.movesCount++;
        if (attacker.x == 0) {
            attacker.x = scanner.nextInt();
            attacker.y = scanner.nextInt();
            if (attacker.x < 1 || attacker.y < 1 || attacker.x > 10 || attacker.y > 10) {
                System.out.println("Invalid input. Try again");
                return;
            }
            if (victim.field[attacker.x][attacker.y] == Cell.empty) {
                misHit(attacker, victim.field);
                attacker.x = 0;
            } else {
                if (victim.field[attacker.x][attacker.y] == Cell.ship) {
                    hit(attacker, victim);
                } else {
                    System.out.println(" That was missClick :D");
                    attacker.x = 0;
                    gameState.isFirstUnitMove = !gameState.isFirstUnitMove;
                }
            }
        } else {
            finishingMove(attacker, victim);
        }
    }
    private void finishingMove(Unit attacker, Unit victim) {
        if (attacker.moveDirection == Direction.none) {
            int tmpDir;
            do {
                attacker.dx = attacker.x;
                attacker.dy = attacker.y;
                tmpDir = random.nextInt(4);
                switch (tmpDir) {
                    case 0 -> {
                        attacker.dx--;
                        attacker.moveDirection = Direction.up;
                    }
                    case 1 -> {
                        attacker.dy--;
                        attacker.moveDirection = Direction.left;
                    }
                    case 2 -> {
                        attacker.dx++;
                        attacker.moveDirection = Direction.down;
                    }
                    case 3 -> {
                        attacker.dy++;
                        attacker.moveDirection = Direction.right;
                    }
                }
            }
            while (!(victim.field[attacker.dx][attacker.dy] == Cell.ship || victim.field[attacker.dx][attacker.dy] == Cell.empty));
            attacker.x = attacker.dx;
            attacker.y = attacker.dy;
            if (victim.field[attacker.x][attacker.y] == Cell.empty) {
                misHit(attacker, victim.field);
                attacker.moveDirection = Direction.none;
                switch (tmpDir) {
                    case 0 -> attacker.x++;
                    case 1 -> attacker.y++;
                    case 2 -> attacker.x--;
                    case 3 -> attacker.y--;
                }
            } else {
                hit(attacker, victim);
            }
        } else { // direction != none
            switch (attacker.moveDirection) {
                case left -> attacker.y--;
                case right -> attacker.y++;
                case up -> attacker.x--;
                case down -> attacker.x++;
            }
            if (victim.field[attacker.x][attacker.y] == Cell.ship) {
                hit(attacker, victim);
            } else {
                if (victim.field[attacker.x][attacker.y] == Cell.empty) {
                    misHit(attacker, victim.field);
                    reverseDirection(attacker, victim.field);
                } else {
                    reverseDirection(attacker, victim.field);
                    switch (attacker.moveDirection) {
                        case up -> attacker.x--;
                        case down -> attacker.x++;
                        case left -> attacker.y--;
                        case right -> attacker.y++;
                    }
                    hit(attacker, victim);
                }
            }
        }
    }

    public void play() {
        /* game mods:
         * 1 - PvP
         * 2 - PvE
         * */
        System.out.println("Select game mod (1-PvE, 2-PvP):");
        gameState.gameMod = scanner.nextInt();
        System.out.println("Type nickname for 1st player:");
        unit1.name = scanner.next();
        if (gameState.gameMod == 2) {
            System.out.println("Type nickname for 2nd player:");
            unit2.name = scanner.next();
        }
        gameState.printResult = gameState.printBattleTrace = true;

        unit1.init();
        unit2.init();
        gameState.isGameOver = false;
        do {
            if (gameState.isFirstUnitMove) { playerMove(unit1, unit2);
            }
            else {
                switch (gameState.gameMod) {
                    case 1 -> botMove(unit2, unit1);
                    case 2 -> playerMove(unit2, unit1);
                }
            }
            showPublicBattleField();
        }
        while (!gameState.isGameOver);
        if (unit1.shipsAliveCount() > 0) { System.out.println(" " + unit1.name + " won! " + unit1.movesCount + " moves"); }
        else { System.out.println(" " + unit2.name + " won! " + unit2.movesCount + " moves"); }
    }
    public int cleverBotTest() {
        /*gameState.printResult = true;
        gameState.printBattleTrace = true;*/
        unit1.init();
        unit2.init();
        gameState.isGameOver = false;
        do {
            cleverBotMove(unit1, unit2);
            /*botMove(unit2, unit1)*/
            if (gameState.printBattleTrace) { showPublicBattleField(); }
        }
        while (!gameState.isGameOver);
        if (gameState.printResult) { System.out.println(" " + unit1.name + " won! " + unit1.movesCount + " moves"); }
        /*else*/
        return unit1.movesCount;
    }
}