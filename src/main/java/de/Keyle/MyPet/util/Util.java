/*
 * This file is part of MyPet
 *
 * Copyright (C) 2011-2014 Keyle
 * MyPet is licensed under the GNU Lesser General Public License.
 *
 * MyPet is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyPet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package de.Keyle.MyPet.util;

import com.google.common.base.Charsets;
import de.Keyle.MyPet.util.logger.DebugLogger;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.UUID;

public class Util {
    public static Field getField(Class<?> clazz, String field) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return f;
        } catch (Exception ignored) {
            return null;
        }
    }

    public static boolean setFieldValue(Field field, Object object, Object value) {
        try {
            field.set(object, value);
            return true;
        } catch (IllegalAccessException e) {
            return false;
        }
    }

    public static boolean isInt(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isByte(String number) {
        try {
            Byte.parseByte(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isDouble(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isLong(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isFloat(String number) {
        try {
            Float.parseFloat(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static boolean isShort(String number) {
        try {
            Short.parseShort(number);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    public static String cutString(String string, int length) {
        if (string.length() > length) {
            return string.substring(0, length);
        }
        return string;
    }

    public static String formatText(String text, Object... values) {
        return MessageFormat.format(text, values);
    }

    public static String capitalizeName(String name) {
        Validate.notNull(name, "Name can't be null");

        name = name.replace("_", " ");
        name = WordUtils.capitalizeFully(name);
        name = name.replace(" ", "");
        return name;
    }

    public static String readFileAsString(String filePath) throws java.io.IOException {
        StringBuilder fileData = new StringBuilder(1000);
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

    public static String convertStreamToString(java.io.InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static String readUrlContent(String address) throws IOException {
        StringBuilder contents = new StringBuilder(2048);
        BufferedReader br = null;

        try {
            URL url = new URL(address);
            br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = br.readLine()) != null) {
                contents.append(line);
            }
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                DebugLogger.printThrowable(e);
            }
        }
        return contents.toString();
    }

    public static String decimal2roman(int src) {
        char digits[] = {'I', 'V', 'X', 'L', 'C', 'D', 'M'};
        String thousands = "", result = "";
        int rang, digit, i;

        for (i = src / 1000; i > 0; i--) {
            thousands += "M";
        }
        src %= 1000;

        rang = 0;
        while (src > 0) {
            digit = src % 10;
            src /= 10;
            switch (digit) {
                case 1:
                    result = "" + digits[rang] + result;
                    break;
                case 2:
                    result = "" + digits[rang] + digits[rang] + result;
                    break;
                case 3:
                    result = "" + digits[rang] + digits[rang] + digits[rang] + result;
                    break;
                case 4:
                    result = "" + digits[rang] + digits[rang + 1] + result;
                    break;
                case 5:
                    result = "" + digits[rang + 1] + result;
                    break;
                case 6:
                    result = "" + digits[rang + 1] + digits[rang] + result;
                    break;
                case 7:
                    result = "" + digits[rang + 1] + digits[rang] + digits[rang] + result;
                    break;
                case 8:
                    result = "" + digits[rang + 1] + digits[rang] + digits[rang] + digits[rang] + result;
                    break;
                case 9:
                    result = "" + digits[rang] + digits[rang + 2] + result;
                    break;
            }
            rang += 2;
        }
        return thousands + result;
    }

    public static boolean isBetween(int intMin, int intMax, int intValue) {
        return intValue > intMin && intValue < intMax;
    }

    public static UUID getOfflinePlayerUUID(String name) {
        return UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes(Charsets.UTF_8));
    }

    public static boolean findClassInStackTrace(StackTraceElement[] stackTrace, String className) {
        return findClassInStackTrace(stackTrace, className, 0, stackTrace.length - 1, false);
    }

    public static boolean findClassInStackTrace(StackTraceElement[] stackTrace, String className, int element) {
        return findClassInStackTrace(stackTrace, className, element, element, false);
    }

    public static boolean findClassInStackTrace(StackTraceElement[] stackTrace, String className, int from, int to, boolean debug) {
        Validate.isTrue(to >= from, "\"to\" has to be >= \"from\".");
        Validate.isTrue(from >= 0, "\"from\" has to be >= 0.");
        to = Math.min(stackTrace.length - 1, to);
        if (debug) {
            DebugLogger.info("=====================================================================================================================================");
        }
        for (int i = from; i <= to; i++) {
            if (stackTrace[i].getClassName().equals(className)) {
                if (debug) {
                    DebugLogger.info("=====================================================================================================================================");
                }
                return true;
            }
        }
        if (debug) {
            DebugLogger.info("=====================================================================================================================================");
        }
        return false;
    }
}