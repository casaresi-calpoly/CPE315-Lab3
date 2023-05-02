//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static int STARTING_ADDRESS = 0;

    public static void main(String[] args) throws IOException {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.

        HashMap<String, Integer> labels = new HashMap<String, Integer>();
        ArrayList<String> stringInstructions = new ArrayList<String>();
        System.out.println(new File(".").getAbsoluteFile());
        BufferedReader br = new BufferedReader(new FileReader(args[0]));

        findLabels(br, labels, stringInstructions);
        br.close();
        Instruction[] instructions = new Instruction[stringInstructions.size()];
        readInstructions(labels, stringInstructions, instructions);


    }

    private static void findLabels(BufferedReader br, HashMap<String, Integer> labels, ArrayList<String> instructions) throws IOException {
        // The string containing the current line being read
        String currentLine = "";
        // The address of the current line being read
        int currentAddress = STARTING_ADDRESS;
        // A hashmap with labels as keys and their addresses as values

        while ((currentLine = br.readLine()) != null) {
            // The index of the first instance of '#'. It equals -1 if there is no such instance
            int commentIndex = currentLine.indexOf("#");

            // Checks to see if the line of code contains a comment and grabs the substring before the '#' if true
            if (commentIndex != -1) {
                currentLine = currentLine.substring(0, commentIndex);
            }

            // Gets rid of any leading or trailing whitespaces
            currentLine = currentLine.trim();

            // Checks if the currentLine is not empty, meaning it contained more than just a comment
            if (currentLine.length() != 0) {
                // The index of the first instance of a ':'. It equals -1 if there is no such instance
                int colonIndex = currentLine.indexOf(':');
                // Prints an error message if it has a colon at the start
                if (colonIndex == 0) {
                    br.close();
                    invalidLine(currentLine, currentAddress);
                }
                // Has a label
                else if (colonIndex > 0) {
                    String parts[] = currentLine.split(":", 2);
                    // Checks if the label is valid and puts it in the hashmap
                    if (parts[0].matches("[a-zA-Z0-9]*")) {
                        labels.put(currentLine.substring(0, colonIndex), currentAddress);
                    }
                    // Prints an error message otherwise
                    else {
                        br.close();
                        invalidLine(currentLine, currentAddress);
                    }
                    // Checks if it has an instruction after the ':' and increases the address if true.
                    if (!parts[1].trim().isEmpty()) {
                        instructions.add(parts[1].trim());
                        currentAddress++;
                    }
                }
                // If it is not a label or just a comment, it is an instruction, so the address is increased
                else {
                    instructions.add(currentLine);
                    currentAddress++;
                }
            }
        }
    }

    //and, or, add, addi, sll, sub, slt, beq, bne, lw, sw, j, jr, and jal.
    private static void readInstructions(HashMap<String, Integer> labels, ArrayList<String> stringInstructions, Instruction[] instructions) {
        int address = STARTING_ADDRESS;

        for (int i = 0; i < stringInstructions.size(); i++) {
            String current = stringInstructions.get(i).trim();
            boolean found = false;
            String l;
            String target;
            address++;
            if (current.length() >= 4 && !found) {
                switch (current.substring(0,4)) {
                    case "addi":
                        instructions[i] = new Addi(stringInstructions.get(i), address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                }
            }
            if (current.length() >= 3 && !found) {
                switch (current.substring(0,3)) {
                    case "and":
                        instructions[i] = new And(stringInstructions.get(i), address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "add":
                        instructions[i] = new Add(stringInstructions.get(i), address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "sll":
                        instructions[i] = new Sll(stringInstructions.get(i), address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "sub":
                        instructions[i] = new Sub(stringInstructions.get(i), address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "slt":
                        instructions[i] = new Slt(stringInstructions.get(i), address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "beq":
                        l = stringInstructions.get(i).trim().substring(stringInstructions.get(i).lastIndexOf(44) + 1)
                                .trim();
                        target = "";
                        if (labels.containsKey(l)) {
                            target = Integer.toString(labels.get(l) - address);
                        }
                        else {
                            invalidLine(stringInstructions.get(i), address);
                        }
                        instructions[i] = new Beq(stringInstructions.get(i).substring(0,
                                stringInstructions.get(i).lastIndexOf(44) + 1) + target, address);
                        found = true;
                        System.out.println(instructions[i].getMachineCode());
                        break;
                    case "bne":
                        l = stringInstructions.get(i).substring(stringInstructions.get(i).lastIndexOf(44) + 1)
                                .trim();
                        target = "";
                        if (labels.containsKey(l)) {
                            target = Integer.toString(labels.get(l) - address);
                        }
                        else {
                            invalidLine(stringInstructions.get(i), address);
                        }
                        instructions[i] = new Bne(stringInstructions.get(i).substring(0,
                                stringInstructions.get(i).lastIndexOf(44) + 1) + target, address);
                        found = true;
                        System.out.println(instructions[i].getMachineCode());
                        break;

                    case "jal":
                        l = stringInstructions.get(i).trim().substring(3).trim();
                        target = "";
                        if (labels.containsKey(l)) {
                            target = Integer.toString(labels.get(l));
                        }
                        else {
                            invalidLine(stringInstructions.get(i), address);
                        }
                        instructions[i] = new Jal(stringInstructions.get(i).substring(0, 3) + " " + target, address);
                        found = true;
                        System.out.println(instructions[i].getMachineCode());
                        break;
                }
            }
            if (current.length() >= 2 && !found) {
                switch (current.substring(0,2)) {
                    case "or":
                        instructions[i] = new Or(stringInstructions.get(i),address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "lw":
                        instructions[i] = new Lw(stringInstructions.get(i),address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "sw":
                        instructions[i] = new Sw(stringInstructions.get(i),address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "jr":
                        instructions[i] = new Jr(stringInstructions.get(i),address);
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                }
            }
            if (current.length() >= 1 && !found) {
                switch (current.charAt(0)) {
                    case 'j':
                        l = stringInstructions.get(i).trim().substring(1).trim();
                        target = "";
                        if (labels.containsKey(l)) {
                            target = Integer.toString(labels.get(l));
                        }
                        else {
                            invalidLine(stringInstructions.get(i), address);
                        }
                        instructions[i] = new J(stringInstructions.get(i).substring(0, 1) + " " + target, address);
                        found = true;
                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                }
            }

            if (!found) {
                invalidLine(current, address);
            }
        }
    }
    public static void invalidLine(String line, int address){

        Pattern pattern = Pattern.compile("^[^\\s\\$]*");
        Matcher matcher = pattern.matcher(line.trim());
        if (matcher.find())
        {
            System.out.printf("invalid instruction: %s\n", matcher.group());
        }
        System.exit(0);
    }

}