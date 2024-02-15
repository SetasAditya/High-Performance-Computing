import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

class Node {
    int key;
    int priority;
    Node left, right;

    public Node(int key) {
        this.key = key;
        this.priority = new Random().nextInt();
    }
}

class ConcurrentTreap {
    private ReentrantLock lock = new ReentrantLock();
    private Node root;

    public boolean contains(int key) {
        lock.lock();
        try {
            return contains(root, key);
        } finally {
            lock.unlock();
        }
    }

    private boolean contains(Node node, int key) {
        if (node == null)
            return false;

        if (key == node.key)
            return true;

        if (key < node.key)
            return contains(node.left, key);

        return contains(node.right, key);
    }

    public boolean add(int key) {
        lock.lock();
        try {
            if (contains(root, key)) {
                return false; // Key already exists
            }
            root = add(root, key);
            return true;
        } finally {
            lock.unlock();
        }
    }

    private Node add(Node node, int key) {
        if (node == null)
            return new Node(key);

        if (key < node.key) {
            node.left = add(node.left, key);
            if (node.left.priority > node.priority)
                node = rotateRight(node);
        } else if (key > node.key) {
            node.right = add(node.right, key);
            if (node.right.priority > node.priority)
                node = rotateLeft(node);
        }

        return node;
    }

    public boolean remove(int key) {
        lock.lock();
        try {
            if (!contains(root, key)) {
                return false; 
            }
            root = remove(root, key);
            return true;
        } finally {
            lock.unlock();
        }
    }

    private Node remove(Node node, int key) {
        if (node == null)
            return null;

        if (key < node.key) {
            node.left = remove(node.left, key);
        } else if (key > node.key) {
            node.right = remove(node.right, key);
        } else {
            if (node.left == null)
                return node.right;
            else if (node.right == null)
                return node.left;

            if (node.left.priority > node.right.priority) {
                node = rotateRight(node);
                node.right = remove(node.right, key);
            } else {
                node = rotateLeft(node);
                node.left = remove(node.left, key);
            }
        }

        return node;
    }

    private Node rotateRight(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        return newRoot;
    }

    private Node rotateLeft(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        return newRoot;
    }
}
