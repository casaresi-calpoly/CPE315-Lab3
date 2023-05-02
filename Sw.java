//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Sw extends Instruction{
    String SW_OPCODE = "101011";
    public Sw(String code, int address) {
        super(code, address);
        generate_machine_code();
    }
    @Override
    public void generate_machine_code() {
        // The 2 registers used in sw
        String rs = null, rt = null;

        // The minimum length for this command is 12 (sw$tt,x($tt))
        if (code.length() < 12 ) {
            invalidLine();
        }
        // Splits the different parameters of the command
        code = code.replaceAll("[(]", ",");
        code = code.replaceAll("[)]", "");
        String[] parts = code.substring(2).trim().split("(\s*,\s*)");

        // Checks to make sure there were exactly 3 parameters
        if (parts.length != 3) {
            invalidLine();
        }

        // Checks that the registers are in the correct locations and have the proper formatting
        // Also sets the register values to their bit form
        if ((rt = registerAddress(parts[0])) == null ||
                (rs = registerAddress(parts[2])) == null) {
            invalidLine();
        }
        // Checks if the offset is in dec or hex and converts it to its binary representation
        int offset = 0;
        try {
            if (parts[1].matches("^(0x)[0-9A-F]{1,4}$")) {
                offset = Integer.valueOf(parts[1].substring(2), 16);
            }
            else {
                offset = Integer.parseInt(parts[1]);
            }
        } catch (Exception NumberFormatException) {
            invalidLine();
        }
        machineCode = SW_OPCODE + " "+ rs + " "+ rt + " "+ intToBinary(offset, 16);
    }
}
