package net.ddns.lagarderie.racingplugin.game;

public enum CheckpointType {
    CIRCLE,
    RECTANGLE;

    public static CheckpointType parseType(String type) {
        return switch (type.toLowerCase()) {
            case "circle" -> CIRCLE;
            case "rectangle" -> RECTANGLE;
            default -> null;
        };
    }
}
