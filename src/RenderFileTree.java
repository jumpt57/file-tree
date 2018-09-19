import java.util.*;

import static java.lang.System.out;
import static java.util.Objects.isNull;

class RenderFileTree {

    private static final int START = 0;
    private static final String SPACE = "    ";
    private static final String NEW_LINE = "\n";
    private static final String SPLIT = "/";
    private static final String DELIMITER = "";

    /**
     *     etc
     *         hosts
     *         nginx
     *             conf.d
     *                 website.conf
     *         passwd
     *     home
     *         etc
     *             hosts
     *                 photos
     *                     profile.jpg
     *         michel
     *             cv.pdf
     *             photos
     *                 profile.jpg
     *                 wallpaper.png
     *
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
                "/home/michel",
                "/home/etc/hosts/photos/profile.jpg"
        };

        renderTree(paths);
    }

    private static void renderTree(String[] N) {
        Map<Node, List<Node>> nodes = createNodeTree(N);
        Node firstNode = nodes.entrySet().iterator().next().getKey();
        StringBuilder stringBuilder = new StringBuilder();
        walkTree(nodes, stringBuilder, firstNode, START);
        out.println(stringBuilder.toString());
    }

    private static Map<Node, List<Node>> createNodeTree(String[] paths) {
        Map<Node, List<Node>> nodes = new LinkedHashMap<>();

        for (String path : paths) {
            String[] subs = path.split(SPLIT);
            for (int j = 0; j < subs.length - 1; j++) {

                int next = j + 1;
                Node node = new Node(j, subs[j]);
                Node child = new Node(next, subs[next]);

                if (isNull(nodes.get(node))) {
                    List<Node> children = new ArrayList<>();
                    nodes.put(node, children);
                    children.add(child);
                } else {
                    List<Node> children = nodes.get(node);
                    children.add(child);
                    children.sort(Comparator.comparing(Node::getName));
                    Set<Node> withoutDuplicates = new LinkedHashSet<>(children);
                    nodes.put(node, new ArrayList<>(withoutDuplicates));
                }
            }
        }
        return nodes;
    }

    private static void walkTree(Map<Node, List<Node>> paths, StringBuilder builder, Node next, Integer level) {
        builder.append(String.join(DELIMITER, Collections.nCopies(level, SPACE)));
        builder.append(next.getName());
        builder.append(NEW_LINE);
        for (Node node : findNext(paths, next, level)) {
            walkTree(paths, builder, node, ++level);
            --level;
        }
    }

    private static List<Node> findNext(Map<Node, List<Node>> paths, Node node, Integer level) {
        for (Map.Entry<Node, List<Node>> entry : paths.entrySet()) {
            if (entry.getKey().equals(node)) {
                return entry.getValue();
            }
        }
        return Collections.emptyList();
    }

    static class Node {

        private final Integer level;
        private final String name;

        Node(Integer level, String name) {
            this.level = level;
            this.name = name;
        }

        String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(level, node.level) &&
                    Objects.equals(name, node.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(level, name);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "level=" + level +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}

