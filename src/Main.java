import java.io.*;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        /*HashMap<String, Integer> players = new HashMap<>();
        String name;
        name = "Ilya"; players.put(name, name.hashCode());
        name = "Kate"; players.put(name, name.hashCode());
        name = "Alex"; players.put(name, name.hashCode());
        name = "Alla"; players.put(name, name.hashCode());
        name = "Andrew"; players.put(name, name.hashCode());
        name = "Kirill"; players.put(name, name.hashCode());
        name = "Anton"; players.put(name, name.hashCode());
        name = "Vlad"; players.put(name, name.hashCode());
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("players.ser"))) {
            oos.writeObject(players); }
        catch (IOException e) { throw new RuntimeException(e); }

        HashMap<String, PlayersPair> pairs = new HashMap<>();
        pairs.put("IlyaKate", new PlayersPair("Ilya", "Kate", 4, 7));
        pairs.put("IlyaAlla", new PlayersPair("Ilya", "Alla", 5, 6));
        pairs.put("KirillIlya", new PlayersPair("Kirill", "Ilya", 1, 3));
        pairs.put("KirillAndrew", new PlayersPair("Kirill", "Andrew", 3, 4));
        pairs.put("VladAndrew", new PlayersPair("Vlad", "Andrew", 2, 4));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("playerStat.ser"))) {
            oos.writeObject(pairs); }
        catch (IOException e) { throw new RuntimeException(e); }*/
        SeaBattle seaBattle = new SeaBattle();
        seaBattle.play();
    }
}
