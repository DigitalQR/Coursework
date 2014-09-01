package Tools.String;

public class Parametres {

	public static String[] getParameters(String input, char separator){
		input+=separator;
		String[] Str = new String[0];
		String Temp = "";
		for(int i = 0; i<input.length(); i++){
			if(input.charAt(i) != separator){
				Temp+=input.charAt(i);
			}else{
				if(!Temp.replace(separator, ' ').isEmpty()){
					Str = append(Str, Temp);
					Temp = "";
				}
			}
		}
		
		return Str;
	}
	
	public static String writeParameters(String[] input, char separator){
		String output = "";
		for(String s:input){
			output+= s+separator;
		}
		return output;
	}
	
	
	private static String[] append(String[] Str, String s){
		String[] Temp = Str.clone();
		Str = new String[Temp.length+1];
		
		for(int i = 0; i<Temp.length; i++){
			Str[i] = Temp[i];
		}
		Str[Temp.length] = s;
		return Str;
	}
}
