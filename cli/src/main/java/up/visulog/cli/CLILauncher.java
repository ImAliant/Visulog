package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

/*
La classe FileSystems définit des méthodes pour créer des systèmes de fichiers qui fournissent l'accès à d'autres types de systèmes de fichiers (personnalisés). 
Un système de fichiers est la fabrique de plusieurs types d'objets: La méthode getPath convertit une chaîne de chemin dépendante du système, renvoyant un objet Path qui peut 
être utilisé pour localiser et accéder à un fichier.La classe FileSystems définit des méthodes pour créer des systèmes de fichiers qui fournissent l'accès à d'autres types de 
systèmes de fichiers (personnalisés). Un système de fichiers est la fabrique de plusieurs types d'objets: La méthode getPath convertit une chaîne de chemin dépendante du système, 
renvoyant un objet Path qui peut être utilisé pour localiser et accéder à un fichier.
*/
import java.nio.file.FileSystems;
/*
HashMap est une classe de collection basée sur Map qui est utilisée pour stocker des paires clé et valeur, elle est désignée par HashMap <Key, Value> ou HashMap <K, V>. 
Cette classe ne donne aucune garantie quant à l'ordre de la carte. Elle est similaire à la classe Hashtable sauf qu'elle n'est pas synchronisée et autorise les valeurs nulles 
(valeurs nulles et clé nulle).
*/
import java.util.HashMap;
/*
Optional est un objet conteneur utilisé pour contenir des objets non nuls. L'objet Optional est utilisé pour représenter null avec une valeur absente. 
Cette classe a diverses méthodes utilitaires pour faciliter le code pour gérer les valeurs comme «disponibles» ou «non disponibles» au lieu de vérifier les valeurs nulles.
*/
import java.util.Optional;

/*
    Le fichier CLILauncher.java possèdent deux méthodes : makeConfigFromCommandLineArgs(String[] args) et displayHelpAndExit().
        La méthode makeConfigFromCommandLineArgs(String[] args) permet à l'utilisateur de choisir une des commandes disponibles 
            ( les commandes sont expliquées avec des commentaires dans la méthode ).
        La méthode displayHelpAndExit() permet à l'utilisateur de connnaître les commandes disponibles.
*/

public class CLILauncher {

    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            results.createPageHtml("page", "pie"); // nom de la page et le type de graphique
        } else displayHelpAndExit();
    }

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var gitPath = FileSystems.getDefault().getPath(".");
        var plugins = new HashMap<String, PluginConfig>();
        for (var arg : args) {
            if (arg.startsWith("--")) {
                String[] parts = arg.split("=");
                if (parts.length != 2) return Optional.empty();
                else {
                    String pName = parts[0];
                    String pValue = parts[1];
                    switch (pName.toLowerCase()) { //to Lower case = rendre ne miniscule
	                	case "--allplugin":
	                		// TODO : Ce system compile l'emsemble des plugins pr�sent dans nos plus
	                		//Il permet de gagner du temps quant a la generation d'un site de plugin #WilliamBenakli
	                		break;
	                    case "--addplugin":
	                        // TODO: parse argument and make an instance of PluginConfig	
	                        // Let's just trivially do this, before the TODO is fixed:
	                        if (pValue.equalsIgnoreCase("countCommits"))plugins.put("countCommits", new PluginConfig() {});
                            if (pValue.equalsIgnoreCase("countMergesCommits")) plugins.put("countMergesCommits", new PluginConfig() {});
	                        
	//                        if (pValue.equalsIgnoreCase("countCommitPerDate:54050:WILLIAM")) plugins.put("countCommitPerDate--29/59/2002", new PluginConfig() {
	//
	//                        });
	                        break;
	                    case "--loadconfigfile":
	                		// TODO: Ce system prend en parametre un fichier YAML et recherche toutes les plugins demand�s 
	                		//Une fois fait il cr�er tous les plugins necessaires et cr�er le html correspondant #WilliamBenakli
	                        break;
	                    case "--justsaveconfigfile":
	                        // TODO: (save command line options to a file instead of running the analysis)
	                    	//Objectif est de cr�er un fichier File et d'y entrer les options et les commandes !
	//                    	System.out.println("List de vos configuration pr�sent dans : " + filename.path()); #WilliamBenakli
	                    	break;
	                    default:
                        return Optional.empty();
                }
            }
            } else {
                gitPath = FileSystems.getDefault().getPath(arg);
            }
        }
        return Optional.of(new Configuration(gitPath, plugins));
    }

    private static void displayHelpAndExit() {
        System.out.println("(WTS) Commande non reconnue");
        System.out.println("# Commande pr�sente  #");
        System.out.println("  --addPlugin=[countCommits,countMerges, countCommitsPerDate:00/00/00]");
        System.out.println("  --loadConfigFile");
        System.out.println("  --justSaveConfigFile");
        System.out.println("  --allPlugin");
        System.out.println("# Commande pr�sente  #");
        System.exit(0);
    }
}
