package up.visulog.analyzer;

import java.awt.Desktop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
        return subResults.stream().map(AnalyzerPlugin.Result::getResultAsString).reduce("", (acc, cur) -> acc + cur);
    }

    public String toHTML() {
        return "<html><body>"+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc, cur) -> acc + cur) + "</body></html>";
    }
    
  //Ce traitement sera provisoire car il n'est pas complet    - #WilliamBenakli
    /**PROVISOIRE Cette fonction permet de cr�at un code HTMLFLOW avec le graphique compris
     * @param typegraphique
     * @return String (correspondant au site web avec graphique) #WilliamBenakli
     */
    @SuppressWarnings("unchecked")
	public String createCodePageHtml(String typegraphique){
    	Graphique graph = new Graphique("graphique");
    	Map<String, Integer> map =  subResults.get(0).getPluginInfoByArray();
		return PageCreation.createPage("Commit", graph.import_chartJS(), graph.toGraph(typegraphique, new ArrayList<String>(map.keySet()), new ArrayList<Integer>(map.values())), graph.ouvrir_canvas());
	 
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
