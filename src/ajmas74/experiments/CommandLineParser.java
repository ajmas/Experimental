/* Created on 8-Oct-2003 */
package ajmas74.experiments;

import java.util.*;

/**
 * no description
 * 
 * @author Andre-John Mas
 */
public class CommandLineParser {

	public static final String UNAMED_PARAMETERS = "<<>>";
	
  List _supportedOptions;
  HashMap _shortOptionsMap;
  HashMap _longOptionsMap;

  public CommandLineParser() {
    _supportedOptions = new Vector();
    _shortOptionsMap = new HashMap();
    _longOptionsMap = new HashMap();
  }

  public HashMap parseCommandLine(String[] args) throws Exception {
    HashMap properties = new HashMap();
		properties.put("<<>>",new Vector());
		
    boolean waitingForParam = false;
    String prevOptStr = null;
    boolean namedParamsRemaining = true;
    for (int i = 0; i < args.length; i++) {
      if (args[i].startsWith("--")) {
        if (waitingForParam) {
          throw new Exception(
            "option '" + prevOptStr + "' is missing a parameter");
        }
        if (!namedParamsRemaining) {
					throw new Exception(
						"no named options my appear after anonymous non-switch parameters");        	
        }
        String optStr = args[i].substring(2);
        String value = null;
        //int idxA = optStr.indexOf("\"");
        int idxEq = optStr.indexOf("=");
        if (idxEq > -1) {
          value = optStr.substring(idxEq + 1);
          optStr = optStr.substring(0, idxEq);
        }
        SupportedOption option = (SupportedOption) _longOptionsMap.get(optStr);
        if (option == null) {
          throw new Exception("unknown option '--" + optStr + "'");
        }
        if (option.isParameterRequired() && value == null) {
          throw new Exception(
            "option '--" + optStr + "' is missing a parameter");
        }
        if (value == null) {
          value = "";
        }
        properties.put(option.getName(), value);
      } else if (args[i].startsWith("-")) {
        if (waitingForParam) {
          throw new Exception(
            "option '-" + prevOptStr + "' requires a parameter");
        }
				if (!namedParamsRemaining) {
					throw new Exception(
						"no named options my appear after anonymous non-switch parameters");        	
				}        
        String block = args[i].substring(1);
        if (block.length() > 1) {
          for (int j = 0; j < block.length(); j++) {
            String optStr = block.charAt(j) + "";
            SupportedOption option =
              (SupportedOption) _shortOptionsMap.get(optStr);
            if (option == null) {
              throw new Exception("unknown option '-" + optStr + "'");
            }

            
            if ( option.isParameterRequired() && option.isShortParamGlued() ) {
							if ( j > 1) {
								throw new Exception(
									"option '-"
										+ optStr
										+ "' can not be in a parameter block since a parameter is required");
							}            	
							String param = block.substring(1);
							properties.put(option.getName(), param);
							break;
            } else {
							if (option.isParameterRequired()) {
								throw new Exception(
									"option '-"
										+ optStr
										+ "' can not be in a parameter block since a parameter is required");
							}            	
            	properties.put(optStr, "");
            }
          }
        } else {
          String optStr = block;
          SupportedOption option =
            (SupportedOption) _shortOptionsMap.get(optStr);
          if (option == null) {
            throw new Exception("unknown option '" + optStr + "'");
          }
          if ( option.isParameterRequired() ) {
						waitingForParam = true;
          	prevOptStr = optStr;
          } else {
						properties.put(option.getName(), "");
          }
        }
      } else {
        if (waitingForParam) {
					SupportedOption option = (SupportedOption) _shortOptionsMap.get(prevOptStr);
          properties.put(option.getName(), args[i]);
          prevOptStr = null;
          waitingForParam = false;
        } else {
					List l = (List) properties.get("<<>>");
					l.add(args[i]);
					namedParamsRemaining = false;
        }
      }
    }

    return properties;
  }

  public void addSupportedOption(SupportedOption option) {
    _supportedOptions.add(option);
    if (option.getLongOption() != null) {
      _longOptionsMap.put(option.getLongOption(), option);
    }
    if (option.getShortOption() != null) {
      _shortOptionsMap.put(option.getShortOption(), option);
    }
  }

  public static class SupportedOption {
    String _name;
    String _shortOption;
    String _longOption;
    boolean _parameterRequired;
		boolean _shortParamGlued;

    public SupportedOption(
      String name,
      String shortOption,
      String longOption,
      boolean parameterRequired,
      boolean shortParamGlued ) {
      _name = name;
      _shortOption = shortOption;
      _longOption = longOption;
      _parameterRequired = parameterRequired;
      _shortParamGlued = shortParamGlued;
    }

    /**
     * @return
     */
    public String getLongOption() {
      return _longOption;
    }

    /**
     * @return
     */
    public String getName() {
      return _name;
    }

    /**
     * @return
     */
    public boolean isParameterRequired() {
      return _parameterRequired;
    }

    /**
     * @return
     */
    public String getShortOption() {
      return _shortOption;
    }

    /**
     * @return
     */
    public boolean isShortParamGlued() {
      return _shortParamGlued;
    }

  }
  public static void main(String[] args) {
    args = new String[] { "-e","UTF-8", "-sh", "-Dabc=hello", "file1","file2" };
    try {
      CommandLineParser parser = new CommandLineParser();
      parser.addSupportedOption(
        new CommandLineParser.SupportedOption(
          "encoding",
          "e",
          "encoding",
          true,false));
      parser.addSupportedOption(
        new CommandLineParser.SupportedOption("help", "h", "help", false,false));
      parser.addSupportedOption(
        new CommandLineParser.SupportedOption("sync", "s", "sync", false,false));
			parser.addSupportedOption(
				new CommandLineParser.SupportedOption("defined", "D", "define", false,true));

			System.out.println(parser.parseCommandLine(args));    
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
