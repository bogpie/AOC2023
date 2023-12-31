package D07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class D07 {
    public D07(String[] args) {
        main(args);
    }

    enum Card {
        card_A, card_K, card_Q, card_J, card_T, card_9, card_8, card_7, card_6, card_5, card_4, card_3, card_2;

        public static Card valueOf(char c) {

            return switch (c) {
                case 'A' -> card_A;
                case 'K' -> card_K;
                case 'Q' -> card_Q;
                case 'J' -> card_J;
                case 'T' -> card_T;
                case '9' -> card_9;
                case '8' -> card_8;
                case '7' -> card_7;
                case '6' -> card_6;
                case '5' -> card_5;
                case '4' -> card_4;
                case '3' -> card_3;
                case '2' -> card_2;
                default -> null;
            };
        }

        @Override
        public String toString() {
            return switch (this) {
                case card_A -> "A";
                case card_K -> "K";
                case card_Q -> "Q";
                case card_J -> "J";
                case card_T -> "T";
                case card_9 -> "9";
                case card_8 -> "8";
                case card_7 -> "7";
                case card_6 -> "6";
                case card_5 -> "5";
                case card_4 -> "4";
                case card_3 -> "3";
                case card_2 -> "2";
            };
        }
    }

    enum Type {
        FIVE_OF_A_KIND, FOUR_OF_A_KIND, FULL_HOUSE, THREE_OF_A_KIND, TWO_PAIRS, ONE_PAIR, HIGH_CARD;

        @Override
        public String toString() {
            return switch (this) {
                case FIVE_OF_A_KIND -> "FIVE_OF_A_KIND";
                case FOUR_OF_A_KIND -> "FOUR_OF_A_KIND";
                case FULL_HOUSE -> "FULL_HOUSE";
                case THREE_OF_A_KIND -> "THREE_OF_A_KIND";
                case TWO_PAIRS -> "TWO_PAIRS";
                case ONE_PAIR -> "ONE_PAIR";
                case HIGH_CARD -> "HIGH_CARD";
            };
        }
    }

    static class Hand {
        ArrayList<Card> cards = new ArrayList<>();

        public Hand(String string) {
            for (int i = 0; i < string.length(); i++) {
                cards.add(Card.valueOf(string.charAt(i)));
            }
        }

        public Hand getReplace() {
            String string = cards.toString();
            Hand replace = this;

            // If there is no J, we don't need to replace anything
            if (!string.contains("J")) {
                return replace;
            }

            var possible = new ArrayList<Hand>();

            // For each possible card to replace the J
            for (Card card : Card.values()) {
                // Create a new hand with the replaced card
                var newString = string
                        .replace("J", card.toString())
                        .replace("[", "")
                        .replace("]", "")
                        .replace(" ", "")
                        .replace(",", "");
                Hand newHand = new Hand(newString);
                // Add it to the list of possible hands
                possible.add(newHand);
            }

            possible.sort(handOrderPartOne);
            replace = possible.get(0);

            return replace;
        }

        public Type getType() {
            List<Card> distinct = cards.stream().distinct().toList();

            // AAAAA
            if (distinct.size() == 1) {
                return Type.FIVE_OF_A_KIND;
            }

            // AAAA8, 33322
            if (distinct.size() == 2) {
                HashMap<Card, Integer> dict = new HashMap<>();
                for (Card card : cards) {
                    if (dict.containsKey(card)) {
                        dict.put(card, dict.get(card) + 1);
                    } else {
                        dict.put(card, 1);
                    }
                }
                if (dict.containsValue(4)) {
                    return Type.FOUR_OF_A_KIND;
                }

                return Type.FULL_HOUSE;
            }

            // TTT98, 43322
            if (distinct.size() == 3) {
                HashMap<Card, Integer> dict = new HashMap<>();
                for (Card card : cards) {
                    if (dict.containsKey(card)) {
                        dict.put(card, dict.get(card) + 1);
                    } else {
                        dict.put(card, 1);
                    }
                }

                if (dict.containsValue(3)) {
                    return Type.THREE_OF_A_KIND;
                }

                if (dict.containsValue(2)) {
                    return Type.TWO_PAIRS;
                }
            }

            if (distinct.size() == 4) {
                return Type.ONE_PAIR;
            }

            return Type.HIGH_CARD;
        }

        @Override
        public int hashCode() {
            return cards.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Hand hand)) {
                return false;
            }

            return cards.equals(hand.cards);
        }

        @Override
        public String toString() {
            return cards.toString();
        }
    }

    static Comparator<Hand> handOrderPartOne = (hand, other) -> {
        // Check for type
        Type type = hand.getType();
        Type otherType = other.getType();

        if (type.compareTo(otherType) != 0) {
            return hand.getType().compareTo(other.getType());
        }

        // Check for high card instead
        for (int i = 0; i < hand.cards.size(); i++) {
            try {
                if (hand.cards.get(i).compareTo(other.cards.get(i)) != 0) {
                    return hand.cards.get(i).compareTo(other.cards.get(i));
                }
            } catch (Exception e) {
                return 0;
            }

        }

        return 0;
    };

    static Comparator<Card> cardOrderPartTwo = (card, other) -> {
        if (card == other) {
            return 0;
        }

        // "J" is the lowest card
        if (card == Card.card_J) {
            return 1;
        }

        if (other == Card.card_J) {
            return -1;
        }

        return card.compareTo(other);
    };

    static Comparator<Hand> handOrderPartTwo = (hand, other) -> {
        var replace = hand.getReplace();
        var otherReplace = other.getReplace();
        var replaceType = replace.getType();
        var otherReplaceType = otherReplace.getType();

        if (replaceType.compareTo(otherReplaceType) != 0) {
            return replace.getType().compareTo(otherReplace.getType());
        }

        // Check for high card instead
        for (int i = 0; i < hand.cards.size(); i++) {
            Card card = hand.cards.get(i);
            Card otherCard = other.cards.get(i);
            if (cardOrderPartTwo.compare(
                    card,
                    otherCard
            ) != 0) {
                return cardOrderPartTwo.compare(
                        card,
                        otherCard
                );
            }
        }

        return 0;
    };

    static class Play {
        Hand hand;
        long bid;

        public Play(Hand hand, long bid) {
            this.hand = hand;
            this.bid = bid;
        }

        @Override
        public String toString() {
            return hand + " " + bid;
        }

    }

    private static void getLines(ArrayList<String> lines) {
        try {
            File myObj = new File("src/D07/input.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                lines.add(line);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void parse(ArrayList<String> lines, ArrayList<Play> plays) {
        for (String line : lines) {
            Hand hand = new Hand(line.substring(0, 5));
            long bid = Long.parseLong(line.substring(6));
            Play play = new Play(hand, bid);
            plays.add(play);
        }
    }

    private void main(String[] args) {
        ArrayList<String> lines = new ArrayList<>();
        getLines(lines);

        ArrayList<Play> plays = new ArrayList<>();

        parse(lines, plays);

        partOne(plays);
        partTwo(plays);
    }

    private void partTwo(ArrayList<Play> plays) {
        plays.sort(
                (play, other) -> handOrderPartTwo.compare(play.hand, other.hand)
        );

        long sum = 0;
        int i = 0;
        for (Play play : plays) {
            System.out.println("Play: " + play
                    + " Rank: " + (plays.size() - i)
                    + " Replace: " + play.hand.getReplace()
                    + " Type: " + play.hand.getReplace().getType()
            );

            int rank = plays.size() - i++;
            sum += play.bid * rank;
        }

        System.out.println(sum);
    }

    private static void partOne(ArrayList<Play> plays) {
        plays.sort(
                (play, other) -> handOrderPartOne.compare(play.hand, other.hand)
        );

        long sum = 0;
        int i = 0;
        for (Play play : plays) {
            int rank = plays.size() - i++;
            sum += play.bid * rank;
        }

        System.out.println(sum);
    }
}
