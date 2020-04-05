package com.simulation.reporter;

import java.util.ArrayList;
import java.util.List;

import com.simulation.common.Coordinates;

public class QuadTree {

    private Node root;

    // helper node data type
    public class Node {
        Double x, y;              // x- and y- coordinates
        Node NW, NE, SE, SW;   // four subtrees
        String value;           // associated data

        Node(Double x, Double y, String value) {
            this.x = x;
            this.y = y;
            this.value = value;
        }
    }

    public void insert(Double x, Double y, String value) {
        root = insert(root, x, y, value);
    }

    private Node insert(Node h, Double x, Double y, String value) {
        if (h == null) return new Node(x, y, value);
            //// if (eq(x, h.x) && eq(y, h.y)) h.value = value;  // duplicate
        else if ( less(x, h.x) &&  less(y, h.y)) h.SW = insert(h.SW, x, y, value);
        else if ( less(x, h.x) && !less(y, h.y)) h.NW = insert(h.NW, x, y, value);
        else if (!less(x, h.x) &&  less(y, h.y)) h.SE = insert(h.SE, x, y, value);
        else if (!less(x, h.x) && !less(y, h.y)) h.NE = insert(h.NE, x, y, value);
        return h;
    }

    private boolean less(Double k1, Double k2) { return k1.compareTo(k2) <  0; }

    public List<Coordinates> query(Coordinates point) {
        List<Coordinates> found = new ArrayList<>();
        if (root == null)
            return found;

        queryLeafs(root, point, found);;

        return found;
    }

    private void queryLeafs(Node node, Coordinates point, List<Coordinates> accumulated) {
        if (node == null)
            return;

        if (isLeaf(node)){
            accumulated.add(new Coordinates(node.x, node.y));
            return;
        }

        if (less(point.getLatitude(), node.x) &&  less(point.getLongitude(), node.y)) {
            queryLeafs(node.SW, point, accumulated);
        }

        if (less(point.getLatitude(), node.x) &&  !less(point.getLongitude(), node.y)) {
            queryLeafs(node.NW, point, accumulated);
        }

        if (!less(point.getLatitude(), node.x) &&  less(point.getLongitude(), node.y)) {
            queryLeafs(node.SE, point, accumulated);
        }

        if (!less(point.getLatitude(), node.x) &&  !less(point.getLongitude(), node.y)) {
            queryLeafs(node.NE, point, accumulated);
        }

        accumulated.add(new Coordinates(node.x, node.y));
    }

    public boolean isLeaf(Node node) {
        return node.SW == null && node.NW == null && node.SE == null && node.NE == null;
    }

}
