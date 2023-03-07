public enum Color {
    EMPTY(0, "images/empty_marbel.jpg"),
    GREEN(2, "images/green_marbel.jpg"),
    BLUE(2, "images/blue_marbel.jpg"),
    PURPLE(2, "images/purple_marbel.jpg"),
    RED(1, "images/red_marbel.jpg"),
    YELLOW(1, "images/yellow_marbel.jpg"),
    ORANGE(1, "images/orange_marbel.jpg");
    boolean isPlayer = false, isComputer = false;
    String iconPath;

    Color(int playerType, String iconPath) {
        if (playerType == 1) {
            isPlayer = true;
        } else if (playerType == 2) {
            isComputer = true;
        } else {
            isPlayer = false;
            isComputer = false;
        }
        this.iconPath = iconPath;
    }
}
