package up.visulog.analyzer; //Déclaration du package d'appartenance de la classe

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//importation des classes nécessaires àla classe
public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {// déclaration de la classe CountCommitsPerAuthorPlugin qui utilise l'interface de la classe 
//AnalyzerPlugin 
    private final Configuration configuration; // déclaration de l'attribut configuration avec le modificateur 
   // private qui rend l'attribut visible que dans cette classe; ainsi que le modificateur final qui rend l'attribut inchangable.
    private Result result;
  // déclaration de l'attribut configuration avec le modificateur 
   // private qui fait rend l'attribut visible que dans cette classe;
    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {// constructeur de la classe (en public)
    
        this.configuration = generalConfiguration;// initialisation de l'attribut configuration;
    }

    static Result processLog(List<Commit> gitLog) { // methode static processLog qui prend comme argument une liste de type commit que l'on nome gitLog
        var result = new Result(); //initisation   de result 
        for (var commit : gitLog) {  // parcourrire gitLog 
            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0); //
            result.commitsPerAuthor.put(commit.author, nb + 1); 
        }
        return result; //retourn le nombre de commit emis par chaqu'un(je crois)
    }

    @Override // redefinition de la fonction run
    public void run() {
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));// redefinition de l'attribut de result
    }
// redefinition de la fonction Result qui est un getteur(fonction d'accès à un attribut qui est ici result
    @Override
    public Result getResult() {
        if (result == null) run(); // si getGitPath est null, alors la fonction run est invoquée
        return result;
    }

    static class Result implements AnalyzerPlugin.Result { //// déclaration de la classe Result qui utilise l'interface de la classe AnalyzerPlugin.Result 
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>(); 
        // déclaration de l'attribut commitsPerAuthor avec le modificateur private qui rend l'attribut visible que dans cette classe; l'attribut est de type
        // Map<String, Integer> cette notation fait référence à L'interface java.util.Map contient les pairs <clé, valeur>. Chaque pair est connu comme entrée. Map contient des éléments à clé unique.
        //Cette map accepte seulement les objets Integer pour les clés et String pour les valeurs. 
      //elle est implémenter par HashMap qui en hérite
      //il sagit donc d'une instance de Map
      
        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }// Il sagit d'une methode get qui retourn la personne qui à commit sous la forme
        //nom, numéro(grace au type de retour Map<String, Integer> )

        @Override //redefinition de la methode getResultAsString (pour chaque personne qui dépose son commit)
        public String getResultAsString() {
            return commitsPerAuthor.toString();//retourn maintenant l'auteur du commit avec la methode toString
        }

        @Override //redefinition de la methode getResultAsHtmlDiv() 
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");//déclaration de l'attribut de type StringBuilder qui crée un objet nomée htlm qui 
            //contient un message htlm
            for (var item : commitsPerAuthor.entrySet()) {//boucle qui parcours les auteurs de commit avec la methode entrySet()(pour pouvoir modifier), avec un ittirateur item
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");// pour chaqu'un d'eux un message de type htlm
                //du type "<div>Commits per author: <ul> "<li>"+(la clé de Map<String, Integer> de chaque auteur ):le resultat de la methode getValue de l'auteur"</li>" "
            }
            html.append("</ul></div>");// ajout au message htlm "</ul></div>"
            return html.toString(); //retourn le message htlm crée avec la fonction toQtring()
        }
    }
}
