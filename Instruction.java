import java.util.HashMap;

//Lab 2- Rajvir Vyas and Daniel Casares-Iglesias
public abstract class Instruction {
    public String code;
    public int address;
    public String machineCode;

    public int intCode;

    public Instruction(String code, int address) {
        this.code = code;
        this.address = address;
        generate_machine_code();
        intCode = binToInt(machineCode);
    }

    public abstract void generate_machine_code();

    public abstract int run_code(HashMap<String, Integer> registers, int[] dataMemory, int pc);

    // Takes an integer num and converts it to a String representation of its bits with length len.
    public String intToBinary(int num, int len) {
        String binary = "";
        // Checks to make sure the num isn't too large for len
        if (num > Math.pow(2, len) || num < -1 * Math.pow(2, len-1)) {
            invalidLine();
        }
        // Checks if num is negative
        if (num < 0) {
            for (int i = 1; i < Math.pow(2, len-1); i=i<<1) {
                switch (num & i) {
                    case 0:
                        binary = "0" + binary;
                        break;
                    default:
                        binary = "1" + binary;
                        break;
                }
            }
            binary = "1" + binary;
        }
        // Num is positive
        else {
            for (int i = 0; i < len; ++i, num /= 2) {
                switch (num % 2) {
                    case 0:
                        binary = "0" + binary;
                        break;
                    case 1:
                        binary = "1" + binary;
                        break;
                }
            }
        }
        return binary;
    }

    // Takes a String representation of a binary number and converts it to an int
    public int binToInt(String binaryString) {
        return Integer.parseInt(binaryString,2);
    }

    // Prints message and exits the program when given an invalid instruction
    public void invalidLine(){
        System.out.printf("INVALID INSTRUCTION on line %d: %s", address, code);
        System.exit(0);
    }

    // Returns the address of a given register or null if the string is not a valid register.
    public String registerAddress(String register) {
        switch (register) {
            case "$0", "$zero":
                return "00000";
            case "$v0":
                return "00010";
            case "$v1":
                return "00011";
            case "$a0":
                return "00100";
            case "$a1":
                return "00101";
            case "$a2":
                return "00110";
            case "$a3":
                return "00111";
            case "$t0":
                return "01000";
            case "$t1":
                return "01001";
            case "$t2":
                return "01010";
            case "$t3":
                return "01011";
            case "$t4":
                return "01100";
            case "$t5":
                return "01101";
            case "$t6":
                return "01110";
            case "$t7":
                return "01111";
            case "$s0":
                return "10000";
            case "$s1":
                return "10001";
            case "$s2":
                return "10010";
            case "$s3":
                return "10011";
            case "$s4":
                return "10100";
            case "$s5":
                return "10101";
            case "$s6":
                return "10110";
            case "$s7":
                return "10111";
            case "$t8":
                return "11000";
            case "$t9":
                return "11001";
            case "$sp":
                return "11101";
            case "$ra":
                return "11111";
            default:
                return null;
        }
    }

    public String getMachineCode() {
        return machineCode;
    }
}
