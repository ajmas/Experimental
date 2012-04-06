package ajmas74.experimental;

import java.text.MessageFormat;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateMatcher {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MessageFormat mf;
        
        String str = "hello ${who}, you  $$ \\${xxxx} logged in at ${time} on ${date} $";
        Properties prop = new Properties();
        prop.setProperty("who", "world");
        prop.setProperty("time", "12:20");
        prop.setProperty("date", "Mnday" +
        		"");
        String replace = "....";
        
//        CharSequence inputStr = "ab12 cd efg34";
//        String patternStr = "(\\{[a-z]*\\})";
        
        StringBuilder buf = new StringBuilder();
        StringBuilder varName = new StringBuilder();
        
        boolean invar = false;  
        boolean escape = false;
        int length = str.length();
        for ( int i=0; i<length; i++ ) {
            char c = str.charAt(i);
            if ( escape ) {
                buf.append(c);
            }
            else if ( !invar && c == '\\' ) {
                escape = true;
            }
            else if ( !invar && c == '$' && (i+1) < length && str.charAt(i+1) == '{') {
                invar = true;
                i++;
            }
            else if ( invar ) {
                if ( c == '}') {
                    buf.append(prop.getProperty(varName.toString(),""));
                    varName.setLength(0);
                    invar = false;
                }
                else {
                    varName.append(c);
                }
            }
            else {
                buf.append(c);
            }
        }
        
        
        
        

//        Configuration cfg = new Configuration();
//        // Specify the data source where the template files come from.
//        // Here I set a file directory for it:
//        cfg.setDirectoryForTemplateLoading(
//                new File("/where/you/store/templates"));
//        // Specify how templates will see the data-model. This is an advanced topic...
//        // but just use this:
//        cfg.setObjectWrapper(new DefaultObjectWrapper());
        
//        Template temp = cfg.getTemplate("test.ftl");
        
        
//        // Compile regular expression
//        Pattern pattern = Pattern.compile(patternStr);
//        Matcher matcher = pattern.matcher(str);
//        
//        // Replace all occurrences of pattern in input
//        StringBuffer buf = new StringBuffer();
//        boolean found = false;
//        while ((found = matcher.find())) {
//            // Get the match result
//            String replaceStr = matcher.group();
//        
//            System.out.println("replaceStr: " + replaceStr);
//            // Convert to uppercase
//            replaceStr = replace;//replaceStr.toUpperCase();
//        
//            // Insert replacement
//            matcher.appendReplacement(buf, replaceStr);
//        }
//        matcher.appendTail(buf);
        

        System.out.println("result: " + buf);

    }

}
