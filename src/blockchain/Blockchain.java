import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
public class Blockchain {


    Blockchain(){


        int id = 1;
        int starthash = 0;

        int time = 3;
        String previoushash = Integer.toString(starthash);
        String currenthash = "";
        Boolean sec;


        for(int i = 0; i < time; i ++)
        {
            System.out.println("Block:");
            System.out.println("id: " + id);

            currenthash = GenerateBlock(previoushash);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime().hashCode());
            System.out.println("Timestamp: " + timeStamp);
            System.out.println("Hash of the previous block: " + previoushash);
            System.out.println("Hash of the block: "+ currenthash);

            sec = validate(previoushash, currenthash);

            if (sec){
                previoushash = currenthash;
                id++;
                System.out.println();
            }else{
                System.out.println("Hash dont valid!!!");

                break;
            }




        }




    }

    private Boolean validate(String previousblock, String currentblock) {
        String check = GenerateBlock(previousblock);
        if(check.equals(currentblock)){
            return true;
        }else {
            return false;
        }

    }

    private String GenerateBlock(String currentid) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            /* Applies sha256 to our input */
            byte[] hash = digest.digest(currentid.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte elem: hash) {
                String hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }

    }


}
