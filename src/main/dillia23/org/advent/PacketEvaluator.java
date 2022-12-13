package org.advent;

import org.advent.utils.FileFinder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

public class PacketEvaluator {
    private static final String PACKETS_FILE_LOC = "/packets.txt";

    private final int correctPacketSum;

    public PacketEvaluator() {
        final Scanner scanner;
        try  {
            scanner = FileFinder.getScannerForFileName(PACKETS_FILE_LOC);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        correctPacketSum = evaluatePackets(scanner);
    }

    public int getCorrectPacketSum() {
        return correctPacketSum;
    }

    private int evaluatePackets(final Scanner scanner) {
        int index = 0;
        int sum = 0;
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            if (line.isBlank()) {
                continue;
            } else if (!scanner.hasNextLine()) {
                throw new IllegalArgumentException("packets should come in pairs");
            } else {
                final String right = scanner.nextLine();
                if (right.isBlank()) {
                    throw new IllegalArgumentException("packets should come in pairs");
                }
                index++;
                final Packet leftPacket = parsePacket(line);
                final Packet rightPacket = parsePacket(right);
                if (compare(leftPacket, rightPacket) == 1) {
                    sum += index;
                }
            }
        }

        return sum;
    }

    private static Packet parsePacket(final String s) {
        Packet packet = new Packet();
        int index = 1;
        while(index < s.length()) {
            //sublist
            if (s.charAt(index) == '[') {
                //find end of sublist by comparing bracket depth
                int packetDepth = 1;
                int endIndex = index + 1;
                while (packetDepth > 0) {
                    if (s.charAt(endIndex) == ']')
                        packetDepth--;
                    else if (s.charAt(endIndex) == '[')
                        packetDepth++;
                    endIndex++;
                }
                //break out sublist and recursively parse, then skip over to end of sublist
                packet.subpackets.add(parsePacket(s.substring(index, endIndex)));
                index = endIndex;
            } else {
                //break out number and add as sub-packet
                int endIndex = s.indexOf(",",index + 1);
                if(endIndex == -1)
                    endIndex = s.indexOf("]",index);
                String num = s.substring(index, endIndex);
                Packet number = new Packet();
                try {
                    number.value = Integer.parseInt(num);
                    packet.subpackets.add(number);
                } catch(NumberFormatException e) {
                    //empty packet - just don't add as a sublist
                }
                index = endIndex;
            }
            index++;
        }
        return packet;
    }

    private static Packet createPackets(final String input) {
        final Packet head = new Packet();
        Packet curr = head;
        final Stack<Packet> parents = new Stack<>();
        for (int i = 0; i < input.length(); i++) {
            final char c = input.charAt(i);

            if (c == '[') {
                final Packet newPacket = new Packet();
                curr.subpackets.add(newPacket);
                parents.push(curr);
                curr = newPacket;
            } else if (c == ']') {
                curr = parents.pop();
            } else if (Character.isDigit(c)) {
                int start = i;
                while (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
                    i++;
                }
//                curr.values.add(Integer.parseInt(input.substring(start, i + 1)));
                final Packet newPacket = new Packet();
                newPacket.value = Integer.parseInt(input.substring(start, i + 1));
                curr.subpackets.add(newPacket);
                parents.push(curr);
                curr = newPacket;
            }
        }

        return head;
    }

    private static boolean arePacketsInRightOrder(final Packet left, final Packet right) {
        final Queue<PacketLevel> ql = new LinkedList<>();
        final Queue<PacketLevel> qr = new LinkedList<>();
        ql.add(new PacketLevel(left, 0));
        qr.add(new PacketLevel(right, 0));

        while (!ql.isEmpty() && !qr.isEmpty()) {
            final PacketLevel l = ql.remove();
            final PacketLevel r = qr.remove();
            final Packet currL = l.packet;
            final Packet currR = r.packet;

            if (currL.subpackets.size() > currR.subpackets.size()) {
                return false;
            }

            if (currL.values.size() == 1 || currR.values.size() == 1) {
                if (!currL.values.isEmpty() && !currR.values.isEmpty() && currL.values.get(0) > currR.values.get(0)) {
                    return false;
                }
            } else if (currL.values.size() > currR.values.size()) {
//                return false;
            } else {
                for (int i = 0; i < currL.values.size(); i++) {
                    if (currL.values.get(i) > currR.values.get(i)) {
                        return false;
                    }
                }
            }

            for (int i = 0; i < currR.subpackets.size(); i++) {
                if (i < currL.subpackets.size()) {
                    ql.add(new PacketLevel(currL.subpackets.get(i), l.level + 1));
                }

                qr.add(new PacketLevel(currR.subpackets.get(i), l.level + 1));
            }
        }

        return true;
    }

