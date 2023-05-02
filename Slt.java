import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Slt extends Instruction{
    String SLT_OPCODE = "000000";
    String SHAMT = "00000";
    String FUNCT = "101010";
    public Slt(String code, int address) {
        super(code, address);
        generate_machine_code();
    }
    @Override
    public void generate_machine_code() {
        // The 3 registers used in Slt
        String rs = null, rt = null, rd = null;

        // The minimum length for this command is 14 (slt$tt,$tt,$tt)
        if (code.length() < 14 ) {
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

        machineCode = SLT_OPCODE + " "+ rs + " "+ rt + " "+ rd + " "+ SHAMT + " "+ FUNCT;
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        // The 3 registers used in Slt
        int rs = 0, rt = 1, rd = 2;

        // The minimum length for this command is 14 (slt$tt,$tt,$tt)
        if (code.length() < 14 ) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }

        registers.put(parts[rd], booleanToInt(registers.get(parts[rs]) < registers.get(parts[rt])));
        return pc+1;
    }
    public static int booleanToInt(boolean value) {
        // T is 1, F is 0
        return value ? 1 : 0;
    }

}
