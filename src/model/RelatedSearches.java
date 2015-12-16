package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatedSearches {
	
	private final Map<String,List<String>> THESAURUS;
	
	public RelatedSearches(){
		THESAURUS = buildThesaurus();
	}
	
	public List<String> getRelated(String search){
		String[] split = search.split(" ");
		for(int i = 0; i < split.length; i++){
			if(THESAURUS.containsKey(split[i].toLowerCase())){
				return THESAURUS.get(split[i].toLowerCase());
			}
		}
		return Arrays.asList("","","");
	}
	
	public Map<String, List<String>> buildThesaurus(){
		Map<String,List<String>> output = new HashMap<String,List<String>>();
		
		output.put("abstract", Arrays.asList("inheritance","interface","superclass"));
		output.put("accessor", Arrays.asList("get","attribute","private"));
		output.put("api", Arrays.asList("interface","package","library"));
		output.put("applet", Arrays.asList("application","web","gui"));
		output.put("argument", Arrays.asList("parameter","header","method"));
		output.put("array", Arrays.asList("arraylist","element","container"));
		output.put("arraylist", Arrays.asList("array","list","collection"));
		output.put("attribute", Arrays.asList("instance","variable","state"));
		output.put("block", Arrays.asList("statement","declatation","body"));
		output.put("boolean", Arrays.asList("variable","true","false"));
		output.put("break", Arrays.asList("loop","switch","block"));
		output.put("case", Arrays.asList("switch","label","break"));
		output.put("catch", Arrays.asList("try","error","statement"));
		output.put("class", Arrays.asList("object","attribute","instance"));
		output.put("comment", Arrays.asList("code","text","reader"));
		output.put("concurrency", Arrays.asList("parallel","thread","execution"));
		output.put("condition", Arrays.asList("boolean","if","loop"));
		output.put("constant", Arrays.asList("variable","final","class"));
		output.put("constructor", Arrays.asList("class","instance","method"));
		output.put("continue", Arrays.asList("break","loop","control"));
		output.put("decrement", Arrays.asList("increment","operator","value"));
		output.put("default", Arrays.asList("switch","case","break"));
		output.put("do", Arrays.asList("while","loop","for"));
		output.put("encapsulation", Arrays.asList("private","polymorphism","inheritance"));
		output.put("enum", Arrays.asList("constant","static","variable"));
		output.put("exception", Arrays.asList("throws","throwable","checked"));
		output.put("expression", Arrays.asList("boolean","arithmetic","regex"));
		output.put("factory", Arrays.asList("pattern","instance","decouple"));
		output.put("field", Arrays.asList("variable","member","private"));
		output.put("final", Arrays.asList("extended","variable","class"));
		output.put("finally", Arrays.asList("try","catch","protected"));
		output.put("for", Arrays.asList("foreach","while","loop"));
		output.put("foreach", Arrays.asList("for","while","loop"));
		output.put("hashmap", Arrays.asList("map","hashset","collection"));
		output.put("hashset", Arrays.asList("set","hashmap","collection"));
		output.put("identifier", Arrays.asList("variable","method","class"));
		output.put("if", Arrays.asList("else","conditional","switch"));
		output.put("immutable", Arrays.asList("string","state","fixed"));
		output.put("implements", Arrays.asList("inheritance","interface","header"));
		output.put("import", Arrays.asList("package","library","api"));		
		output.put("increment", Arrays.asList("decrement","operator","value"));
		output.put("inheritance", Arrays.asList("class","abstract","interface"));
		output.put("initialize", Arrays.asList("variable","instance","build"));
		output.put("instance", Arrays.asList("object","operator","variable"));
		output.put("int", Arrays.asList("byte","short","long"));
		output.put("iteration", Arrays.asList("loop","statement","for"));
		output.put("iterator", Arrays.asList("pattern","collection","data"));
		output.put("java", Arrays.asList("language","class","program"));
		output.put("key", Arrays.asList("hash","map","value"));
		output.put("logical", Arrays.asList("and","or","not"));
		output.put("local", Arrays.asList("method","variable","body"));
		output.put("loop", Arrays.asList("for","while","do"));
		output.put("main", Arrays.asList("method","public","static"));
		output.put("method", Arrays.asList("body","public","void"));
		output.put("mutator", Arrays.asList("set","method","variable"));
		output.put("new", Arrays.asList("instance","operator","class"));
		output.put("object", Arrays.asList("class","state","instance"));
		output.put("operator", Arrays.asList("arithmetic","ternary","equality"));
		output.put("parameter", Arrays.asList("argument","header","method"));
		output.put("pattern", Arrays.asList("iterator","factory","prototype"));
		output.put("polymorphism", Arrays.asList("inheritance","object","encapsulation"));
		output.put("protected", Arrays.asList("access","private","public"));
		output.put("primitive", Arrays.asList("int","double","float"));
		output.put("public", Arrays.asList("modifier","private","protected"));
		output.put("queue", Arrays.asList("stack","fifo","collection"));
		output.put("return", Arrays.asList("method","statement","terminate"));
		output.put("stack", Arrays.asList("queue","lifo","collection"));
		output.put("statement", Arrays.asList("if","assignment","return"));
		output.put("static", Arrays.asList("main","method","public"));
		output.put("string", Arrays.asList("boolean","int","object"));
		output.put("super", Arrays.asList("inheritance","subclass","object"));
		output.put("switch", Arrays.asList("if","else","default"));
		output.put("synchronized", Arrays.asList("thread","parallel","critical"));
		output.put("thread", Arrays.asList("parallel","process","synchronized"));
		output.put("throws", Arrays.asList("exception","method","header"));
		output.put("variable", Arrays.asList("type","class","field"));
		output.put("while", Arrays.asList("foreach","for","loop"));	
		
		return output;
	}
	
}
