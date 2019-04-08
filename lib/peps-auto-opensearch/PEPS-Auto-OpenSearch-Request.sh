#!/bin/sh

# HISTORIQUE
# VERSION:1.0::05/08/2016:version initiale
# VERSION:1.1::20/10/2016:possibilite de generer une liste de produits contenant les liens http + suppression de la fonction cut_line
# VERSION:1.2::25/01/2017:ajout du parametre COLLECTION pour prendre en compte S2ST
# VERSION:1.3::03/04/2017: prise en compte des recommandations issues du controle Qualite
# VERSION:1.4::19/09/2017: prise en compte des S2B et S3A
# VERSION:1.5::03/09/2018: prise en compte ICode
# FIN-HISTORIQUE

########################################################################################
# PROJET: PEPS
# NOM DU FICHIER: PEPS-Auto-OpenSearch-Request.sh
# TYPE DU FICHIER: Script shell bash
# AUTEUR: CAPGEMINI/SOPRA-STERIA
########################################################################################
# ROLE: Elaboration d'une liste de produits Sentinel via la fonion OpenSearch de PEPS
########################################################################################
# DEPENDANCES:
#- fichier de conf param.txt
########################################################################################
########################################################################################
# SORTIE:
#- liste des produits retournes par OpenSearch liste_produits.txt
#- liste des produits retournes par OpenSearch liens_http.txt
########################################################################################
PATH=/usr/local/bin:/usr/bin:/usr/local/sbin:/usr/sbin:/opt/dell/srvadmin/bin:/home/exppeps/bin
# Remove  each name from the list of defined aliases.  If -a is supplied, all alias definitions are removed.
unalias -a

# Valeur de retour conditionnee à la reussite de la ligne complete incluant le pipe
set -o pipefail

# Lorsque l'utilisateur courant est root, on demande a
# l'operateur s'il souhaite poursuivre le traitement
current_users="$(id -u -n)"

if [ "${current_users}" == "root" ]; then
	echo "! Execution avec les droits de ${current_users} !"
	echo -n "! Voulez-vous continuer (Oui,Non) ?"
	read reponse
	if [ "${reponse}" == "N" ] || [ "${reponse}" == "Non" ] || [ "${reponse}" == "n" ] || [ "${reponse}" == "non" ]; then
		echo "Sortie du script ..."
		exit 0
	fi
fi




