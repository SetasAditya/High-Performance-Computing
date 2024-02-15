import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BST {
    private Node root;

    public BST() {
        root = null;
    }

    public boolean contains(int key) {
        Node current = root;

        while (current != null) {
            if (key == current.key) {
                return true;
            } else if (key < current.key) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return false;
    }

    public boolean add(int key) {
        Node newNode = new Node(key);
        if (root == null) {
            root = newNode;
            return true;
        }
        Node parent = null;
        Node current = root;
        while (true) {
            current.lock();
            if (current != root) {
                parent.unlock();
            }
            parent = current;
            if (key < current.key) {
                if (current.left != null) {
                    current = current.left;
                } else {
                    current.left = newNode;
                    current.unlock();
                    return true;
                }
            } else if (key > current.key) {
                if (current.right != null) {
                    current = current.right;
                } else {
                    current.right = newNode;
                    current.unlock();
                    return true;
                }
            } else {
                current.unlock();
                return false; // Duplicate key, do not insert.
            }
        }
    }

    public boolean remove(int key) {
        Node parent = null;
        Node current = root;
        while (current != null) {
            current.lock();
            if (current != root) {
                parent.unlock();
            }
            if (key < current.key) {
                parent = current;
                current = current.left;
            } else if (key > current.key) {
                parent = current;
                current = current.right;
            } else {
                if (current.left == null && current.right == null) {
                    if (parent == null) {
                        root = null;
                    } else if (parent.left == current) {
                        parent.left = null;
                    } else {
                        parent.right = null;
                    }
                } else if (current.left == null) {
                    if (parent == null) {
                        root = current.right;
                    } else if (parent.left == current) {
                        parent.left = current.right;
                    } else {
                        parent.right = current.right;
                    }
                } else if (current.right == null) {
                    if (parent == null) {
                        root = current.left;
                    } else if (parent.left == current) {
                        parent.left = current.left;
                    } else {
                        parent.right = current.left;
                    }
                } else {
                    Node successor = current.right;
                    Node successorParent = current;
                    successor.lock();
                    try {
                        while (successor.left != null) {
                            successorParent = successor;
                            successor = successor.left;
                            successor.lock();
                        }
                        current.key = successor.key;
                        if (successorParent.left == successor) {
                            successorParent.left = successor.right;
                        } else
                        {
                            successorParent.right = successor.right;
                        }
                    } finally {
                        successor.unlock();
                    }
                }
                current.unlock();
                return true;
            }
        }
        if (parent != null) {
            parent.unlock();
        }
        return false;
    }

    private class Node {
        private int key;
        private Node left;
        private Node right;
        private Lock lock;

        public Node(int key) {
            this.key = key;
            this.left = null;
            this.right = null;
            this.lock = new ReentrantLock();
        }

        public void lock() {
            lock.lock();
        }

        public void unlock() {
            lock.unlock();
        }
    }
}
