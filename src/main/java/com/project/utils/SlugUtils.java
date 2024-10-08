package com.project.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class SlugUtils {

    public static String toSlug(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = Pattern.compile("[^\\w\\s-]").matcher(normalized).replaceAll("");  // Özel karakterleri kaldır
        slug = slug.trim().replaceAll("\\s+", "-").toLowerCase();  // Boşlukları tire ile değiştir ve küçük harfe çevir
        return slug;
    }
}