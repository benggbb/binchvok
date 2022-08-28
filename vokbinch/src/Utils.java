

import java.util.HashMap;

public class Utils {
	static HashMap<String, String> GetProps(String[] args){
		HashMap<String, String> temp = new HashMap<>();
		for (int i = 0; i < args.length; i++) {
			
			if (args[i].startsWith("-") ) {
				if(!args[i+1].startsWith("-"))
					temp.put(args[i], args[i+1]);
				else
					temp.put(args[i], "true");
			} 
			
			temp.put("CONF", args[args.length-1]);
		}
		return temp;
	}

}
