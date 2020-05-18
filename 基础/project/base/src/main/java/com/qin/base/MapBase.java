package com.qin.base;

import java.util.*;

public class MapBase<E> {

    transient int size = 0;

    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
     *            (first.prev == null && first.item != null)
     */
    transient Node<E> first;

    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     *            (last.next == null && last.item != null)
     */
    transient Node<E> last;


    private void linkFirst(E e) {
        // 原第一个
        final Node<E> f = first;

        final Node<E> newNode = new Node<>(null, e, f);
        // 新的作为第一个
        first = newNode;

        if (f == null) {
            // 第一次 第一个 为空 last 就是本事 形成一个闭环
            last = newNode;
        } else {
            // 否则就是之前的first
            f.prev = newNode;
        }
        size++;
    }



    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }

}

