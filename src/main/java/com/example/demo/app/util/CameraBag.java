package com.example.demo.app.util;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.example.demo.app.entity.Camera;

import org.springframework.stereotype.Component;

@Component
public class CameraBag {

    final static private ConcurrentHashMap<Integer, Integer> map = new ConcurrentHashMap<>();
    
    public void put(Camera camera) {
        Integer id = camera.getId();
        while (true) {
            int count = map.getOrDefault(id, -1);
            if (count == -1) {
                if (map.putIfAbsent(id, 1) == null)
                    return;
                continue;
            }
            if (map.replace(id, count, count + 1))
                return;
        }
    }

    public void remove(Camera camera) {
        Integer id = camera.getId();
        while (true) {
            int count = map.getOrDefault(id, 0);
            if (count <= 0 || map.replace(id, count, count - 1))
                return;
        }
    }

    public Set<Map.Entry<Integer, Integer>> entrySet() {
        return map.entrySet();
    }
}