// /* FineList.java*/
// import java.util.concurrent.locks.Lock;
// import java.util.concurrent.locks.ReentrantLock;
// public class FineList{
//   private Node head;
//   public FineList() {
//     head      = new Node(Integer.MIN_VALUE);
//     head.next = new Node(Integer.MAX_VALUE);
//   }
//   /* Add an element.*/
//   public boolean add(int item) {
//     int key = item;
//     head.lock();
//     Node pred = head;
//     try {
//       Node curr = pred.next;
//       curr.lock();
//       try {
//         while (curr.key < key) {
//           pred.unlock();
//           pred = curr;
//           curr = curr.next;
//           curr.lock();
//         }
//         if (curr.key == key) {
//           return false;
//         }
//         Node newNode = new Node(item);
//         newNode.next = curr;
//         pred.next = newNode;
//         return true;
//       } finally {
//         curr.unlock();
//       }
//     } finally {
//       pred.unlock();
//     }
//   }
//   /* Remove an element.*/
//   public boolean remove(int item) {
//     Node pred = null, curr = null;
//     int key = item;
//     head.lock();
//     try {
//       pred = head;
//       curr = pred.next;
//       curr.lock();
//       try {
//         while (curr.key < key) {
//           pred.unlock();
//           pred = curr;
//           curr = curr.next;
//           curr.lock();
//         }
//         if (curr.key == key) {
//           pred.next = curr.next;
//           return true;
//         }
//         return false;
//       } finally {
//         curr.unlock();
//       }
//     } finally {
//       pred.unlock();
//     }
//   }
//   public boolean contains(int item) {
//     Node last = null, pred = null, curr = null;
//     int key = item;
//     head.lock();
//     try {
//       pred = head;
//       curr = pred.next;
//       curr.lock();
//       try {
//         while (curr.key < key) {
//           pred.unlock();
//           pred = curr;
//           curr = curr.next;
//           curr.lock();
//         }
//         return (curr.key == key);
//       } finally {
//         curr.unlock();
//       }
//     } finally {
//       pred.unlock();
//     }
//   }
//   public void display()
//   {
// 	Node temp=head;
// 	while(temp.next!=null){
// 		System.out.print("\t"+temp.key);
// 		temp=temp.next;
// 	}
//   }
//  /* Node*/
//   private class Node {
//     int key;
//     Node next;
//     Lock lock;
//     Node(int item) {
//       this.key = item;
//       this.lock = new ReentrantLock();
//       this.next=null;
//     }
//     void lock() {lock.lock();}
//     void unlock() {lock.unlock();}
//   }
// }



import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FineList {
    private Node head;
    private Node tail;

    public FineList() {
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next = tail;
        tail.prev = head;
    }

    /* Add an element. */
    public boolean add(int item) {
        int key = item;
        Node pred, curr;
        pred = head;
        pred.lock();
        try {
            curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    return false;
                }
                Node newNode = new Node(item);
                newNode.next = curr;
                newNode.prev = pred;
                curr.prev = newNode;
                pred.next = newNode;
                return true;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    /* Remove an element. */
    public boolean remove(int item) {
        Node pred, curr;
        int key = item;
        pred = head;
        pred.lock();
        try {
            curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                if (curr.key == key) {
                    curr.prev.next = curr.next;
                    curr.next.prev = curr.prev;
                    return true;
                }
                return false;
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    /* Check if the list contains an element. */
    public boolean contains(int item) {
        Node pred, curr;
        int key = item;
        pred = head;
        pred.lock();
        try {
            curr = pred.next;
            curr.lock();
            try {
                while (curr.key < key) {
                    pred.unlock();
                    pred = curr;
                    curr = curr.next;
                    curr.lock();
                }
                return (curr.key == key);
            } finally {
                curr.unlock();
            }
        } finally {
            pred.unlock();
        }
    }

    /* Display the list. */
    public void display() {
        Node temp = head.next;
        while (temp != tail) {
            System.out.print(temp.key + " ");
            temp = temp.next;
        }
        System.out.println();
    }

    /* Node */
    private class Node {
        int key;
        Node next;
        Node prev;
        Lock lock;

        Node(int item) {
            key = item;
            lock = new ReentrantLock();
        }

        void lock() {
            lock.lock();
        }

        void unlock() {
            lock.unlock();
        }
    }
}
