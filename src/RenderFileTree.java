import java.util.*;

import static java.util.Objects.isNull;

class RenderFileTree {


    /**
     *     etc
     *         hosts
     *         nginx
     *             conf.d
     *                 website.conf
     *         passwd
     *     home
     *         michel
     *             cv.pdf
     *             photos
     *                 profile.jpg
     *                 wallpaper.jpg
     * @param args
     */
    public static void main(String[] args) {

        String[] paths = {
                "/home/michel/photos/wallpaper.png",
                "/etc/passwd",
                "/etc/nginx/conf.d/website.conf",
                "/home/michel/cv.pdf",
                "/etc/hosts",
                "/home/michel/photos/profile.jpg",
                "/home/michel"
        };

        renderTree(paths);
    }

    private static void renderTree(String[] N) {

        Map<String, List<String>> paths = new LinkedHashMap<>();

        for (String aN : N) {
            String[] subs = aN.split("/");
            for (int j = 0; j < subs.length - 1; j++) {
                if (isNull(paths.get(subs[j]))) {
                    List<String> newList = new ArrayList<>();
                    paths.put(subs[j], newList);
                    newList.add(subs[j + 1]);
                } else {
                    List<String> strings = paths.get(subs[j]);
                    strings.add(subs[j + 1]);
                    strings.sort(String::compareTo);
                    Set<String> withoutDuplicates = new LinkedHashSet<>(strings);
                    paths.put(subs[j], new ArrayList<>(withoutDuplicates));
                }
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        walkTree(paths, stringBuilder, "", 1);
        System.out.println(stringBuilder.toString());
    }

    private static void walkTree(Map<String, List<String>> paths, StringBuilder builder, String next, Integer level) {
        builder.append(String.join("", Collections.nCopies(level, "    ")));
        builder.append(next);
        builder.append("\n");
        for (String sub : findNext(paths, next)) {
            walkTree(paths, builder, sub, ++level);
            --level;
        }
    }

    private static List<String> findNext(Map<String, List<String>> paths, String key) {
        for (Map.Entry<String, List<String>> entry : paths.entrySet()) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return Collections.emptyList();
    }
}

