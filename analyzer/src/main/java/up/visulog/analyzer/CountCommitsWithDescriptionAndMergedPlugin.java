package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountCommitsWithDescriptionAndMergedPlugin implements AnalyzerPlugin{

    private final Configuration configuration;
    private Result result;

    public CountCommitsWithDescriptionAndMergedPlugin(Configuration config) {
        this.configuration = config;
    }

    /**
     * Counts the number of merge commits in the git log
     * @param gitLog - a List of all the commits inside the git repo
     * @return a Result containing a HashMap (authorName -> number of merge commits)
     */
    public static Result processLog(List<Commit> gitLog) { // methode static processLog qui prend comme argument une liste de type commit que l'on nome gitLog
        var result = new Result(); //initialisation   de result
        for (var commit : gitLog) {  // parcourrire gitLog
            if (commit.isMergeCommit() && commit.description.length()>0) {
                var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0); //
                result.commitsPerAuthor.put(commit.author, nb + 1);
            }
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

    public static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();
        public Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }
        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
        }
        @Override //redefinition de la methode getResultAsHtmlDiv()
        public String getResultAsHtmlDiv() {
            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            //contient un message htlm
            for (var item : commitsPerAuthor.entrySet()) {
                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
            }
            html.append("</ul></div>");
            return html.toString();
        }

        @Override //Pas encore achevé #WilliamBenakli
        public Map<String, Integer> getPluginInfoByArray() {
            return commitsPerAuthor;
        }
    }

}
