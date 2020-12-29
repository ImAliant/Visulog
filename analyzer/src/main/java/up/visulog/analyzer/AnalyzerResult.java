package up.visulog.analyzer;

import java.util.List;

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
}
