import java.io.Serializable;

public class PlayersPair implements Serializable {
    String first;
    String second;
    int firstWinsCount;
    int battlesCount;

    public PlayersPair(String first, String second, int firstWinsCount, int battlesCount) {
        this.first = first;
        this.second = second;
        this.firstWinsCount = firstWinsCount;
        this.battlesCount = battlesCount;
    }

    @Override
    public String toString() {
        return this.first + " wins " + this.second + " " + this.firstWinsCount + " of " + this.battlesCount;
    }
}
