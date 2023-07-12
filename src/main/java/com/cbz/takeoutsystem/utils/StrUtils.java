package com.cbz.takeoutsystem.utils;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StrUtils {
    public static List<Integer> splitIds(String ids) {
        return Arrays.stream(ids.split(",")).map(Integer::valueOf).collect(Collectors.toList());
    }

    public static String createUUID(boolean isMinus) {
        String uuid = UUID.randomUUID().toString();
        if (!isMinus) {
            uuid = uuid.replace("-", "");
        }
        return uuid;
    }
}
