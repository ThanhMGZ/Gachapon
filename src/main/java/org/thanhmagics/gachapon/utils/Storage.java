package org.thanhmagics.gachapon.utils;

import java.util.ArrayList;
import java.util.List;

public class Storage<X, Y, Z> {

    private final List<X> x = new ArrayList<>();

    private final List<Y> y = new ArrayList<>();

    private final List<Z> z = new ArrayList<>();

    public List<X> key1List() {
        return x;
    }

    public List<Y> key2List() {
        return y;
    }

    public List<Z> valueList() {
        return z;
    }


    public Storage<X, Y, Z> put(X k1,Y k2,Z v) {
        x.add(k1);
        y.add(k2);
        z.add(v);
        return this;
    }


    public Z get(X k1, Y k2) {
        if (x.contains(k1) && y.contains(k2)) {
            int k = getSlot(x,k1);
            if (y.get(k).equals(k2)) {
                return z.get(k);
            }
        }
        return null;
    }

    public int size() {
        return z.size();
    }

    public boolean contains(X k1,Y k2) {
        return x.contains(k1) && y.contains(k2);
    }

    public boolean contains(Z v) {
        return z.contains(v);
    }

    public void remove(X k1,Y k2) {
        if (x.contains(k1) && y.contains(k2)) {
            int k = getSlot(x,k1);
            x.remove(k);
            y.remove(k);
            z.remove(k);
        }
    }

    private Integer getSlot(List<X> list, X g) {
        if (list.contains(g)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(g))
                    return i;
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{");
        for (int i = 0; i < x.size(); i++) {
            stringBuilder.append(x.get(i) + "," + y.get(i)  +"," + z.get(i) + " | ");
        }
        if (x.size() > 0)
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

}
