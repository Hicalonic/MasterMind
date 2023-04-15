package academy.mindswap.game.messages;

public enum Items {
    PURPLE("\u001B[38;2;190;1;253m⬤\u001B[0m", "P ⏩ "),
    YELLOW("\u001B[38;2;253;236;1m⬤\u001B[0m", "Y ⏩ "),
    GREEN("\u001B[32m⬤\u001B[0m", " G ⏩ "),
    BLUE("\u001B[34m⬤\u001B[0m", "B ⏩ "),
    ORANGE("\u001B[38;2;253;126;1m⬤\u001B[0m", " O ⏩ "),
    WB("\u001B[47m", "WB"),
    R("\u001B[0m", "R"),
    WHITE("\u001B[37m☗\u001B[0m", "Correct Color Wrong Position"),
    RED("\u001B[31m☗\u001B[0m", "Correct Color and Position"),
    BLACK("\u001B[30m☗\u001B[0m", "Wrong Color"),
    HTBAR("▂", "HorizontalTopBar"),

    HBBAR("▔", "HorizontalBottomBar"),

    VBAr("   ▍  ", "VerticalBar");

    public final String color;

    public final String description;

    Items(String color, String description) {
        this.color = color;
        this.description = description;
    }
}