package com.ttnm.chillchatting.utils;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by: IntelliJ IDEA
 * User      : thangpx
 * Date      : 3/30/22
 * Time      : 10:11
 * Filename  : StringUtils
 */
public class StringUtilsVN {
    public static String removeAccentJava(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }


    public static String generateFileName() {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            result.append(random.nextInt(26) + 'a');
        }
        result.append(new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()));
        return result.toString();
    }

    public static String makeVietNameseRegex(String s) {
        s = s.replaceAll("a","[aàáâãăạảấầẩẫậắằẳẵặ]");
        s = s.replaceAll("d","[dđ]");
        s = s.replaceAll("e","[eèéêẹẻẽếềểễệ]");
        s = s.replaceAll("i","[iìíĩỉị]");
        s = s.replaceAll("o","[oòóõọỏôốồổỗộơớờởỡợ]");
        s = s.replaceAll("u","[uùũúưụủứừửữự]");
        s = s.replaceAll("y","[yýỹỳỷỵ]");
        return s;
    }
}

