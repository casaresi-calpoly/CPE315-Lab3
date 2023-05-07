import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public class Jr extends Instruction{
    String Jr_OPCODE = "000000";
    String SHAMT = "000000000000000";
    String FUNCT= "001000";

    public Jr(String code, int address) {
        super(code, address);
        generate_machine_code();
    }
    @Override
    public void generate_machine_code() {
        // The 1 register used in Jr
        String rs;

        // The minimum length for this command is 5 (jr$ra)
        if (code.length() < 5 ) {
            invalidLine();
        }

        // Checks that the registers are in the correct locations and have the proper formatting
        // Also sets the register values to their bit form
        if ((rs = registerAddress(code.trim().substring(2).trim())) == null) {
            invalidLine();
        }

        machineCode = Jr_OPCODE + " " + rs  + " " +SHAMT + " "+FUNCT ;
    }

    @Override
    public int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc) {
        // The 1 register used in Jr
        int rs=0;

        // Splits the different parameters of the command
        String[] parts = code.substring(2).trim().split("\s*,\s*");

        return registers.get(parts[0]);
    }
}