# Fonctions
# Controle de presence des parametres proxy
proxy_check () {
# Initialisation de la variable Proxy_log
PROXY_LOG=$(grep "^PROXY_LOG=" ${FICHIER_CONF} | cut -d '=' -f2) 
     if [ "$PROXY_LOG" = "" ]
     then echo "ERREUR: le login du compte proxy n est pas renseigne"
     exit 1
     fi

# Initialisation de la variable Proxy_pwd
PROXY_PWD=$(grep "^PROXY_PWD=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ "$PROXY_PWD" = "" ]
     then echo "ERREUR: le mot de passe du compte proxy n est pas renseigne"
     exit 1
     fi
}

# Mise en forme du resultat open search
#liste differente Ã  generer suivant ftp (on recupere le title) ou http on recupere l id)
construct_list () {
      while read line
        do
             if   [ "$PROTOCOL" = "HTTP" ] || [ "$PROTOCOL" = "http" ]
            then 
            # mise dans un fichier des résultats d'un grep
             grep "link rel=\"icon" | cut -d '"' -f 4 | cut -d ' ' -f 5 > liste_temp.txt
             elif [ "$PROTOCOL" = "FTP" ] || [ "$PROTOCOL" = "ftp" ]
             then
             	# Initialisation de la variable HPSS_LOG
                HPSS_LOG=$(grep "^HPSS_LOG=" ${FICHIER_CONF} | cut -d '=' -f2)
                     if [ "$HPSS_LOG" = "" ]
                     then echo "ERREUR: le login du compte ftp HPSS n est pas renseigne"
                     exit 1
                     fi
				# Initialisation de la variable HPSS_PWD
                HPSS_PWD=$(grep "^HPSS_PWD=" ${FICHIER_CONF} | cut -d '=' -f2)
                     if [ "$HPSS_PWD" = "" ]
                     then echo "ERREUR: le mot de passe du compte ftp HPSS n est pas renseigne"
                     exit 1
                     fi
			          # Mise de donnee dans le fichier liste_temp.txt		     
             grep "link rel=\"icon" | cut -d ' ' -f 11 | cut -d'"' -f 2 > liste_temp.txt
             else
             echo "champ PROTOCOL invalide, veuillez le modifier par ftp ou http"
             exit 1
             fi
             done < result.txt
}
									    

# RÃ©cupÃ©ration du nom de fichier
format_list () {
     while read line
     do
          if   [ "$PROTOCOL" = "HTTP" ] || [ "$PROTOCOL" = "http" ]
          then
          #echo "https://peps-q.cst.cnes.fr/resto/collections/"$SAT"/"$line"/download" >> liens_http.txt
          echo "https://peps.cnes.fr/resto/collections/${COLLECTION}/${line}/download" >> liens_http.txt
          elif [ "$PROTOCOL" = "FTP" ] || [ "$PROTOCOL" = "ftp" ]
          # Mise de donnee dans le fichier liste_produits.txt
          then  basename ${line}  | sed 's/_quicklook.jpg*$//g'  >> liste_produits.txt
          else echo "PROTOCOL invalide"
          fi
     done < liste_temp.txt
}



#nettoyage
clean_list () {
     if [ -f "liste_raw.txt" ]; then
     rm liste_raw.txt
     fi
     if [ -f "liste_temp.txt" ]; then
     rm liste_temp.txt
     fi
     if [ -f "result.txt" ]; then
     rm result.txt
     fi
     if [ -f "cmd.sh" ]; then
     rm cmd.sh
     fi
}

########################################################################################
# MAIN 
########################################################################################
# Fichier de configuration 
FICHIER_CONF=param.txt
chmod 700 ${FICHIER_CONF}

     if [ ! -e ${FICHIER_CONF} ]
     then
     echo " ${FICHIER_CONF} n existe pas veuillez le creer"
     exit 1
     fi

########################################################################################
# Variables
########################################################################################
PEPS_LOG=$(grep "^PEPS_LOG=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ "$PEPS_LOG" = "" ]
     then echo "ERREUR: le login du compte PEPS n est pas renseigne"
     exit 1
     fi

# Initialisation de la variable PEPS_PWD
PEPS_PWD=$(grep "^PEPS_PWD=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ "$PEPS_PWD" = "" ]
     then echo "ERREUR: le mot de passe du compte PEPS n est pas renseigne"
     exit 1
     fi

# Initialisation de la variable REQ_OS
REQ_OS=$(grep "^REQ_OS@" ${FICHIER_CONF} | cut -d '@' -f2)
     if [ "$REQ_OS" = "" ]
     then echo "ERREUR: ajoutez une requete Open Search dans le fichier param.txt"
     exit 1
     fi
# Initialisation de la variable COL
COL=$(grep "^REQ_OS@" ${FICHIER_CONF} | cut -d '/' -f7)

# Initialisation de la variable PROTOCOL
PROTOCOL=$(grep "^PROTOCOL=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ "$PROTOCOL" = "" ]
     then echo "ERREUR: veuillez specifier le protocole utilise  dans le fichier param.txt: code 1"
     exit 1
     fi

if [ "$PROTOCOL" != "FTP" ] && [ "$PROTOCOL" != "ftp" ] &&  [ "$PROTOCOL" != "HTTP" ] && [ "$PROTOCOL" != "http" ]
then
echo "ERREUR : champ PROTOCOL invalide, veuillez le modifier par ftp ou http"
exit 1
fi

# Initialisation de la variable TYPE_DATA
TYPE_DATA=$(grep "^TYPE_DATA=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ "$TYPE_DATA" = "" ]
     then echo "ERREUR: veuillez specifier un type de donnees dans le fichier param.txt"
     exit 1
     fi

if [ "$TYPE_DATA" != "S1A" ] && [ "$TYPE_DATA" != "S1B" ] && [ "$TYPE_DATA" != "S2A" ] && [ "$TYPE_DATA" != "S2B" ] && [ "$TYPE_DATA" != "S3A" ]; then
	echo "ERREUR : champ TYPE_DATA invalide, veuillez le modifier par S1A ou S1B ou S2A ou S2B ou S3A"
	exit 1
fi

# Initialisation de la variable COLLECTION
COLLECTION=$(grep "^COLLECTION=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ "$COLLECTION" = "" ]
     then echo "ERREUR: veuillez specifier une collection dans le fichier param.txt"
     exit 1
     fi
if [ "$COLLECTION" != "S1" ] && [ "$COLLECTION" != "S2" ] && [ "$COLLECTION" != "S2ST" ] && [ "$COLLECTION" != "S3" ]
then
        echo "ERREUR : champ COLLECTION invalide, veuillez le modifier par S1 ou S2 ou S2ST ou S3"
exit 1
fi
# Initialisation de la variable Proxy_use
PROXY_USE=$(grep "^PROXY_USE=" ${FICHIER_CONF} | cut -d '=' -f2)
case ${PROXY_USE} in
        O|o)
         # proxy_check ;  CMD_CURL="-U " {$PROXY_LOG} ":" {$PROXY_PWD} " --proxy proxy-http1.cnes.fr:8050 -k --silent" ;;
          proxy_check ;  CMD_CURL="-U  ${PROXY_LOG}:${PROXY_PWD} --proxy proxy-http1.cnes.fr:8050 -k --silent";;
        N|n)
          CMD_CURL=" -k --silent" ;;
*) echo "parametre d utilisation du proxy invalide" ; exit 1 ;;
esac

#Coherence entre les parametres
if [ "`echo ${COLLECTION} | cut -c 1,2`" != "`echo ${TYPE_DATA} | cut -c 1,2`" ]
then
	echo "le type de donnees et la collection ne sont pas coherents"
exit 1
fi

if [ ${COLLECTION} != ${COL} ]
then
        echo "la collection renseignee dans les parametres n'est pas coherente avec celle specifiee dans la requete"
exit 1
fi


#fichier liens_http.txt
if [ -e liens_http.txt ]
then
    rm liens_http.txt
fi

#fichier liste_produits.txt
if [ -e liste_produits.txt ]
then
    rm liste_produits.txt
fi

#elaboration de la liste
curl -o result.txt  ${CMD_CURL} ${REQ_OS}

#check de la requete
if [ ! -e result.txt ]
then
    echo "ERREUR : votre requete contient une erreur"
exit 1
fi
 
#check du nb de resultats
NB_RESULT=$(grep "<subtitle type=\"html\">"  result.txt | sed -nr 's/^.*>(.*)\s.*$/\1/p')         
if [ ${NB_RESULT} == 0 ]
then 
echo "Aucun resultat, veuillez verifier votre requete"
exit 1
fi

#check du nb de pages
NB_PAGE=$(grep "link rel=\"next" result.txt | sed -n 's,.*page=\(.*\)"/>,\1,p')
     if [ -z ${NB_PAGE} ]
     then
     echo "OpenSearch n a renvoye qu une seule page"
     construct_list
     format_list
     echo "constrution de la liste OK"
     else
     # PAGE 1 
     construct_list
     format_list
     
#PAGES SUIVANTES
          while : [ ! -z ${NB_PAGE} ]   
          do
          echo "curl " ${CMD_CURL} \"$REQ_OS"&page="${NB_PAGE}\"  > cmd.sh
          chmod 700 cmd.sh
          ./cmd.sh > result.txt
                        
          construct_list
          format_list     
                   
          # Page suivante
          NB_PAGE=$(grep "link rel=\"next" result.txt | sed -n 's,.*page=\(.*\)"/>,\1,p')
          # Gestion derniere ligne
               if [ "$NB_PAGE" = "" ] 
               then
               echo "construction de la liste OK" 
               break
               fi
          done
     fi	 
        
clean_list
         exit 0
