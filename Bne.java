import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Bne extends Instruction{
    String BNE_OPCODE = "000101";
    public Bne(String code, int address) {
        super(code, address);
        generate_machine_code();
    }
    @Override
    public void generate_machine_code() {
        // The 2 registers used in Beq
        String rs, rt = null;

        // The minimum length for this command is 12 (bne$tt,$tt,l)
        if (code.length() < 12) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\\s*,\\s*");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }

        // Checks that the registers are in the correct locations and have the proper formatting
        // Also sets the register values to their bit form
        if ((rs = registerAddress(parts[0])) == null ||
                (rt = registerAddress(parts[1])) == null) {
            invalidLine();
        }

        // Checks if the offset is in dec or hex and converts it to its binary representation
        int offset = 0;
        try {
            if (parts[2].matches("^(0x)[0-9A-F]{1,4}$")) {
                offset = Integer.valueOf(parts[2].substring(2), 16);
            }
            else {
                offset = Integer.parseInt(parts[2]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }
        machineCode = BNE_OPCODE + " "+ rs + " "+ rt + " "+ intToBinary(offset, 16);
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        int rs = 0, rt = 1;

        // The minimum length for this command is 14 (and$tt,$tt,$tt)
        if (code.length() < 14) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }
        int offset = 0;
        if (registers.get(parts[0]) !=registers.get(parts[1])) {

            try {
                if (parts[2].matches("^(0x)[0-9A-F]{1,4}$")) {
                    offset = Integer.valueOf(parts[2].substring(2), 16);
                } else {
                    offset = Integer.parseInt(parts[2]);
                }
            } catch (Exception NumberFormatException) {
                invalidLine();
            }
        }
        return offset;
    }
}
