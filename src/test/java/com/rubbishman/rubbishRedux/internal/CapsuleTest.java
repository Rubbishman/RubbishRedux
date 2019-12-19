package com.rubbishman.rubbishRedux.internal;

import io.usethesource.capsule.Map;
import io.usethesource.capsule.core.PersistentTrieMap;
import org.junit.Test;
import org.organicdesign.fp.collections.PersistentHashMap;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class CapsuleTest {
    public static final int ITERATIONS = 1000;

    @Test
    public void capsuleTest() {
        trieInsertThenUpdate();
        hashInsertThenUpdate();
        hashCloneInsertThenUpdate();
        persistentHashInsertThenUpdate();
    }

    private void trieInsertThenUpdate() {
        long thenTime = System.nanoTime();
        Map.Immutable<Integer, Integer> originalTrie = PersistentTrieMap.<Integer, Integer>of();

        Map.Immutable<Integer, Integer> trie = trieInsert(originalTrie);
        assertEquals(ITERATIONS, trie.size());

        trie = trieUpdate(trie);
        assertEquals(ITERATIONS, trie.size());

        assertEquals(0, originalTrie.size());
        System.out.println("Trie performance performance (total): \n    " + (System.nanoTime() - thenTime));
    }

    private Map.Immutable<Integer, Integer> trieInsert(Map.Immutable<Integer, Integer> trie) {
        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            trie = trie.__put(new Integer(i), new Integer(i));
        }

        System.out.println("Trie performance (initialValue): \n    " + (System.nanoTime() - thenTime));
        return trie;
    }

    private Map.Immutable<Integer, Integer> trieUpdate(Map.Immutable<Integer, Integer> trie) {
        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            trie = trie.__put(new Integer(i), new Integer(i+1000));
        }

        System.out.println("Trie performance (updateValue): \n    " + (System.nanoTime() - thenTime));
        return trie;
    }

    private void hashCloneInsertThenUpdate() {
        long thenTime = System.nanoTime();
        HashMap<Integer, Integer> originalHashMap = new HashMap<>();

        HashMap<Integer, Integer> hashMap = hashCloneInsert(originalHashMap);
        assertEquals(ITERATIONS, hashMap.size());

        hashMap = hashCloneUpdate(hashMap);
        assertEquals(ITERATIONS, hashMap.size());

        assertEquals(0, originalHashMap.size());
        System.out.println("Hashmap clone performance performance (total): \n    " + (System.nanoTime() - thenTime));
    }

    private HashMap<Integer, Integer> hashCloneInsert(HashMap<Integer, Integer> hashMap) {
        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            HashMap<Integer, Integer> tempMap = new HashMap<>();
            tempMap.putAll(hashMap);
            tempMap.put(new Integer(i), new Integer(i));
            hashMap = tempMap;
        }

        System.out.println("HashMap clone performance (initial value): \n    " + (System.nanoTime() - thenTime));

        return hashMap;
    }

    private HashMap<Integer, Integer> hashCloneUpdate(HashMap<Integer, Integer> hashMap) {
        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            HashMap<Integer, Integer> tempMap = new HashMap<>();
            tempMap.putAll(hashMap);
            tempMap.put(new Integer(i), new Integer(i + 1000));
            hashMap = tempMap;
        }

        System.out.println("HashMap clone performance (update value): \n    " + (System.nanoTime() - thenTime));

        return hashMap;
    }

    private void hashInsertThenUpdate() {
        long thenTime = System.nanoTime();
        HashMap<Integer, Integer> originalHashMap = new HashMap<>();

        HashMap<Integer, Integer> hashMap = hashInsert(originalHashMap);
        assertEquals(ITERATIONS, hashMap.size());

        hashMap = hashUpdate(hashMap);
        assertEquals(ITERATIONS, hashMap.size());

        assertEquals(ITERATIONS, originalHashMap.size());
        System.out.println("Hashmap performance performance (total): \n    " + (System.nanoTime() - thenTime));
    }

    private HashMap<Integer, Integer> hashInsert(HashMap<Integer, Integer> hashMap) {
        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            hashMap.put(new Integer(i), new Integer(i));
        }

        System.out.println("HashMap performance (initial value): \n    " + (System.nanoTime() - thenTime));

        return hashMap;
    }

    private HashMap<Integer, Integer> hashUpdate(HashMap<Integer, Integer> hashMap) {
        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            hashMap.put(new Integer(i), new Integer(i + 1000));
        }

        System.out.println("HashMap performance (update value): \n    " + (System.nanoTime() - thenTime));

        return hashMap;
    }

    private void persistentHashInsertThenUpdate() {
        long thenTime = System.nanoTime();

        PersistentHashMap<Integer,Integer> originalHashMap = PersistentHashMap.empty();

        PersistentHashMap<Integer,Integer> hashMap = persestentHashMapInsert(originalHashMap);
        assertEquals(ITERATIONS, hashMap.size());

        hashMap = persestentHashMapUpdate(hashMap);
        assertEquals(ITERATIONS, hashMap.size());

        assertEquals(0, originalHashMap.size());

        System.out.println("PresistentHashMap performance performance (total): \n    " + (System.nanoTime() - thenTime));
    }

    private PersistentHashMap<Integer,Integer> persestentHashMapUpdate(PersistentHashMap<Integer,Integer> ourMap) {

        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            ourMap = ourMap.assoc(new Integer(i), new Integer(i+1000));
        }

        System.out.println("PresistentHashMap performance (updateValue): \n    " + (System.nanoTime() - thenTime));

        return ourMap;
    }

    private PersistentHashMap<Integer,Integer> persestentHashMapInsert(PersistentHashMap<Integer,Integer> ourMap) {
        long thenTime = System.nanoTime();

        for(int i = 0; i < ITERATIONS; i++) {
            ourMap = ourMap.assoc(new Integer(i), new Integer(i));
        }

        System.out.println("PresistentHashMap performance (initialValue): \n    " + (System.nanoTime() - thenTime));

        return ourMap;
    }
}
