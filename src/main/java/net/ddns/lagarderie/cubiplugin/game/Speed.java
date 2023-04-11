package net.ddns.lagarderie.cubiplugin.game;

public enum Speed {
    CC50,
    CC100,
    CC150,
    CC200;

    public String toString() {
        return switch (this) {
            case CC50 -> "50";
            case CC100 -> "100";
            case CC150 -> "150";
            case CC200 -> "200";
        };
    }
}
