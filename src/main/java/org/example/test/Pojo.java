package org.example.test;

import java.util.HashMap;
import java.util.Map;

public final class Pojo {
        private String value;
        private Integer length;

        public String getValue(){
            return value;
        }

        public void setValue(String value){
            this.value = value;
        }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pojo pojo = (Pojo) o;

        return value != null ? value.equals(pojo.value) : pojo.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    public static void main(String[] args){
            Map<Pojo, Integer> map = new HashMap();
            Pojo key = new Pojo();
            key.setValue("abc");
            map.put(key, 1);
            key.setLength(3);
            Integer result = map.get(key);
            System.out.println(result);
        }
    }
