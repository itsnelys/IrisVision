# IrisVision

**Minecraft 1.21.1 · NeoForge 21.1.248 · Java 21**

[Français](#français) · [English](#english)

## Français

IrisVision est le module de consultation de la suite Iris. Il ajoute les recettes, le catalogue d'objets, les favoris, la recherche d'inventaire et les tooltips détaillés. Il dépend de [IrisCore](https://github.com/itsnelys/IrisCore), qui fournit les menus, thèmes, HUD, options et raccourcis communs.

### Fonctionnalités

- Catalogue d'objets avec catégories, pages et recherche.
- Consultation des recettes et des utilisations, favoris et recette épinglée.
- Recherche et surlignage optionnel des objets dans l'inventaire.
- Tooltips pour blocs, entités, objets au sol, fluides et inventaires.
- Options de providers dédiées aux tooltips et à l'inventaire.
- Wiki intégré et intégrations de recettes extensibles.

### Développement

Placer les deux dépôts côte à côte :

```text
Minecraft-DevKit/
├── IrisCore/
└── IrisVision/
```

Depuis `IrisCore`, utiliser :

```powershell
.\gradlew.bat runClient
.\gradlew.bat test
```

Le client partage le répertoire `IrisCore/run`, ce qui conserve les sauvegardes et les options. IrisVision peut également être construit depuis son propre dossier avec `./gradlew.bat build`.

### Installation

Installer IrisCore et IrisVision avec Minecraft 1.21.1, NeoForge 21.1.248 et Java 21. Placer les deux JAR dans le dossier `mods` de l'instance.

## English

IrisVision is the browsing module of the Iris suite. It adds recipes, the item catalogue, favorites, inventory search and detailed tooltips. It depends on [IrisCore](https://github.com/itsnelys/IrisCore), which supplies the shared menus, themes, HUD, options and key bindings.

### Features

- Item catalogue with categories, pages and search.
- Recipe and usage browsing, favorites and a pinned recipe.
- Optional search and highlighting of items in inventories.
- Tooltips for blocks, entities, ground items, fluids and inventories.
- Dedicated tooltip and inventory-provider settings.
- Built-in wiki and extensible recipe integrations.

### Development

Keep the two repositories side by side:

```text
Minecraft-DevKit/
├── IrisCore/
└── IrisVision/
```

From `IrisCore`, run:

```powershell
.\gradlew.bat runClient
.\gradlew.bat test
```

The client shares `IrisCore/run`, preserving saves and settings. IrisVision can also be built from its own directory with `./gradlew.bat build`.

### Installation

Install IrisCore and IrisVision with Minecraft 1.21.1, NeoForge 21.1.248 and Java 21. Put both JARs in the instance's `mods` directory.

## License

Copyright © 2026 itsnelys. See IrisCore's [LICENSE](https://github.com/itsnelys/IrisCore/blob/main/LICENSE).
