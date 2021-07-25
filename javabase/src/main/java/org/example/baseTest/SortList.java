package org.example.baseTest;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

/**
 * @Author: Jdragon
 * @email: 1061917196@qq.com
 * @Date: 2020.08.25 16:13
 * @Description:
 */
public class SortList {
    public static void main(String[] args) {
        Map<String, List<Integer>> map = new HashMap<>();

        map.put("1", new ArrayList<>(Arrays.asList(1, 2, 4)));
        map.put("2", new ArrayList<>(Arrays.asList(5, 10)));
        map.put("3", new ArrayList<>(Arrays.asList(1, 2, 4, 7, 8)));

        List<String> list = new ArrayList<>(map.keySet());
        list.sort(Comparator.comparingInt(o -> map.get(o).size()));

        LinkedHashMap<String,List<Integer>> linkedHashMap = new LinkedHashMap();
        for (String key : list) {
            linkedHashMap.put(key,map.get(key));
        }

        for (Map.Entry<String, List<Integer>> entry
                : linkedHashMap.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }


        Map<String,List<Integer>> linkedMap = Maps.newLinkedHashMap();
        map.entrySet()
                .stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().size()))
                .forEach(e->linkedMap.put(e.getKey(),e.getValue()));
        for (Map.Entry<String, List<Integer>> entry : linkedMap.entrySet()) {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }
}
