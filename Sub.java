import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Sub extends Instruction {
    String ADD_OPCODE = "000000";
    String SHAMT = "00000";
    String FUNCT = "100010";
    public Sub(String code, int address) {
        super(code, address);
        generate_machine_code();
    }

    // Reads the given MIPs code and converts it to strings of its binary representation (1s and 0s)
    @Override
    public void generate_machine_code() {
        // The 3 registers used in sub
        String rs = null, rt = null, rd = null;

        // The minimum length for this command is 14 (sub$tt,$tt,$tt)
        if (code.length() < 14) {
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
                (rs = registerAddress(parts[1])) == null ||
                (rt = registerAddress(parts[2])) == null) {
            invalidLine();
        }

        machineCode = ADD_OPCODE + " "+ rs + " "+ rt + " "+ rd + " "+ SHAMT + " "+ FUNCT;
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        // The 3 registers used in sub
        int rs = 0, rt = 1, rd = 2;

        // The minimum length for this command is 14 (sub$tt,$tt,$tt)
        if (code.length() < 14) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }
        registers.put(parts[rd], registers.get(parts[rs]) - registers.get(parts[rt]));
        return pc+1;
    }
}