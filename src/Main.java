import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class Main {
    private static final ArrayList<String> lines = new ArrayList<>();
    private static final StringBuilder hexBuffer = new StringBuilder(); // IO time reducing
    private static final String[] op = new String[]{
            "lsls", "lsrs", "asrs", "add", "sub", "movs", "cmp",
            "ands", "eors", "adcs", "sbsc", "rors", "tst", "rsbs",
            "cmn", "orrs", "muls", "bics", "mvns", "add", "str", "sub"};

    public static void main(String[] args) throws IOException {
        readProgram();
        // lines.forEach(x -> System.out.println(x));
        lines.forEach(x -> packetSwitching(cleanedLine(x)));
        writeResult(hexBuffer.toString());
    }

    private static String[] cleanedLine(String x) {
        String[] tmp = x.strip().split("\\s+");
        tmp[1] = tmp[1].replaceAll(",", "");
        if (tmp.length > 2) {
            tmp[2] = tmp[2]
                    .trim()
                    .replaceAll("\\[sp, ", "")
                    .replaceAll("]", "")
                    // .replaceAll("#", "")
                    .replaceAll(",", "");
        }
        return tmp;
    }

    private static void readProgram() {
        try {
            FileReader reader = new FileReader("code_c/tty.s");
            BufferedReader bufferedReader = new BufferedReader(reader);

            String line; // temp
            while ((line = bufferedReader.readLine()) != null) {
                // Selecting & Filtering ASM lines
                if (Arrays.stream(op).anyMatch(line.trim()::contains) && !(line.contains("."))) {
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
            FileWriter writer = new FileWriter("peterSander.bin", APPEND);
            writer.write(buffer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void packetSwitching(String[] line) {
        System.out.println(line[0] + " " + line[1] + " " + line[2]);
        switch (line[0].toLowerCase(Locale.ROOT)) {
            case "lsls":
                if (line.length == 3) LSL_IMMEDIATE(line[0], line[1], line[2]);
                else LSL_REGISTER(line[1], line[2]);
                break;
            case "lsrs":
                if (line.length == 3) LSR_IMMEDIATE(line[0], line[1], line[2]);
                else LSR_REGISTER(line[1], line[2]);
                break;
            case "asrs":
                if (line.length == 3) ASR_IMMEDIATE(line[0], line[1], line[2]);
                else ASR_REGISTER(line[1], line[2]);
                break;
            case "add":
                if (line[2].contains("#")) ADD_IMMEDIATE(line[0], line[1], line[2]);
                else ADD_REGISTER(line[0], line[1], line[2]);
                break;
            case "sub":
                if (line[2].contains("#")) {
                    SUB_IMMEDIATE(line[0], line[1], line[2]);
                } else {
                    SUB_REGISTER(line[0], line[1], line[2]);
                }
                break;
            case "movs":
                MOV_IMMEDIATE(line[1], line[2]);
                break;
            case "cmp":
                if (line[2].contains("#")) CMP_IMMEDIATE(line[1], line[2]);
                else CMP_REGISTER(line[1], line[2]);
                break;
            case "ands":
                AND_REGISTER(line[1], line[2]);
                break;
            case "eors":
                EOR_REGISTER(line[1], line[2]);
                break;
            case "adcs":
                ADC_REGISTER(line[1], line[2]);
                break;
            case "sbcs":
                SBC_REGISTER(line[1], line[2]);
                break;
            case "rors":
                ROR_REGISTER(line[1], line[2]);
                break;
            case "tst":
                TST_REGISTER(line[1], line[2]);
                break;
            case "rsbs":
                RSB_IMMEDIATE(line[1], line[2], "#0");
                break;
            case "cmn":
                CMN_REGISTER(line[1], line[2]);
                break;
            case "orrs":
                ORR_REGISTER(line[1], line[2]);
                break;
            case "muls":
                MUL(line[0], line[1], line[2]);
                break;
            case "bics":
                BIC_REGISTER(line[1], line[2]);
                break;
            case "mvns":
                MVN_REGISTER(line[1], line[2]);
                break;
            default:
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

    // LSL (immediate) : Logical Shift Left
    private static void LSL_IMMEDIATE(String rd, String rm, String imm5) {

        String binary = "00000";
        String imm5Binary = immToBinary(imm5, 5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("LSL (Immediate) Binary : " + binary);
        System.out.println("LSL (Immediate) Hex : " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // LSR (immediate) : Logical Shift Right
    private static void LSR_IMMEDIATE(String rd, String rm, String imm5) {

        String binary = "00001";
        String imm5Binary = immToBinary(imm5, 5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("LSR (Immediate) Binary : " + binary);
        System.out.println("LSR (Immediate) Hex : " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // ASR (immediate) : Arithmetic Shift Right
    private static void ASR_IMMEDIATE(String rd, String rm, String imm5) {

        String binary = "00010";
        String imm5Binary = immToBinary(imm5, 5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("ASR (Immediate) Binary : " + binary);
        System.out.println("ASR (Immediate) Hex : " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // ADD (register) : Add Register
    private static void ADD_REGISTER(String rd, String rn, String rm) {

        String binary = "0001100";
        binary += rm;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("ADD (Register) Binary : " + binary);
        System.out.println("ADD (Register) Hex : " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // SUB (register) : Subtract Register
    private static void SUB_REGISTER(String rd, String rn, String rm) {

        String binary = "0001101";
        binary += rm;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("SUB (Register) Binary : " + binary);
        System.out.println("SUB (Register) Hex : " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // ADD (immediate) : Add 3-bit Immediate
    private static void ADD_IMMEDIATE(String rd, String rn, String imm3) {

        String binary = "0001110";
        String imm3Binary = immToBinary(imm3, 3);
        binary += imm3Binary;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("ADD (Immediate) Binary : " + binary);
        System.out.println("ADD (Immediate) Hex : " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // SUB (immediate) : Substract 3-bit immediate
    private static void SUB_IMMEDIATE(String rd, String rn, String imm3) {

        String binary = "0001111";
        String imm3Binary = immToBinary(imm3, 3);
        binary += imm3Binary;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("SUB (Immediate) Binary : " + binary);
        System.out.println("SUB (Immediate) Hex : " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // MOV (immediate) : Move
    private static void MOV_IMMEDIATE(String rd, String imm8) {

        String binary = "00100";
        binary += rd;
        binary += immToBinary(imm8, 8);

        String result = binaryToHex(binary);

        System.out.println("MOV (Immediate) Binary " + binary);
        System.out.println("MOV (Immediate) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // CMP (immediate) : Compare
    private static void CMP_IMMEDIATE(String rd, String imm8) {

        String binary = "00101";
        binary += rd;
        binary += immToBinary(imm8, 8);

        String result = binaryToHex(binary);

        System.out.println("CMP (Immediate) Binary " + binary);
        System.out.println("CMP (Immediate) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // AND (register) : Bitwise AND
    private static void AND_REGISTER(String rdn, String rm) {

        String binary = "0100000000";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("AND (Register) Binary " + binary);
        System.out.println("AND (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // EOR (register) : Exclusive OR
    private static void EOR_REGISTER(String rdn, String rm) {

        String binary = "0100000001";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("EOR (Register) Binary " + binary);
        System.out.println("EOR (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // LSL (register) : Logical Shift Left
    private static void LSL_REGISTER(String rdn, String rm) {

        String binary = "0100000010";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("LSL (Register) Binary " + binary);
        System.out.println("LSL (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // LSR (register) : Logical Shift Right
    private static void LSR_REGISTER(String rdn, String rm) {

        String binary = "0100000011";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("LSR (Register) Binary " + binary);
        System.out.println("LSR (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    // ASR (register) : Arithmetic Shift Right
    private static void ASR_REGISTER(String rdn, String rm) {

        String binary = "0100000100";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ASR (Register) Binary " + binary);
        System.out.println("ASR (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //ADC (register) : Add with Carry
    private static void ADC_REGISTER(String rdn, String rm) {

        String binary = "0100000101";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ADC (Register) Binary " + binary);
        System.out.println("ADC (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //SBC (register) : Substract with Carry
    private static void SBC_REGISTER(String rdn, String rm) {

        String binary = "0100000110";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("SBC (Register) Binary " + binary);
        System.out.println("SBC (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //ROR (register) : Rotate Right
    private static void ROR_REGISTER(String rdn, String rm) {

        String binary = "0100000111";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ROR (Register) Binary " + binary);
        System.out.println("ROR (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //TST (register) : Set flags on bitwise AND
    private static void TST_REGISTER(String rn, String rm) {

        String binary = "0100001000";
        binary += rm;
        binary += rn;

        String result = binaryToHex(binary);

        System.out.println("TST (Register) Binary " + binary);
        System.out.println("TST (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //RSB (immediate) : Reverse Subtract from 0
    private static void RSB_IMMEDIATE(String rd, String rn, String imm0) {

        String binary = "0100001001";
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("RSB (Immediate) Binary " + binary);
        System.out.println("RSB (Immediate) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //CMP (register) : Compare Registers
    private static void CMP_REGISTER(String rn, String rm) {

        String binary = "0100001010";
        binary += rm;
        binary += rn;

        String result = binaryToHex(binary);

        System.out.println("CMP (Register) Binary " + binary);
        System.out.println("CMP (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //CMN (register) : Compare Negative
    private static void CMN_REGISTER(String rn, String rm) {

        String binary = "0100001011";
        binary += rm;
        binary += rn;

        String result = binaryToHex(binary);

        System.out.println("CMN (Register) Binary " + binary);
        System.out.println("CMN (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //ORR (register) : Logical OR
    private static void ORR_REGISTER(String rdn, String rm) {

        String binary = "0100001100";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ORR (Register) Binary " + binary);
        System.out.println("ORR (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //MUL : Multiply Two Registers
    private static void MUL(String rdm1, String rn, String rdm2) {

        String binary = "0100001101";
        binary += rn;
        binary += rdm1;

        String result = binaryToHex(binary);

        System.out.println("MUL Binary " + binary);
        System.out.println("MUL Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //BIC (register) : Bit Clear
    private static void BIC_REGISTER(String rdn, String rm) {

        String binary = "0100001110";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("BIC (Register) Binary " + binary);
        System.out.println("BIC (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }

    //MVN (register) : Bitwise NOT
    private static void MVN_REGISTER(String rd, String rm) {

        String binary = "0100001111";
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("MVN (Register) Binary " + binary);
        System.out.println("MVN (Register) Hex " + result);

        hexBuffer.append(result).append(" "); // save result in buffer
    }
}
