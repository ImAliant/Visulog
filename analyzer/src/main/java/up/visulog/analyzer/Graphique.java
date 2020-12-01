public class Graphique {
	String nom_graphique;

	 public Graphique (String n) {
		 this.nom_graphique=n;
	 }

	 public String getNom_graphique(){
		 return this.nom_graphique;
	 }

	 public String ouvrir_canvas() {
		 return "<canvas id="+this.nom_graphique+"></canvas>";
	 }

	 public String import_chartJS() {//doit être dans le head
		 return "<script src="+"https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js>"+"</script>";
		  
	 }
}
