import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SeaBattle {

    static final int SIZE = 10;
    static final Random random = new Random();

    private final Scanner scanner = new Scanner(System.in);
    private final GameState gameState = new GameState();
    private final Unit unit1 = new Unit();
    private final Unit unit2 = new Unit();

    private boolean isShipAlive(char[][] field, int x, int y, Direction checkingDirection) {
        if (checkingDirection == Direction.none) {
            if (field[x - 1][y] == Cell.ship || field[x + 1][y] == Cell.ship || field[x][y - 1] == Cell.ship ||
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
        System.out.println(" " + attacker.name + " missed at [" + attacker.x + "][" + attacker.y + "]");
    }

    private void hit(Unit attacker, Unit victim) {
        victim.field[attacker.x][attacker.y] = Cell.hit;
            System.out.println(" " + attacker.name + " hit at [" + attacker.x + "][" + attacker.y + "]");
        if (!isShipAlive(victim.field, attacker.x, attacker.y, Direction.none)) {
            destroyedShipProcessing(victim, attacker.x, attacker.y, Direction.none);
            attacker.x = 0;
            attacker.moveDirection = Direction.none;
                System.out.println(" This ship destroyed! Ships alive count:");
                System.out.println(" " + unit1.shipsAliveCount() + " " + unit1.name);
                System.out.println(" " + unit2.shipsAliveCount() + " " + unit2.name);
            if (victim.shipsAliveCount() == 0) {
                gameState.isGameOver = true;
            }
        }
    }

    private void botMove(Unit attacker, Unit victim) {
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
                            if (!(victim.field[z][y] == Cell.empty) && !(victim.field[z][y] == Cell.ship)) {
                                isClear = false;
                                break;
                            }
                        }
                        if (isClear) {
                            for (int z = x; z < x + max; z++) {
                                observer[z][y]++;
                            }
                        }
                    }
                    if (y + max - 1 < 11) {
                        isClear = true;
                        for (int z = y; z < y + max; z++) {
                            if (!(victim.field[x][z] == Cell.empty) && !(victim.field[x][z] == Cell.ship)) {
                                isClear = false;
                                break;
                            }
                        }
                        if (isClear) {
                            for (int z = y; z < y + max; z++) {
                                observer[x][z]++;
                            }
                        }
                    }
                }
            }
            max = 0;
            for (int x = 1; x < 11; x++) {
                for (int y = 1; y < 11; y++) {
                    if (observer[x][y] > max) {
                        max = observer[x][y];
                    }
                }
            }
            do {
                attacker.x = 1 + random.nextInt(SIZE);
                attacker.y = 1 + random.nextInt(SIZE);
            } while (observer[attacker.x][attacker.y] < max);
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

    private void playerMove(Unit attacker, Unit victim) {
        System.out.println(" " + attacker.name + "'s move:");
        attacker.movesCount++;
        if (attacker.x == 0) {
            attacker.x = scanner.nextInt();
            attacker.y = scanner.nextInt();
            if (attacker.x < 1 || attacker.y < 1 || attacker.x > 10 || attacker.y > 10) {
                System.out.println("Invalid input, try again");
                return;
            }
            if (victim.field[attacker.x][attacker.y] == Cell.empty) {
                misHit(attacker, victim.field);
                attacker.x = 0;
            } else {
                if (victim.field[attacker.x][attacker.y] == Cell.ship) {
                    hit(attacker, victim);
                } else {
                    System.out.println("Lolz))0)");
                    attacker.x = 0;
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
            } while (!(victim.field[attacker.dx][attacker.dy] == Cell.ship ||
                    victim.field[attacker.dx][attacker.dy] == Cell.empty));
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

    private byte initPlayer(HashMap<String, Integer> players, Unit unit) {
        String name;
        System.out.println("Would you like to sign up or log in?\n1-sign up\n2-log in");
        switch (scanner.nextInt()) {
            case 1 -> {
                do {
                    System.out.println("Select player nickname:");
                    name = scanner.next();
                    if (players.containsKey(name)) {
                        System.out.println("This nickname is already taken");
                    }
                } while (players.containsKey(name));
                System.out.println("Create password:");
                players.put(name, scanner.next().hashCode());
                unit.name = name;
                System.out.println("Connected");
                return 1;
            }
            case 2 -> {
                System.out.println("Select player nickname:");
                name = scanner.next();
                System.out.println("Input password for " + name);
                String password = scanner.next();
                if (players.containsKey(name) && password.hashCode() == players.get(name)) {
                    unit.name = name;
                    System.out.println("Connected");
                    return 0;
                } else {
                    System.out.println("Wrong password");
                    System.exit(1);
                    return 0;
                }
            }
            default -> { throw new InputMismatchException("Digits 1,2 available only"); }
        }
    }

    private void saveResults(String winner, String looser) {
        HashMap<String, PlayersPair> playersPairs = new HashMap<>();
        if (Files.exists(Paths.get("playerStat.ser"))) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("playerStat.ser"));) {
                playersPairs = (HashMap<String, PlayersPair>) ois.readObject();
            }
            catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e); }
        }
        String sum = winner + looser;
        if (playersPairs.containsKey(sum)) {
            playersPairs.put(sum, new PlayersPair(winner, looser,playersPairs.get(sum).firstWinsCount + 1,
                    playersPairs.get(sum).battlesCount + 1));
        }
        else {
            sum = looser + winner;
            if (playersPairs.containsKey(sum)) {
                playersPairs.put(sum, new PlayersPair(looser, winner, playersPairs.get(sum).firstWinsCount,
                        playersPairs.get(sum).battlesCount + 1));
            }
            else { playersPairs.put(sum, new PlayersPair(looser, winner, 0, 1)); }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("playerStat.ser"))) {
            oos.writeObject(playersPairs);
        }
        catch (FileNotFoundException e) { throw new RuntimeException(e); }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    private void suggestShowInfo() {
        if (Files.exists(Paths.get("players.ser"))) {
            HashMap<String, Integer> players;
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("players.ser"))) {
                players = (HashMap<String, Integer>) ois.readObject();
            }
            catch (ClassNotFoundException | IOException e) {throw new RuntimeException(e); }
            if (!players.isEmpty()) {
                System.out.println("Would you like to see list of signed players?\n1-yes\n0-no");
                switch (scanner.nextInt()) {
                    case 1 -> { System.out.println(players.keySet()); }
                    case 0 -> {}
                    default -> { new InputMismatchException("Digits 1,2 available only"); }
                }
            }

            if (Files.exists(Paths.get("playerStat.ser"))) {
                HashMap<String, PlayersPair> pairs;
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("playerStat.ser"))) {
                    pairs = (HashMap<String, PlayersPair>) ois.readObject();
                }
                catch (ClassNotFoundException | IOException e) {throw new RuntimeException(e); }
                if (!pairs.isEmpty()) {
                    boolean more = true;
                    do {
                        System.out.println("Would you like to see personal, pair of full statistics?" +
                                "\n1-personal\n2-pair\n3-full\n0-nope");
                        switch (scanner.nextInt()) {
                            case 1 -> {
                                System.out.println("Type the name:");
                                String name = scanner.next();
                                Collection<PlayersPair> viewPairs = pairs.values();
                                for (PlayersPair vp : viewPairs) {
                                    if (vp.first.equals(name) || vp.second.equals(name)) { System.out.println(vp); }
                                }
                            }
                            case 2 -> {
                                System.out.println("Type the name:");
                                String name1 = scanner.next();
                                System.out.println("Type the second name:");
                                String name2 = scanner.next();
                                if (pairs.containsKey(name1+name2)) { System.out.println(pairs.get(name1+name2)); }
                                else {
                                    if (pairs.containsKey(name2+name1)) {System.out.println(pairs.get(name2+name1)); }
                                    else System.out.println("This pair hasn't played any battles");
                                }
                            }
                            case 3 -> {
                                Collection<PlayersPair> viewPairs = pairs.values();
                                viewPairs.forEach(System.out::println);
                            }
                            case 0 -> { more = false; }
                            default -> { new InputMismatchException("Digits 1,2 available only"); }
                        }
                        if (more) {
                            System.out.println("Type \"more\" if need of more statistics:");
                            if (!scanner.next().equalsIgnoreCase("more")) { more = false; }
                        }
                    } while (more);
                }
            }
        }
        else { System.out.println("There is no registered players yet"); }
    }

    public void play() {
        suggestShowInfo();
        System.out.println("Select game-mod:\n1-PvE\n2-PvP");
        gameState.gameMode = scanner.nextInt() == 1 ? GameMode.PvE : GameMode.PvP;
        HashMap<String, Integer> players = null;
        System.out.println("Would you like to save results?\n1-yes\n2-no");
        switch (scanner.nextInt()) {
            case 1 -> {
                players = new HashMap<>();
                if (Files.exists(Paths.get("players.ser"))) {
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("players.ser"))) {
                        players = (HashMap<String, Integer>) ois.readObject(); }
                    catch (ClassNotFoundException | IOException e) { throw new RuntimeException(e); }
                }

                int isModified = 0;
                isModified += initPlayer(players, unit1);
                if (gameState.gameMode == GameMode.PvP) {isModified += initPlayer(players, unit2); }
                if (isModified > 0) {
                    try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("players.ser"))) {
                        oos.writeObject(players);
                    }
                    catch (IOException e) { throw new RuntimeException(e); }
                }
            }
            case 2 -> {
                System.out.println("Choose player nickname");
                unit1.name = scanner.next();
                if (gameState.gameMode == GameMode.PvE) {
                    System.out.println("Choose another player nickname");
                    unit2.name = scanner.next();
                }
            }
            default -> { throw new InputMismatchException("digits 1,2 available only"); }
        }

        unit1.init();
        unit2.init();
        gameState.isGameOver = false;

        showPublicBattleField();
        do {
            if (gameState.isFirstUnitMove) {
                playerMove(unit1, unit2);
            } else {
                switch (gameState.gameMode) {
                    case PvE -> botMove(unit2, unit1);
                    case PvP -> playerMove(unit2, unit1);
                }
            }
            showPublicBattleField();
        } while (!gameState.isGameOver);

        if (unit1.shipsAliveCount() > 0) {
            if (players != null) saveResults(unit1.name, unit2.name);
            System.out.println(" " + unit1.name + " won! " + unit1.movesCount + " moves");
        }
        else {
            if (players != null) saveResults(unit2.name, unit1.name);
            System.out.println(" " + unit2.name + " won! " + unit2.movesCount + " moves");
        }
    }
}