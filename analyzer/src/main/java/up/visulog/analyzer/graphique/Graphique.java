package up.visulog.analyzer.graphique;
import java.util.ArrayList;
import java.util.Random; 
 
public class Graphique{
    
    String nom_graphique;
 
    public Graphique(String nom) {
         this.nom_graphique=nom;
    }
     
    public String script(String text) {
        this.import_chartJS();
        return "<script>"+text+"</script>";
    }
    
 
     public String ouvrir_canvas() {
         return "<canvas id='"+this.nom_graphique+"'></canvas>";
     } 
 
     public String import_chartJS() {
         return "<script src='https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.9.4/Chart.min.js'></script>";
          
     }
     
    public String toGraph(String type, ArrayList<String> noms, ArrayList<Integer> nombreCommit) {
        Random r = new Random();
        String result = "";
        		 result += "var graph;";
        		 result += "var config;";
        		 result += "function changeGraphique(){";
        		 result +="";
        		 result +="if(graph == null){ ";
	                result+="var context = document.getElementById('"+this.nom_graphique+"').getContext('2d'); ";
	                result+="var data = {";
	                result+="labels : [";
	                for (int i =0;i<noms.size()-1;i++) {
	                    result+="'";
	                    result+=noms.get(i);
	                    result+="',";
	                }
	                result+="'"+noms.get(noms.size()-1)+"'],";
	                result+="datasets : [{ data : [";
	                for (int i =0;i<nombreCommit.size()-1;i++)result+=nombreCommit.get(i)+",";
	                result+=nombreCommit.get(nombreCommit.size()-1)+"],";
	                result+="backgroundColor :  [ ";
	                for(int i =0;i<noms.size()-1;i++)
	                    result+="'rgba(" + r.nextInt(255) +","+ r.nextInt(255) +","+ r.nextInt(255) +",1)', ";
	                result+="]}]};";        
	                result += "var type = document.getElementById('graphiqueListe').value;";
	                result += "config = { type : type, data : data};";
	                result += "graph = new Chart (context, config);";
            result += "}else{ ";
            result +=  "graph.destroy();";
            result += " graph = null;";
            result +=  "changeGraphique();";
            result +=  "}";
            result +=  "} ";
    return result;
}
    
    public String verificationType(String type) {
        String[] list = {"bar", "pie", "radar", "line", "doughnut"};
        for(String str: list) {
            if(str.equalsIgnoreCase(type.toLowerCase())) {
                return type.toLowerCase();
            }
        }
        return "pie";
    }
}