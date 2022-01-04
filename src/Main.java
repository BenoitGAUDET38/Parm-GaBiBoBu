import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Main {
    private static final ArrayList<String> lines = new ArrayList<>();
    private static final StringBuilder hexBuffer = new StringBuilder("v2.0 raw\n"); // IO time reducing
    private static final String[] op = new String[]{
            "lsls", "lsrs", "asrs", "add", "sub", "movs", "cmp",
            "ands", "eors", "adcs", "sbsc", "rors", "tst", "rsbs",
            "cmn", "orrs", "muls", "bics", "mvns", "add", "str", "sub",
            "LBB0_"};

    public static void main(String[] args) throws IOException {
        readProgram();
        // lines.forEach(x -> System.out.println(Arrays.toString(cleanedLine(x))));
        lines.forEach(x -> packetSwitching(cleanedLine(x)));
        writeResult(hexBuffer.toString());
    }

    private static String[] cleanedLine(String x) {
        String[] tmp = x.strip().split("\\s+");
        tmp[0] = tmp[0].trim();
        if (tmp[0].contains("LBB")) {
            // LABEL = ".LBB0_10:"
            tmp[0] = tmp[0].replaceAll(".LBB0_", "").replaceAll(":", "");
        } else if (tmp[0].equals("b")) {
            // LABEL "b	.LBB0_12"
            tmp[1] = tmp[1].trim().replaceAll(".LBB0_", "");
        } else {
            if (tmp.length > 1) {
                tmp[1] = tmp[1].replaceAll(",", "");
            }
            if (tmp.length > 2) {
                tmp[2] = tmp[2].trim()
                        .replaceAll("\\[sp, ", "")
                        .replaceAll("]", "")
                        .replaceAll(",", "");
            }
            if (tmp.length > 3) {
                tmp[2] = tmp[2].replaceAll("\\[", "");
                tmp[3] = tmp[3].trim()
                        .replaceAll("\\[sp, ", "")
                        .replaceAll("]", "")
                        .replaceAll(",", "");
            }
        }
        System.out.println("Cleaned to " + Arrays.toString(tmp));
        return tmp;
    }

    private static void readProgram() {
        try {
            FileReader reader = new FileReader("code_c/tty.s");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line; // temp
            while ((line = bufferedReader.readLine()) != null) {
                // Selecting & Filtering ASM lines
                if (Arrays.stream(op).anyMatch(line.trim()::contains)) {
                    lines.add(line);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeResult(String buffer) {
        final boolean APPEND = false;
        try {
            FileWriter writer = new FileWriter("result.bin", APPEND);
            writer.write(buffer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void packetSwitching(String[] line) {
        /*
        System.out.print(line[0] + " " + line[1]);
        System.out.println(line.length == 3 ? " " + line[2] : "");
        System.out.println(line.length == 4 ? " " + line[3] : "");
         */

        switch (line[0].toLowerCase(Locale.ROOT)) {
            case "lsls":
                if (line.length == 3) {
                    System.out.println("Call LSL_IMMEDIATE with " + line[0] + " : " + line[1] + " : " + line[2]);
                    LSL_IMMEDIATE(line[0], line[1], line[2]);
                } else {
                    System.out.println("Call LSL_REGISTER with " + line[1] + " : " + line[2]);
                    LSL_REGISTER(line[1], line[2]);
                }
                break;
            case "lsrs":
                if (line.length == 3) {
                    System.out.println("Call LSR_IMMEDIATE with " + line[0] + " : " + line[1] + " : " + line[2]);
                    LSR_IMMEDIATE(line[0], line[1], line[2]);
                } else {
                    System.out.println("Call LSL_REGISTER with " + line[1] + " : " + line[2]);
                    LSR_REGISTER(line[1], line[2]);
                }
                break;
            case "asrs":
                if (line.length == 3) {
                    System.out.println("Call ASR_IMMEDIATE with " + line[0] + " : " + line[1] + " : " + line[2]);
                    ASR_IMMEDIATE(line[0], line[1], line[2]);
                } else {
                    System.out.println("Call ASR_REGISTER with " + line[1] + " : " + line[2]);
                    ASR_REGISTER(line[1], line[2]);
                }
                break;
            case "add":
                if (line[1].contains("sp")){
                    if (line[2].contains("#")) {
                        System.out.println("Call ADD_MINUS_IMMEDIATE with " + line[2]);
                        ADD_MINUS_IMMEDIATE(line[2]);
                    } else {
                        System.out.println("Call ADD_MINUS_IMMEDIATE with " + line[3]);
                        ADD_MINUS_IMMEDIATE(line[3]);
                    }
                }else if (line[2].contains("#")) {
                    System.out.println("Call ADD_IMMEDIATE with " + line[0] + " : " + line[1] + " : " + line[2]);
                    ADD_IMMEDIATE(line[0], line[1], line[2]);

                } else {
                    System.out.println("Call ADD_REGISTER with " + line[1] + " : " + line[2] + " : " + line[3]);
                    ADD_REGISTER(line[1], line[2], line[3]);
                }
                break;
            case "sub":
                if (line[1].contains("sp")) {
                    if (line[2].contains("#")) {
                        System.out.println("Call SUB_MINUS_IMMEDIATE with " + line[2]);
                        SUB_MINUS_IMMEDIATE(line[2]);
                    } else {
                        System.out.println("Call SUB_MINUS_IMMEDIATE with " + line[3]);
                        SUB_MINUS_IMMEDIATE(line[3]);
                    }
                } else if (line[2].contains("#")) {
                    System.out.println("Call SUB_IMMEDIATE with " + line[0] + " : " + line[1] + " : " + line[2]);
                    SUB_IMMEDIATE(line[0], line[1], line[2]);
                } else {
                    System.out.println("Call SUB_REGISTER with " + line[0] + " : " + line[1] + " : " + line[2]);
                    SUB_REGISTER(line[0], line[1], line[2]);
                }
                break;
            case "movs":
                System.out.println("Call MOV_IMMEDIATE with " + line[1] + " : " + line[2]);
                MOV_IMMEDIATE(line[1], line[2]);
                break;
            case "cmp":
                if (line[2].contains("#")) {
                    System.out.println("Call CMP_IMMEDIATE with " + line[1] + " : " + line[2]);
                    CMP_IMMEDIATE(line[1], line[2]);
                } else {
                    System.out.println("Call LSR_IMMEDIATE with " + line[1] + " : " + line[2]);
                    CMP_REGISTER(line[1], line[2]);
                }
                break;
            case "ands":
                System.out.println("Call AND_REGISTER with " + line[1] + " : " + line[2]);
                AND_REGISTER(line[1], line[2]);
                break;
            case "eors":
                System.out.println("Call EOR_REGISTER with " + line[1] + " : " + line[2]);
                EOR_REGISTER(line[1], line[2]);
                break;
            case "adcs":
                System.out.println("Call ADC_REGISTER with " + line[1] + " : " + line[2]);
                ADC_REGISTER(line[1], line[2]);
                break;
            case "sbcs":
                System.out.println("Call SBC_REGISTER with " + line[1] + " : " + line[2]);
                SBC_REGISTER(line[1], line[2]);
                break;
            case "rors":
                System.out.println("Call ROR_REGISTER with " + line[1] + " : " + line[2]);
                ROR_REGISTER(line[1], line[2]);
                break;
            case "tst":
                System.out.println("Call TST_REGISTER with " + line[1] + " : " + line[2]);
                TST_REGISTER(line[1], line[2]);
                break;
            case "rsbs":
                System.out.println("Call RSB_IMMEDIATE with " + line[1] + " : " + line[2]);
                RSB_IMMEDIATE(line[1], line[2], "#0");
                break;
            case "cmn":
                System.out.println("Call CMN_REGISTER with " + line[1] + " : " + line[2]);
                CMN_REGISTER(line[1], line[2]);
                break;
            case "orrs":
                System.out.println("Call ORR_REGISTER with " + line[1] + " : " + line[2]);
                ORR_REGISTER(line[1], line[2]);
                break;
            case "muls":
                System.out.println("Call NUL with " + line[0] + " : " + line[1]);
                MUL(line[0], line[1]);
                break;
            case "bics":
                System.out.println("Call BIC_REGISTER with " + line[1] + " : " + line[2]);
                BIC_REGISTER(line[1], line[2]);
                break;
            case "mvns":
                System.out.println("Call MVN_REGISTER with " + line[1] + " : " + line[2]);
                MVN_REGISTER(line[1], line[2]);
                break;
            case "str":
                System.out.println("Call STR_IMMEDIATE with " + line[1] + " : " + line[3]);
                STR_IMMEDIATE(line[1], line[3]);
                break;
            case "ldr":
                System.out.println("Call LDR_IMMEDIATE with " + line[1] + " : " + line[3]);
                LDR_IMMEDIATE(line[1], line[3]);
            default:
                System.out.println("NON TRAITE DANS LE SWITCH " + line[0]);
                break;
        }
    }

    private static String binaryToHex(String binary) {

        int decimal = Integer.parseInt(binary, 2);
        StringBuilder hexStr = new StringBuilder(Integer.toString(decimal, 16));

        while (hexStr.length() != 4) {
            hexStr.insert(0, "0");
        }

        return hexStr.toString();
    }

    private static String immToBinary(String imm, int bits) {

        String immWithoutHashtag = imm.substring(1);
        int immInt = Integer.parseInt(immWithoutHashtag);

        StringBuilder binStr = new StringBuilder(Integer.toBinaryString(immInt));

        while (binStr.length() != bits) {
            binStr.insert(0, "0");
        }

        return binStr.toString();
    }

    private static String immToBinaryDividedBy4(String imm, int bits) {

        String immWithoutHashtag = imm.substring(1);
        int immInt = Integer.parseInt(immWithoutHashtag);

        int immIntDividedBy4 = immInt / 4;

        return immToBinary("#" + immIntDividedBy4, bits);
    }

    // LSL (immediate) : Logical Shift Left
    private static void LSL_IMMEDIATE(String rd, String rm, String imm5) {
        String binary = "00000";
        binary += immToBinary(imm5, 5);
        binary += immToBinary(rm, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // LSR (immediate) : Logical Shift Right
    private static void LSR_IMMEDIATE(String rd, String rm, String imm5) {
        String binary = "00001";
        binary += immToBinary(imm5, 5);
        binary += immToBinary(rm, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // ASR (immediate) : Arithmetic Shift Right
    private static void ASR_IMMEDIATE(String rd, String rm, String imm5) {
        String binary = "00010";
        binary += immToBinary(imm5, 5);;
        binary += immToBinary(rm, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // ADD (register) : Add Register
    private static void ADD_REGISTER(String rm, String rn, String rd) {
        String binary = "0001100";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rn, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // SUB (register) : Subtract Register
    private static void SUB_REGISTER(String rm, String rn, String rd) {
        String binary = "0001101";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rn, 3);
        binary += immToBinary(rd, 3);

        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // ADD (immediate) : Add 3-bit Immediate
    private static void ADD_IMMEDIATE(String rd, String rn, String imm3) {
        String binary = "0001110";
        binary += immToBinary(imm3, 3);
        binary += immToBinary(rn, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // SUB (immediate) : Subtract 3-bit immediate
    private static void SUB_IMMEDIATE(String rd, String rn, String imm3) {
        String binary = "0001111";
        binary += immToBinary(imm3, 3);
        binary += immToBinary(rn, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // MOV (immediate) : Move
    private static void MOV_IMMEDIATE(String rd, String imm8) {
        String binary = "00100";
        binary += immToBinary(rd, 3);
        binary += immToBinary(imm8, 8);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // CMP (immediate) : Compare
    private static void CMP_IMMEDIATE(String rd, String imm8) {
        String binary = "00101";
        binary += immToBinary(rd, 3);
        binary += immToBinary(imm8, 8);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // AND (register) : Bitwise AND
    private static void AND_REGISTER(String rdn, String rm) {
        String binary = "0100000000";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // EOR (register) : Exclusive OR
    private static void EOR_REGISTER(String rdn, String rm) {
        String binary = "0100000001";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // LSL (register) : Logical Shift Left
    private static void LSL_REGISTER(String rdn, String rm) {
        String binary = "0100000010";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // LSR (register) : Logical Shift Right
    private static void LSR_REGISTER(String rdn, String rm) {
        String binary = "0100000011";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    // ASR (register) : Arithmetic Shift Right
    private static void ASR_REGISTER(String rdn, String rm) {
        String binary = "0100000100";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //ADC (register) : Add with Carry
    private static void ADC_REGISTER(String rdn, String rm) {
        String binary = "0100000101";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binaryToHex(binary))).append(" "); // save result in buffer
    }

    //SBC (register) : Substract with Carry
    private static void SBC_REGISTER(String rdn, String rm) {
        String binary = "0100000110";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //ROR (register) : Rotate Right
    private static void ROR_REGISTER(String rdn, String rm) {
        String binary = "0100000111";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //TST (register) : Set flags on bitwise AND
    private static void TST_REGISTER(String rn, String rm) {
        String binary = "0100001000";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //RSB (immediate) : Reverse Subtract from 0
    private static void RSB_IMMEDIATE(String rd, String rn, String imm0) {
        String binary = "0100001001";
        binary += immToBinary(rn, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //CMP (register) : Compare Registers
    private static void CMP_REGISTER(String rn, String rm) {
        String binary = "0100001010";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //CMN (register) : Compare Negative
    private static void CMN_REGISTER(String rn, String rm) {
        String binary = "0100001011";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //ORR (register) : Logical OR
    private static void ORR_REGISTER(String rdn, String rm) {
        String binary = "0100001100";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //MUL : Multiply Two Registers
    private static void MUL(String rdm, String rn) {
        String binary = "0100001101";
        binary += immToBinary(rn, 3);
        binary += immToBinary(rdm, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //BIC (register) : Bit Clear
    private static void BIC_REGISTER(String rdn, String rm) {
        String binary = "0100001110";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rdn, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //MVN (register) : Bitwise NOT
    private static void MVN_REGISTER(String rd, String rm) {
        String binary = "0100001111";
        binary += immToBinary(rm, 3);
        binary += immToBinary(rd, 3);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //SUB (SP minus immediate) : Subtract Immediate from SP
    private static void SUB_MINUS_IMMEDIATE(String offset) {
        String binary = "101100001";
        binary += immToBinaryDividedBy4(offset, 7);;
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    //ADD (SP minus immediate) : Add Immediate from SP
    private static void ADD_MINUS_IMMEDIATE(String offset) {
        String binary = "101100000";
        binary += immToBinaryDividedBy4(offset, 7);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    private static void LDR_IMMEDIATE(String rt, String imm8) {
        String binary = "10011";
        binary += immToBinary(rt, 3);
        binary += immToBinaryDividedBy4(imm8, 8);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }

    private static void STR_IMMEDIATE(String rt, String imm8) {
        String binary = "10010";
        binary += immToBinary(rt, 3);
        binary += immToBinaryDividedBy4(imm8, 8);
        hexBuffer.append(binaryToHex(binary)).append(" "); // save result in buffer
    }
}
