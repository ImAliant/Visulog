package up.visulog.analyzer;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import up.visulog.analyzer.graphique.Graphique;
import up.visulog.analyzer.html.PageCreation;

public class AnalyzerResult {
	//Cette fonction est un getter renvoyant l'attribut subResults de l'objet courant
    public List<AnalyzerPlugin.Result> getSubResults() {
        return subResults;
    }

    //Ici est défini l'unique attribut des objets de cette classe
    private final List<AnalyzerPlugin.Result> subResults;

    
    //Cette fonction est un setter permettant de modifier l'attribut subResults de l'objet courant
    public AnalyzerResult(List<AnalyzerPlugin.Result> subResults) {
        this.subResults = subResults;
    }

    
    //Les fonctions ci-dessous permettent de renvoyer sous forme de String ou d'HTML
    //les données de l'objet
    @Override
    public String toString() {
        return subResults.stream().map(AnalyzerPlugin.Result::getResultAsString).reduce("", (acc, cur) -> acc + "\n" + cur);
    }

    public String toHTML() {
        return "<html><body>"+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc, cur) -> acc + cur) + "</body></html>";
    }
    
  //Ce traitement sera provisoire car il n'est pas complet    - #WilliamBenakli
    /**PROVISOIRE Cette fonction permet de cr�at un code HTMLFLOW avec le graphique compris
     * @param typegraphique
     * @return String (correspondant au site web avec graphique) #WilliamBenakli
     */
    public String createCodePageHtml(String typegraphique){
    	Graphique graph = new Graphique("graphique");
    	ArrayList<String> nomCommit = new ArrayList<String>();
    	ArrayList<String> nombreCommit = new ArrayList<String>();
//    	C:\\Users\\Ropste\\Documents\\L2\\Pr�Pro\\Projet\\visulog --addplugin=cOunTcommits
    	if((toString() != null)) {
	    	String[] tab = toString().split(",");
		    	for(String present: tab) {
		    		System.out.println(present);
		    		nomCommit.add(present.split("=")[0].replace("{", ""));
		    		nombreCommit.add(present.split("=")[1].replace("}", ""));
		    	}
		    	return PageCreation.createPage("Commit", graph.import_chartJS(), graph.toGraph(typegraphique, nomCommit, nombreCommit), graph.ouvrir_canvas());
	    }else {
    		return "Une erreur est surevenue lors de la cr�ation de la page. Fichier .git introuvable";
    	}
    }
    
    
    /**
     * Cette fonction cr�es un fichier .HTML #WilliamBenakli
     * @param name Le nom du fichier
     * @param typegraphique Le type de graphique
     * 
     */
    //TODO: Plutot que de cr�er un fichier au meme endroit que le plugin il serai
    //pr�f�rable de cr�er un fichier siteWebPage au centre de graddle
    //Je vous laisse comment faire #WilliamBenakli
    public void createPageHtml(String name, String typegraphique) {
	    	File desc = new File(name+".html");    	
	    	if(!desc.isDirectory()){
				FileWriter newfile;
				try {    
					newfile = new FileWriter(desc.getPath());
			    	newfile.write(createCodePageHtml(typegraphique));
					newfile.close();
				} catch (IOException e) {
					System.out.println("Fichier non traitable. Une erreur s'est produite veuillez ressayer");
				}
				try {
					Desktop.getDesktop().browse(desc.toURI());
				} catch (IOException e) {
					System.out.println("Nous n'avons pas trouv� de navigateur par defaut pour ouvrir la page g�n�r�e.");
					System.out.println("Cependant les fichiers ont �t� cr�e : siteWebHtml/" + name + ".html");
				}
		}
}

}