    //returns 1 if the packets are in the correct order, -1 if they are not, and
    public static int compare(Packet left, Packet right) {
        int compareIndex = 0;
        while(compareIndex < left.subpackets.size() || compareIndex < right.subpackets.size()) {
            //handle if either list runs out of values
            if(left.subpackets.size() <= compareIndex)
                return 1;
            if(right.subpackets.size() <= compareIndex)
                return -1;

            Packet leftCur = left.subpackets.get(compareIndex);
            Packet rightCur = right.subpackets.get(compareIndex);

            //handle conversion for list on the left, integer on the right
            if(leftCur.subpackets.size() > 0 && rightCur.value != -1) {
                Packet mask = new Packet();
                Packet sub = new Packet();
                sub.value = rightCur.value;
                mask.subpackets.add(sub);
                int compare = compare(leftCur,mask);
                //we only care if this result was "definitive" - if not, we need to go over the rest of the parent list
                if(compare != 0)
                    return compare;
                else {
                    compareIndex++;
                    continue;
                }
            }

            //handle conversion for integer on the left, list on the right
            if(leftCur.value != -1 && rightCur.subpackets.size() > 0) {
                Packet mask = new Packet();
                Packet sub = new Packet();
                sub.value = leftCur.value;
                mask.subpackets.add(sub);
                int compare = compare(mask,rightCur);
                //we only care if this result was "definitive" - if not, we need to go over the rest of the parent list
                if(compare != 0)
                    return compare;
                else {
                    compareIndex++;
                    continue;
                }
            }

            //get "values"
            int leftVal = left.subpackets.get(compareIndex).value;
            int rightVal = right.subpackets.get(compareIndex).value;

            //if both have a value of -1, both are lists rather than single integers - do comparison of the subpacketss
            if(leftVal == -1 && rightVal == -1) {
                //only care about definitive result
                int compare = compare(left.subpackets.get(compareIndex),right.subpackets.get(compareIndex));
                if(compare != 0)
                    return compare;
                else {
                    compareIndex++;
                    continue;
                }
            }

            //finally, simple integer comparison
            if(leftVal < rightVal)
                return 1;
            if(leftVal > rightVal)
                return -1;

            //if equal, move onto next index of loop
            compareIndex++;
        }
        //if nothing returned yet, test was inconclusive
        return 0;
    }

    // todo come back to this; I feel like something is just off
    // w/ the comparison and not moving the index properly
    private static boolean arePacketsInRightOrder(final String left, final String right) {
        final String leftList = wrapAllNums(left);
        final String rightList = wrapAllNums(right);
        int leftBrace = 0;
        int rightBrace = 0;
        Integer leftVal= null;
        Integer rightVal = null;
        boolean correct = true;
        int il = 0;
        int ir = 0;
        // maybe assume there are no solo ints. everything is list
        while (correct && il < leftList.length() && ir < rightList.length()) {
            final char l = leftList.charAt(il);
            final char r = rightList.charAt(ir);

            if (l == '[') {
                leftBrace++;
            }
            if (l == ']') {
                leftBrace--;
            }
            if (r == '[') {
                rightBrace++;
            }
            if (r == ']') {
                rightBrace--;
            }
            if (Character.isDigit(l)) {
                leftVal = Integer.parseInt(String.valueOf(l));
            }
            if (Character.isDigit(r)) {
                rightVal = Integer.parseInt(String.valueOf(r));
            }

            if (rightBrace < leftBrace)
                correct = false;

            if (leftVal != null && rightVal != null && leftVal > rightVal)
                correct = false;

            il++;
            ir++;
        }

        return correct;
    }

    private static String wrapAllNums(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (Character.isDigit(s.charAt(i)) && isNotWrapped(s, i)) {
                sb.append('[');
                int start = i;
                while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    i++;
                }
                sb.append(s.substring(start, i + 1));
                sb.append(']');
            } else if (Character.isDigit(s.charAt(i))) {
                int start = i;
                while (i + 1 < s.length() && Character.isDigit(s.charAt(i + 1))) {
                    i++;
                }
                sb.append(s.substring(start, i + 1));
            } else {
                sb.append(s.charAt(i));
            }
        }

        return sb.toString();
    }

    private static boolean isNotWrapped(final String s, final int index) {
        return index - 1 > 0 && index + 1 < s.length()
                && (s.charAt(index - 1) != '[' || s.charAt(index + 1) != ']');
    }

    record PacketLevel(Packet packet, int level){}
    static class Packet {
        final List<Packet> subpackets;
        final List<Integer> values;
        Integer value;
        public Packet() {
            this.subpackets = new ArrayList<>();
            this.values = new ArrayList<>();
            value = -1;
        }
    }
}
