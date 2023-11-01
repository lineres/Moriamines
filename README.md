# **MoriaMines**

## A propos du projet :
Notre projet c'est basé sur le jeu alchimie que vous pouvez retrouver sur ce lien : littlealchemy.com, mais nous avons choisi le thème Minecraft.  
Votre but dans ce jeu est de combiner différents éléments et de créer des éléments de plus en plus compliqués, jusqu'à les avoir tous découvert.  
Vous commencez avec 6 éléments de base et vous devez débloquer tous les 30 éléments restants.   
![alt text](https://i.imgur.com/yqWuf2h.png)

## Fonctionnalités :
Fonctionnalités obligatoires :  
* possibilité de glisser et déposer des éléments  
* possibilités de fusionner des éléments
* possibilités de récupérer des éléments dans la réserve  
* plus d'une 30 aine de recettes  
  
Fonctionnalités recommandées :  
* Menu listant les recettes déjà découvertes  
* Sauvegarde de la progression  
* Personnalisation de l'interface (choix de couleur de l'interface...)  
* Support du son (Musique de fond, bruitages lors de la découverte d'éléments...)  
* Support de plusieurs profiles, qu'on peut créer, supprimer et réinitialiser.
* Score et classements des joueurs

## Exécutions :
Pour directement exécuter l'application, ouvrez votre terminal dans la source du projet, et écrivez `./gradlew run`.

Sinon vous avez la possibilité de construire l'application dans un unique fichier .jar en exécutant la commande `./gradlew build` dans le terminal. Vous pouvez ensuite aller dans le dossier `\builds\libs\` pour exécuter le fichier `MoriaMines.jar` soit avec un double-clic, soit avec la commande `java -jar MoriaMines.jar`.

## Désinstallation :  
Si pour quelconque raison vous voulez supprimer le jeu de votre ordinateur, vous pouvez simplement allez dans le menu `Settings` et cliquer sur le bouton `Delete all settings`, ceci vas supprimer tous les fichiers sauvegardées sur votre ordinateur, il ne vous restera plus qu'a supprimer le dossier du projet et/ou le fichier .jar.
