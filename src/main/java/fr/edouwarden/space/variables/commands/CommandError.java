package fr.edouwarden.space.variables.commands;

import fr.edouwarden.space.variables.TChat;

public final class CommandError {

    private CommandError() {
    }

    public static String koCommandIncomplete() {
        return TChat.Head() + TChat.Error()
                + "Incomplete command: /space ko set|remove <player>";
    }

    public static String dfCommandIncomplete() {
        return TChat.Head() + TChat.Error()
                + "Incomplete command: /space power set|remove <player> <power>";
    }

    public static String jobsCommandIncomplete() {
        return TChat.Head() + TChat.Error()
                + "Incomplete command: /space jobs set|remove <player> <job>";
    }

    public static String playerNotExist() {
        return TChat.Head() + TChat.Error() + "That player does not exist.";
    }
}
