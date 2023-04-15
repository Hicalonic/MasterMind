package academy.mindswap.game.commands;


public enum Command {
    HELP("/HELP", new HelpHandler()),
    QUIT("/QUIT", new QuitHandler()),
    START("/START", new StartNewGameHandler()),
    SHOW("/SHOW", new ShowHandler());

    private final String description;
    private final CommandHandler handler;

    Command(String description, CommandHandler handler) {
        this.description = description;
        this.handler = handler;
    }


    /**
     * returns a command in case the description inputted belongs to the command List.
     * @param description
     * @return command returned
     */
    public static Command getCommandFromDescription(String description) {
        for (Command command : values()) {
            if (description.equals(command.description)) {
                return command;
            }
        }
        return null;
    }

    public CommandHandler getHandler() {
        return handler;
    }
}