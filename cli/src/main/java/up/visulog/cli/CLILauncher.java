package up.visulog.cli;

import up.visulog.analyzer.Analyzer;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.io.FileWriter;
import java.io.IOException;
/*
La classe FileSystems définit des méthodes pour créer des systèmes de fichiers qui fournissent l'accès à d'autres types de systèmes de fichiers (personnalisés). 
Un système de fichiers est la fabrique de plusieurs types d'objets: La méthode getPath convertit une chaîne de chemin dépendante du système, renvoyant un objet Path qui peut 
être utilisé pour localiser et accéder à un fichier.La classe FileSystems définit des méthodes pour créer des systèmes de fichiers qui fournissent l'accès à d'autres types de 
systèmes de fichiers (personnalisés). Un système de fichiers est la fabrique de plusieurs types d'objets: La méthode getPath convertit une chaîne de chemin dépendante du système, 
renvoyant un objet Path qui peut être utilisé pour localiser et accéder à un fichier.
*/
import java.nio.file.FileSystems;
import java.util.ArrayList;
/*
HashMap est une classe de collection basée sur Map qui est utilisée pour stocker des paires clé et valeur, elle est désignée par HashMap <Key, Value> ou HashMap <K, V>. 
Cette classe ne donne aucune garantie quant à l'ordre de la carte. Elle est similaire à la classe Hashtable sauf qu'elle n'est pas synchronisée et autorise les valeurs nulles 
(valeurs nulles et clé nulle).
*/
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/*
Optional est un objet conteneur utilisé pour contenir des objets non nuls. L'objet Optional est utilisé pour représenter null avec une valeur absente. 
Cette classe a diverses méthodes utilitaires pour faciliter le code pour gérer les valeurs comme «disponibles» ou «non disponibles» au lieu de vérifier les valeurs nulles.
*/
import java.util.Optional;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class CLILauncher {

    static String prefix = "(WVS) ";
	
    public static void main(String[] args) {
    	if(args.length > 0) {
    		if(args[0].equalsIgnoreCase("help")) {
    			displayHelpAndExit();
    			return;
    		}
	        var config = makeConfigFromCommandLineArgs(args);
	        if (config.isPresent()) {
	            var analyzer = new Analyzer(config.get());
	            var results = analyzer.computeResults();
	            results.createPageHtml("page", "pie"); // nom de la page et le type de graphique
	        } else displayHelpAndExit();	
	}else {
		displayErreur();
	}
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
                            if (pValue.equalsIgnoreCase("countDescriptionAndMergedCommits")) plugins.put("countDescriptionAndMergedCommits", new PluginConfig() {});
                            if (pValue.equalsIgnoreCase("countDescriptionCommits")) plugins.put("countDescriptionCommits", new PluginConfig() {});
                            if (pValue.equalsIgnoreCase("countRemoveLine")) plugins.put("countRemoveLine", new PluginConfig() {});
                            if (pValue.equalsIgnoreCase("countAllModifyLine")) plugins.put("countAllModifyLine", new PluginConfig() {});
                            if (pValue.equalsIgnoreCase("countLineAdd")) plugins.put("countLineAdd", new PluginConfig() {});
                            //                        if (pValue.equalsIgnoreCase("countCommitPerDate:54050:WILLIAM")) plugins.put("countCommitPerDate--29/59/2002", new PluginConfig() {
	//  
	//                        });           
	                        break;
	                    case "--loadconfigfile":
	                		// TODO: Ce system prend en parametre un fichier YAML et recherche toutes les plugins demand�s 
	                		//Une fois fait il cr�er tous les plugins necessaires et cr�er le html correspondant #WilliamBenakli
	                        break;
	                    case "--justsaveconfigfile":
	                    	DumperOptions options = new DumperOptions();
                            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                            options.setPrettyFlow(true);
                            Yaml save=  new Yaml(options);
                            String [] nameFile= pValue.split("/");
                            String commandes = nameFile[1];
                            String [] saveConfig= commandes.replace(" ", "$").split(":");
                            if(saveConfig.length>0) {
                                List <String> val = new ArrayList<String>();
                                for(int i=0 ; i<saveConfig.length ; i++) {
                                    val.add(saveConfig[i].replace("$", " "));   
                                }
                                FileWriter writer;
                                try {
                                    writer = new FileWriter(nameFile[0] + ".yml");
                                     save.dump(val, writer);
                                     System.out.println("Votre fichier a bien été enrengistré voir /configYaml/" + nameFile[0] + ".yml");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                  
                            }else {
                                System.out.println("erreur --Help");
                            }
                            	return Optional.empty();
                            
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
        System.out.println("(WTS) Commande d'aide");
        System.out.println("# Commande presente  #");
        System.out.println("  --addPlugin=[countCommits,countMerges,countAllModifyLine]");
        System.out.println("  --listPlugin");
        System.out.println("  --loadConfigFile");
        System.out.println("  --justSaveConfigFile");
        System.out.println("  --allPlugin");
        System.out.println("# Commande presente  #");
        System.exit(0);
    }

    private static void displayErreur() {
		System.out.println(prefix + "Erreur aucune entree sasie");
		System.out.println(prefix + "Essayez avec 'help' comme entree pour plus d'info");
    }
}
