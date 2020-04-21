package com.basicit.util;

import org.apache.commons.lang3.StringUtils;


public class CoreUtil extends StringUtils {
    public static String toFirstCharLowerCase(String name) {
        if (isEmpty(name)) {
            return StringUtils.EMPTY;
        }

        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

}
