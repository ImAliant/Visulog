package up.visulog.config;


/* La classe Path permet de créer un objet de type Path qui contient 
 * des informations sur les fichiers et les répertoires, tels que leurs emplacements, 
 *leurs tailles, leurs dates de création et même leur existence.
 * Dans notre cas il contient le chemin relatif ou absolu vers le fichier ou le répertoire
 * qui contient Git.
 */
import java.nio.file.Path;

/* Deux import qui n'ont pas l'air d'être utilisé
 *(peut-être à nous de le faire ...)
 */
 
import java.util.ArrayList;
import java.util.List;

/* La classe Map permet de créer un objet de type Map qui contient une combinaison 
 * de type "clé -> valeur" notée Map <K,V> avec K la clé et V la valeur . Une  Map peut contenir 
 * plusieurs combinaisons comme un trousseau. Map n’autorise pas les clés en double,
 * par contre il autorise les valeurs en double.
 */
import java.util.Map;

public class Configuration {
    /* Une fois le chemin créé, on utilise son identificateur (dans ce cas, gitPath )
     pour faire référence au fichier(ou dossier) et y effectuer des opérations.
    */
    private final Path gitPath;
    
    /* Par défaut, on peut mettre n'importe quel type dans une map,
     * ici le type est limité à String pour la clé et PluginConfig pour la valeur
      (la classe PluginConfig est pour l'instant vide )
    */
    private final Map<String, PluginConfig> plugins;

    public Configuration(Path gitPath, Map<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.plugins = Map.copyOf(plugins);
        /* Ici on fait appel à la metode copyOf() qui crée une copie d'une Map .
         * Cette copie n'est pas modifiable 
         */
    }
     /* Un "plugin" permet d'ajouter une extension à un programme pour lui apporter de nouvelles fonctionalités.
      * Dans notre cas on ajoute des arguments à l'execution du programme pour avoir des options en plus,
      *comme --addplugin="countCommit" (donnée en exemple dans le ReadME du projet).
      * Lorsque que l'on run sans arguments,le programme affiche une page(pour l'instant vide)
      * si on ajoute un argument  il nous affiche en plus de la page d'origine les options demandées. 
      * Un objet Configuration permet donc de réaliser une action sur les élements du dossier/fichier git 
     */
    

    public Path getGitPath() {
        return gitPath;
    }

    public Map<String, PluginConfig> getPluginConfigs() {
        return plugins;
    }
}
