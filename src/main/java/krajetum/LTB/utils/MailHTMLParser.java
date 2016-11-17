package krajetum.LTB.utils;

/**
 * Created by Lorenzo on 17/11/2016.
 */
public class MailHTMLParser {




    public static String PolishMAIL(String mailText){
        StringBuilder result = new StringBuilder();



        boolean tag = false;
        int beginIndex =0;
        int endIndex =0;

        for(int i=0; i<mailText.length();i++) {
            if(mailText.charAt(i)=='<'){
                beginIndex = i;
                tag = true;
                continue;
            }else if (mailText.charAt(i)=='>'){
                endIndex = i;
                tag = false;
            }else if(!tag){
                result.append(mailText.charAt(i));
            }

            if(endIndex>0){
                if(mailText.substring(beginIndex, endIndex).contains("br")){
                    result.append("\n");
                }
                endIndex = 0;
            }

        }


        return result.toString();
    }


}
