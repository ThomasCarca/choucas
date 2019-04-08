#!/bin/sh

# HISTORIQUE
# VERSION:1.0::07/12/2016:version initiale
# VERSION:1.1:::03/04/2017: prise en compte des recommandations issues du controle Qualite
# VERSION:1.2:::19/07/2017: prise en compte des produits sur bande
# FIN-HISTORIQUE

########################################################################################
# PROJET: PEPS
# NOM DU FICHIER: PEPS-Auto-OpenSearch-HTTP-Download.sh
# TYPE DU FICHIER: Script shell bash
# AUTEUR: CAPGEMINI/SOPRA-STERIA
########################################################################################
# ROLE: telecharger les produits PEPS par http 
########################################################################################
# DEPENDANCES:
#	- fichier de conf param.txt
#   - la liste des produits a telecharger liens_http.txt
# - avoir curl installÃ© sur la machine
################################################################################
# Le script est décrit dans le PEX du PROJET associe avec toutes les options et 
# les informations s’il y a lieu.
################################################################################

# SORTIE:
#	- fichier de log des transferts
########################################################################################

# Remove  each name from the list of defined aliases.  If -a is supplied, all alias definitions are removed.
unalias .a

# Valeur de retour conditionnee à la reussite de la ligne complete incluant le pipe
set -o pipefail

################################################################################
# FONCTION      : telechargement
# ROLE          : telecharge un produit via curl
# PARAMETRE1    : NA
################################################################################

#Fonction pour verifier les téléchargement
function telechargement
{
	# Se placer dans le repertoire note dans le fichier param.txt
	cd  ${LOCAL_DIR}
	# Telechargement
	curl -k --basic -u ${PEPS_LOG}:${PEPS_PWD} ${PRODUCT_URL} -o ${PRODUCT_NAME}.zip
	
        # Test du telechargement a introduire dans le script
        fichier=`head -1 ${PRODUCT_NAME}.zip`
	# Si le mot Error apparait
        if [[ ${fichier} =~ "Error" ]]
        then
		# Tracer l'erreur de telechargement dans le fichier de log
        	echo -e "Erreur lors du telechargement ${fichier} \n">> ${DIR}/log_http_${DATE}.log
	else
		# Tracer la reussite du telechargement dans le fichier de log
		echo -e "Telechargement reussi \n" >> ${DIR}/log_http_${DATE}.log
        fi
	# Se replacer dans le repertoire courant
	cd ${DIR}
}

########################################################################################
# MAIN 
########################################################################################

# Lorsque l'utilisateur courant est root, on demande a
# l'operateur s'il souhaite poursuivre le traitement
current_users="$(id -u -n)"

if [ ${current_users} == "root" ]; then
	echo "! Execution avec les droits de ${current_users} !"
	echo -n "! Voulez-vous continuer (Oui,Non) ?"
	read reponse
	if [ ${reponse} == "N" ] || [ ${reponse} == "Non" ] || [ ${reponse} == "n" ] || [ ${reponse} == "non" ]; then
		echo "Sortie du script ..."
		exit 0
	fi
fi


# Fichier de configuration 
FICHIER_CONF=param.txt
if [ ! -e ${FICHIER_CONF} ]
then
    echo ${FICHIER_CONF} "n existe pas veuillez le creer"
    exit 1
fi

chmod 700 ${FICHIER_CONF}

# Liste des produits
LISTE_PRODUITS=liens_http.txt
if [ ! -e ${LISTE_PRODUITS} ]
then
    echo ${LISTE_PRODUITS} "n existe pas veuillez le creer"
    exit 1
fi

chmod 700 ${LISTE_PRODUITS}

# Variables
LOCAL_DIR=$(grep "^LOCAL_DIR=" ${FICHIER_CONF} | cut -d '=' -f2)
if [ ${LOCAL_DIR} = "" ]
then echo "ERREUR: le chemin de sauvegarde local n est pas specifie"
    exit 1
fi

