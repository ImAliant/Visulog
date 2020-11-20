
public class Graphique {
	String nom_graphique;
	
	 public Graphique (String n) {
		 this.nom_graphique=n;
	 }
	 
	 public String getNom_graphique(){
		 return this.nom_graphique;
	 }
	 
	 public void ouvrir_canvas() {
		 System.out.println("<canvas id="+this.nom_graphique+"></canvas>");
	 }
	 
	 public void import_chartJS() {
		 System.out.println("<script src=\'https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js'>");
	 }
}
