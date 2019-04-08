
*** ETAPE 1 ***
Renseigner le fichier param.txt

!!! ATTENTION : les requêtes se font par collection, il faut donc la renseigner dans la requete sinon vous risquez d'avoir des liens incorrects !!!
!!! ATTENTION : la collection renseignée dans la requete, celle renseignée dans les paramètres et le type de données doivent être cohérents.

*** ETAPE 2 ***
Générer la liste des produits correspondants à une requête opensearch en exécutant le script PEPS-Auto-OpenSearch-Request.sh :
./PEPS-Auto-OpenSearch-Request.sh

*** ETAPE 3 ***
- SI PROTOCOL=HTTP : utilisation du script PEPS-Auto-OpenSearch-HTTP-Download.sh
Pré-requis : Avoir un compte PEPS
			 L'exécution du script de l'étape 2 a généré un fichier liens_http.txt (le fichier est bien présent et se trouve dans le répertoire d'exécution du script PEPS-Auto-OpenSearch-HTTP-Download.sh).
			 Avoir CURL installé (sinon, utiliser le fichier liens_http.txt qui contient les liens http vers les produits sélectionnés avec un autre outil de téléchargement tel que WGET)
Télécharger les produits en exécutant le script PEPS-Auto-OpenSearch-HTTP-Download.sh :
./PEPS-Auto-OpenSearch-HTTP-Download.sh

!!! ATTENTION : si vous souhaitez conserver le fichier liens_http.txt, penser à le sauvegarder avant toute nouvelle utilisation !!!


#################
### IMPORTANT ###
#################

NE PAS INTERROMPRE L'EXECUTION DES SCRIPTS sans quoi
les sessions resteront ouvertes jusqu'à ce que le max de sessions soit
atteint (ou penser à killer les process de type ftp -inv si besoin dans le cas d'un téléchargement ftp)


