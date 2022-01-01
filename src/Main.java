import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String[] instr = new String[] {"MOVS", "100", "#10"};

        switch (instr[0]){
            case "LSLS":
                if (instr.length == 3) LSL_IMMEDIATE(instr[1],instr[2],instr[3]);
                else LSL_REGISTER(instr[1], instr[2]);
                break;
            case "LSRS":
                if (instr.length == 3) LSR_IMMEDIATE(instr[1],instr[2],instr[3]);
                else LSR_REGISTER(instr[1], instr[2]);
                break;
            case "ASRS":
                if  (instr.length == 3) ASR_IMMEDIATE(instr[1],instr[2],instr[3]);
                else ASR_REGISTER(instr[1], instr[2]);
                break;
            case "ADDS":
                if (instr[3].contains("#")) ADD_IMMEDIATE(instr[1],instr[2],instr[3]);
                else ADD_REGISTER(instr[1],instr[2],instr[3]);
                break;
            case "SUBS":
                if (instr[3].contains("#")){
                    SUB_IMMEDIATE(instr[1],instr[2],instr[3]);
                } else {
                    SUB_REGISTER(instr[1],instr[2],instr[3]);
                }
                break;
            case "MOVS":
                MOV_IMMEDIATE(instr[1], instr[2]);
                break;
            case "CMP":
                if (instr[2].contains("#")) CMP_IMMEDIATE(instr[1],instr[2]);
                else CMP_REGISTER(instr[1],instr[2]);
                break;
            case "ANDS":
                AND_REGISTER(instr[1], instr[2]);
                break;
            case "EORS":
                EOR_REGISTER(instr[1], instr[2]);
                break;
            case "ADCS":
                ADC_REGISTER(instr[1], instr[2]);
                break;
            case "SBCS":
                SBC_REGISTER(instr[1], instr[2]);
                break;
            case "RORS":
                ROR_REGISTER(instr[1], instr[2]);
                break;
            case "TST":
                TST_REGISTER(instr[1], instr[2]);
                break;
            case "RSBS":
                RSB_IMMEDIATE(instr[1], instr[2], "#0");
                break;
            case "CMN":
                CMN_REGISTER(instr[1], instr[2]);
                break;
            case "ORRS":
                ORR_REGISTER(instr[1], instr[2]);
                break;
            case "MULS":
                MUL(instr[1], instr[2], instr[3]);
                break;
            case "BICS":
                BIC_REGISTER(instr[1], instr[2]);
                break;
            case "MVNS":
                MVN_REGISTER(instr[1], instr[2]);
                break;
            default:
                break;
        }

    }

    private static String binaryToHex(String binary) {

        int decimal = Integer.parseInt(binary,2);
        StringBuilder hexStr = new StringBuilder(Integer.toString(decimal, 16));

        while(hexStr.length() != 4){
            hexStr.insert(0, "0");
        }

        return hexStr.toString();
    }

    private static String immToBinary(String imm, int bits) {

        String immWithoutHashtag = imm.substring(1);
        int immInt = Integer.parseInt(immWithoutHashtag);

        StringBuilder binStr = new StringBuilder(Integer.toBinaryString(immInt));

        while(binStr.length() != bits){
            binStr.insert(0, "0");
        }

        return binStr.toString();
    }

    // LSL (immediate) : Logical Shift Left
    private static void LSL_IMMEDIATE(String rd, String rm, String imm5) {

        String binary = "00000";
        String imm5Binary = immToBinary(imm5,5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("LSL (Immediate) Binary : " + binary);
        System.out.println("LSL (Immediate) Hex : " + result);
    }

    // LSR (immediate) : Logical Shift Right
    private static void LSR_IMMEDIATE(String rd, String rm, String imm5) {

        String binary = "00001";
        String imm5Binary = immToBinary(imm5,5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("LSR (Immediate) Binary : " + binary);
        System.out.println("LSR (Immediate) Hex : " + result);
    }

    // ASR (immediate) : Arithmetic Shift Right
    private static void ASR_IMMEDIATE(String rd, String rm, String imm5) {

        String binary = "00010";
        String imm5Binary = immToBinary(imm5,5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("ASR (Immediate) Binary : " + binary);
        System.out.println("ASR (Immediate) Hex : " + result);
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
    }

    // SUB (register) : Substract Register
    private static void SUB_REGISTER(String rd, String rn, String rm) {

        String binary = "0001101";
        binary += rm;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("SUB (Register) Binary : " + binary);
        System.out.println("SUB (Register) Hex : " + result);
    }

    // ADD (immediate) : Add 3-bit Immediate
    private static void ADD_IMMEDIATE(String rd, String rn, String imm3) {

        String binary = "0001110";
        String imm3Binary = immToBinary(imm3,3);
        binary += imm3Binary;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("ADD (Immediate) Binary : " + binary);
        System.out.println("ADD (Immediate) Hex : " + result);
    }

    // SUB (immediate) : Substract 3-bit immediate
    private static void SUB_IMMEDIATE(String rd, String rn, String imm3) {

        String binary = "0001111";
        String imm3Binary = immToBinary(imm3,3);
        binary += imm3Binary;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("SUB (Immediate) Binary : " + binary);
        System.out.println("SUB (Immediate) Hex : " + result);
    }

    // MOV (immediate) : Move
    private static void MOV_IMMEDIATE(String rd, String imm8) {

        String binary = "00100";
        binary += rd;
        binary += immToBinary(imm8,8);

        String result = binaryToHex(binary);

        System.out.println("MOV (Immediate) Binary " + binary);
        System.out.println("MOV (Immediate) Hex " + result);
    }

    // CMP (immediate) : Compare
    private static void CMP_IMMEDIATE(String rd, String imm8) {

        String binary = "00101";
        binary += rd;
        binary += immToBinary(imm8,8);

        String result = binaryToHex(binary);

        System.out.println("CMP (Immediate) Binary " + binary);
        System.out.println("CMP (Immediate) Hex " + result);
    }

    // AND (register) : Bitwise AND
    private static void AND_REGISTER(String rdn, String rm){

        String binary = "0100000000";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("AND (Register) Binary " + binary);
        System.out.println("AND (Register) Hex " + result);
    }

    // EOR (register) : Exclusive OR
    private static void EOR_REGISTER(String rdn, String rm){

        String binary = "0100000001";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("EOR (Register) Binary " + binary);
        System.out.println("EOR (Register) Hex " + result);
    }

    // LSL (register) : Logical Shift Left
    private static void LSL_REGISTER(String rdn, String rm){

        String binary = "0100000010";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("LSL (Register) Binary " + binary);
        System.out.println("LSL (Register) Hex " + result);
    }

    // LSR (register) : Logical Shift Right
    private static void LSR_REGISTER(String rdn, String rm){

        String binary = "0100000011";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("LSR (Register) Binary " + binary);
        System.out.println("LSR (Register) Hex " + result);
    }

    // ASR (register) : Arithmetic Shift Right
    private static void ASR_REGISTER(String rdn, String rm){

        String binary = "0100000100";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ASR (Register) Binary " + binary);
        System.out.println("ASR (Register) Hex " + result);
    }

    //ADC (register) : Add with Carry
    private static void ADC_REGISTER(String rdn, String rm){

        String binary = "0100000101";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ADC (Register) Binary " + binary);
        System.out.println("ADC (Register) Hex " + result);
    }

    //SBC (register) : Substract with Carry
    private static void SBC_REGISTER(String rdn, String rm){

        String binary = "0100000110";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("SBC (Register) Binary " + binary);
        System.out.println("SBC (Register) Hex " + result);
    }

    //ROR (register) : Rotate Right
    private static void ROR_REGISTER(String rdn, String rm){

        String binary = "0100000111";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ROR (Register) Binary " + binary);
        System.out.println("ROR (Register) Hex " + result);
    }

    //TST (register) : Set flags on bitwise AND
    private static void TST_REGISTER(String rn, String rm){

        String binary = "0100001000";
        binary += rm;
        binary += rn;

        String result = binaryToHex(binary);

        System.out.println("TST (Register) Binary " + binary);
        System.out.println("TST (Register) Hex " + result);
    }

    //RSB (immediate) : Reverse Substract from 0
    private static void RSB_IMMEDIATE(String rd, String rn, String imm0){

        String binary = "0100001001";
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("RSB (Immediate) Binary " + binary);
        System.out.println("RSB (Immediate) Hex " + result);
    }

    //CMP (register) : Compare Registers
    private static void CMP_REGISTER(String rn, String rm){

        String binary = "0100001010";
        binary += rm;
        binary += rn;

        String result = binaryToHex(binary);

        System.out.println("CMP (Register) Binary " + binary);
        System.out.println("CMP (Register) Hex " + result);
    }

    //CMN (register) : Compare Negative
    private static void CMN_REGISTER(String rn, String rm){

        String binary = "0100001011";
        binary += rm;
        binary += rn;

        String result = binaryToHex(binary);

        System.out.println("CMN (Register) Binary " + binary);
        System.out.println("CMN (Register) Hex " + result);
    }

    //ORR (register) : Logical OR
    private static void ORR_REGISTER(String rdn, String rm){

        String binary = "0100001100";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("ORR (Register) Binary " + binary);
        System.out.println("ORR (Register) Hex " + result);
    }

    //MUL : Multiply Two Registers
    private static void MUL(String rdm1, String rn, String rdm2){

        String binary = "0100001101";
        binary += rn;
        binary += rdm1;

        String result = binaryToHex(binary);

        System.out.println("MUL Binary " + binary);
        System.out.println("MUL Hex " + result);
    }

    //BIC (register) : Bit Clear
    private static void BIC_REGISTER(String rdn, String rm){

        String binary = "0100001110";
        binary += rm;
        binary += rdn;

        String result = binaryToHex(binary);

        System.out.println("BIC (Register) Binary " + binary);
        System.out.println("BIC (Register) Hex " + result);
    }

    //MVN (register) : Bitwise NOT
    private static void MVN_REGISTER(String rd, String rm){

        String binary = "0100001111";
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("MVN (Register) Binary " + binary);
        System.out.println("MVN (Register) Hex " + result);
    }
}
