package com.chai.wiki.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ArrayUtil {
    public static List<Long> strArrayConvertToLongList(String ids) {
        String[] idArray = ids.split(",");
        return Arrays.stream(idArray)
                .map(s ->Long.parseLong(s.trim())).collect(Collectors.toList());
    }
}
