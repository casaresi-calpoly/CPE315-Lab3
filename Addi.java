import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Addi extends Instruction{
    String ADDI_OPCODE = "001000";
    public Addi(String code, int address) {
        super(code, address);
        generate_machine_code();
    }

    // Reads the given MIPs code and converts it to strings of its binary representation (1s and 0s)
    @Override
    public void generate_machine_code() {
        // The 2 registers used in addi
        String rs = null, rt = null;

        // The minimum length for this command is 13 (addi$tt,$tt,0)
        if (code.length() < 13) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(4).trim().split("\s*,\s*");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }

        // Checks that the registers are in the correct locations and have the proper formatting
        // Also sets the register values to their bit form
        if ((rt = registerAddress(parts[0])) == null  ||
                (rs = registerAddress(parts[1])) == null) {
            invalidLine();
        }

        // Checks if the immediate is in dec or hex and converts it to its binary representation
        int immediate = 0;
        try {
            if (parts[2].matches("^(0x)[0-9A-F]{1,4}$")) {
                immediate = Integer.valueOf(parts[2].substring(2), 16);
            }
            else {
                immediate = Integer.parseInt(parts[2]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }
        machineCode = ADDI_OPCODE + " "+ rs + " "+ rt + " "+ intToBinary(immediate, 16);
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        // The 2 registers used in addi
        int rs = 0;
        int rt = 1;

        // The minimum length for this command is 13 (addi$tt,$tt,0)
        if (code.length() < 13) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(4).trim().split("\s*,\s*");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }


        // Checks if the immediate is in dec or hex and converts it to its binary representation
        int immediate = 0;
        try {
            if (parts[2].matches("^(0x)[0-9A-F]{1,4}$")) {
                immediate = Integer.valueOf(parts[2].substring(2), 16);
            }
            else {
                immediate = Integer.parseInt(parts[2]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }
        registers.put(parts[rt], registers.get(parts[rs]) + registers.get(parts[immediate]));

        return pc + 1;
    }
}
