import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Jal extends Instruction{
    String JAL_OPCODE = "000011";
    public Jal(String code, int address) {
        super(code, address);
        generate_machine_code();
    }
    @Override
    public void generate_machine_code() {
        // The minimum length for this command is 5 (jal l)
        if (code.length() < 5) {
            invalidLine();
        }
        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        // Checks to make sure there were exactly 1 parameter
        if (parts.length != 1) {
            invalidLine();
        }

//        // Checks if the target is in dec or hex and converts it to its binary representation
        int target = 0;
        try {
            if (parts[0].matches("^(0x)[0-9A-F]{1,4}$")) {
                target = Integer.valueOf(parts[0].substring(2), 16);
            }
            else {
                target = Integer.parseInt(parts[0]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }
        machineCode = JAL_OPCODE + " "+ intToBinary(target, 26);
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        // Splits the different parameters of the command
        String[] parts = code.substring(3).trim().split("\s*,\s*");

        // Checks if the target is in dec or hex and converts it to its binary representation
        int target = 0;
        try {
            if (parts[0].matches("^(0x)[0-9A-F]{1,4}$")) {
                target = Integer.valueOf(parts[0].substring(2), 16);
            }
            else {
                target = Integer.parseInt(parts[0]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }

        registers.put("$ra", pc + 1);

        return target;   //how to put target in $ra since that is a label; r31=pc
    }
}
