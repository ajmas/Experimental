package ajmas74.experimental.proxy;

public class ClassX implements InterfaceX {

	String value = "Hello World";
	
	public ClassX () {
		
	}
	
	public ClassX ( String defaultValue ) {
		this.value = defaultValue;
	}
	
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