# variable avec recherche dans un fichier
PEPS_LOG=$(grep "^PEPS_LOG=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ ${PEPS_LOG} = "" ]
     then echo "ERREUR: le login du compte PEPS n est pas renseigne"
     exit 1
     fi

# variable avec recherche dans un fichier
PEPS_PWD=$(grep "^PEPS_PWD=" ${FICHIER_CONF} | cut -d '=' -f2)
     if [ ${PEPS_PWD} = "" ]
     then echo "ERREUR: le mot de passe du compte PEPS n est pas renseigne"
     exit 1
     fi

# Date courante
DATE=`date +"%d%m%y%H%M%S"`

# Repertoire courant
DIR=`pwd`

while read line
#exemple de ligne : href="https://peps.cnes.fr/resto/collections/S1/35762929-ffa4-52a4-a33a-cb258feedb0f/download"/>
do
	# Recuperation de l URL du produit
	PRODUCT_URL1=${line}
	# modification d'un variable existante
	PRODUCT_URL2=`echo ${PRODUCT_URL1} | sed 's/^.*https/https/g' `
		# modification d'un variable existante
	PRODUCT_URL=`echo ${PRODUCT_URL2} | sed 's/\"\/>.*$//g' `
		# modification d'un variable existante
	METADATA_URL=`echo ${PRODUCT_URL} | sed 's/\/download.*$/\//g' `

	# Recuperation du nom du produit
	PRODUCT_NAME=`curl -k --silent --basic -u ${PEPS_LOG}:${PEPS_PWD} ${METADATA_URL} | sed 's/^.*\"productIdentifier\":\"//g' | sed 's/\"\,.*$//g'`
	# Recuperation du mode de stockage du produit
	STORAGE=`curl -k --silent --basic -u ${PEPS_LOG}:${PEPS_PWD} ${METADATA_URL} | sed 's/^.*\"storage\":{\"mode\":\"//g' | sed 's/\"\,.*$//g'`
	# Si erreur dans l url
        if [[ ${PRODUCT_NAME} =~ "Error" || ${PRODUCT_NAME} == "" ]]
        then
		# Traces "Erreur" a ecrire dans le fichier de log
                echo -e "Erreur de connexion ou dans l URL du produit : ${METADATA_URL} \n " >> log_http_${DATE}.log
        else
		# Traces "Telechargement" a ecrire dans le fichier de log
		echo "downloading ${PRODUCT_NAME}.zip ..." >> log_http_${DATE}.log
		echo ${PRODUCT_NAME}
		# Si le produit est sur bande
		if [ ${STORAGE} == "tape" ]
		then
			# Premier appel pour le mettre sur disque
			curl -k --basic -u ${PEPS_LOG}:${PEPS_PWD} ${PRODUCT_URL} -o ${PRODUCT_NAME}.zip > /dev/null 
			echo "Produit sur bande ... le telechargement peut prendre plus de temps"
			typeset -i attempt=1
			# 5 tentatives maximum 
			while [[ ${attempt} -le 5 ]]
			do
				# Attendre 30 secondes
				sleep 60
				# Recuperation du nouveau mode de stockage
				STORAGE=`curl -k --silent --basic -u ${PEPS_LOG}:${PEPS_PWD} ${METADATA_URL} | sed 's/^.*\"storage\":\"mode\":\"//g' | sed 's/\"\,.*$//g'`
				# Si toujours sur bande et 5 tentatives deja faites
				if [[ ${STORAGE} == "tape"  &&  ${attempt} -eq 5 ]]
				then
					# Tracer le fait que le produit n a pas ete telecharge dans le fichier de log
					echo -e "Produit sur bande : retenter un telechargement \n" >> log_http_${DATE}.log
					attempt=`expr ${attempt} + 1`
				# Si toujours sur bande mais moins de 5 tentatives
				elif [[ ${STORAGE} == "tape"  &&  ${attempt} -lt 5 ]] 
				then
					# Incrementation du nombre de tentative
					attempt=`expr ${attempt} + 1`
				# Si sur disque
				elif [ ${STORAGE} == "disk" ]
				then 
					# Appel de la fonction telechargement
					telechargement
					typeset -i attempt=6
				fi
					
			done
		else 
			# Si sur disque, appel de la fonction telechargement
			telechargement
		fi					
	fi
# Le fichier de sortie est la liste des produits à télécharger
done < ${LISTE_PRODUITS}


exit 0
