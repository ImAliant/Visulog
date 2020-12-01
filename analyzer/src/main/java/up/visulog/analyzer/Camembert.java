import java.util.ArrayList;
public class Camembert extends Graphique{

	public Camembert(String n) {
		super(n);
	}
	public void ouvrir_script() {
		System.out.println("<script>");
	}
	
	public void var_context() { 
		System.out.println("var context = document.getElementById('"+this.getNom_graphique()+"').getContext('2d')");
	}// cette fonction permet de mettre du code dans l'element dans getElementById('')
	
	public void data (ArrayList<String> noms, ArrayList<Integer> nb_tache, ArrayList<String> colors) {
	System.out.println("var data = {\n");
	
	
	System.out.print("labels : [");
	for (int i =0;i<noms.size()-1;i++) {
		System.out.print("'");
		System.out.print(noms.get(i));
		System.out.print("',");
	}
	System.out.print(noms.get(noms.size()-1));
	System.out.print("],");
	
	System.out.println();
	
	System.out.println("datasets : [{");
	System.out.print("data : [");
	for (int i =0;i<nb_tache.size()-1;i++) {
		System.out.print(nb_tache.get(i));
		System.out.print(",");
	}
	System.out.print(nb_tache.get(nb_tache.size()-1));
	System.out.print("],");
	
	System.out.println();
	
	System.out.print("backgroundColor : [");
	for (int i =0;i<colors.size()-1;i++) {
		System.out.print("'");
		System.out.print(colors.get(i));
		System.out.print("',");
	}
	System.out.print(colors.get(colors.size()-1));
	System.out.print("]");
	
	System.out.println();
	System.out.println("}]");
	System.out.println();
	System.out.println("}");
	}
	
	public void var_config() {
		System.out.println("var config = {");
		System.out.println("type : 'pie',");
		System.out.println("data : data}");
	}
	
	public void var_camembert() {
		System.out.println("var camembert = new Chart (context, config)");
	}
			
	public void fermer_script() {
		System.out.println("</script>");
	}	
	
}
