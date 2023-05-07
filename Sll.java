import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Sll extends Instruction {
    String SLL_OPCODE = "000000";
    String FUNCT = "000000";
    String RS = "00000";
    public Sll(String code, int address) {
        super(code, address);
        generate_machine_code();
    }

    // Reads the given MIPs code and converts it to strings of its binary representation (1s and 0s)
    @Override
    public void generate_machine_code() {
        // The 2 registers used in sll
        String rt = null, rd = null;
        int sa = 0;

        // The minimum length for this command is 12 (sll$tt,$tt,1)
        if (code.length() < 12) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }

        // Checks that the registers are in the correct locations and have the proper formatting
        // Also sets the register values to their bit form
        if ((rd = registerAddress(parts[0])) == null  ||
                (rt = registerAddress(parts[1])) == null) {
            invalidLine();
        }

        // Extracts the sa integer, whether in hex or in decimal form, from the parameter
        try {
            if (parts[2].matches("^(0x)[0-9A-F]{1,4}$")) {
                sa = Integer.valueOf(parts[2].substring(2), 16);
            }
            else {
                sa = Integer.parseInt(parts[2]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }
        machineCode = SLL_OPCODE + " "+ RS + " "+ rt + " "+ rd + " "+ intToBinary(sa, 5) + " "+ FUNCT;
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        // The 2 registers used in sll
        int rd = 0, rt = 1;
        int sa = 0;

        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        // Extracts the sa integer, whether in hex or in decimal form, from the parameter
        try {
            if (parts[2].matches("^(0x)[0-9A-F]{1,4}$")) {
                sa = Integer.valueOf(parts[2].substring(2), 16);
            }
            else {
                sa = Integer.parseInt(parts[2]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }
        registers.put(parts[rd], registers.get(parts[rt]) << sa);
        return pc+1;
    }
}