import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String[] instr = new String[] {"MOVS", "100", "#10"};

        switch (instr[0]){
            case "LSLS":
                LSL(instr[1],instr[2],instr[3]);
                break;
            case "LSRS":
                LSR(instr[1],instr[2],instr[3]);
                break;
            case "ASRS":
                ASR(instr[1],instr[2],instr[3]);
                break;
            case "ADDS":
                if (instr[3].contains("#")){
                    ADD_IMMEDIATE(instr[1],instr[2],instr[3]);
                } else {
                    ADD_REGISTER(instr[1],instr[2],instr[3]);
                }
                break;
            case "SUBS":
                if (instr[3].contains("#")){
                    SUB_IMMEDIATE(instr[1],instr[2],instr[3]);
                } else {
                    SUB_REGISTER(instr[1],instr[2],instr[3]);
                }
            case "MOVS":
                MOV(instr[1], instr[2]);
            case "CMP":
                CMP(instr[1],instr[2]);
            default:
                break;
        }

    }

    private static void CMP(String rd, String imm8) {

        String binary = "00101";
        binary += rd;
        binary += immToBinary(imm8,8);

        String result = binaryToHex(binary);

        System.out.println("CMP Binary " + binary);
        System.out.println("CMP Hex " + result);
    }

    private static void MOV(String rd, String imm8) {

        String binary = "00100";
        binary += rd;
        binary += immToBinary(imm8,8);

        String result = binaryToHex(binary);

        System.out.println("MOV Binary " + binary);
        System.out.println("MOV Hex " + result);
    }

    private static void SUB_REGISTER(String rd, String rn, String rm) {

        String binary = "0001101";
        binary += rm;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("SUB (Register) Binary : " + binary);
        System.out.println("SUB (Register) Hex : " + result);

    }

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

    private static void ADD_REGISTER(String rd, String rn, String rm) {

        String binary = "0001100";
        binary += rm;
        binary += rn;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("ADD (Register) Binary : " + binary);
        System.out.println("ADD (Register) Hex : " + result);

    }

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

    private static void ASR(String rd, String rm, String imm5) {

        String binary = "00010";
        String imm5Binary = immToBinary(imm5,5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("ASR Binary : " + binary);
        System.out.println("ASR Hex : " + result);

    }

    private static void LSR(String rd, String rm, String imm5) {

        String binary = "00001";
        String imm5Binary = immToBinary(imm5,5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("LSR Binary : " + binary);
        System.out.println("LSR Hex : " + result);

    }

    private static void LSL(String rd, String rm, String imm5) {

        String binary = "00000";
        String imm5Binary = immToBinary(imm5,5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("LSL Binary : " + binary);
        System.out.println("LSL Hex : " + result);
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

}
