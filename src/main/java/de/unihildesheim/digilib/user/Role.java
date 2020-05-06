package de.unihildesheim.digilib.user;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum Role {

    ADMIN(),
    USER();

    public static String[] getAllRoles() {
        String[] result = new String[Role.values().length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Role.values()[i].name();
        }
        return result;
    }

}
