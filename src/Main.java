import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        String[] instr = new String[] {"LSLS", "111", "111", "#13"};

        switch (instr[0]){
            case "LSLS":
                LSL(instr[1],instr[2],instr[3]);
                break;
            default:
                break;
        }

    }

    private static void LSL(String rd, String rm, String imm5) {

        String binary = "00000";
        String imm5Binary = imm5ToBinary(imm5);
        binary += imm5Binary;
        binary += rm;
        binary += rd;

        String result = binaryToHex(binary);

        System.out.println("LSL Binary : " + binary);
        System.out.println("LSL Hex : " + result);
    }

    private static String binaryToHex(String binary) {

        int decimal = Integer.parseInt(binary,2);
        String hexStr = Integer.toString(decimal,16);
        if (hexStr.length() < 4){
            return "0" + hexStr;
        } else {
            return hexStr;
        }

    }

    private static String imm5ToBinary(String imm5) {

        String imm5WithoutHastag = imm5.substring(1);
        int imm5Int = Integer.parseInt(imm5WithoutHastag);

        return Integer.toBinaryString(imm5Int);

    }

}
