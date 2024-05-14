package version3;

import java.util.Scanner;


public class GameEngine {
    private GameMap gameMap;
    private Player player;
    private boolean running;
    private Kiosk kiosk;
    private VendingMachine vendingMachine;
    private Guest_1 guest;

    public GameEngine() {
        gameMap = new GameMap(12, 12);
        player = new Player(10, 5);
        running = true;
        kiosk = new Kiosk();
        vendingMachine = new VendingMachine();
        guest = new Guest_1();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        kiosk.kiosk_main_manager();
        vendingMachine.vendingMachine_main1();
        vendingMachine.vendingMachine_main2();
        while (running) {
            clearConsole();
            if (player.getFlag(Player.FlagType.SECOND_FLOOR)) { // 2층 입장
                gameMap.gameMapSecondFloor();
            } else { // 1층 재입장
                gameMap.gameMapFirstFloor();
            }

            gameMap.printMap(player.getX(), player.getY());
            
            System.out.print("Move (WASD): ");
            char move = scanner.nextLine().toUpperCase().charAt(0);
            player.move(move, gameMap);
            //키오스크 상호작용
            if (player.getFlag(Player.FlagType.KIOSK)) {
                kiosk.kiosk_main();
                player.setFlag(Player.FlagType.KIOSK, false);
            }
            // 메뉴를 카운터로 전송
            if (player.getFlag(Player.FlagType.MENU)) {
                Kiosk.lastOrder.displayOrdered();
                player.setFlag(Player.FlagType.MENU, false);
            }
            // 문으로 나가면 실행 종료
            if (player.getFlag(Player.FlagType.END_GAME)) {
                break;
            }
            if (player.getFlag(Player.FlagType.GUEST)) {
                guest.print();
                player.setFlag(Player.FlagType.GUEST, false);
            }
        }
        scanner.close();
        
    }

    private void clearConsole() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new GameEngine().start();
    }
}
