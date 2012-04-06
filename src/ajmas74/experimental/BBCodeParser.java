package ajmas74.experimental;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * This is a simple BBCode parser. It is not polished and still requires some work.
 * Currently I have focused on tags that are supported by square brackets, such
 * as [b], [i], etc. Detection of URLs and smilies is not yet implemented.
 * <p>
 * Details on BBCode can be found here: http://en.wikipedia.org/wiki/BBCode
 * 
 * @author Andre-John Mas
 *
 */
//TODO add support for [img] and http tags which are prefixed by url:
public class BBCodeParser {

	public static final String COLOR_REGEX = "#\\p{XDigit}{3,6}";
	
	private static final String[][] COLORS_MAP = new String[][] {
		{"red","#ff0000"}
	};

	
	public String convertString(String str) {
		// TODO find smilies
		
		ParserContext parserCtx = new ParserContext();
		parseBBCode(str, parserCtx);		
		System.out.println(parserCtx.tagPositions);
		correctNesting( parserCtx );
		return convertToHtml( parserCtx );
	}
	
	/**
	 * Parse the BBCode finding the tags
	 * 
	 * @param str
	 * @param parserContext
	 */
	private void parseBBCode(String str, ParserContext parserContext ) {
		StringBuilder tag = new StringBuilder();
		int savedPosition = -1;
		int state = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			switch (state) {
			case 0:
				if (c == '[') {
					savedPosition = parserContext.unformattedTextBuilder.length();
					state = 1;
				} else {
					parserContext.unformattedTextBuilder.append(c);
				}
				break;
			case 1:
				if (c == ']') {
					TagPosition tagPos = new TagPosition(tag.toString(),
							savedPosition);
					parserContext.tagPositions.add(tagPos);
					tag.setLength(0);
					state = 0;
				} else {
					tag.append(c);
				}
				break;
			default:
				throw new RuntimeException("Unsupported state");
			}
		}
	}

	/**
	 * Handle the color values
	 * 
	 * @param text
	 * @return
	 */
	private String parseColor ( String text ) {
	   if ( Pattern.matches(COLOR_REGEX, text) ) {
		   return text;
	   }
	   
	   for ( int i =0; i<COLORS_MAP.length; i++ ) {
		   if ( text.equalsIgnoreCase( COLORS_MAP[i][0] ) ) {
			   return COLORS_MAP[i][1];
		   }
	   }
	   return "";
	}
	
	private boolean isValidURI ( String str ) {
		try {
			URI.create(str);
			return true;
		} catch (Throwable e) {
			// ignore
		}
		return false;
	}
	
	/**
	 * Correct any incorrect nesting.
	 * <p>
	 * The implementation is designed around a stack, where the tags are pushed onto the stack
	 * and if the matching tag is not found when the stack is popped, then the tag is created
	 * and inserted before the next tag.
	 * 
	 * @param parserContext
	 */
	private void correctNesting( ParserContext parserContext ) {
		Stack<String> tagStack = new Stack<String>();
		for (int i = 0; i < parserContext.tagPositions.size(); i++) {
			TagPosition tagPosition = parserContext.tagPositions.get(i);
			String tag = tagPosition.tag;
			if (tag.indexOf('=') > -1) {
				String[] parts = tag.split("=");
				tag = parts[0];
			}
			
			if ( tag.startsWith("/") ) {
				tag = tag.substring(1);
				String stackItem = tagStack.pop();
				if ( !stackItem.equals(tag) ) {
					TagPosition tagPosition2 = new TagPosition("/"+stackItem,tagPosition.offset);
					parserContext.tagPositions.add(i, tagPosition2);
					i++;
				}
			} else {
				tagStack.push(tag);
			}
		}
	}

	/**
	 * Converts the parsed content to HTML
	 * 
	 * @param parserContext
	 * @return
	 */
	private String convertToHtml( ParserContext parserContext ) {
		
		boolean needInnerText = false;		
		
		StringBuilder strBuilder = new StringBuilder();
		TagPosition lastTagPosition = null;

		for (TagPosition tagPosition : parserContext.tagPositions) {
			String tag = tagPosition.tag;
			String tagParam = "";
			String innerText = "";
			
			int offset = tagPosition.offset;

			if (tag.indexOf('=') > -1) {
				String[] parts = tag.split("=");
				tag = parts[0];
				if ( parts.length > 1) {
					tagParam = parts[1];
				}
			}

			if ( needInnerText ) {
				innerText = parserContext.unformattedTextBuilder.substring(
						lastTagPosition.offset, offset);
			} else if (lastTagPosition == null) {
				strBuilder.append(parserContext.unformattedTextBuilder.substring(0, offset));

			} else {
				strBuilder.append( parserContext.unformattedTextBuilder.substring(
						lastTagPosition.offset, offset));
			}

			if (tag.equals("b")) {
				strBuilder.append("<b>");
			} else if (tag.equals("/b")) {
				strBuilder.append("</b>");
			
			} else if (tag.equals("i")) {
				strBuilder.append("<i>");
			} else if (tag.equals("/i")) {
				strBuilder.append("</i>");
				
			} else if (tag.equals("u")) {
				strBuilder.append("<u>");
			} else if (tag.equals("/u")) {
				strBuilder.append("</u>");
				
			} else if (tag.equals("size")) {
				strBuilder.append("<span style=\"font-size:" + tagParam + "px\">");
			} else if (tag.equals("/size")) {
				strBuilder.append("</span>");
			
			} else if (tag.equals("color")) {
				String colorStr = parseColor ( tagParam );
				strBuilder.append("<span style=\"color:" + colorStr + "\">");
			} else if (tag.equals("/color")) {
				strBuilder.append("</span>");
				
			} else if (tag.equals("code")) {
				strBuilder.append("<pre>");
			} else if (tag.equals("/code")) {
				strBuilder.append("</pre>");
				
			} else if (tag.equals("code")) {
				strBuilder.append("<pre>");
			} else if (tag.equals("/code")) {
				strBuilder.append("</pre>");
				
			} else if (tag.equals("quote")) {
				strBuilder.append("<blockquote><p>");
			} else if (tag.equals("/quote")) {
				strBuilder.append("</p></blockquote>");
			
			} else if (tag.equals("img")) {
				needInnerText = true;				
			} else if (tag.equals("/img")) {
				needInnerText = false;
				strBuilder.append("<img src=\""+innerText+"\">");
				
			} else if (tag.equals("url")) {
				if ( tagParam.trim().length()== 0) {
					needInnerText = true;
				} else {
					strBuilder.append("<a href=\"" + tagParam + "\">");
				}
			} else if (tag.equals("/url")) {
				if ( !needInnerText ) {
					strBuilder.append("</a>"); 
				} else {
					needInnerText = false;
					if ( isValidURI(innerText) )
						strBuilder.append("<a href=\"" + innerText + "\">"+innerText+"</a>");
					else
						strBuilder.append(innerText);
				}
			}
			
			lastTagPosition = tagPosition;
		}

		if (lastTagPosition == null) {
			strBuilder.append( parserContext.unformattedTextBuilder.substring(0));
		} else if (lastTagPosition.offset < parserContext.unformattedTextBuilder.length()) {
			strBuilder.append( parserContext.unformattedTextBuilder
					.substring(lastTagPosition.offset));
		}

		return strBuilder.toString();
	}


	// -----------------------------------------------------------------------------------
	// Inner Classes
	// -----------------------------------------------------------------------------------
	
	private static class ParserContext {
		List<TagPosition> tagPositions = new ArrayList<TagPosition>();

		StringBuilder unformattedTextBuilder = new StringBuilder();		
	}
	
	private static class TagPosition {
		String tag;
		int offset;

		TagPosition(String tag, int offset) {
			this.tag = tag;
			this.offset = offset;
		}

		public String toString() {
			return "[" + tag + ":" + offset + "]";
		}
	}

	// -----------------------------------------------------------------------------------
	// Main entry point, for testing purposes
	// -----------------------------------------------------------------------------------
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// info: valid strings
		System.out.println(new BBCodeParser().convertString("HelloWorld"));
		System.out.println(new BBCodeParser()
				.convertString("[b]HelloWorld[/b]"));
		System.out.println(new BBCodeParser()
				.convertString("[size=15]HelloWorld[/size]"));	
		System.out.println(new BBCodeParser()
		.convertString("[color=red]HelloWorld[/color]"));
		System.out.println(new BBCodeParser()
		.convertString("[color=organge]HelloWorld[/color]"));		
		System.out.println(new BBCodeParser()
		.convertString("[color=#00ff00]HelloWorld[/color]"));		
		System.out.println(new BBCodeParser()
				.convertString("[b][i]HelloWorld[/i][/b]"));
		System.out.println(new BBCodeParser()
				.convertString("aaaaaa [b][i]HelloWorld[/i][/b]"));
		System.out.println(new BBCodeParser()
				.convertString("[b][i]HelloWorld[/i][/b] bbbbbbb"));
		System.out.println(new BBCodeParser()
				.convertString("aaaaaa [b][i]HelloWorld[/i][/b] bbbbbb"));
		System.out.println(new BBCodeParser()
		.convertString("[img]http://myhost/myimage[/img]"));		
		System.out
				.println(new BBCodeParser()
						.convertString("[b][url=http://www.arstechnica.com/]HelloWorld[/url][/b]"));

		System.out
		.println(new BBCodeParser()
				.convertString("[b][url=]http://www.arstechnica.com/[/url][/b]"));
		System.out
		        .println(new BBCodeParser()
				      .convertString("[b][url]http://www.arstechnica.com/[/url][/b]"));		
		System.out
        .println(new BBCodeParser()
		      .convertString("[b][url]http://www.arstechnica.com/[/url][/b]"));		

		
		// info: strings with nesting issues
		System.out.println(new BBCodeParser()
				.convertString("[b][i]HelloWorld[/b][/i]"));

		// info: strings with missing end tags
		System.out.println(new BBCodeParser()
				.convertString("[b][i]HelloWorld[/b]"));
	}

}
