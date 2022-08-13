public class Main {

    public static void main(String[] args) {
        SeaBattle seaBattle = new SeaBattle();
        int aver = 0;
        for (int i = 0; i < 5000; i++) {
            aver += seaBattle.cleverBotTest();
        }
        aver /= 5000;
        System.out.println(aver);
    }
}