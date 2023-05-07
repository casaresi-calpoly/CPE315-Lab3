import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class And extends Instruction {
    String AND_OPCODE = "000000";
    String SHAMT = "00000";
    String FUNCT = "100100";
    public And(String code, int address) {
        super(code, address);
        generate_machine_code();
    }

    // Reads the given MIPs code and converts it to strings of its binary representation (1s and 0s)
    @Override
    public void generate_machine_code() {
        // The 3 registers used in and
        String rs = null, rt = null, rd = null;

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

        // Checks that the registers are in the correct locations and have the proper formatting
        // Also sets the register values to their bit form
        if ((rd = registerAddress(parts[0])) == null  ||
                (rs = registerAddress(parts[1])) == null ||
                (rt = registerAddress(parts[2])) == null) {
            invalidLine();
        }

        machineCode = AND_OPCODE + " "+ rs + " "+ rt + " "+ rd + " "+ SHAMT + " "+ FUNCT;
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        // The 3 registers used in and
        int rd = 0, rs = 1, rt = 2;

        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        registers.put(parts[rd], registers.get(parts[rs]) & registers.get(parts[rt])); //bitwise &

        return pc+1;
    }
}