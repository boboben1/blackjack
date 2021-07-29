package me.brecher.blackjack.shared.models;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Hand implements Serializable {
    final List<Card> cards;
    int value;

    public Hand() {
        this.value = 0;
        this.cards = Collections.synchronizedList(new ArrayList<>());
    }

    synchronized void recalculate() {
        Stream<Collection<List<Integer>>> cardStream = this.cards.stream()
                .map(Card::getValue)
                .filter(Objects::nonNull)
                .map(c -> Arrays.stream(c).boxed()
                        .map(o -> new ArrayList<>(Collections.singletonList(o)))
                        .collect(Collectors.toList())
                );


        Collection<List<Integer>> combinations = cardStream.reduce((lhs, rhs) -> {
            Collection<List<Integer>> merged = new ArrayList<>();
            lhs.forEach(perm1 -> rhs.forEach(perm2 -> {
                List<Integer> combination = new ArrayList<>();
                combination.addAll(perm1);
                combination.addAll(perm2);
                merged.add(combination);
            }));
            return merged;
        }).orElse(new HashSet<>());

        List<Integer> possibleValues = combinations.stream()
                .map(l -> l.stream()
                        .reduce(0, Integer::sum))
                .sorted((l,r) -> r - l).collect(Collectors.toList());

        this.value = possibleValues.stream().filter(v -> v <= 21)
                .findFirst().or(() -> possibleValues.stream().sorted().findFirst()).orElse(0);
    }


    public synchronized void addCard(Card card) {
        synchronized (this.cards) {
            this.cards.add(card);
            this.recalculate();
        }

    }

    public synchronized boolean hasBlackjack() {
        synchronized (this.cards) {
            return (this.value() == 21 && this.cards.size() == 2);
        }
    }

    public synchronized boolean canDouble() {
        synchronized (this.cards) {
            return this.cards.size() == 2;
        }
    }

    public synchronized int value() {
        return value;
    }

    public synchronized boolean canSplit() {
        synchronized (this.cards) {
            return this.cards.size() == 2 && this.cards.get(0).getValue()[0] == this.cards.get(1).getValue()[0];
        }
    }

    public synchronized int handSize() {
        return this.cards.size();
    }

    public synchronized List<Card> split() {
        return this.cards;
    }

}
