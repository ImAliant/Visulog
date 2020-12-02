package up.visulog.analyzer;

import java.util.ArrayList;
public class Camembert extends Graphique{

	public Camembert(String n) {
		super(n);
	}
	
	
	
	 
	public String script(String test) {//doit être dans le head 
		this.import_chartJS();
		
		return "<script>"+test+"</script>";
	}

	
	public String toCamembert(ArrayList<String> noms, ArrayList<Integer> nb_tache) {
	
	String result = "var context = document.getElementById('"+this.getNom_graphique()+"').getContext('2d')";
	result+="var data = {";
	
	result+="labels : [";
	for (int i =0;i<noms.size()-1;i++) {
		result+="'";
		result+=noms.get(i);
		result+="',";
	}
	result+="'"+noms.get(noms.size()-1)+"'],";
	
	
	result+="datasets : [{ data : [";
	for (int i =0;i<nb_tache.size()-1;i++) {
		result+=nb_tache.get(i)+",";
	}
	result+=nb_tache.get(nb_tache.size()-1)+"],";

	result+="backgroundColor : ['blue','green','pink','red', 'purple', 'yellow', 'grey', 'orange', 'black']}]}";
	
	result += "var config = { type : 'pie', data : data}";
	
	
	result += "var camembert = new Chart (context, config)";
	
	return result;
	
			 
	
}
}

