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
        if (args.length > 2) {
            System.out.println("invalid command: java Main asmFile [scriptFile]");
            System.exit(0);
        }

        HashMap<String, Integer> labels = new HashMap<String, Integer>();
        ArrayList<String> stringInstructions = new ArrayList<String>();
        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        BufferedReader instBr;
        int[] dataMemory = new int[8192];
        int pc = STARTING_ADDRESS;


        HashMap<String, Integer> registers = generateRegisterHashMap();


        findLabels(br, labels, stringInstructions);
        br.close();

        Instruction[] instructions = new Instruction[stringInstructions.size()];
        readInstructions(labels, stringInstructions, instructions);

        if (args.length == 2) {
            instBr = new BufferedReader(new FileReader(args[1]));
            String script;
            while ((script = instBr.readLine()) != null && pc != -1) {
                System.out.printf("mips> %s\n", script);
                pc = runScriptCommand(registers, dataMemory, instructions, script, pc);
            }
            instBr.close();
        }
        else {
            BufferedReader input = new BufferedReader( new InputStreamReader(System.in));
            while (pc != -1) {
                System.out.print("mips> ");
                String script = input.readLine();
                pc = runScriptCommand(registers, dataMemory, instructions, script, pc);
            }
            input.close();
        }
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
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                }
            }
            if (current.length() >= 3 && !found) {
                switch (current.substring(0,3)) {
                    case "and":
                        instructions[i] = new And(stringInstructions.get(i), address);
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "add":
                        instructions[i] = new Add(stringInstructions.get(i), address);
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "sll":
                        instructions[i] = new Sll(stringInstructions.get(i), address);
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "sub":
                        instructions[i] = new Sub(stringInstructions.get(i), address);
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "slt":
                        instructions[i] = new Slt(stringInstructions.get(i), address);
//                        System.out.println(instructions[i].getMachineCode());
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
//                        System.out.println(instructions[i].getMachineCode());
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
//                        System.out.println(instructions[i].getMachineCode());
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
//                        System.out.println(instructions[i].getMachineCode());
                        break;
                }
            }
            if (current.length() >= 2 && !found) {
                switch (current.substring(0,2)) {
                    case "or":
                        instructions[i] = new Or(stringInstructions.get(i),address);
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "lw":
                        instructions[i] = new Lw(stringInstructions.get(i),address);
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "sw":
                        instructions[i] = new Sw(stringInstructions.get(i),address);
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                    case "jr":
                        instructions[i] = new Jr(stringInstructions.get(i),address);
//                        System.out.println(instructions[i].getMachineCode());
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
//                        System.out.println(instructions[i].getMachineCode());
                        found = true;
                        break;
                }
            }

            if (!found) {
                invalidLine(current, address);
            }
        }
    }

    private static int runScriptCommand(HashMap<String, Integer> registers, int[] dataMemory, Instruction[] instructions,
                                        String script, int pc) {
        switch(script) {
            // Show Help
            case "h":
                System.out.println("\nh = show help\n" +
                        "d = dump register state\n" +
                        "s = single step through the program (i.e. execute 1 instruction and stop)\n" +
                        "s num = step through num instructions of the program\n" +
                        "r = run until the program ends\n" +
                        "m num1 num2 = display data memory from location num1 to num2\n" +
                        "c = clear all registers, memory, and the program counter to 0\n" +
                        "q = exit the program\n");
                return pc;
            // Dump Registers
            case "d":
                System.out.printf("\npc = %d\n" +
                        "$0 = %d          $v0 = %d         $v1 = %d         $a0 = %d\n" +
                        "$a1 = %d         $a2 = %d         $a3 = %d         $t0 = %d\n" +
                        "$t1 = %d         $t2 = %d         $t3 = %d         $t4 = %d\n" +
                        "$t5 = %d         $t6 = %d         $t7 = %d         $s0 = %d\n" +
                        "$s1 = %d         $s2 = %d         $s3 = %d         $s4 = %d\n" +
                        "$s5 = %d         $s6 = %d         $s7 = %d         $t8 = %d\n" +
                        "$t9 = %d         $sp = %d         $ra = %d\n\n", pc, registers.get("$0"), registers.get("$v0"),
                        registers.get("$v1"), registers.get("$a0"), registers.get("$a1"), registers.get("$a2"),
                        registers.get("$a3"), registers.get("$t0"), registers.get("$t1"), registers.get("$t2"),
                        registers.get("$t3"), registers.get("$t4"), registers.get("$t5"), registers.get("$t6"),
                        registers.get("$t7"), registers.get("$s0"), registers.get("$s1"), registers.get("$s2"),
                        registers.get("$s3"), registers.get("$s4"), registers.get("$s5"), registers.get("$s6"),
                        registers.get("$s7"), registers.get("$t8"), registers.get("$t9"), registers.get("$sp"),
                        registers.get("$ra"));
                return pc;
            // Single Step through program
            case "s":
                if (pc >= instructions.length) {
                    System.out.println("Invalid script command: No instructions left to run");
                    System.exit(0);
                }
                pc = instructions[pc].run_code(registers, dataMemory, pc);
                System.out.print("        1 instruction(s) executed\n");
                return pc;
            // Run until program ends
            case "r":
                while(pc < instructions.length) {
                    pc = instructions[pc].run_code(registers, dataMemory, pc);
                }
                return pc;
            // Clear all registers, memory, and the program counter to 0
            case "c":
                // Clear registers
                registers.replaceAll((key, value) -> value = 0);
                // Clear memory
                Arrays.fill(dataMemory, 0);
                System.out.println("        Simulator reset\n");
                // Reset program counter
                return STARTING_ADDRESS;
            // Exit the program
            case "q":
                return -1;
            default:
                // Step through num instructions
                if (script.matches("^s [0-9]+$")) {
                    for (int i = 0; i < Integer.parseInt(script.substring(2)); i++) {
                        if (pc >= instructions.length) {
                            System.out.println("Invalid script command: No instructions left to run");
                            System.exit(0);
                        }
                        pc = instructions[pc].run_code(registers, dataMemory, pc);
                    }
                    System.out.printf("        %d instruction(s) executed\n", Integer.parseInt(script.substring(2)));
                    return pc;
                }
                // Display data memory from location num1 to num2
                else if (script.matches("^m [0-9]+ [0-9]+$")) {
                    String[] indices = script.substring(2).split("\s");
                    for (int i = Integer.parseInt(indices[0]); i <= Integer.parseInt(indices[1]); i++) {
                        System.out.printf("\n[%d] = %d", i, dataMemory[i]);
                    }
                    return pc;
                }
                // Invalid script command
                else {
                    System.out.println("Invalid script command");
                    System.exit(0);
                }
        }
        return -1;
    }

    private static HashMap<String, Integer> generateRegisterHashMap() {
        HashMap<String, Integer> registers = new HashMap<String, Integer>();
        registers.put("$0", 0);
        registers.put("$zero", 0);
        registers.put("$v0", 0);
        registers.put("$v1", 0);
        registers.put("$a0", 0);
        registers.put("$a1", 0);
        registers.put("$a2", 0);
        registers.put("$a3", 0);
        registers.put("$t0", 0);
        registers.put("$t1", 0);
        registers.put("$t2", 0);
        registers.put("$t3", 0);
        registers.put("$t4", 0);
        registers.put("$t5", 0);
        registers.put("$t6", 0);
        registers.put("$t7", 0);
        registers.put("$s0", 0);
        registers.put("$s1", 0);
        registers.put("$s2", 0);
        registers.put("$s3", 0);
        registers.put("$s4", 0);
        registers.put("$s5", 0);
        registers.put("$s6", 0);
        registers.put("$s7", 0);
        registers.put("$t8", 0);
        registers.put("$t9", 0);
        registers.put("$sp", 0);
        registers.put("$ra", 0);
        return registers;
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